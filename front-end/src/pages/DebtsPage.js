import React, { useState } from "react";
import DebtsFromOthers from "./DebtsFromOthers"; // Component for "My Debts"
import MyDebts from "./MyDebts"; // Component for "Debts to Others"
import PayDebts from "./PayDebts"; // Component for "Pay Debts"

export default function DebtsPage() {
    const [selectedOption, setSelectedOption] = useState("myDebts");

    const renderSelectedComponent = () => {
        switch (selectedOption) {
            case "DebtsFromOthers":
                return <DebtsFromOthers />;
            case "MyDebts":
                return <MyDebts />;
            case "payDebts":
                return <PayDebts />;
            default:
                return <DebtsFromOthers />;
        }
    };

    return (
        <div style={{ padding: "20px", backgroundColor: "#f4f4f4" }}>
            <h2 style={{ textAlign: "center", color: "#4CAF50" }}>Debts and Payment Management</h2>
            <div style={{ display: "flex", justifyContent: "center", marginBottom: "20px" }}>
                <select
                    value={selectedOption}
                    onChange={(e) => setSelectedOption(e.target.value)}
                    style={{
                        padding: "10px",
                        fontSize: "16px",
                        borderRadius: "5px",
                        border: "1px solid #ccc",
                        cursor: "pointer",
                    }}
                >
                    <option value="DebtsFromOthers">Debts from others</option>
                    <option value="MyDebts">My Debts</option>
                    <option value="payDebts">Pay Debts</option>
                </select>
            </div>
            <div>{renderSelectedComponent()}</div>
        </div>
    );
}

