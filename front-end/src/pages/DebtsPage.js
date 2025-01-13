import React, { useState } from "react";
import MyDebts from "./MyDebts"; // Component for "My Debts"
import DebtsToOthers from "./DebtsToOthers"; // Component for "Debts to Others"
import PayDebts from "./PayDebts"; // Component for "Pay Debts"

export default function DebtsPage() {
    const [selectedOption, setSelectedOption] = useState("myDebts");

    const renderSelectedComponent = () => {
        switch (selectedOption) {
            case "myDebts":
                return <MyDebts />;
            case "debtsToOthers":
                return <DebtsToOthers />;
            case "payDebts":
                return <PayDebts />;
            default:
                return <MyDebts />;
        }
    };

    return (
        <div style={{ padding: "20px", backgroundColor: "#f4f4f4" }}>
            <h2 style={{ textAlign: "center", color: "#4CAF50" }}>Debts Management</h2>
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
                    <option value="myDebts">My Debts</option>
                    <option value="debtsToOthers">Debts to Others</option>
                    <option value="payDebts">Pay Debts</option>
                </select>
            </div>
            <div>{renderSelectedComponent()}</div>
        </div>
    );
}

