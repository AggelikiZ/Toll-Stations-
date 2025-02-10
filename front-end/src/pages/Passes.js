import React, { useState, useEffect } from 'react';
import { Line, Bar } from 'react-chartjs-2';
import { getPasses, getStations, getOperatorId } from '../api/api';
import 'chartjs-adapter-date-fns';

export default function TollStationPasses() {
    const [operatorId, setOperatorId] = useState('');
    const [role, setRole] = useState('');
    const [stations, setStations] = useState([]);
    const [stationId, setStationId] = useState('');
    const [dateFrom, setDateFrom] = useState('');
    const [dateTo, setDateTo] = useState('');
    const [passes, setPasses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const { opId, role } = await getOperatorId();
                setOperatorId(opId);
                setRole(role);

                const response = await getStations();
                let availableStations = response.data;

                if (role !== 'admin' && role !== 'ministry') {
                    availableStations = response.data.filter(station => station.opId === opId);
                }

                setStations(availableStations);

                if (availableStations.length > 0) {
                    setStationId(availableStations[0].tollId);
                }
            } catch (err) {
                console.error('Error fetching stations:', err);
                setError('Failed to load stations.');
            }
        };

        fetchData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        if (!stationId || !dateFrom || !dateTo) {
            setError('All fields are required.');
            setLoading(false);
            return;
        }

        const formattedDateFrom = dateFrom.replace(/-/g, '');
        const formattedDateTo = dateTo.replace(/-/g, '');

        try {
            const response = await getPasses(stationId, formattedDateFrom, formattedDateTo);
            if (response.data && response.data.passList) {
                setPasses(response.data.passList);
            } else {
                setPasses([]);
                setError('No data found for the selected period.');
            }
        } catch (err) {
            console.error('Error fetching passes:', err);
            setError('Failed to fetch passes. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const formatTimestamp = (timestamp) => {
        const dateObj = new Date(timestamp);
        return isNaN(dateObj.getTime()) ? "Invalid Date" : dateObj.toISOString().slice(0, 16).replace("T", " ");
    };

    const prepareTimeChartData = () => {
        const passesByMonth = {};

        passes.forEach((pass) => {
            if (!pass.timestamp) return;
            const dateObj = new Date(pass.timestamp);
            if (isNaN(dateObj.getTime())) return;

            const monthYear = `${dateObj.getFullYear()}-${String(dateObj.getMonth() + 1).padStart(2, '0')}`;
            passesByMonth[monthYear] = (passesByMonth[monthYear] || 0) + 1;
        });

        const sortedMonths = Object.keys(passesByMonth).sort();

        return {
            labels: sortedMonths,
            datasets: [
                {
                    label: 'Number of Passes Per Month',
                    data: sortedMonths.map((date) => passesByMonth[date]),
                    borderColor: '#4CAF50',
                    backgroundColor: 'rgba(76, 175, 80, 0.2)',
                    fill: true,
                },
            ],
        };
    };

    const prepareTagChartData = () => {
        const passesByTagProvider = {};

        passes.forEach((pass) => {
            const tagProvider = pass.tagProvider || 'Unknown';
            passesByTagProvider[tagProvider] = (passesByTagProvider[tagProvider] || 0) + 1;
        });

        const sortedProviders = Object.keys(passesByTagProvider).sort();

        return {
            labels: sortedProviders,
            datasets: [
                {
                    label: 'Passes by Tag Provider',
                    data: sortedProviders.map((provider) => passesByTagProvider[provider]),
                    backgroundColor: '#4CAF50',
                    borderColor: '#388E3C',
                    borderWidth: 1,
                },
            ],
        };
    };

    return (
        <div style={{ padding: '20px', backgroundColor: '#f4f4f4', minHeight: 400 }}>
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
                <select
                    value={stationId}
                    onChange={(e) => setStationId(e.target.value)}
                    required
                    style={{ padding: '10px', width: '320px', borderRadius: '4px', fontSize: '16px' }}
                >
                    {stations.length > 0 ? (
                        stations.map((station) => (
                            <option key={station.tollId} value={station.tollId}>
                                {station.name} ({station.tollId})
                            </option>
                        ))
                    ) : (
                        <option value="">No stations available</option>
                    )}
                </select>

                <input type="date" value={dateFrom} onChange={(e) => setDateFrom(e.target.value)} required style={{ padding: '10px', width: '300px', borderRadius: '4px' }} />
                <input type="date" value={dateTo} onChange={(e) => setDateTo(e.target.value)} required style={{ padding: '10px', width: '300px', borderRadius: '4px' }} />
                <button type="submit" style={{ padding: '10px 20px', backgroundColor: '#4CAF50', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                    {loading ? 'Fetching...' : 'Fetch Passes'}
                </button>
            </form>

            {loading && <p>Loading...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {passes.length > 0 && (
                <>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px', gap: '20px' }}>
                        <div style={{ flex: '1', backgroundColor: '#fff', padding: '20px', borderRadius: '8px' }}>
                            <h3 style={{ textAlign: 'center', color: '#4CAF50' }}>Passes Over Time (Monthly)</h3>
                            <Line data={prepareTimeChartData()} />
                        </div>

                        <div style={{ flex: '1', backgroundColor: '#fff', padding: '20px', borderRadius: '8px' }}>
                            <h3 style={{ textAlign: 'center', color: '#4CAF50' }}>Passes by Tag Provider</h3>
                            <Bar data={prepareTagChartData()} />
                        </div>
                    </div>

                    <div style={{ backgroundColor: '#fff', padding: '20px', marginTop: '20px', borderRadius: '8px' }}>
                        <h3 style={{ textAlign: 'center', color: '#4CAF50', marginBottom: '15px' }}>Pass Details</h3>
                        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '16px', textAlign: 'center' }}>
                            <thead>
                            <tr style={{ backgroundColor: '#4CAF50', color: 'white' }}>
                                <th>Timestamp</th>
                                <th>Tag Provider</th>
                                <th>Pass Charge</th>
                            </tr>
                            </thead>
                            <tbody>
                            {passes.map((pass, index) => (
                                <tr key={pass.passID} style={{ backgroundColor: index % 2 === 0 ? '#f9f9f9' : '#ffffff' }}>
                                    <td>{formatTimestamp(pass.timestamp)}</td>
                                    <td>{pass.tagProvider}</td>
                                    <td>{pass.passCharge}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </>
            )}
        </div>
    );
}

