import React, { useState, useEffect } from "react";
import { submitProof, getOperatorsFromDebts } from "../api/api"; // Fetch operators and submit proof

export default function PayDebts() {
    const [operators, setOperators] = useState([]); // Operators fetched from the API
    const [selectedOperator, setSelectedOperator] = useState(""); // Selected operator from dropdown
    const [selectedFile, setSelectedFile] = useState(null);
    const [uploadMessage, setUploadMessage] = useState("");
    const [loading, setLoading] = useState(false);

    // Fetch operators on mount
    useEffect(() => {
        const fetchOperators = async () => {
            try {
                const operatorsData = await getOperatorsFromDebts(); // Fetch operator names
                setOperators(operatorsData);
                if (operatorsData.length > 0) {
                    setSelectedOperator(operatorsData[0].id); // Set the first operator as default
                }
            } catch (error) {
                console.error("Error fetching operators:", error);
                setUploadMessage("Failed to fetch operators. Please try again later.");
            }
        };

        fetchOperators();
    }, []);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
        setUploadMessage(""); // Reset message on new file selection
    };

    const handleUpload = async () => {
        if (!selectedOperator) {
            setUploadMessage("Please select an operator.");
            return;
        }

        if (!selectedFile) {
            setUploadMessage("Please select a PDF file to upload.");
            return;
        }

        // Check file type
        if (selectedFile.type !== "application/pdf") {
            setUploadMessage("Only PDF files are allowed.");
            return;
        }

        try {
            setLoading(true);
            const response = await submitProof(selectedOperator, selectedFile);
            setUploadMessage(response.message || "Payment proof submitted successfully!");
            setLoading(false);
        } catch (error) {
            console.error("Error submitting payment proof:", error);
            setUploadMessage("Failed to submit payment proof. Please try again.");
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3 style={{ textAlign: "center", color: "#4CAF50" }}>Pay Debts</h3>
            <p style={{ textAlign: "center" }}>
                Select the operator and upload your payment confirmation (PDF):
            </p>

            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <select
                    value={selectedOperator}
                    onChange={(e) => setSelectedOperator(e.target.value)}
                    style={{
                        width: "50%",
                        padding: "10px",
                        borderRadius: "5px",
                        border: "1px solid #ccc",
                        fontSize: "16px",
                        marginBottom: "10px",
                    }}
                >
                    {operators.length === 0 ? (
                        <option value="">Loading operators...</option>
                    ) : (
                        operators.map((operator) => (
                            <option key={operator.id} value={operator.name}>
                                {operator.name}
                            </option>
                        ))
                    )}
                </select>
            </div>

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
                    disabled={loading}
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
