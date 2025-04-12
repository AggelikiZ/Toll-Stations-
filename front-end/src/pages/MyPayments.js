import React, { useState, useEffect } from "react";
import { getMyPayments } from "../api/api";

export default function MyPayments() {
    const [payments, setPayments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPayments = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await getMyPayments();
                setPayments(response);
            } catch (err) {
                setError("Failed to fetch your uploaded payments. Please try again.");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchPayments();
    }, []);

    return (
        <div style={{ padding: "10px" }}>
            {loading ? (
                <p style={{ textAlign: "center", color: "#555" }}>Loading payments...</p>
            ) : error ? (
                <p style={{ textAlign: "center", color: "red" }}>{error}</p>
            ) : payments.length === 0 ? (
                <p style={{ textAlign: "center", color: "#555",fontSize: "16px" }}>
                    No uploaded payments found.
                </p>
            ) : (
                <div>
                    <p style={{ textAlign: "left", color: "#555", fontSize: "16px" }}>
                        Previously uploaded payments to other operators.
                    </p>
                    <table
                        style={{
                            width: "100%",
                            borderCollapse: "collapse",
                            marginTop: "20px",
                            border: "1px solid #ddd",
                        }}
                    >
                        <thead>
                        <tr style={{ backgroundColor: "#4CAF50", color: "white" }}>
                            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Operator Name</th>
                            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Payment Amount</th>
                            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Upload Date</th>
                            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Details</th>
                        </tr>
                        </thead>
                        <tbody>
                        {payments.map((payment, index) => (
                            <tr
                                key={index}
                                style={{
                                    backgroundColor: index % 2 === 0 ? "#f9f9f9" : "#ffffff",
                                    textAlign: "center",
                                }}
                            >
                                <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                    {payment.toOpName}
                                </td>
                                <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                    {payment.amount.toFixed(2)} €
                                </td>
                                <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                    {payment.date || "No timestamp available"}  {/* ✅ Fix: Use directly */}
                                </td>
                                <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                    {payment.details || "No details provided"}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}
