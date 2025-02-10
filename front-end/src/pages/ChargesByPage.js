import React, { useState, useEffect } from 'react';
import { getChargesBy, getOperatorId, getAllOperators } from '../api/api'; // Import the API functions
import { Bar } from 'react-chartjs-2'; // For visualizing results

export default function ChargesBy() {
    const [operatorId, setOperatorId] = useState('');
    const [role, setRole] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [chargesData, setChargesData] = useState([]);
    const [operators, setOperators] = useState([]); // Stores all operators
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [searched, setSearched] = useState(false); // ✅ Tracks if search was made

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const { opId, role } = await getOperatorId();
                setOperatorId(opId || '');
                setRole(role);
            } catch (err) {
                console.error('Error fetching user data:', err);
                setError('Failed to fetch user data.');
            }
        };

        const fetchOperators = async () => {
            try {
                const response = await getAllOperators();
                if (Array.isArray(response.data)) {
                    setOperators(response.data);
                } else {
                    console.error("Unexpected response format:", response.data);
                    setOperators([]);
                }
            } catch (err) {
                console.error('Error fetching operators:', err);
                setOperators([]);
            }
        };

        fetchUserData();
        fetchOperators();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSearched(true); // ✅ Marks that a search has been made

        const formattedDateFrom = fromDate.replace(/-/g, '');
        const formattedDateTo = toDate.replace(/-/g, '');

        try {
            const response = await getChargesBy(operatorId, formattedDateFrom, formattedDateTo);
            if (response.data && response.data.vOpList) {
                setChargesData(response.data.vOpList);
            } else {
                setChargesData([]);
            }
        } catch (err) {
            console.error('Error fetching charges by operator data:', err);
            setError('Failed to fetch charges. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const getOperatorName = (opId) => {
        const operator = operators.find(op => op.opId === opId);
        return operator ? operator.opName : `Unknown (${opId})`;
    };

    const prepareChartData = () => {
        const labels = chargesData.map((item) => item.visitingOpID); // 🔥 Only use ID in chart
        const data = chargesData.map((item) => item.passesCost);

        return {
            labels,
            datasets: [
                {
                    label: 'Passes Cost by Visiting Operator',
                    data,
                    backgroundColor: 'rgba(76, 175, 80, 0.7)',
                    borderColor: '#4CAF50',
                    borderWidth: 2,
                    barThickness: 35,
                    maxBarThickness: 50,
                },
            ],
        };
    };

    return (
        <div style={{ padding: '20px', backgroundColor: '#f4f4f4', textAlign: 'center', minHeight: 400 }}>
            <h2 style={{ color: '#4CAF50' }}>Charges By All Operators</h2>
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
                {role === 'admin' || role === 'ministry' ? (
                    <select
                        value={operatorId}
                        onChange={(e) => setOperatorId(e.target.value)}
                        required
                        style={{
                            padding: '10px',
                            width: '320px',
                            borderRadius: '4px',
                            fontSize: '16px',
                        }}
                    >
                        <option value="">To Operator</option>
                        {operators.map((op) => (
                            <option key={op.opId} value={op.opId}>
                                {op.opName} ({op.opId})
                            </option>
                        ))}
                    </select>
                ) : null}

                <input
                    type="date"
                    value={fromDate}
                    onChange={(e) => setFromDate(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px', fontSize: '16px' }}
                />
                <input
                    type="date"
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
                <div style={{ width: '100%', maxWidth: '900px', margin: '0 auto' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px', fontSize: '16px' }}>
                        <thead>
                        <tr style={{ backgroundColor: '#4CAF50', color: 'white' }}>
                            <th style={{ padding: '15px', border: '1px solid #ddd' }}>Visiting Operator</th>
                            <th style={{ padding: '15px', border: '1px solid #ddd' }}>Number of Passes</th>
                            <th style={{ padding: '15px', border: '1px solid #ddd' }}>Total Cost (€)</th>
                        </tr>
                        </thead>
                        <tbody>
                        {chargesData.map((item, index) => (
                            <tr key={index} style={{ textAlign: 'center', backgroundColor: index % 2 === 0 ? '#f9f9f9' : '#ffffff' }}>
                                <td style={{ padding: '15px', border: '1px solid #ddd' }}>
                                    {getOperatorName(item.visitingOpID)} ({item.visitingOpID})
                                </td>
                                <td style={{ padding: '15px', border: '1px solid #ddd' }}>{item.nPasses}</td>
                                <td style={{ padding: '15px', border: '1px solid #ddd' }}>{item.passesCost.toFixed(2)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>

                    <div style={{ marginTop: '30px', display: 'flex', justifyContent: 'center' }}>
                        <Bar
                            data={prepareChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    legend: { position: 'top' },
                                },
                                scales: {
                                    y: { beginAtZero: true },
                                },
                            }}
                        />
                    </div>
                </div>
            ) : (
                searched && !loading && !error && (
                    <p style={{ textAlign: 'center' }}>No data found for the given criteria.</p>
                )
            )}
        </div>
    );
}
