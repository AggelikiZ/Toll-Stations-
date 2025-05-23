package com.payway.services;

import com.payway.models.*;
import com.payway.repositories.DebtRepository;
import com.payway.repositories.PaymentRepository;
import com.payway.repositories.OperatorRepository;
import com.payway.repositories.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final UserRepository userRepository;
    private final OperatorRepository operatorRepository;
    private final DebtRepository debtRepository;
    private final PaymentRepository paymentRepository;

    private void validateFile(MultipartFile file) throws IllegalArgumentException {
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("application/pdf"))) {
            throw new IllegalArgumentException("Invalid file type. Only PDF is supported.");
        }
        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB
            throw new IllegalArgumentException("File size exceeds the 10MB limit.");
        }
    }

    private File saveTempFile(MultipartFile file) throws IOException {
        Path tempDir = Files.createTempDirectory("uploads");
        Path tempFile = tempDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile.toFile();
    }

    private String extractTextFromPdf(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    private BigDecimal extractAmount(String text) {
        // Match decimal amounts like 123.45
        Pattern pattern = Pattern.compile("\\b\\d+\\.\\d{2}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return new BigDecimal(matcher.group());
        }
        return null;
    }

    private LocalDate extractPaymentDate(String text) {
        // Match date format yyyy-MM-dd
        Pattern pattern = Pattern.compile("\\b\\d{4}-\\d{2}-\\d{2}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return LocalDate.parse(matcher.group(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }

    private Map<String, Object> extractPaymentDetails(MultipartFile file) throws Exception {
        // Save file temporarily
        File tempFile = saveTempFile(file);
        String contentType = file.getContentType();
        System.out.println("Temporary file saved at: " + tempFile.getAbsolutePath());
        System.out.println("File content type: " + contentType);

        // Extract text
        System.out.println("Processing PDF file...");
        String extractedText = extractTextFromPdf(tempFile);
        System.out.println("Extracted text: " + extractedText);

        String[] lines = extractedText.split("\\r?\\n");

        // Parse text for payment details
        BigDecimal amount = extractAmount(extractedText);
        LocalDate paymentDate = extractPaymentDate(extractedText);
        System.out.println("Extracted amount: " + amount);
        System.out.println("Extracted payment date: " + paymentDate);
        List<String> detailsList = new ArrayList<>();

        // Ensure extracted details are valid
        if (amount == null || paymentDate == null) {
            System.out.println("Failed to extract valid payment details from the file.");
            throw new Exception("Failed to extract valid payment details from the file.");
        }

        // Clean up temporary file
        if (tempFile.delete()) {
            System.out.println("Temporary file deleted successfully.");
        } else {
            System.out.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }

        for (String line : lines) {
            String lowerline = line.toLowerCase();
            if (lowerline.contains("payment receipt") || lowerline.contains("amount paid") || lowerline.contains("payment date")) {
                continue; // Skip this line
            } else {
                detailsList.add(line); // Collect everything after the payment date
            }
        }
        String details = String.join("\n", detailsList).trim();

        // Return extracted details
        Map<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put("amount", amount);
        paymentDetails.put("paymentDate", paymentDate);
        paymentDetails.put("details", details);

        return paymentDetails;
    }
    public PaymentService(UserRepository userRepository, OperatorRepository operatorRepository, DebtRepository debtRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.operatorRepository = operatorRepository;
        this.debtRepository = debtRepository;
        this.paymentRepository = paymentRepository;
    }

    public String ToOpId(String opName) {
        Operator operator = operatorRepository.findByOpName(opName)
                .orElseThrow(() -> new IllegalArgumentException("Operator not found"));
        return operator.getOpId();
    }

    public String SourceOpId(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Operator operator = operatorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Operator not found"));
        return operator.getOpId();
    }

    public List<DebtDetails> getDebtsToOperator(String toOpId) {
        return debtRepository.findDebtsToOperator(toOpId);
    }
    public List<DebtDetails> getDebtsFromOperator(String fromOpId) {
        return debtRepository.findDebtsFromOperator(fromOpId);
    }
    public List<Map<String, Object>> paymentsFrom(String fromOpId) {
        List<Map<String, Object>> rawPayments = paymentRepository.findPaymentsByFromOpId(fromOpId);

        for (Map<String, Object> payment : rawPayments) {
            if (payment.get("updateTime") != null) {
                Timestamp ts = (Timestamp) payment.get("updateTime");
                payment.put("updateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts));
            }
        }
        return rawPayments;
    }

    public List<Map<String, Object>> paymentsTo(String toOpId) {
        return paymentRepository.findPaymentsByToOpId(toOpId);
    }

    @Transactional
    public void processPayment(String sourceOpID, String toOpID, MultipartFile file, String details) throws Exception {
        //Validate file
        validateFile(file);

        // Extract data from the file
        Map<String, Object> paymentDetails = extractPaymentDetails(file);

        // Retrieve the details from the map
        BigDecimal amount = (BigDecimal) paymentDetails.get("amount");
        LocalDate paymentDate = (LocalDate) paymentDetails.get("paymentDate");
        String newdetails = (String)paymentDetails.get("details");
        // Find Debt
        Debt debt = debtRepository.findByFromOpIdAndToOpId(sourceOpID, toOpID)
                .orElseThrow(() -> new IllegalArgumentException("Debt record not found for sourceOpId: " + sourceOpID + " and toOpId: " + toOpID));

        if (debt.getDebtAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Payment amount exceeds outstanding debt.");
        }

        // Update Debt
        debt.setDebtAmount(debt.getDebtAmount().subtract(amount));
        debt.setLastUpdate(LocalDateTime.now());

        // Save payment record
        Payment payment = new Payment();
        payment.setFromOpId(sourceOpID);
        payment.setToOpId(toOpID);
        payment.setAmount(amount);
        payment.setDate(paymentDate);
        payment.setUpdateTime(LocalDateTime.now());
        if (Objects.equals(details, null)) {
            payment.setDetails(newdetails);
        } else {
            payment.setDetails(details);
        }

        debtRepository.save(debt);
        paymentRepository.save(payment);
    }
}