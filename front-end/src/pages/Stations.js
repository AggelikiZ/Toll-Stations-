import React, { useEffect, useState } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { getStations } from '../api/api'; // Assuming you have this API call defined
import 'leaflet.markercluster/dist/leaflet.markercluster'; // Add MarkerCluster library
import 'leaflet.markercluster/dist/MarkerCluster.Default.css'; // Add MarkerCluster CSS


// Define a custom icon
const customIcon = L.icon({
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png', // URL for the pin icon
    iconSize: [25, 41], // Size of the icon
    iconAnchor: [12, 41], // Point of the icon which corresponds to marker's location
    popupAnchor: [1, -34], // Point from which the popup should open relative to the iconAnchor
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png', // Shadow for the icon
    shadowSize: [41, 41], // Size of the shadow
});
export default function Stations() {
    const [stations, setStations] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Initialize the map
        const map = L.map('map', {
            center: [39.0742, 21.8243], // Centered in Greece
            zoom: 6,
        });

        // Add the OpenStreetMap tile layer
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map);

        // Initialize marker cluster group
        const markers = L.markerClusterGroup();

        // Fetch stations from the API
        getStations()
            .then((response) => {
                setStations(response.data);
                setLoading(false);

                // Add markers for each station
                response.data.forEach((station) => {
                    const marker = L.marker([station.lat, station.lng],{ icon: customIcon }).bindPopup(
                        `<strong>${station.name}</strong>`
                    );
                    markers.addLayer(marker); // Add marker to cluster group
                });

                // Add the cluster group to the map
                map.addLayer(markers);
            })
            .catch((error) => {
                console.error('Error fetching stations:', error);
                setLoading(false);
            });

        // Cleanup the map instance on component unmount
        return () => {
            map.remove();
        };
    }, []);

    return (
        <div className="TollStations">
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
                        Toll Stations Map
                    </h2>
                </div>

                {/* Loading State */}
                {loading && <p>Loading map and stations...</p>}

                {/* Map Section */}
                <div
                    id="map"
                    style={{
                        width: '100%',
                        maxWidth: '1200px', // Retain the width of the map
                        height: '500px', // Retain the height of the map
                        border: '1px solid #ccc',
                        borderRadius: '8px',
                        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                    }}
                ></div>
            </div>
        </div>
    );
}
