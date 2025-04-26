import React from 'react';
import ReactDOM from 'react-dom/client'; // Use client API for React 18+
import { BrowserRouter } from 'react-router-dom';

import './index.css'; // Import global styles (including Tailwind)
import App from './App'; // Import the main App component
import { AuthProvider } from './context/AuthContext'; // Import the Auth Context Provider
import reportWebVitals from './reportWebVitals'; // Optional: For measuring performance

// Get the root DOM element
const rootElement = document.getElementById('root');

// Create a React root
const root = ReactDOM.createRoot(rootElement);

// Render the application
root.render(
  // StrictMode helps catch potential problems in an application
  <React.StrictMode>
    {/* BrowserRouter provides routing capabilities */}
    <BrowserRouter>
      {/* AuthProvider makes authentication state available to the app */}
      <AuthProvider>
        {/* The main application component */}
        <App />
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();