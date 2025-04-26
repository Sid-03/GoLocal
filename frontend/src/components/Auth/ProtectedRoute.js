import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth'; // Import the custom auth hook
import LoadingSpinner from '../Common/LoadingSpinner'; // Import the loading spinner

/**
 * A component that wraps routes requiring authentication.
 * If the user is authenticated, it renders the child components.
 * If not authenticated, it redirects the user to the login page,
 * preserving the intended destination URL.
 * Shows a loading indicator while authentication status is being checked.
 *
 * @param {object} props - Component props.
 * @param {React.ReactNode} props.children - The child components to render if authenticated.
 */
const ProtectedRoute = ({ children }) => {
  // Get authentication status and initialization status from the AuthContext
  const { isAuthenticated, authInitialized } = useAuth();
  // Get the current location to redirect back after login
  const location = useLocation();

  // If authentication status hasn't been determined yet (initial load), show a loading spinner.
  if (!authInitialized) {
    // You can customize this loading state (e.g., full page spinner)
    return (
      <div className="flex justify-center items-center min-h-[calc(100vh-200px)]">
        <LoadingSpinner size="h-12 w-12" />
      </div>
    );
  }

  // If authentication check is complete and the user is NOT authenticated, redirect to login.
  if (!isAuthenticated) {
    console.log('ProtectedRoute: User not authenticated, redirecting to login.');
    // Redirect to the login page.
    // Pass the current location (`location.pathname`) in the state object.
    // This allows the login page to redirect back to the originally requested page after successful login.
    // `replace` prop prevents adding the current (protected) page to the history stack.
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // If authentication check is complete and the user IS authenticated, render the child components.
  return children;
};

export default ProtectedRoute;