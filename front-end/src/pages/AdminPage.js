import React, { useState } from 'react';
import { resetStations, resetPasses, addPasses } from '../api/api'; // Import all API functions
import '../styles/AdminStyle.css';

export default function AdminPage() {
    const [message, setMessage] = useState('');
    const [file, setFile] = useState(null);

    const handleResetStations = async () => {
        try {
            await resetStations();
            setMessage('Stations reset successfully.');
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    const handleResetPasses = async () => {
        try {
            await resetPasses();
            setMessage('Passes reset successfully.');
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleAddPasses = async (event) => {
        event.preventDefault(); // Prevent the default form submission
        if (!file) {
            setMessage('Please select a file before uploading.');
            return;
        }
        try {
            await addPasses(file);
            setMessage('Passes uploaded successfully.');
        } catch (error) {
            setMessage(`Error uploading passes: ${error.message}`);
        }
    };

    return (
        <div className="admin-tools-container">
            <div className="admin-tools-header">
                <i className="fas fa-tools icon"></i> Admin Tools
            </div>

            {/* Reset Buttons */}
            <div className="admin-buttons-container">
                <button
                    className="admin-button reset-stations"
                    onClick={handleResetStations}
                >
                    Reset Stations
                </button>
                <button
                    className="admin-button reset-passes"
                    onClick={handleResetPasses}
                >
                    Reset Passes
                </button>
            </div>

            {/* Add Passes Section */}
            <div className="add-passes-header">Add Passes</div>
            <form className="add-passes-form" onSubmit={handleAddPasses}>
                <input
                    type="file"
                    accept=".csv"
                    onChange={handleFileChange}
                />
                <button type="submit" className="upload-button">
                    Upload Passes
                </button>
            </form>

            {/* Message Section */}
            {message && <div className="message">{message}</div>}
        </div>
    );
}
