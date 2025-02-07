import React, { useState, useEffect } from 'react';
import { passAnalysis, getOperatorId } from '../api/api';
import { Line } from 'react-chartjs-2'; // Chart for visualization
import Chart from 'chart.js/auto';
import 'chartjs-adapter-date-fns'; // For date handling

export default function PassAnalysis() {
    const [stationOp, setStationOp] = useState('');
    const [role, setRole] = useState('');
    const [tagOp, setTagOp] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [passes, setPasses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const { opId, role } = await getOperatorId();
                setStationOp(opId || '');
                setRole(role);
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

        if (!stationOp || !tagOp || !fromDate || !toDate) {
            setError('All fields are required.');
            setLoading(false);
            return;
        }

        const formattedDateFrom = fromDate.replace(/-/g, '');
        const formattedDateTo = toDate.replace(/-/g, '');

        try {
            const response = await passAnalysis(
                stationOp,
                tagOp,
                formattedDateFrom,
                formattedDateTo
            );

            if (response.data && response.data.passList) {
                setPasses(response.data.passList);
            } else {
                setPasses([]);
                setError('No data found for the given criteria.');
            }
        } catch (err) {
            console.error('Error fetching pass analysis:', err);
            setError('Failed to fetch Pass Analysis. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const prepareChartData = () => {
        const passesByDate = {};
        passes.forEach((pass) => {
            const date = pass.timestamp.split('T')[0]; // Extract date (YYYY-MM-DD)
            passesByDate[date] = (passesByDate[date] || 0) + 1; // Increment count
        });

        const sortedDates = Object.keys(passesByDate).sort();

        return {
            labels: sortedDates,
            datasets: [
                {
                    label: 'Number of Passes Over Time',
                    data: sortedDates.map((date) => passesByDate[date]),
                    borderColor: '#4CAF50',
                    backgroundColor: 'rgba(76, 175, 80, 0.2)',
                    fill: true,
                },
            ],
        };
    };

    return (
        <div style={{ padding: '20px', backgroundColor: '#f4f4f4' }}>
            <h2 style={{ textAlign: 'center', color: '#4CAF50' }}>Pass Analysis</h2>
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
                    placeholder="Station Operator ID"
                    value={stationOp}
                    onChange={(e) => setStationOp(e.target.value)}
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
                    value={tagOp}
                    onChange={(e) => setTagOp(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <input
                    type="date"
                    placeholder="From Date"
                    value={fromDate}
                    onChange={(e) => setFromDate(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <input
                    type="date"
                    placeholder="To Date"
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
                    {loading ? 'Fetching...' : 'Fetch Analysis'}
                </button>
            </form>

            {loading && <p>Loading...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {passes.length > 0 && (
                <>
                    <div
                        style={{
                            backgroundColor: '#fff',
                            padding: '20px',
                            borderRadius: '8px',
                            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                            marginBottom: '20px',
                        }}
                    >
                        <h3>Passes:</h3>
                        <table
                            style={{
                                width: '100%',
                                borderCollapse: 'collapse',
                                marginTop: '10px',
                            }}
                        >
                            <thead>
                            <tr style={{ backgroundColor: '#4CAF50', color: 'white' }}>
                                <th>Pass Index</th>
                                <th>Pass ID</th>
                                <th>Timestamp</th>
                                <th>Station ID</th>
                                <th>Tag ID</th>
                                <th>Pass Charge</th>
                            </tr>
                            </thead>
                            <tbody>
                            {passes.map((pass, index) => (
                                <tr key={index} style={{ textAlign: 'center', backgroundColor: index % 2 === 0 ? '#f9f9f9' : '#ffffff' }}>
                                    <td>{pass.passIndex}</td>
                                    <td>{pass.passID}</td>
                                    <td>{pass.timestamp}</td>
                                    <td>{pass.stationID}</td>
                                    <td>{pass.tagID}</td>
                                    <td>{pass.passCharge}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                    <div
                        style={{
                            backgroundColor: '#fff',
                            padding: '20px',
                            borderRadius: '8px',
                            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                        }}
                    >
                        <Line
                            key={JSON.stringify(passes)}
                            data={prepareChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    legend: { position: 'top' },
                                    title: { display: true, text: 'Pass Analysis Over Time' },
                                },
                                scales: {
                                    x: { title: { display: true, text: 'Date (Month/Year)' } },
                                    y: {
                                        title: { display: true, text: 'Number of Passes' },
                                        beginAtZero: true,
                                        ticks: { stepSize: 1 },
                                    },
                                },
                            }}
                        />
                    </div>
                </>
            )}
        </div>
    );
}
