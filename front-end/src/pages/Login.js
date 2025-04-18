import React, { useState } from 'react';
import background from './background.jpg';
import { login } from '../api/api.js';

export default function Login({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        try {
            const response = await login(username, password); // Call API login
            const token = response.data.token;

            if (token) {
                localStorage.setItem('authToken', token); // Save token
                onLogin(token); // Notify parent
            } else {
                throw new Error('Token missing in response');
            }
        } catch (err) {
            console.error('Login error:', err);
            setError('Invalid username or password. Please try again.');
        } finally {
            setLoading(false);
        }
    };


    return (
        <div
            style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                height: '100vh',
                backgroundImage: `url(${background})`, // Ensure the correct path to your image
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                fontFamily: 'Arial, sans-serif',
            }}
        >
            <div
                style={{
                    width: '90%',
                    maxWidth: '400px',
                    padding: '30px',
                    backgroundColor: 'rgba(255, 255, 255, 0.95)', // Slightly transparent white
                    borderRadius: '10px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                }}
            >
                <h1
                    style={{
                        textAlign: 'center',
                        color: '#4CAF50',
                        marginBottom: '20px',
                        fontSize: '24px',
                    }}
                >
                    PayWay System
                </h1>
                <h2
                    style={{
                        textAlign: 'center',
                        color: '#333',
                        marginBottom: '20px',
                        fontSize: '20px',
                    }}
                >
                    Login
                </h2>
                <form onSubmit={handleLogin}>
                    <div style={{ marginBottom: '15px' }}>
                        <input
                            type="text"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            style={{
                                width: '100%',
                                padding: '10px',
                                borderRadius: '5px',
                                border: '1px solid #ccc',
                                fontSize: '16px',
                            }}
                        />
                    </div>
                    <div style={{ marginBottom: '20px' }}>
                        <input
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            style={{
                                width: '100%',
                                padding: '10px',
                                borderRadius: '5px',
                                border: '1px solid #ccc',
                                fontSize: '16px',
                            }}
                        />
                    </div>
                    <button
                        type="submit"
                        style={{
                            width: '100%',
                            padding: '10px',
                            backgroundColor: '#4CAF50',
                            color: 'white',
                            border: 'none',
                            borderRadius: '5px',
                            fontSize: '16px',
                            cursor: 'pointer',
                            fontWeight: 'bold',
                        }}
                    >
                        {loading ? 'Logging in...' : 'Login'}
                    </button>
                </form>
                {error && (
                    <p
                        style={{
                            color: 'red',
                            textAlign: 'center',
                            marginTop: '15px',
                            fontSize: '14px',
                        }}
                    >
                        {error}
                    </p>
                )}
            </div>
        </div>
    );
}
