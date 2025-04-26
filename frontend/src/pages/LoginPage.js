import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth'; // Import useAuth hook
import { Eye, EyeOff } from 'lucide-react';
import LoadingSpinner from '../components/Common/LoadingSpinner'; // Import LoadingSpinner
import ErrorMessage from '../components/Common/ErrorMessage'; // Import ErrorMessage

/**
 * LoginPage component for user authentication.
 */
const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [pageError, setPageError] = useState(null); // For errors specific to this page attempt
  const [successMessage, setSuccessMessage] = useState(null); // For messages like successful registration

  const { login, loading: authLoading, error: authError, isAuthenticated } = useAuth(); // Get login function and state from context
  const navigate = useNavigate();
  const location = useLocation(); // To get potential messages or redirect info

  // Check if user is already authenticated
  useEffect(() => {
    if (isAuthenticated) {
       // If already logged in, redirect away from login page
       const from = location.state?.from?.pathname || '/';
       console.log("LoginPage: Already authenticated, redirecting to", from);
       navigate(from, { replace: true });
    }
     // Check for messages passed via route state (e.g., after registration)
    if (location.state?.message) {
      setSuccessMessage(location.state.message);
      // Clear the message from location state so it doesn't reappear on refresh
      window.history.replaceState({}, document.title);
    }

  }, [isAuthenticated, navigate, location.state]);

  // Combine auth context error and page-specific error
  const displayError = pageError || authError;

  // Handle form submission
  const handleSubmit = async (event) => {
    event.preventDefault();
    setPageError(null); // Clear previous page errors
    setSuccessMessage(null); // Clear success message

    if (!username || !password) {
      setPageError('Please enter both username and password.');
      return;
    }

    // Call the login function from AuthContext
    // Navigation is handled within the login function upon success
    await login({ username, password });
    // Error state (authError) will be updated by the context if login fails
  };

  return (
    <div className="flex items-center justify-center min-h-[calc(100vh-150px)] bg-gradient-to-br from-green-50 via-white to-blue-50 px-4 py-12">
      <div className="w-full max-w-md bg-white p-8 md:p-10 rounded-xl shadow-lg border border-gray-200">
        {/* Logo or Title */}
        <div className="text-center mb-6">
          <Link to="/" className="text-3xl font-bold text-green-700 hover:text-green-800 transition-colors">
            GoLocal
          </Link>
        </div>

        <h2 className="text-3xl font-bold text-center text-gray-900 mb-2">Log in</h2>
        <p className="text-center text-gray-600 mb-6">Welcome back! Please enter your details.</p>

        {/* Display Success Message (e.g., after registration) */}
        {successMessage && (
          <div className="mb-4 p-3 bg-green-50 border border-green-300 text-green-700 rounded-md text-sm text-center">
              {successMessage}
          </div>
        )}

         {/* Display Login Error Message */}
         <ErrorMessage message={displayError} className="mb-4" />

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Username Field */}
          <div>
            <label
              htmlFor="username-login"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              Username
            </label>
            <input
              id="username-login"
              name="username"
              type="text"
              autoComplete="username"
              required
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm"
              placeholder="Enter your username"
              disabled={authLoading} // Disable input while loading
            />
          </div>

          {/* Password Field */}
          <div>
            <div className="flex items-center justify-between mb-1">
              <label
                htmlFor="password-login"
                className="block text-sm font-medium text-gray-700"
              >
                Password
              </label>
              {/* Optional Forgot Password Link */}
              {/* <div className="text-sm">
                <Link to="/forgot-password" className="font-medium text-green-600 hover:text-green-500">
                  Forgot password?
                </Link>
              </div> */}
            </div>
            <div className="relative">
              <input
                id="password-login"
                name="password"
                type={showPassword ? 'text' : 'password'}
                autoComplete="current-password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm pr-10"
                placeholder="Enter your password"
                disabled={authLoading} // Disable input while loading
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-500 hover:text-gray-700 focus:outline-none"
                aria-label={showPassword ? 'Hide password' : 'Show password'}
                disabled={authLoading}
              >
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
          </div>

          {/* Submit Button */}
          <div>
            <button
              type="submit"
              disabled={authLoading} // Disable button while loading
              className={`w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-700 hover:bg-green-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-150 ease-in-out ${
                authLoading ? 'opacity-50 cursor-not-allowed' : ''
              }`}
            >
              {authLoading ? (
                 <>
                    <LoadingSpinner size="h-5 w-5" color="text-white" className="mr-2"/>
                    Logging in...
                 </>
              ) : (
                'Log in'
              )}
            </button>
          </div>
        </form>

        {/* Optional Link to Registration Page */}
        <p className="mt-8 text-center text-sm text-gray-600">
          Don't have an account?{' '}
          <Link to="/register" className="font-medium text-green-600 hover:text-green-500">
            Sign up
          </Link>
           {/* If register isn't a separate page: */}
           {/* <button onClick={() => alert('Sign up not implemented')} className="font-medium text-green-600 hover:text-green-500 bg-transparent border-none cursor-pointer p-0 underline">
             Sign up
           </button> */}
        </p>
      </div>
    </div>
  );
};

export default LoginPage;