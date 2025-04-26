import React from 'react';
import { AlertCircle } from 'lucide-react'; // Icon for error message

/**
 * Component to display an error message in a styled box.
 * @param {string | React.ReactNode} message - The error message to display.
 * @param {string} [className=''] - Additional CSS classes for the container.
 * @param {function} [onDismiss] - Optional callback function to dismiss the error.
 */
const ErrorMessage = ({ message, className = '', onDismiss }) => {
  // Don't render if no message is provided
  if (!message) {
    return null;
  }

  return (
    <div
      className={`bg-red-50 border border-red-300 text-red-700 px-4 py-3 rounded-md relative ${className}`}
      role="alert"
    >
      <div className="flex items-center">
        <AlertCircle className="h-5 w-5 mr-3 flex-shrink-0" aria-hidden="true" />
        <span className="block sm:inline">{message}</span>
      </div>
      {onDismiss && (
        <button
          onClick={onDismiss}
          type="button"
          className="absolute top-0 bottom-0 right-0 px-4 py-3"
          aria-label="Dismiss error"
        >
          <span className="text-xl text-red-500 hover:text-red-700" aria-hidden="true">×</span>
        </button>
      )}
    </div>
  );
};

export default ErrorMessage;