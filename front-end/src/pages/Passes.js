import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2';
import { getPasses } from '../api/api'; // Assume you have an API function for fetching passes
import Chart from 'chart.js/auto';
import 'chartjs-adapter-date-fns'; // Import the date adapter

export default function TollStationPasses() {
    const [stationId, setStationId] = useState('');
    const [dateFrom, setDateFrom] = useState('');
    const [dateTo, setDateTo] = useState('');
    const [passes, setPasses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Cleanup function to destroy chart instances
        return () => {
            const chartInstances = Chart.instances;
            if (chartInstances && chartInstances.length > 0) {
                chartInstances.forEach((instance) => instance.destroy());
            }
        };
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const formattedDateFrom = dateFrom.replace(/-/g, '');
        const formattedDateTo = dateTo.replace(/-/g, '');

        getPasses(stationId, formattedDateFrom, formattedDateTo)
            .then((response) => {
                if (response.data && response.data.passList) {
                    setPasses(response.data.passList); // Use passList from API response
                } else {
                    setPasses([]); // Set to an empty array if no data is returned
                }
                setLoading(false);
            })
            .catch((error) => {
                setError('Failed to fetch passes. Please try again.');
                setLoading(false);
            });
    };

    const prepareChartData = () => {
        const passesByMonth = {};
        passes.forEach((pass) => {
            // Extract the "YYYY-MM" part from the date string (first 7 characters)
            const monthYear = pass.timestamp.slice(0, 7); // e.g., "2022-01"
            passesByMonth[monthYear] = (passesByMonth[monthYear] || 0) + 1; // Increment count for the month
        });
        const sortedDates = Object.keys(passesByMonth).sort();

        return {
            labels: sortedDates, // Dates must be in ISO 8601 format (e.g., YYYY-MM-DD)
            datasets: [
                {
                    label: 'Number of Passes Over Time',
                    data: sortedDates.map((date) => passesByMonth[date]),
                    borderColor: '#4CAF50',
                    backgroundColor: 'rgba(76, 175, 80, 0.2)',
                    fill: true,
                },
            ],
        };
    };



    return (
        <div style={{ padding: '20px', backgroundColor: '#f4f4f4' }}>
            <h2 style={{ textAlign: 'center', color: '#4CAF50' }}>Toll Station Passes</h2>
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
                    placeholder="Toll Station ID"
                    value={stationId}
                    onChange={(e) => setStationId(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <input
                    type="date"
                    placeholder="Date From"
                    value={dateFrom}
                    onChange={(e) => setDateFrom(e.target.value)}
                    required
                    style={{ padding: '10px', width: '300px', borderRadius: '4px' }}
                />
                <input
                    type="date"
                    placeholder="Date To"
                    value={dateTo}
                    onChange={(e) => setDateTo(e.target.value)}
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
                    Fetch Passes
                </button>
            </form>
            {loading && <p>Loading...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {passes.length > 0 && (
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
                            <th style={{ padding: '10px', border: '1px solid #ddd' }}>Tag ID</th>
                            <th style={{ padding: '10px', border: '1px solid #ddd' }}>Tag Provider</th>
                            <th style={{ padding: '10px', border: '1px solid #ddd' }}>Pass Type</th>
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
                                <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.tagID}</td>
                                <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.tagProvider}</td>
                                <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.passType}</td>
                                <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.passCharge}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
            {passes.length > 0 && (
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
                                title: { display: true, text: 'Passes Over Time' },
                            },
                            scales: {
                                x: {
                                    title: { display: true, text: 'Date (Month/Year)' }
                                },
                                y: {
                                    title: { display: true, text: 'Number of Passes' },
                                    beginAtZero: true,
                                    ticks: { stepSize: 1 },
                                },
                            },
                        }}
                    />
                </div>
            )}
        </div>
    );
}