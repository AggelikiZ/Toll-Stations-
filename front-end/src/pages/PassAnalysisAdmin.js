import React, { useState, useEffect } from 'react';
import { passAnalysis, getPassesCost, getAllOperators } from '../api/api';
import { Bar, Line } from 'react-chartjs-2';
import Chart from 'chart.js/auto';
import 'chartjs-adapter-date-fns';

export default function PassAnalysisAdmin() {
    const [homeOperator, setHomeOperator] = useState('');
    const [visitingOperator, setVisitingOperator] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [operators, setOperators] = useState([]);
    const [passes, setPasses] = useState([]);
    const [totalCost, setTotalCost] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [searched, setSearched] = useState(false);

    useEffect(() => {
        const fetchOperators = async () => {
            try {
                const response = await getAllOperators();
                if (Array.isArray(response.data)) {
                    setOperators(response.data);
                } else {
                    setOperators([]);
                    console.error("Unexpected response format:", response.data);
                }
            } catch (err) {
                console.error('Error fetching operators:', err);
                setOperators([]);
            }
        };

        fetchOperators();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSearched(true);

        if (!homeOperator || !visitingOperator || !fromDate || !toDate) {
            setError('All fields are required.');
            setLoading(false);
            return;
        }

        const formattedDateFrom = fromDate.replace(/-/g, '');
        const formattedDateTo = toDate.replace(/-/g, '');

        try {
            const passResponse = await passAnalysis(homeOperator, visitingOperator, formattedDateFrom, formattedDateTo);
            if (passResponse.data && passResponse.data.passList) {
                setPasses(passResponse.data.passList);
            }

            const costResponse = await getPassesCost(homeOperator, visitingOperator, formattedDateFrom, formattedDateTo);
            setTotalCost(costResponse.data.passesCost || 0);
        } catch (err) {
            if (err.response.status === 400) {
                setError('Invalid given search criteria');
            } else {
                setError('Failed to fetch analysis. Please try again.');
            }} finally {
            setLoading(false);
        }
    };

    const prepareChartData = () => {
        const passesByMonth = {};
        const passesByStation = {};

        passes.forEach((pass) => {
            const date = new Date(pass.timestamp);
            const month = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`;

            passesByMonth[month] = (passesByMonth[month] || 0) + 1;
            passesByStation[pass.stationID] = (passesByStation[pass.stationID] || 0) + 1;
        });

        return {
            monthlyData: {
                labels: Object.keys(passesByMonth).sort(),
                datasets: [
                    {
                        label: 'Passes Per Month',
                        data: Object.values(passesByMonth),
                        borderColor: '#4CAF50',
                        backgroundColor: 'rgba(76, 175, 80, 0.2)',
                        fill: true,
                    },
                ],
            },
            stationData: {
                labels: Object.keys(passesByStation),
                datasets: [
                    {
                        data: Object.values(passesByStation),
                        backgroundColor: 'rgba(76, 175, 80, 0.7)',
                        borderColor: '#388E3C',
                        borderWidth: 2,
                    },
                ],
            },
        };
    };

    return (
        <div style={{ padding: '20px', backgroundColor: '#f4f4f4', textAlign: 'center', width: 500, minHeight: 400}}>
            <h2 style={{ color: '#4CAF50' }}>Passes and Costs Analysis</h2>

            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '10px', marginBottom: '20px' }}>
                <select value={homeOperator} onChange={(e) => setHomeOperator(e.target.value)} required style={{ padding: '10px', width: '320px', borderRadius: '4px' }}>
                    <option value="">Select Home Operator</option>
                    {operators.map((op) => (
                        <option key={op.opId} value={op.opId}>{op.opName}</option>
                    ))}
                </select>

                <select value={visitingOperator} onChange={(e) => setVisitingOperator(e.target.value)} required style={{ padding: '10px', width: '320px', borderRadius: '4px' }}>
                    <option value="">Select Visiting Operator</option>
                    {operators.map((op) => (
                        <option key={op.opId} value={op.opId}>{op.opName}</option>
                    ))}
                </select>

                <input type="date" value={fromDate} onChange={(e) => setFromDate(e.target.value)} required style={{ padding: '10px', width: '300px', borderRadius: '4px' }} />
                <input type="date" value={toDate} onChange={(e) => setToDate(e.target.value)} required style={{ padding: '10px', width: '300px', borderRadius: '4px' }} />

                <button type="submit" style={{ padding: '10px 20px', backgroundColor: '#4CAF50', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                    {loading ? 'Fetching...' : 'Fetch Analysis'}
                </button>
            </form>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            {passes.length > 0 && !error  && (
                <>
                    <p style={{ textAlign: "left" }}><strong>Total Passes:</strong> {passes.length}</p>
                    <p style={{ textAlign: "left" }}><strong>Total Cost:</strong> {totalCost?.toFixed(2)} €</p>

                    <div style={{ marginBottom: '20px', padding: '20px', backgroundColor: 'white', borderRadius: '8px' }}>
                        <h3 style={{ color: '#4CAF50', marginBottom: '10px' }}>Passes Over Time (Monthly)</h3>
                        <Line key={JSON.stringify(passes)} data={prepareChartData().monthlyData} />
                    </div>

                    <div style={{ padding: '10px', backgroundColor: 'white', borderRadius: '8px' }}>
                        <h3 style={{ color: '#4CAF50', marginBottom: '10px' }}>Passes Per Station</h3>
                        <Bar
                            key={JSON.stringify(passes)}
                            data={prepareChartData().stationData}
                            options={{
                                responsive: true,
                                maintainAspectRatio: false,
                                indexAxis: "y",
                                plugins: { legend: { display: false } },
                                scales: {
                                    x: { beginAtZero: true },
                                    y: { ticks: { autoSkip: false } },
                                },
                                barThickness: 30,
                            }}
                            style={{ height: Math.min(700, passes.length * 30), minHeight: 200, maxHeight: 700 }}
                        />
                    </div>

                    <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
                        <thead>
                        <tr style={{ backgroundColor: '#4CAF50', color: 'white' }}>
                            <th style={{ padding: '10px', border: '1px solid #ddd' }}>Timestamp</th>
                            <th style={{ padding: '10px', border: '1px solid #ddd' }}>Station ID</th>
                            <th style={{ padding: '10px', border: '1px solid #ddd' }}>Tag ID</th>
                            <th style={{ padding: '10px', border: '1px solid #ddd' }}>Pass Charge</th>
                        </tr>
                        </thead>
                        <tbody>
                        {passes.map((pass, index) => (
                            <tr key={index} style={{ textAlign: 'center', backgroundColor: index % 2 === 0 ? '#f9f9f9' : '#ffffff' }}>
                                <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.timestamp}</td>
                                <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.stationID}</td>
                                <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.tagID}</td>
                                <td style={{ padding: '10px', border: '1px solid #ddd' }}>{pass.passCharge} €</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </>
            )}
            {passes.length===0 && !error && searched === true &&(
                    <p style={{ color: "#555" }}>{"No data found for the given criteria."}</p>
                )
            }
        </div>
    );
}
