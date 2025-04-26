import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchProductById, submitInquiry } from '../services/apiService'; // API functions
import { useAuth } from '../hooks/useAuth'; // To ensure user is logged in (though ProtectedRoute does this)
import LoadingSpinner from '../components/Common/LoadingSpinner';
import ErrorMessage from '../components/Common/ErrorMessage';
import { Send, ArrowLeft, AlertCircle } from 'lucide-react';

/**
 * Page component for submitting an inquiry about a specific product.
 * Accessed via a protected route.
 */
const ProductInquiryPage = () => {
  const { productId } = useParams(); // Get product ID from URL parameter
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth(); // Get auth state

  const [product, setProduct] = useState(null);
  const [inquiryData, setInquiryData] = useState({ subject: '', message: '' });
  const [isLoadingProduct, setIsLoadingProduct] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null); // For general page errors or product fetch errors
  const [submitError, setSubmitError] = useState(null); // For inquiry submission errors

  // Fetch product details when component mounts or productId changes
  const loadProductDetails = useCallback(async () => {
    if (!productId) {
      setError("No Product ID provided.");
      setIsLoadingProduct(false);
      return;
    }
    setIsLoadingProduct(true);
    setError(null);
    try {
      const response = await fetchProductById(productId);
      setProduct(response.data);
      // Pre-fill subject line (optional)
      setInquiryData(prev => ({ ...prev, subject: `Inquiry about ${response.data?.name || 'Product ID ' + productId}`}));
    } catch (err) {
      console.error("Error fetching product details:", err);
      setError(err.response?.data?.message || err.message || 'Failed to load product details.');
      setProduct(null);
    } finally {
      setIsLoadingProduct(false);
    }
  }, [productId]); // Depend on productId

  useEffect(() => {
    loadProductDetails();
  }, [loadProductDetails]); // Run fetch logic

  // Handle input changes for the form
  const handleChange = (e) => {
    const { name, value } = e.target;
    setInquiryData(prevState => ({ ...prevState, [name]: value }));
  };

  // Handle inquiry form submission
  const handleInquirySubmit = async (e) => {
    e.preventDefault();
    setSubmitError(null); // Clear previous submission errors

    if (!inquiryData.subject || !inquiryData.message) {
      setSubmitError('Please fill in both Subject and Message fields.');
      return;
    }
    if (!isAuthenticated || !user) {
        setSubmitError('You must be logged in to send an inquiry.');
        // Optionally redirect to login
        // navigate('/login', { state: { from: location } });
        return;
    }

    setIsSubmitting(true);

    const payload = {
      productId: parseInt(productId, 10), // Ensure ID is number
      subject: inquiryData.subject,
      message: inquiryData.message,
    };

    try {
      await submitInquiry(payload);
      // Handle successful submission
      console.log("Inquiry submitted successfully for product:", productId);
      alert(`Inquiry sent successfully to ${product?.supplierName || 'the supplier'} regarding ${product?.name || 'this product'}!`);
      // Redirect back to product list or maybe a "my inquiries" page
      navigate('/products');
    } catch (err) {
      console.error("Error submitting inquiry:", err);
      setSubmitError(err.response?.data?.message || err.message || 'Failed to send inquiry. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  // Fallback image handler for product image
  const handleImageError = (e) => {
    e.target.onerror = null;
    e.target.src = `https://placehold.co/200x150/EAEAEA/cc0000?text=Image+Missing`;
  };

  // Render loading state for product details
  if (isLoadingProduct) {
    return (
      <div className="flex justify-center items-center min-h-[calc(100vh-200px)]">
        <LoadingSpinner size="h-12 w-12" />
      </div>
    );
  }

  // Render error state if product fetch failed
  if (error && !product) {
     return (
         <div className="container mx-auto max-w-xl text-center py-20 px-4">
              <ErrorMessage message={error}/>
              <button onClick={() => navigate('/products')} className="mt-4 inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
                  <ArrowLeft size={16} className="mr-1" /> Back to Products
              </button>
          </div>
     );
  }

  // Render error if product ID is invalid or product is null somehow after loading
  if (!product) {
      return (
           <div className="container mx-auto max-w-xl text-center py-20 px-4">
               <div className="bg-yellow-50 border border-yellow-300 text-yellow-800 px-4 py-3 rounded-md flex items-center justify-center">
                   <AlertCircle className="h-5 w-5 mr-2" /> Could not load product information. It might not exist.
               </div>
               <button onClick={() => navigate('/products')} className="mt-4 inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
                  <ArrowLeft size={16} className="mr-1" /> Back to Products
              </button>
           </div>
      );
  }


  // Main Inquiry Form Render
  return (
    <section id="product-inquiry-page" className="py-12 px-4 bg-gray-50 min-h-[calc(100vh-150px)] flex justify-center items-start">
      <div className="w-full max-w-xl bg-white p-8 md:p-10 rounded-xl shadow-lg border border-gray-200 my-8">
        {/* Back Button */}
        <button
          onClick={() => navigate(-1)} // Go back to previous page (likely product list)
          className="text-sm text-green-600 hover:text-green-800 mb-5 inline-flex items-center transition-colors duration-150"
        >
          <ArrowLeft size={16} className="mr-1" /> Back
        </button>

        {/* Page Title */}
        <h1 className="text-3xl font-bold text-center text-gray-800 mb-2">Submit Inquiry</h1>
        <p className="text-center text-gray-600 mb-6">
          Contact <span className="font-semibold">{product.supplierName || 'the supplier'}</span> about this product.
        </p>

         {/* Display Submission Error */}
         <ErrorMessage message={submitError} className="mb-5" onDismiss={() => setSubmitError(null)} />

        {/* Product Info Display */}
        <div className="bg-gray-50 border border-gray-200 rounded-lg p-4 flex items-center mb-8 space-x-4">
          <img
            src={product.image || 'https://placehold.co/200x150/EAEAEA/cc0000?text=No+Image'}
            alt={product.name || 'Product'}
            className="w-24 h-20 object-cover rounded-md flex-shrink-0 border"
            onError={handleImageError}
          />
          <div className="flex-grow">
            <h2 className="text-lg font-semibold text-gray-800">{product.name || 'Product Name Missing'}</h2>
            <p className="text-sm text-gray-600">Supplier: {product.supplierName || 'N/A'}</p>
             <p className="text-sm text-gray-500 mt-1">Product ID: {product.id}</p>
          </div>
        </div>

        {/* Inquiry Form */}
        <form onSubmit={handleInquirySubmit} className="space-y-5">
          {/* Subject Field */}
          <div>
            <label htmlFor="subject-inquiry" className="block text-sm font-medium text-gray-700 mb-1">Subject</label>
            <input
              type="text"
              name="subject"
              id="subject-inquiry"
              required
              value={inquiryData.subject}
              onChange={handleChange}
              className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm"
              placeholder="Enter inquiry subject"
              disabled={isSubmitting}
              maxLength={200}
            />
          </div>
          {/* Message Field */}
          <div>
            <label htmlFor="message-inquiry" className="block text-sm font-medium text-gray-700 mb-1">Message</label>
            <textarea
              name="message"
              id="message-inquiry"
              rows="6"
              required
              value={inquiryData.message}
              onChange={handleChange}
              className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm"
              placeholder="Please provide details about your inquiry, required quantity, delivery needs, etc."
              disabled={isSubmitting}
              maxLength={5000}
            ></textarea>
          </div>
          {/* Submit Button */}
          <div>
            <button
              type="submit"
              disabled={isSubmitting}
              className={`w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-800 hover:bg-green-900 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-700 transition duration-150 ease-in-out ${
                isSubmitting ? 'opacity-50 cursor-not-allowed' : ''
              }`}
            >
              {isSubmitting ? (
                <>
                  <LoadingSpinner size="h-5 w-5" color="text-white" className="mr-2" />
                  Sending...
                </>
              ) : (
                <>
                  <Send size={16} className="mr-2" /> Send Inquiry
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </section>
  );
};

export default ProductInquiryPage;