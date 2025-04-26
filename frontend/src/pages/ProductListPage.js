import React, { useState, useEffect, useCallback } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { fetchProducts } from '../services/apiService'; // API function
import ProductCard from '../components/Product/ProductCard'; // Product card component
import LoadingSpinner from '../components/Common/LoadingSpinner';
import ErrorMessage from '../components/Common/ErrorMessage';
import { Search } from 'lucide-react';

// Debounce helper function (optional, but good for search)
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

/**
 * Page component to display a list of products with search functionality.
 */
const ProductListPage = () => {
  const [products, setProducts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searchParams, setSearchParams] = useSearchParams(); // For reading/setting URL search params
  const navigate = useNavigate(); // For updating URL without full reload

  // Local state for the input field value
  const [searchTermInput, setSearchTermInput] = useState(() => searchParams.get('search') || '');

  // Get the 'search' query parameter from the URL
  const currentSearchTerm = searchParams.get('search') || '';

  // Function to fetch products based on the current search term
  const loadProducts = useCallback(async (term) => {
    setIsLoading(true);
    setError(null);
    console.log(`Fetching products with term: "${term}"`);
    try {
      const response = await fetchProducts(term);
      setProducts(response.data || []); // Assuming response.data is the array
    } catch (err) {
      console.error("Error fetching products:", err);
      setError(err.response?.data?.message || err.message || 'Failed to load products.');
      setProducts([]); // Clear products on error
    } finally {
      setIsLoading(false);
    }
  }, []); // useCallback ensures this function identity is stable

  // Debounced version of loadProducts for input changes
  const debouncedLoadProducts = useCallback(debounce(loadProducts, 400), [loadProducts]);

  // Effect to load products when the URL search parameter changes
  useEffect(() => {
    loadProducts(currentSearchTerm);
    // No need to update searchTermInput here, it's driven by user input
  }, [currentSearchTerm, loadProducts]); // Depend on the URL param and load function

  // Handle changes in the search input field
  const handleSearchInputChange = (event) => {
    const newInputValue = event.target.value;
    setSearchTermInput(newInputValue);
    // Trigger debounced fetch based on input value
    debouncedLoadProducts(newInputValue);
    // Update URL immediately or wait for debounce? Let's wait.
  };

  // Handle form submission (e.g., pressing Enter)
  const handleSearchSubmit = (event) => {
    event.preventDefault();
    // Update the URL search parameter, which triggers the useEffect above
    // Use navigate for finer control or setSearchParams
    setSearchParams(searchTermInput ? { search: searchTermInput } : {});
    // loadProducts(searchTermInput); // useEffect will handle this
  };

  return (
    <section id="products-page" className="py-10 px-4 bg-gray-50 min-h-[calc(100vh-150px)]">
      <div className="container mx-auto">
        {/* Search Bar */}
        <form
          onSubmit={handleSearchSubmit}
          className="flex flex-col sm:flex-row justify-center items-center mb-8 md:mb-12 gap-2 max-w-2xl mx-auto bg-white p-4 rounded-lg shadow border border-gray-200"
        >
          <label htmlFor="product-search" className="sr-only">Search products or suppliers</label>
          <input
            id="product-search"
            type="text"
            placeholder="Search products or suppliers..."
            value={searchTermInput}
            onChange={handleSearchInputChange}
            className="border border-gray-300 rounded-md px-4 py-3 focus:ring-2 focus:ring-green-200 focus:border-green-500 outline-none w-full sm:flex-grow"
          />
          <button
            type="submit" // Submit form on button click
            className="bg-green-700 text-white hover:bg-green-800 transition duration-200 px-6 py-3 rounded-md font-semibold inline-flex items-center space-x-2 w-full sm:w-auto flex-shrink-0 justify-center"
          >
            <Search size={20} /> <span>Search</span>
          </button>
        </form>

        {/* Page Title */}
        <h1 className="text-3xl font-bold text-gray-800 mb-6 text-left">
          Product Listings {currentSearchTerm && `(Results for "${currentSearchTerm}")`}
        </h1>

         {/* Error Message Display */}
         <ErrorMessage message={error} className="mb-6" />

        {/* Loading State */}
        {isLoading && (
          <div className="text-center py-10">
            <LoadingSpinner size="h-12 w-12" />
            <p className="mt-2 text-gray-600">Loading products...</p>
          </div>
        )}

        {/* Product Grid or No Results Message */}
        {!isLoading && !error && (
          products.length > 0 ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {products.map(product => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          ) : (
            <p className="text-center text-gray-500 mt-10 py-10 bg-white rounded-md shadow-sm border">
              No products found{currentSearchTerm ? ` matching "${currentSearchTerm}"` : ''}.
            </p>
          )
        )}
      </div>
    </section>
  );
};

export default ProductListPage;