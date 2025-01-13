import React, { useState } from "react";
import axios from "axios";

export default function PayDebts() {
    const [selectedFile, setSelectedFile] = useState(null);
    const [uploadMessage, setUploadMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
        setUploadMessage(""); // Reset message on new file selection
    };

    const handleUpload = async () => {
        if (!selectedFile) {
            setUploadMessage("Please select a PDF file to upload.");
            return;
        }

        // Check file type
        if (selectedFile.type !== "application/pdf") {
            setUploadMessage("Only PDF files are allowed.");
            return;
        }

        const formData = new FormData();
        formData.append("file", selectedFile);

        try {
            setLoading(true);
            const response = await axios.post("http://localhost:9115/api/payments/submitproof", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            setUploadMessage("PDF uploaded successfully!");
            setLoading(false);
        } catch (error) {
            console.error("Error uploading PDF:", error);
            setUploadMessage("Failed to upload the PDF. Please try again.");
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3 style={{ textAlign: "center", color: "#4CAF50" }}>Pay Debts</h3>
            <p style={{ textAlign: "center" }}>Upload your payment confirmation (PDF) below:</p>

            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <input
                    type="file"
                    accept=".pdf"
                    onChange={handleFileChange}
                    style={{
                        padding: "10px",
                        fontSize: "16px",
                        borderRadius: "5px",
                        border: "1px solid #ccc",
                    }}
                />
            </div>

            <div style={{ textAlign: "center" }}>
                <button
                    onClick={handleUpload}
                    style={{
                        padding: "10px 20px",
                        backgroundColor: "#4CAF50",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                        fontSize: "16px",
                    }}
                >
                    {loading ? "Uploading..." : "Upload Confirmation"}
                </button>
            </div>

            {uploadMessage && (
                <p
                    style={{
                        textAlign: "center",
                        color: uploadMessage.includes("successfully") ? "green" : "red",
                        marginTop: "20px",
                    }}
                >
                    {uploadMessage}
                </p>
            )}
        </div>
    );
}
