import React, { useState, useEffect } from 'react';
import { getPassesCost, getOperatorId } from '../api/api'; // Import API functions

export default function PassCost() {
    const [tollOpId, setTollOpId] = useState('');
    const [role, setRole] = useState('');
    const [tagOpId, setTagOpId] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [costData, setCostData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const { opId, role } = await getOperatorId(); // Fetch both ID and role
                setTollOpId(opId || ''); // Default to empty if null
                setRole(role); // Store role in state
            } catch (err) {
                console.error('Error fetching operator ID and role:', err);
                setError('Failed to fetch user data.');
            }
        };

        fetchUserData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const formattedDateFrom = fromDate.replace(/-/g, '');
        const formattedDateTo = toDate.replace(/-/g, '');

        try {
            const response = await getPassesCost(tollOpId, tagOpId, formattedDateFrom, formattedDateTo);
            if (response.data) {
                setCostData(response.data);
            } else {
                setCostData(null);
                setError('No data found for the given criteria.');
            }
        } catch (err) {
            console.error('Error fetching pass cost data:', err);
            setError('Failed to fetch pass cost data. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: '20px', backgroundColor: '#f4f4f4' }}>
            <h2 style={{ textAlign: 'center', color: '#4CAF50' }}>Pass Cost Analysis</h2>
            <form
                onSubmit={handleSubmit}
                style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    gap: '10px',
                    marginBottom: '20px',
                }}
            >
                <input
                    type="text"
                    placeholder="Toll Operator ID"
                    value={tollOpId}
                    onChange={(e) => setTollOpId(e.target.value)}
                    readOnly={!(role === 'admin' || role === 'ministry')} // Editable only for admins
                    style={{
                        padding: '10px',
                        width: '300px',
                        borderRadius: '4px',
                        backgroundColor: role === 'admin' || role === 'ministry' ? 'white' : '#e9e9e9',
                        cursor: role === 'admin' || role === 'ministry' ? 'text' : 'not-allowed',
                    }}
                />
                <input
                    type="text"
                    placeholder="Tag Operator ID"
                    value={tagOpId}
                    onChange={(e) => setTagOpId(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <input
                    type="date"
                    placeholder="Date From"
                    value={fromDate}
                    onChange={(e) => setFromDate(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <input
                    type="date"
                    placeholder="Date To"
                    value={toDate}
                    onChange={(e) => setToDate(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <button
                    type="submit"
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                    }}
                >
                    {loading ? 'Fetching...' : 'Fetch Cost'}
                </button>
            </form>

            {loading && <p>Loading...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {costData && (
                <div
                    style={{
                        backgroundColor: '#fff',
                        padding: '20px',
                        borderRadius: '8px',
                        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                        marginTop: '20px',
                    }}
                >
                    <h3>Pass Cost Details</h3>
                    <p><strong>Toll Operator ID:</strong> {costData.tollOpID}</p>
                    <p><strong>Tag Operator ID:</strong> {costData.tagOpID}</p>
                    <p><strong>From:</strong> {costData.periodFrom}</p>
                    <p><strong>To:</strong> {costData.periodTo}</p>
                    <p><strong>Number of Passes:</strong> {costData.nPasses}</p>
                    <p><strong>Total Cost:</strong> ${costData.totalCost.toFixed(2)}</p>
                </div>
            )}

            {!loading && !error && !costData && (
                <p style={{ textAlign: 'center', color: '#777' }}>No data available. Please provide input to fetch cost analysis.</p>
            )}
        </div>
    );
}
