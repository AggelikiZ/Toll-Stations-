import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { submitProof } from "../api/api"; // Importing API function
import { FaArrowLeft } from "react-icons/fa";

export default function PayDebts() {
    const location = useLocation();
    const navigate = useNavigate();

    const params = new URLSearchParams(location.search);
    const operator = params.get("operator") || "Unknown Operator";
    const amount = params.get("amount") || "0.00";

    const [selectedFile, setSelectedFile] = useState(null);
    const [uploadMessage, setUploadMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
        setUploadMessage(""); // Reset message on new file selection
    };

    const handleUpload = async () => {
        if (!operator) {
            setUploadMessage("Invalid operator. Please go back and select a debt.");
            return;
        }

        if (!selectedFile) {
            setUploadMessage("Please select a PDF file to upload.");
            return;
        }

        if (selectedFile.type !== "application/pdf") {
            setUploadMessage("Only PDF files are allowed.");
            return;
        }

        try {
            setLoading(true);
            const response = await submitProof(operator, selectedFile);
            setUploadMessage(response.message || "Payment proof submitted successfully!");
        } catch (error) {
            console.error("Error submitting payment proof:", error);
            setUploadMessage("Failed to submit payment proof. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: "20px", backgroundColor: "#f4f4f4", minHeight: 400 }}>

            <button
                onClick={() => navigate("/debts")}
                style={{
                    display: "flex",
                    alignItems: "center",
                    gap: "10px",
                    background: "none",
                    border: "none",
                    color: "#4CAF50",
                    fontSize: "16px",
                    cursor: "pointer",
                    marginBottom: "10px"
                }}
            >
                <FaArrowLeft /> Go Back
            </button>

            <h3 style={{ textAlign: "center", color: "#4CAF50", fontSize: 26 }}>Pay Selected Debt</h3>

            <table
                style={{
                    width: "100%",
                    borderCollapse: "collapse",
                    marginTop: "30px",
                    marginBottom: "50px",
                    border: "1px solid #ddd",
                }}
            >
                <thead>
                <tr style={{ backgroundColor: "#4CAF50", color: "white" }}>
                    <th style={{ padding: "10px", border: "1px solid #ddd" }}>Recipient Operator Name</th>
                    <th style={{ padding: "10px", border: "1px solid #ddd" }}>Total Debt Amount</th>
                </tr>
                </thead>
                <tbody>
                <tr style={{ backgroundColor: "#f9f9f9", textAlign: "center" }}>
                    <td style={{ padding: "10px", border: "1px solid #ddd" }}>{operator}</td>
                    <td style={{ padding: "10px", border: "1px solid #ddd" }}>{amount} €</td>
                </tr>
                </tbody>
            </table>

            <p style={{ textAlign: "left", fontSize: 16 }}>
                Upload your payment confirmation (PDF up to 10MB):
            </p>

            <div style={{ textAlign: "left", marginBottom: "30px" }}>
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
                    disabled={loading}
                >
                    {loading ? "Uploading..." : "Submit"}
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
