import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:9115/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Authentication
export const login = (username, password) => api.post('/login', { username, password });
export const logout = () => api.post('/logout');

// Health Check
export const getHealthCheck = () => api.get('/admin/healthcheck');

// Station Management
export const resetStations = () => api.post('/admin/resetstations');

// Pass Management
export const resetPasses = () => api.post('/admin/resetpasses');
export const addPasses = (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post('/admin/addpasses', formData);
};

// Get Passes
export const getPasses = (station, fromDate, toDate) => {
    return api.get(`/tollStationPasses/${station}/${fromDate}/${toDate}`);
};

//Get Stations
export const getStations = () => api.get('/admin/stations');
