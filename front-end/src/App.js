import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import StationMap from './pages/Stations'; // Updated to point to your StationMap component
import Passes from './pages/Passes'; // Placeholder for your Passes component
import './styles/App.css'; // Add a CSS file for styling

export default function App() {
    return (
        <Router>
            <div className="app-container">
                <header className="app-header">
                    <h1>PayWay System</h1>
                    <nav>
                        <ul className="nav-links">
                            <li><Link to="/dashboard">Dashboard</Link></li>
                            <li><Link to="/stations">Stations</Link></li>
                            <li><Link to="/passes">Passes</Link></li>
                        </ul>
                    </nav>
                </header>

                <main className="app-main">
                    <Routes>
                        <Route
                            path="/"
                            element={
                                <div>
                                    <h2>Welcome to PayWay System</h2>
                                    <p>Select an option from the menu to get started.</p>
                                </div>
                            }
                        />
                        <Route path="/dashboard" element={<Dashboard />} />
                        <Route path="/stations" element={<StationMap />} />
                        <Route path="/passes" element={<Passes />} />
                    </Routes>
                </main>

                <footer className="app-footer">
                    <p>&copy; 2025 PayWay System. All Rights Reserved.</p>
                </footer>
            </div>
        </Router>
    );
}
