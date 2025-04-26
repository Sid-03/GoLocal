import axios from 'axios';

// Get the API Gateway URL from environment variables
const API_GATEWAY_URL = process.env.REACT_APP_API_GATEWAY_URL;

// Create an Axios instance with the base URL pre-configured
const apiClient = axios.create({
  baseURL: API_GATEWAY_URL,
  headers: {
    'Content-Type': 'application/json',
    // Add other default headers if needed
  },
});

// --- Interceptor to add JWT token to requests ---
apiClient.interceptors.request.use(
  (config) => {
    // Retrieve the token from local storage (or context/state management)
    const token = localStorage.getItem('authToken');
    if (token) {
      // If token exists, add it to the Authorization header
      config.headers['Authorization'] = `Bearer ${token}`;
      // console.log('Token added to request:', config.url); // For debugging
    } else {
      // console.log('No token found for request:', config.url); // For debugging
    }
    return config; // Return the modified config
  },
  (error) => {
    // Handle request errors (e.g., network issues)
    console.error('Axios request interceptor error:', error);
    return Promise.reject(error);
  }
);

// --- Interceptor for handling common responses/errors ---
apiClient.interceptors.response.use(
  (response) => {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Optionally process successful responses here
    return response;
  },
  (error) => {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    console.error('Axios response error:', error.response || error.message);

    // Handle specific error statuses globally if needed
    if (error.response) {
      const { status } = error.response;
      if (status === 401) {
        // Unauthorized: Token might be expired or invalid
        console.warn('API request returned 401 Unauthorized. Token might be invalid/expired.');
        // Optional: Trigger logout logic here
        // Example: Dispatch logout action or clear token from storage
        // localStorage.removeItem('authToken');
        // window.location.href = '/login'; // Force redirect (can be disruptive)
      } else if (status === 403) {
        // Forbidden: User doesn't have permission
        console.warn('API request returned 403 Forbidden. User lacks permission.');
      }
      // You might want to parse the error response body for specific messages
      // return Promise.reject(error.response.data || error.message);
    }

    // Return the rejected promise so components can handle specific errors
    return Promise.reject(error);
  }
);


// === API Service Functions ===

// --- Authentication ---
/**
 * Logs in a user.
 * @param {object} credentials - { username, password }
 * @returns {Promise<axios.AxiosResponse<any>>} Promise resolving with the Axios response (contains token in data).
 */
export const loginUser = (credentials) => {
  // No token needed for login request itself
  return axios.post(`${API_GATEWAY_URL}/auth/login`, credentials, {
     headers: { 'Content-Type': 'application/json' } // Ensure correct header for public endpoint if not using interceptor
  });
  // Or if interceptor correctly skips adding token for this path:
  // return apiClient.post('/auth/login', credentials);
};

/**
 * Registers a new user.
 * @param {object} userData - { username, email, password }
 * @returns {Promise<axios.AxiosResponse<any>>} Promise resolving with the Axios response.
 */
export const registerUser = (userData) => {
   // No token needed for registration
   return axios.post(`${API_GATEWAY_URL}/auth/register`, userData, {
      headers: { 'Content-Type': 'application/json' }
   });
  // return apiClient.post('/auth/register', userData);
};

// --- Products ---
/**
 * Fetches products, optionally filtering by search term.
 * @param {string} [searchTerm=''] - Optional search term.
 * @returns {Promise<axios.AxiosResponse<any>>} Promise resolving with the list of products.
 */
export const fetchProducts = (searchTerm = '') => {
  // Public endpoint, token might or might not be sent depending on interceptor logic
  return apiClient.get('/products', {
    params: { search: searchTerm || undefined }, // Send param only if it has value
  });
};

/**
 * Fetches a single product by its ID.
 * @param {string | number} productId - The ID of the product.
 * @returns {Promise<axios.AxiosResponse<any>>} Promise resolving with the product details.
 */
export const fetchProductById = (productId) => {
  // Public endpoint
  return apiClient.get(`/products/${productId}`);
};

// --- Inquiries ---
/**
 * Submits a new product inquiry. Requires authentication (token added by interceptor).
 * @param {object} inquiryData - { productId, subject, message }
 * @returns {Promise<axios.AxiosResponse<any>>} Promise resolving with the created inquiry details.
 */
export const submitInquiry = (inquiryData) => {
  // Protected endpoint - Interceptor MUST add the token
  return apiClient.post('/inquiries', inquiryData);
};

/**
 * Fetches inquiries submitted by the currently authenticated user. Requires authentication.
 * @returns {Promise<axios.AxiosResponse<any>>} Promise resolving with the list of user's inquiries.
 */
export const fetchMyInquiries = () => {
  // Protected endpoint - Interceptor MUST add the token
  return apiClient.get('/inquiries/my-inquiries');
};

// Add other API functions as needed (update product, delete inquiry, etc.)

export default apiClient; // Export the configured instance if needed elsewhere