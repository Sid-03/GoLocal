import React from 'react';
import { Link } from 'react-router-dom';
import { AlertTriangle, Home } from 'lucide-react'; // Icon for 404

/**
 * Simple 404 Not Found page component.
 */
const NotFoundPage = () => {
  return (
    <div className="flex flex-col items-center justify-center text-center px-4 py-16 min-h-[calc(100vh-200px)] bg-gray-100">
      <AlertTriangle className="w-16 h-16 text-yellow-500 mb-4" />
      <h1 className="text-4xl md:text-6xl font-bold text-gray-800 mb-2">404</h1>
      <h2 className="text-2xl md:text-3xl font-semibold text-gray-700 mb-4">Page Not Found</h2>
      <p className="text-gray-600 mb-8 max-w-md">
        Oops! The page you are looking for doesn't seem to exist. It might have been moved, deleted, or maybe you just mistyped the URL.
      </p>
      <Link
        to="/"
        className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-green-700 hover:bg-green-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-150 ease-in-out"
      >
        <Home size={20} className="mr-2 -ml-1" />
        Go Back to Homepage
      </Link>
    </div>
  );
};

export default NotFoundPage;