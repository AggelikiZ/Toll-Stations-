import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2'; // Optional for visualizing results
import { passAnalysis } from '../api/api'; // API function
import Chart from 'chart.js/auto';
import 'chartjs-adapter-date-fns'; // For date handling

export default function PassAnalysis() {
    const [stationOp, setStationOp] = useState('');
    const [tagOp, setTagOp] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [passes, setPasses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Cleanup function for chart instances
        return () => {
            const chartInstances = Chart.instances;
            if (chartInstances?.length) {
                chartInstances.forEach((instance) => instance.destroy());
            }
        };
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const formattedDateFrom = fromDate.replace(/-/g, '');
        const formattedDateTo = toDate.replace(/-/g, '');

        passAnalysis(stationOp, tagOp, formattedDateFrom, formattedDateTo)
            .then((response) =>{
                console.log('API Response:', response.data);

                if (response.data && response.data.passList) {
                    setPasses(response.data.passList); // Use passList from API response
                } else {
                    setPasses([]); // Set to empty array if no data is returned
                    setError('No data found for the given criteria.');
                }
        })
        .catch((error) => {
            setError('Failed to fetch Pass Analysis. Please try again.');
            setLoading(false);
        });
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
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
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
                    Fetch Analysis
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
