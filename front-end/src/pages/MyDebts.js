import React, { useState, useEffect } from "react";
import { getMyDebts } from "../api/api";

export default function MyDebts() {
    const [debts, setDebts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchDebts = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await getMyDebts(); // Fetch data using the new endpoint
                setDebts(response);
            } catch (err) {
                setError("Failed to fetch my debts. Please try again.");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchDebts();
    }, []);

    return (
        <div style={{ padding: "20px" }}>
            <h2 style={{ textAlign: "center", color: "#4CAF50" }}>My Debts</h2>

            {loading ? (
                <p style={{ textAlign: "center", color: "#555" }}>Loading debts...</p>
            ) : error ? (
                <p style={{ textAlign: "center", color: "red" }}>{error}</p>
            ) : debts.length === 0 ? (
                <p style={{ textAlign: "center", color: "#555" }}>
                    No debts found to other operators.
                </p>
            ) : (
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
                        <th style={{ padding: "10px", border: "1px solid #ddd" }}>Debt Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    {debts.map((debt, index) => (
                        <tr
                            key={index}
                            style={{
                                backgroundColor: index % 2 === 0 ? "#f9f9f9" : "#ffffff",
                                textAlign: "center",
                            }}
                        >
                            <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                {debt.fromOpName}
                            </td>
                            <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                {debt.debtAmount.toFixed(2)}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

