import React, { useState, useEffect } from 'react';
import { passAnalysis, getOperatorId } from '../api/api';
import { Line } from 'react-chartjs-2'; // Optional for visualizing results
import Chart from 'chart.js/auto';
import 'chartjs-adapter-date-fns'; // For date handling

export default function PassAnalysis() {
    const [stationOp, setStationOp] = useState(''); // Initialize as empty string
    const [tagOp, setTagOp] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [passes, setPasses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOperatorId = async () => {
            try {
                const opId = await getOperatorId(); // Fetch operator ID from API
                setStationOp(opId); // Set the station operator ID
                console.log("ID", opId);
            } catch (err) {
                console.error('Error fetching operator ID:', err);
                setError('Failed to fetch operator ID.');
            }
        };

        fetchOperatorId();
    }, []); // Fetch only once on component mount

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        // Ensure all required fields are filled
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
                    onChange={(e) => setStationOp(e.target.value)} // Controlled input
                    readOnly // Make it read-only as it is fetched dynamically
                    style={{ padding: '10px', width: '300px', borderRadius: '4px', backgroundColor: '#e9e9e9' }}
                />
                <input
                    type="text"
                    placeholder="Tag Operator ID"
                    value={tagOp}
                    onChange={(e) => setTagOp(e.target.value)} // Controlled input
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <input
                    type="date"
                    placeholder="From Date"
                    value={fromDate}
                    onChange={(e) => setFromDate(e.target.value)} // Controlled input
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <input
                    type="date"
                    placeholder="To Date"
                    value={toDate}
                    onChange={(e) => setToDate(e.target.value)} // Controlled input
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
                                <th style={{ padding: '10px', border: '1px solid #ddd' }}>Pass Index</th>
                                <th style={{ padding: '10px', border: '1px solid #ddd' }}>Pass ID</th>
                                <th style={{ padding: '10px', border: '1px solid #ddd' }}>Timestamp</th>
                                <th style={{ padding: '10px', border: '1px solid #ddd' }}>Station ID</th>
                                <th style={{ padding: '10px', border: '1px solid #ddd' }}>Tag ID</th>
                                <th style={{ padding: '10px', border: '1px solid #ddd' }}>Pass Charge</th>
                            </tr>
                            </thead>
                            <tbody>
                            {passes.map((pass, index) => (
                                <tr
                                    key={index}
                                    style={{
                                        textAlign: 'center',
                                        backgroundColor: index % 2 === 0 ? '#f9f9f9' : '#ffffff',
                                    }}
                                >
                                    <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.passIndex}</td>
                                    <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.passID}</td>
                                    <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.timestamp}</td>
                                    <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.stationID}</td>
                                    <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.tagID}</td>
                                    <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.passCharge}</td>
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
                            key={JSON.stringify(passes)} // Ensure the chart re-renders with new data
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
