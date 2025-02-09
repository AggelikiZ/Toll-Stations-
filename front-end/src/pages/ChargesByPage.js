import React, { useState, useEffect } from 'react';
import { getChargesBy, getOperatorId } from '../api/api'; // Import the required API functions
import { Bar } from 'react-chartjs-2'; // For visualizing results
import Chart from 'chart.js/auto';

export default function ChargesBy() {
    const [operatorId, setOperatorId] = useState('');
    const [role, setRole] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [chargesData, setChargesData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const { opId, role } = await getOperatorId(); // Fetch both ID and role
                setOperatorId(opId || ''); // Default to empty if null
                setRole(role); // Store role in state
            } catch (err) {
                console.error('Error fetching user data:', err);
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
            const response = await getChargesBy(operatorId, formattedDateFrom, formattedDateTo);
            if (response.data && response.data.vOpList) {
                setChargesData(response.data.vOpList);
            } else {
                setChargesData([]);
                setError('No data found for the given criteria.');
            }
        } catch (err) {
            console.error('Error fetching charges by operator data:', err);
            setError('Failed to fetch charges. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const prepareChartData = () => {
        const labels = chargesData.map((item) => item.visitingOpID);
        const data = chargesData.map((item) => item.passesCost);

        return {
            labels,
            datasets: [
                {
                    label: 'Passes Cost by Visiting Operator',
                    data,
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                },
            ],
        };
    };

    return (
        <div
            style={{
                padding: '20px',
                backgroundColor: '#f4f4f4',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
            }}
        >
            <h2 style={{ textAlign: 'center', color: '#4CAF50' }}>Charges By Operator</h2>
            <form
                onSubmit={handleSubmit}
                style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    gap: '15px',
                    marginBottom: '20px',
                }}
            >
                <input
                    type="text"
                    placeholder="Operator ID"
                    value={operatorId}
                    onChange={(e) => setOperatorId(e.target.value)}
                    readOnly={!(role === 'admin' || role === 'ministry')} // Editable only for admin users
                    style={{
                        padding: '10px',
                        width: '300px',
                        borderRadius: '4px',
                        backgroundColor: role === 'admin' || role === 'ministry' ? 'white' : '#e9e9e9',
                        cursor: role === 'admin' || role === 'ministry' ? 'text' : 'not-allowed',
                    }}
                />
                <input
                    type="date"
                    placeholder="From Date"
                    value={fromDate}
                    onChange={(e) => setFromDate(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px', fontSize: '16px' }}
                />
                <input
                    type="date"
                    placeholder="To Date"
                    value={toDate}
                    onChange={(e) => setToDate(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px', fontSize: '16px' }}
                />
                <button
                    type="submit"
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        fontSize: '16px',
                        cursor: 'pointer',
                    }}
                >
                    {loading ? 'Fetching...' : 'Fetch Charges'}
                </button>
            </form>

            {loading && <p>Loading...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {chargesData.length > 0 ? (
                <div style={{ width: '80%', maxWidth: '900px' }}>
                    <div>
                        <table
                            style={{
                                width: '100%',
                                borderCollapse: 'collapse',
                                marginTop: '20px',
                                fontSize: '16px',
                                margin: '0 auto',
                            }}
                        >
                            <thead>
                            <tr style={{ backgroundColor: '#4CAF50', color: 'white' }}>
                                <th style={{ padding: '15px', border: '1px solid #ddd' }}>
                                    Visiting Operator ID
                                </th>
                                <th style={{ padding: '15px', border: '1px solid #ddd' }}>
                                    Number of Passes
                                </th>
                                <th style={{ padding: '15px', border: '1px solid #ddd' }}>
                                    Total Cost
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            {chargesData.map((item, index) => (
                                <tr
                                    key={index}
                                    style={{
                                        textAlign: 'center',
                                        backgroundColor: index % 2 === 0 ? '#f9f9f9' : '#ffffff',
                                    }}
                                >
                                    <td style={{ padding: '15px', border: '1px solid #ddd' }}>
                                        {item.visitingOpID}
                                    </td>
                                    <td style={{ padding: '15px', border: '1px solid #ddd' }}>
                                        {item.nPasses}
                                    </td>
                                    <td style={{ padding: '15px', border: '1px solid #ddd' }}>
                                        {item.passesCost.toFixed(2)}
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                    <div style={{ marginTop: '30px', display: 'flex', justifyContent: 'center' }}>
                        <Bar
                            data={prepareChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    legend: { position: 'top' },
                                    title: { display: true, text: 'Charges by Visiting Operator' },
                                },
                                scales: {
                                    y: {
                                        beginAtZero: true,
                                    },
                                },
                            }}
                        />
                    </div>
                </div>
            ) : (
                !loading && <p style={{ textAlign: 'center' }}>No data found for the given criteria.</p>
            )}
        </div>
    );
}
