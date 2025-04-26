import React, { createContext, useState, useEffect, useCallback, useMemo } from 'react';
import { loginUser as apiLogin, registerUser as apiRegister } from '../services/apiService'; // Import API functions
import { useNavigate, useLocation } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode'; // Import jwt-decode (npm install jwt-decode)

// Helper function to check if a token is expired
const isTokenExpired = (token) => {
  if (!token) return true;
  try {
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000; // Convert to seconds
    return decoded.exp < currentTime;
  } catch (error) {
    console.error("Error decoding token:", error);
    return true; // Treat invalid token as expired
  }
};

// Create the authentication context
export const AuthContext = createContext(null);

/**
 * Provides authentication state and functions to the application.
 * Manages user data, JWT token, loading states, and login/logout logic.
 */
export const AuthProvider = ({ children }) => {
  // State for user details (null if not logged in)
  const [user, setUser] = useState(null);
  // State for JWT token, initialized from localStorage
  const [token, setToken] = useState(() => localStorage.getItem('authToken'));
  // State to track if initial auth check is complete
  const [authInitialized, setAuthInitialized] = useState(false);
  // State for loading status during login/register attempts
  const [loading, setLoading] = useState(false);
  // State for storing authentication errors
  const [error, setError] = useState(null);

  const navigate = useNavigate();
  const location = useLocation(); // Get location for redirect after login

  // Effect to check token validity and set user state on initial load or token change
  useEffect(() => {
    const storedToken = localStorage.getItem('authToken');
    if (storedToken && !isTokenExpired(storedToken)) {
      try {
        const decodedUser = jwtDecode(storedToken);
        // Set user state based on decoded token claims (adjust claims as needed)
        setUser({
          username: decodedUser.sub, // 'sub' is typically the username/subject
          roles: decodedUser.roles || [], // Get roles if included in token
          // Add other relevant user details if present in token
        });
        setToken(storedToken);
        console.log("AuthContext: User restored from valid token:", decodedUser.sub);
      } catch (err) {
        console.error("AuthContext: Error decoding stored token:", err);
        // Invalid token found, clear storage and state
        localStorage.removeItem('authToken');
        setToken(null);
        setUser(null);
      }
    } else {
      // No valid token found
      if (storedToken) {
         console.log("AuthContext: Stored token is expired or invalid.");
         localStorage.removeItem('authToken'); // Clean up expired token
      }
      setToken(null);
      setUser(null);
    }
    setAuthInitialized(true); // Mark initial check as complete
  }, []); // Run only once on mount

  // Login function
  const login = useCallback(async (credentials) => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiLogin(credentials);
      const receivedToken = response?.data?.token;

      if (receivedToken) {
        if (isTokenExpired(receivedToken)) {
           throw new Error("Received token is already expired.");
        }
        // Decode token to get user info
        const decodedUser = jwtDecode(receivedToken);
        setUser({
          username: decodedUser.sub,
          roles: decodedUser.roles || [],
        });
        setToken(receivedToken); // Update token state
        localStorage.setItem('authToken', receivedToken); // Store token
        console.log("AuthContext: Login successful for", decodedUser.sub);

        // Redirect user back to where they came from or home page
        const from = location.state?.from?.pathname || '/';
        navigate(from, { replace: true });

      } else {
        throw new Error("Login failed: No token received from server.");
      }
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Login failed. Please check credentials.';
      console.error("AuthContext: Login error -", errorMessage);
      setError(errorMessage);
      // Ensure state is clear on failure
      setUser(null);
      setToken(null);
      localStorage.removeItem('authToken');
    } finally {
      setLoading(false);
    }
  }, [navigate, location.state]); // Include dependencies

   // Registration function (example)
   const register = useCallback(async (userData) => {
     setLoading(true);
     setError(null);
     try {
       const response = await apiRegister(userData);
       console.log("AuthContext: Registration successful:", response.data);
       // Optionally auto-login after registration or redirect to login
       // For now, just navigate to login page with a success message (optional)
       navigate('/login', { state: { message: 'Registration successful! Please log in.' } });
       return true; // Indicate success
     } catch (err) {
       const errorMessage = err.response?.data?.message || err.message || 'Registration failed.';
       console.error("AuthContext: Registration error -", errorMessage);
       setError(errorMessage);
       return false; // Indicate failure
     } finally {
       setLoading(false);
     }
   }, [navigate]);


  // Logout function
  const logout = useCallback(() => {
    console.log("AuthContext: Logging out user:", user?.username);
    setUser(null);
    setToken(null);
    localStorage.removeItem('authToken');
    setError(null); // Clear any previous errors
    navigate('/login'); // Redirect to login page after logout
  }, [navigate, user?.username]); // Depend on navigate and username (for logging)

  // Memoize the context value to prevent unnecessary re-renders
  const value = useMemo(() => ({
    user,
    token,
    isAuthenticated: !!token && !isTokenExpired(token), // Check token existence and validity
    authInitialized, // Indicate if initial check is done
    loading,
    error,
    login,
    logout,
    register // Include register if implemented
  }), [user, token, authInitialized, loading, error, login, logout, register]); // Dependencies for useMemo

  // Provide the context value to children components
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};