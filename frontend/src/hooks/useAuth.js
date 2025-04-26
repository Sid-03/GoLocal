import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext'; // Import the context itself

/**
 * Custom hook to easily access the authentication context.
 * Provides a convenient way for components to get auth state and functions.
 *
 * @returns {object} The authentication context value (user, token, isAuthenticated, loading, error, login, logout, register).
 * @throws {Error} If used outside of an AuthProvider.
 */
export const useAuth = () => {
  // Use the built-in useContext hook to access the AuthContext value
  const context = useContext(AuthContext);

  // Check if the context exists (i.e., if the hook is used within an AuthProvider)
  if (context === undefined || context === null) {
    // Throw an error if the hook is used incorrectly
    throw new Error('useAuth must be used within an AuthProvider component');
  }

  // Return the context value
  return context;
};

// No default export needed, just export the hook function