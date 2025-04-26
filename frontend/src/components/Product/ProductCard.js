import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ShoppingCart } from 'lucide-react'; // Optional icon for "Inquire" button

/**
 * Component to display a single product card.
 * Navigates to the inquiry page when clicked.
 *
 * @param {object} props - Component props.
 * @param {object} props.product - The product data object.
 * @param {number} props.product.id - Product ID.
 * @param {string} props.product.name - Product name.
 * @param {string} props.product.price - Product price string.
 * @param {string} props.product.image - Product image URL.
 * @param {string} props.product.supplierName - Product supplier name.
 */
const ProductCard = ({ product }) => {
  const navigate = useNavigate();

  // Handle navigation to the inquiry page for this product
  const handleInquireClick = () => {
    if (product && product.id) {
      navigate(`/inquire/${product.id}`); // Navigate to the protected inquiry route
    } else {
       console.error("Product data or ID missing, cannot navigate to inquiry page.");
       // Optionally show an error message to the user
    }
  };

  // Fallback image handler
  const handleImageError = (e) => {
    e.target.onerror = null; // Prevent infinite loop
    // Use a more descriptive placeholder if the original fails
    e.target.src = `https://placehold.co/300x200/EAEAEA/cc0000?text=${encodeURIComponent(product.name)}+Image+Not+Found`;
  };

  // Prevent rendering if product data is missing
  if (!product) {
    return null; // Or render a placeholder/error card
  }

  return (
    // Use a div container for layout, button inside triggers action
    <div className="bg-white rounded-lg shadow border border-gray-200 overflow-hidden flex flex-col h-full transition-shadow duration-200 hover:shadow-md">
      {/* Product Image */}
      <div className="relative h-48 w-full flex-shrink-0">
        <img
          src={product.image || 'https://placehold.co/300x200/EAEAEA/cc0000?text=No+Image'} // Default placeholder if image URL is missing
          alt={product.name || 'Product Image'}
          className="w-full h-full object-cover"
          onError={handleImageError}
          loading="lazy" // Lazy load images for better performance
        />
      </div>

      {/* Product Details */}
      <div className="p-4 flex flex-col flex-grow">
        <h3 className="text-lg font-semibold text-gray-800 mb-1 truncate" title={product.name}>
          {product.name || 'Unnamed Product'}
        </h3>
        <p className="text-sm text-gray-500 mb-2">
          Sold by: {product.supplierName || 'Unknown Supplier'}
        </p>
        <p className="text-gray-700 text-base font-medium mt-auto mb-3 pt-2">
          {product.price || 'Price not available'}
        </p>

        {/* Inquire Button */}
        <button
          onClick={handleInquireClick}
          className="mt-auto w-full inline-flex justify-center items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-green-700 hover:bg-green-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-150 ease-in-out"
        >
          <ShoppingCart size={16} className="mr-2 -ml-1" />
          Send Inquiry
        </button>
      </div>
    </div>
  );
};

export default ProductCard;