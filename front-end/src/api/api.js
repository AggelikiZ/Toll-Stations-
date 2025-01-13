import axios from 'axios';

// Axios instance
const api = axios.create({
    baseURL: 'http://localhost:9115/api'
});

// Add a request interceptor to attach the token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken'); // Retrieve the token
        if (token) {
            config.headers['X-OBSERVATORY-AUTH'] = token; // Attach the token to the custom header
        }
        console.log('Request headers:', config.headers); // Debug log
        return config;
    },
    (error) => Promise.reject(error)
);



// Login endpoint
export const login = (username, password) => {
    const formData = new URLSearchParams();
    formData.append('username', username);
    formData.append('password', password);

    return api.post('/login', formData, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    });
};
// Logout endpoint
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
