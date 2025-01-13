import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from 'react-router-dom';
import { FaCog } from 'react-icons/fa'; // Settings icon
import Dashboard from './pages/Dashboard';
import StationMap from './pages/Stations';
import Passes from './pages/Passes';
import PassAnalysis from './pages/PassAnalysisPage';
import AdminPage from './pages/AdminPage';
import PassCost from './pages/PassCostPage';
import ChargesBy from './pages/ChargesByPage';
import Login from './pages/Login';
import './styles/App.css';
export default function App() {
    const [authToken, setAuthToken] = useState(localStorage.getItem('authToken') || null);

    useEffect(() => {
        // Sync token in localStorage when authToken changes
        if (authToken) {
            localStorage.setItem('authToken', authToken);
        } else {
            localStorage.removeItem('authToken');
        }
    }, [authToken]);

    const handleLogout = () => {
        setAuthToken(null); // Clear token state
    };

    const handleLogin = (token) => {
        setAuthToken(token); // Save token to state
    };

    return (
        <Router>
            <div className="app-container">
                {authToken ? (
                    <>
                        <header
                            className="app-header"
                            style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'space-between',
                                backgroundColor: '#4CAF50',
                                padding: '10px 20px',
                            }}
                        >
                            <h1 style={{ color: 'white', margin: 0 }}>PayWay System</h1>
                            <nav
                                style={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: '20px',
                                    position: 'relative',
                                }}
                            >
                                <Link to="/adminpage" className="nav-item">
                                    AdminPage
                                </Link>
                                <Link to="/dashboard" className="nav-item">
                                    Dashboard
                                </Link>
                                <Link to="/stations" className="nav-item">
                                    Stations
                                </Link>
                                <Link to="/passes" className="nav-item">
                                    Passes
                                </Link>
                                <Link to="/passanalysis" className="nav-item">
                                    PassAnalysis
                                </Link>
                                <Link to="/passcost" className="nav-item">
                                    PassCost
                                </Link>
                                <Link to="/chargesby" className="nav-item">
                                    ChargesBy
                                </Link>
                                <div className="settings-dropdown">
                                    <button
                                        style={{
                                            background: 'none',
                                            border: 'none',
                                            color: 'white',
                                            fontSize: '20px',
                                            cursor: 'pointer',
                                        }}
                                    >
                                        <FaCog />
                                    </button>
                                    <div className="dropdown-menu">
                                        <p onClick={handleLogout} className="logout-option">
                                            Logout
                                        </p>
                                    </div>
                                </div>
                            </nav>
                        </header>

                        <main className="app-main">
                            <Routes>
                                <Route path="/adminpage" element={<AdminPage />} />
                                <Route path="/" element={<Navigate to="/dashboard" />} />
                                <Route path="/dashboard" element={<Dashboard />} />
                                <Route path="/stations" element={<StationMap />} />
                                <Route path="/passes" element={<Passes />} />
                                <Route path="/passanalysis" element={<PassAnalysis />} />
                                <Route path="/passcost" element={<PassCost />} />
                                <Route path="/chargesby" element={<ChargesBy />} />
                            </Routes>
                        </main>

                        <footer className="app-footer">
                            <p>&copy; 2025 PayWay System. All Rights Reserved.</p>
                        </footer>
                    </>
                ) : (
                    <Routes>
                        <Route path="/" element={<Login onLogin={handleLogin} />} />
                        <Route path="*" element={<Navigate to="/" />} />
                    </Routes>
                )}
            </div>
        </Router>
    );
}

