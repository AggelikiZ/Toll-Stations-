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

export const addPasses = async (file) => {
    const url = '/admin/addpasses';
    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await api.post(url, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            timeout: 20000, // Timeout set to 20 seconds
        });
        console.log('Passes added successfully.');
        return response.data;
    } catch (error) {
        if (error.response) {
            console.error('Error adding passes:', error.response.data.message || error.response.statusText);
        } else if (error.code === 'ECONNABORTED') {
            console.error('Error: The request timed out.');
        } else {
            console.error('Error adding passes:', error.message);
        }
        throw error; // Propagate error to the caller
    }
};

// Get Passes
export const getPasses = (station, fromDate, toDate) => {
    return api.get(`/tollStationPasses/${station}/${fromDate}/${toDate}`);
};

//Get Stations
export const getStations = () => api.get('/admin/stations');

export const passAnalysis = async (stationOp, tagOp, fromDate, toDate, format = 'json') => {
    return api.get(`/passAnalysis/${stationOp}/${tagOp}/${fromDate}/${toDate}`);

}

export const getPassesCost = async (tollOp, tagOp, fromDate, toDate, format = 'json') => {
    return api.get(`/passesCost/${tollOp}/${tagOp}/${fromDate}/${toDate}`);
}

export const getChargesBy = async (opId, fromDate, toDate, format = 'json') => {
    return api.get(`/chargesBy/${opId}/${fromDate}/${toDate}`);
}
