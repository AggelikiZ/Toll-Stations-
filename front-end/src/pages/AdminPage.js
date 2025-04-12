import React, { useState } from 'react';
import { resetStations, resetPasses, addPasses } from '../api/api';
import '../styles/AdminStyle.css';

export default function AdminPage() {
    const [message, setMessage] = useState('');
    const [file, setFile] = useState(null);
    const [loadingAction, setLoadingAction] = useState(null);

    const handleResetStations = async () => {
        setLoadingAction('resetStations');
        setMessage('Resetting stations...');
        try {
            await resetStations();
            setMessage('Stations reset successfully.');
        } catch (error) {
            console.error('Reset Stations Error:', error.message);
            setMessage('Action failed, please try again.');
        } finally {
            setLoadingAction(null);
        }
    };

    const handleResetPasses = async () => {
        setLoadingAction('resetPasses');
        setMessage('Resetting passes...');
        try {
            await resetPasses();
            setMessage('Passes reset successfully.');
        } catch (error) {
            console.error('Reset Passes Error:', error.message);
            setMessage('Action failed, please try again.');
        } finally {
            setLoadingAction(null);
        }
    };

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleAddPasses = async (event) => {
        event.preventDefault();
        if (!file) {
            setMessage('Please select a file before uploading.');
            return;
        }

        setLoadingAction('uploadPasses');
        setMessage('Uploading passes...');
        try {
            await addPasses(file);
            setMessage('Passes uploaded successfully.');
        } catch (error) {
            console.error('Upload Passes Error:', error.message);
            setMessage('Action failed, please try again.');
        } finally {
            setLoadingAction(null);
        }
    };

    return (
        <div className="admin-tools-container" style={{ padding: '20px', backgroundColor: '#f4f4f4', textAlign: 'center', width: 500, minHeight: 400 }}>
            <div className="admin-tools-header" style={{ padding: '10px' }}>
                <i className="fas fa-tools icon"></i> Admin Tools
            </div>

            {/* Reset Buttons */}
            <div className="admin-buttons-container" style={{ marginBottom: "50px" }}>
                <button
                    className="admin-button reset-stations"
                    onClick={handleResetStations}
                    disabled={loadingAction === 'resetStations'}
                >
                    {loadingAction === 'resetStations' ? 'Resetting...' : 'Reset Stations'}
                </button>
                <button
                    className="admin-button reset-passes"
                    onClick={handleResetPasses}
                    disabled={loadingAction === 'resetPasses'}
                >
                    {loadingAction === 'resetPasses' ? 'Resetting...' : 'Reset Passes'}
                </button>
            </div>

            <div className="add-passes-header" style={{ textAlign: "center", color: "#4caf50" }}>Add Passes</div>
            <form className="add-passes-form" onSubmit={handleAddPasses}>
                <input
                    type="file"
                    accept=".csv"
                    onChange={handleFileChange}
                    disabled={loadingAction === 'uploadPasses'}
                />
                <button type="submit" className="upload-button" disabled={loadingAction === 'uploadPasses'}>
                    {loadingAction === 'uploadPasses' ? 'Uploading...' : 'Upload Passes'}
                </button>
            </form>


            {message && <div className="message">{message}</div>}
        </div>
    );
}
