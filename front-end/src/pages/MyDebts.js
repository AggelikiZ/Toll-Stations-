import React, { useState, useEffect } from "react";
import { getMyDebts } from "../api/api";
import { useNavigate } from "react-router-dom";

export default function MyDebts() {
    const [debts, setDebts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchDebts = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await getMyDebts();
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

    const handleDebtClick = (operatorName, debtAmount) => {
        navigate(`/pay-debts?operator=${encodeURIComponent(operatorName)}&amount=${encodeURIComponent(debtAmount)}`);
    };

    return (
        <div style={{ padding: "10px" }}>
            {loading ? (
                <p style={{ textAlign: "center", color: "#555" }}>Loading debts...</p>
            ) : error ? (
                <p style={{ textAlign: "center", color: "red" }}>{error}</p>
            ) : debts.length === 0 ? (
                <p style={{ textAlign: "center", color: "#555", fontSize: "16px"  }}>
                    No debts found to other operators.
                </p>
            ) : (
                <div>
                    <p style={{ textAlign: "left", color: "#555", fontSize: "16px" }}>
                        Current debts to other operators.
                    </p>
                    <p style={{ textAlign: "left", color: "#555", fontSize: "16px" }}>
                        Click on a debt to upload proof of a relevant payment.
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
                        <th style={{ padding: "10px", border: "1px solid #ddd" }}>Debt Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    {debts.map((debt, index) => (
                        <tr
                            key={index}
                            onClick={() => handleDebtClick(debt.fromOpName, debt.debtAmount)}
                            style={{
                                backgroundColor: index % 2 === 0 ? "#f9f9f9" : "#ffffff",
                                textAlign: "center",
                                cursor: "pointer",
                                transition: "background-color 0.3s ease",
                            }}
                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = "#d4edda"}
                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = index % 2 === 0 ? "#f9f9f9" : "#ffffff"}
                        >
                            <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                {debt.fromOpName}
                            </td>
                            <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                {debt.debtAmount.toFixed(2)} €
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
