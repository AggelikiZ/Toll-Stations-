import React, { useState, useEffect } from 'react';
import './Dashboard.css'; // Add a CSS file for styling
import '@fortawesome/fontawesome-free/css/all.min.css'; // Font Awesome for icons

export default function Dashboard() {
    const [healthData, setHealthData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch('/api/admin/healthcheck')
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                setHealthData(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Error fetching health data:', error);
                setError(error.message);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <div className="dashboard-loading">Loading health data...</div>;
    }

    if (error) {
        return <div className="dashboard-error">Error: {error}</div>;
    }

    if (!healthData) {
        return <div className="dashboard-no-data">No health data available. Please try again later.</div>;
    }

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <h1 className="dashboard-title">System Dashboard</h1>
            </header>
            <div className="dashboard-content">
                <div className="dashboard-card">
                    <i className="fas fa-heartbeat icon"></i>
                    <h2>Status</h2>
                    <p>{healthData.status}</p>
                </div>
                <div className="dashboard-card">
                    <i className="fas fa-database icon"></i>
                    <h2>Database Connection</h2>
                    <p>{healthData.dbconnection}</p>
                </div>
                <div className="dashboard-card">
                    <i className="fas fa-building icon"></i>
                    <h2>Number of Stations</h2>
                    <p>{healthData.nStations}</p>
                </div>
                <div className="dashboard-card">
                    <i className="fas fa-tags icon"></i>
                    <h2>Number of Tags</h2>
                    <p>{healthData.nTags}</p>
                </div>
                <div className="dashboard-card">
                    <i className="fas fa-road icon"></i>
                    <h2>Number of Passes</h2>
                    <p>{healthData.nPasses}</p>
                </div>
            </div>
            <footer className="dashboard-footer">
                <p>&copy; 2025 PayWay System. All Rights Reserved.</p>
            </footer>
        </div>
    );
}
