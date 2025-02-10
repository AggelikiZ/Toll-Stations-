import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from 'react-router-dom';
import { FaSignOutAlt } from 'react-icons/fa'; // Logout icon
import { jwtDecode } from 'jwt-decode';
import Dashboard from './pages/Dashboard';
import StationMap from './pages/Stations';
import Passes from './pages/Passes';
import PassAnalysis from './pages/PassAnalysisPage';
import AdminPage from './pages/AdminPage';
import PassCost from './pages/PassCostPage';
import ChargesBy from './pages/ChargesByPage';
import Login from './pages/Login';
import DebtsPage from "./pages/DebtsPage";
import './styles/App.css';
import PayDebts from "./pages/PayDebts";
import PassAnalysisAdmin from "./pages/PassAnalysisAdmin";

export default function App() {
    const [authToken, setAuthToken] = useState(localStorage.getItem('authToken') || null);
    const [userRole, setUserRole] = useState(null);

    useEffect(() => {
        // Sync token and role in localStorage when authToken changes
        if (authToken) {
            localStorage.setItem('authToken', authToken);
            const decodedToken = jwtDecode(authToken);
            setUserRole(decodedToken.role); // Extract role from token
        } else {
            localStorage.removeItem('authToken');
            setUserRole(null);
        }
    }, [authToken]);

    const handleLogout = () => {
        setAuthToken(null); // Clear token state
    };

    const handleLogin = (token) => {
        setAuthToken(token); // Save token to state
    };

    // Conditional rendering of navigation links based on user role
    const renderNavLinks = () => {
        const links = [];

        if (userRole === 'admin') {
            links.push(<Link key="admin" to="/adminpage" className="nav-item">Admin Page</Link>);
            links.push(<Link key="dashboard" to="/dashboard" className="nav-item">Dashboard</Link>);
        }
        links.push(<Link key="stations" to="/stations" className="nav-item">Stations</Link>);
        links.push(<Link key="passes" to="/passes" className="nav-item">Passes</Link>);
        if (userRole === 'operator') {
            links.push(<Link key="passanalysis" to="/passanalysis" className="nav-item">Passes and Cost Analysis</Link>);
        } else {
            links.push(<Link key="passanalysisadmin" to="/passanalysisadmin" className="nav-item">Passes and Cost Analysis</Link>);

        }
        //links.push(<Link key="passcost" to="/passcost" className="nav-item">Pass Cost</Link>);
        links.push(<Link key="chargesby" to="/chargesby" className="nav-item">Charges By</Link>);

        if (userRole === 'operator') {
            links.push(<Link key="debts" to="/debts" className="nav-item">Debts and Payments</Link>);
        }

        return links;
    };

    const getDefaultRoute = () => {
        if (userRole === 'admin') {
            return <Navigate to="/dashboard" />;
        }
        if (userRole === 'operator' || userRole === 'ministry') {
            return <Navigate to="/stations" />;
        }
        return <Navigate to="/" />;
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
                                {renderNavLinks()}
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
                                        <FaSignOutAlt />
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
                                <Route path="/" element={getDefaultRoute()} />
                                <Route path="/adminpage" element={userRole === 'admin' ? <AdminPage /> : <Navigate to="/" />} />
                                <Route path="/dashboard" element={userRole === 'admin' ? <Dashboard /> : <Navigate to="/" />} />
                                <Route path="/stations" element={<StationMap />} />
                                <Route path="/passes" element={<Passes />} />
                                <Route path="/passanalysis" element={<PassAnalysis />} />
                                <Route path="/passanalysisadmin" element={<PassAnalysisAdmin />} />
                                <Route path="/chargesby" element={<ChargesBy />} />
                                <Route path="/debts" element={userRole === 'operator' ? <DebtsPage /> : <Navigate to="/" />} />
                                <Route path="/pay-debts" element={<PayDebts />} />
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
