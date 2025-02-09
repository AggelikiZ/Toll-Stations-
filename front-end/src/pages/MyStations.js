import React, { useEffect, useState } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { getStations, getOperatorId, getPasses } from '../api/api'; // Import API functions
import 'leaflet.markercluster/dist/leaflet.markercluster'; // Cluster library
import 'leaflet.markercluster/dist/MarkerCluster.Default.css'; // Cluster CSS
import { Bar } from 'react-chartjs-2';
import Chart from 'chart.js/auto';

// Define a custom map marker icon
const customIcon = L.icon({
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
    shadowSize: [41, 41],
});

export default function MyStations() {
    const [stations, setStations] = useState([]); // Filtered stations
    const [operatorId, setOperatorId] = useState(''); // Logged-in user's opId
    const [loading, setLoading] = useState(true);
    const [passesData, setPassesData] = useState({});
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                // ✅ Fetch the logged-in user's operator ID
                const { opId } = await getOperatorId();
                setOperatorId(opId);

                // ✅ Fetch all stations
                const response = await getStations();
                const filteredStations = response.data.filter(station => station.opId === opId); // Filter by opId
                setStations(filteredStations);

                // ✅ Fetch pass statistics for each station
                const passStats = {};
                for (const station of filteredStations) {
                    const passesResponse = await getPasses(station.tollId, '20220101', '20221212'); // Adjust date range
                    passStats[station.tollId] = passesResponse.data.length; // Store number of passes
                }

                setPassesData(passStats);
                setLoading(false);
            } catch (err) {
                console.error('Error fetching data:', err);
                setError('Failed to load station data.');
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        if (stations.length === 0 || loading) return;

        // Initialize the map
        const map = L.map('map', { center: [39.0742, 21.8243], zoom: 6 });

        // Add the OpenStreetMap tile layer
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map);

        // Initialize marker cluster group
        const markers = L.markerClusterGroup();

        // Add markers for each station
        stations.forEach((station) => {
            const marker = L.marker([station.lat, station.lng], { icon: customIcon }).bindPopup(
                `<strong>${station.name}</strong><br/>Passes: ${passesData[station.tollId] || 0}`
            );
            markers.addLayer(marker);
        });

        // Add the cluster group to the map
        map.addLayer(markers);

        return () => {
            map.remove();
        };
    }, [stations, passesData, loading]);

    // ✅ Prepare chart data for Passes Per Station
    const prepareChartData = () => {
        const stationNames = stations.map(station => station.name);
        const passCounts = stations.map(station => passesData[station.tollId] || 0);

        return {
            labels: stationNames,
            datasets: [
                {
                    label: 'Number of Passes per Station',
                    data: passCounts,
                    backgroundColor: '#4CAF50', // ✅ Same green color
                    borderColor: '#388E3C',
                    borderWidth: 1,
                },
            ],
        };
    };

    return (
        <div className="MyStations">
            <div
                style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    backgroundColor: '#f4f4f4',
                    padding: '20px',
                    minWidth: '40vw',
                    minHeight: '80vh',
                }}
            >
                {/* Title Section */}
                <div style={{ marginBottom: '20px', textAlign: 'center' }}>
                    <h2
                        style={{
                            fontSize: '1.8em',
                            color: '#4CAF50',
                            fontWeight: 'bold',
                            margin: '0',
                        }}
                    >
                        My Toll Stations
                    </h2>
                </div>

                {/* Loading/Error State */}
                {loading && <p>Loading map and stations...</p>}
                {error && <p style={{ color: 'red' }}>{error}</p>}

                {/* Map Section */}
                <div
                    id="map"
                    style={{
                        width: '100%',
                        maxWidth: '1200px',
                        height: '500px',
                        border: '1px solid #ccc',
                        borderRadius: '8px',
                        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                    }}
                ></div>

                {/* ✅ Pass Statistics Chart */}
                {stations.length > 0 && (
                    <div
                        style={{
                            backgroundColor: '#fff',
                            padding: '20px',
                            borderRadius: '8px',
                            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                            marginTop: '20px',
                            width: '80%',
                        }}
                    >
                        <h3 style={{ textAlign: 'center', color: '#4CAF50' }}>Pass Statistics</h3>
                        <Bar
                            data={prepareChartData()}
                            options={{
                                responsive: true,
                                plugins: {
                                    legend: { display: false },
                                    title: { display: true, text: 'Passes Per Toll Station' },
                                },
                                scales: {
                                    x: { title: { display: true, text: 'Station Name' } },
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
        </div>
    );
}
