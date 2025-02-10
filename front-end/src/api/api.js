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

export const submitProof = async (operatorName, file) => {
    const url = `/payments/submitproof?toOpName=${operatorName}`;
    const formData = new FormData();
    formData.append('file', file);
    try {
        const response = await api.post(url, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        console.log('Payment proof submitted successfully.');
        return response.data;
    } catch (error) {
        console.error('Error submitting payment proof:', error.message);
        throw error;
    }
};

// Fetch debts from other operators
export const getDebtsFromOthers = async () => {
    try {
        const response = await api.get('/payments/debtsto');
        return response.data;
    } catch (error) {
        console.error('Error fetching debts from others:', error);
        throw error;
    }
};

// Fetch debts to other operators
export const getMyDebts = async () => {
    try {
        const response = await api.get('/payments/debtsfrom');
        return response.data;
    } catch (error) {
        console.error('Error fetching my debts:', error);
        throw error;
    }
};

export const getMyPayments = async () => {
    try {
        const response = await api.get('/payments/fromOp');
        return response.data;
    } catch (error) {
        console.error('Error fetching my payments:', error);
        throw error;
    }
};

export const getPaymentsFromOthers = async () => {
    try {
        const response = await api.get('/payments/toOp');
        return response.data; // Return the response data
    } catch (error) {
        console.error('Error fetching debts from others:', error.response ? error.response.data : error.message);
        throw error; // Propagate error to the caller
    }
};

//Get All Operators
export const getAllOperators = () => api.get('/passAnalysis/operators');

// Get Passes
export const getPasses = (station, fromDate, toDate) => {
    return api.get(`/tollStationPasses/${station}/${fromDate}/${toDate}`);
};

//Get Stations
export const getStations = () => api.get('/stations');

export const passAnalysis = async (stationOp, tagOp, fromDate, toDate, format = 'json') => {
    return api.get(`/passAnalysis/${stationOp}/${tagOp}/${fromDate}/${toDate}`);
}

export const getPassesCost = async (tollOp, tagOp, fromDate, toDate, format = 'json') => {
    return api.get(`/passesCost/${tollOp}/${tagOp}/${fromDate}/${toDate}`);
}

export const getChargesBy = async (opId, fromDate, toDate, format = 'json') => {
    return api.get(`/chargesBy/${opId}/${fromDate}/${toDate}`);
}

// Get Operator ID
// Get Operator ID and Role
export const getOperatorId = async () => {
    try {
        const response = await api.get('/auth/operatorId'); // Fetch from backend
        return {
            opId: response.data.opId || null, // Handle null cases
            role: response.data.role || 'operator' // Default to 'operator' if not specified
        };
    } catch (error) {
        console.error('Error fetching operator ID and role:', error);
        throw error; // Propagate error to caller
    }
};
