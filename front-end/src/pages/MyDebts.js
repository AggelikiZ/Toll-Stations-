import React, { useState, useEffect } from "react";
import { getChargesBy } from "../api/api"; // Adjust the API call accordingly

export default function MyDebts() {
    const [debts, setDebts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchDebts = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await getChargesBy("othersOperatorId"); // Replace with appropriate logic
                setDebts(response.data);
            } catch (err) {
                setError("Failed to fetch debts.");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchDebts();
    }, []);

    return (
        <div>
            {loading ? (
                <p>Loading...</p>
            ) : error ? (
                <p style={{ color: "red" }}>{error}</p>
            ) : (
                <table style={{ width: "100%", borderCollapse: "collapse" }}>
                    <thead>
                    <tr style={{ backgroundColor: "#4CAF50", color: "white" }}>
                        <th style={{ padding: "10px", border: "1px solid #ddd" }}>Operator</th>
                        <th style={{ padding: "10px", border: "1px solid #ddd" }}>Debt</th>
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
                                {debt.visitingOpID}
                            </td>
                            <td style={{ padding: "10px", border: "1px solid #ddd" }}>
                                {debt.passesCost.toFixed(2)}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
