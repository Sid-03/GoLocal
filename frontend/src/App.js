import React from 'react';
import { Routes, Route, useLocation } from 'react-router-dom';

// Layout Components
import Header from './components/Common/Header';
import Footer from './components/Common/Footer';

// Page Components
import HomePage from './pages/HomePage';
import ProductListPage from './pages/ProductListPage';
import ProductInquiryPage from './pages/ProductInquiryPage';
import LoginPage from './pages/LoginPage';
import AboutPage from './pages/AboutPage';
import ContactPage from './pages/ContactPage';
import NotFoundPage from './pages/NotFoundPage';
// import RegisterPage from './pages/RegisterPage'; // Uncomment if you create this page

// Auth Components
import ProtectedRoute from './components/Auth/ProtectedRoute';

/**
 * The main application component that sets up routing.
 */
function App() {
  const location = useLocation(); // Get current location for header active state

  return (
    // Use flex column and min-height to ensure footer stays at the bottom
    <div className="App flex flex-col min-h-screen">
      {/* Header is always visible */}
      <Header currentPath={location.pathname} />

      {/* Main content area where routes are rendered */}
      {/* flex-grow ensures this section takes available space */}
      <main className="flex-grow">
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<HomePage />} />
          <Route path="/products" element={<ProductListPage />} />
          <Route path="/login" element={<LoginPage />} />
          {/* <Route path="/register" element={<RegisterPage />} /> */}
          <Route path="/about" element={<AboutPage />} />
          <Route path="/contact" element={<ContactPage />} />

          {/* Protected Routes */}
          <Route
            path="/inquire/:productId" // Route for product inquiry, expects productId param
            element={
              // Wrap the inquiry page with ProtectedRoute
              <ProtectedRoute>
                <ProductInquiryPage />
              </ProtectedRoute>
            }
          />
          {/* Add other protected routes here (e.g., user profile, order history) */}
          {/*
          <Route
            path="/profile"
            element={
              <ProtectedRoute>
                <UserProfilePage />
              </ProtectedRoute>
            }
          />
          */}

          {/* Catch-all Route for 404 Not Found */}
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </main>

      {/* Footer is always visible */}
      <Footer />
    </div>
  );
}

export default App;