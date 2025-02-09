import React, { useState } from "react";
import DebtsFromOthers from "./DebtsFromOthers";
import MyDebts from "./MyDebts";
import MyPayments from "./MyPayments";
import PaymentsFromOthers from "./PaymentsFromOthers";

export default function DebtsPage() {
    const [selectedOption, setSelectedOption] = useState("myDebts");

    const renderSelectedComponent = () => {
        switch (selectedOption) {
            case "MyDebts":
                return <MyDebts />;
            case "DebtsFromOthers":
                return <DebtsFromOthers />;
            case "PaymentsFromOthers":
                return <PaymentsFromOthers />;
            case "MyPayments":
                return <MyPayments />;
            default:
                return <MyDebts />;
        }
    };

    return (
        <div style={{ padding: "20px", backgroundColor: "#f4f4f4", height: "auto" }}>
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
                    <option value="MyDebts">My Debts</option>
                    <option value="DebtsFromOthers">Debts From Others</option>
                    <option value="MyPayments">My Payments</option>
                    <option value="PaymentsFromOthers">Payments From Others</option>
                </select>
            </div>
            <div style={{minHeight: 220}}>{renderSelectedComponent()}</div>
        </div>
    );
}

