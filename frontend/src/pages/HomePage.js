import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ArrowRight, List, Search as SearchIcon, ShieldCheck, LogIn } from 'lucide-react'; // Renamed Search to SearchIcon

// Reusable components used on the homepage
// Assuming these are defined similarly to the original single-file example

// Placeholder Hero Image Component (Can be moved to components later)
const HeroImage = () => (
  <div className="flex-1 mt-8 md:mt-0 flex justify-center">
    <img
        src="https://placehold.co/450x350/EBF5EB/1B5E20?text=Organic+Products+Marketplace"
        alt="Basket of fresh organic vegetables"
        className="rounded-lg shadow-md object-cover w-full max-w-md"
        onError={(e) => { e.target.onerror = null; e.target.src='https://placehold.co/450x350/EAEAEA/cc0000?text=Image+Not+Found'; }}
        loading="eager" // Eager load hero image
    />
  </div>
);

// Placeholder Feature Card Component (Can be moved to components later)
const FeatureCard = ({ icon: Icon, title, description }) => (
    <div className="bg-white p-6 rounded-lg shadow border border-gray-200 text-center flex flex-col items-center h-full">
      <div className="mb-4 bg-green-100 text-green-700 rounded-full w-16 h-16 flex items-center justify-center flex-shrink-0">
        <Icon size={32} strokeWidth={1.5} />
      </div>
      <h3 className="text-xl font-semibold text-gray-800 mb-2">{title}</h3>
      <p className="text-gray-600 text-sm leading-relaxed">{description}</p>
    </div>
);


/**
 * HomePage component - The landing page of the application.
 */
const HomePage = () => {
    const navigate = useNavigate();

    // Navigate to products page, optionally passing search term
    const handleSearchSubmit = (event) => {
        event.preventDefault();
        const searchTerm = event.target.elements.search.value;
        navigate(`/products?search=${encodeURIComponent(searchTerm)}`);
    };

  return (
    <>
      {/* Hero Section */}
      <section id="home-hero" className="bg-gradient-to-br from-green-50 via-white to-green-50 py-16 md:py-24 px-4">
        <div className="container mx-auto flex flex-col md:flex-row items-center gap-8 md:gap-12">
          <div className="flex-1 text-center md:text-left">
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-green-800 mb-4 leading-tight">
              Connect with Quality<br />Organic Suppliers
            </h1>
            <p className="text-gray-600 mb-8 text-lg md:text-xl">
              Your trusted B2B platform for sourcing fresh, certified organic products directly from verified suppliers.
            </p>
            <button
              onClick={() => navigate('/products')}
              className="bg-green-700 text-white hover:bg-green-800 transition duration-200 px-8 py-3 rounded-md font-semibold text-lg inline-flex items-center space-x-2 shadow-md hover:shadow-lg"
            >
              <span>Explore Products</span> <ArrowRight size={20} />
            </button>
          </div>
          <HeroImage />
        </div>
      </section>

       {/* Discover Section */}
        <section id="home-discover" className="py-16 px-4 bg-white">
          <div className="container mx-auto">
            <h2 className="text-3xl md:text-4xl font-bold text-green-700 mb-4 text-center md:text-left">Discover Products</h2>
            <form onSubmit={handleSearchSubmit} className="flex flex-col sm:flex-row justify-center items-center my-8 md:my-12 gap-2 max-w-2xl mx-auto">
              <label htmlFor="home-search" className="sr-only">Search products or suppliers</label>
              <input id="home-search" name="search" type="text" placeholder="Search products or suppliers..." className="border border-gray-300 rounded-md px-4 py-3 focus:ring-2 focus:ring-green-200 focus:border-green-500 outline-none w-full sm:flex-grow"/>
              <button type="submit" className="bg-green-700 text-white hover:bg-green-800 transition duration-200 px-6 py-3 rounded-md font-semibold inline-flex items-center space-x-2 w-full sm:w-auto flex-shrink-0 justify-center">
                <SearchIcon size={20} /> <span>Search</span>
              </button>
            </form>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-8">
              <FeatureCard icon={List} title="Browse Listings" description="Explore a wide range of organic products from verified local and national suppliers."/>
              <FeatureCard icon={SearchIcon} title="Find Specific Items" description="Utilize search and filtering to quickly find the exact products your business needs."/>
              <FeatureCard icon={ShieldCheck} title="Verified Suppliers" description="Connect and transact with confidence knowing our suppliers meet quality standards."/>
            </div>
          </div>
        </section>

      {/* About Us Snippet Section */}
      <section id="home-about" className="bg-green-50 py-16 px-4 text-center">
        <div className="container mx-auto max-w-3xl">
          <h2 className="text-3xl md:text-4xl font-bold text-green-700 mb-4">About GoLocal</h2>
          <p className="text-gray-700 text-lg md:text-xl leading-relaxed mb-6">
            We are dedicated to simplifying B2B organic trade, fostering sustainable connections,
            and ensuring a seamless, secure experience for both buyers and suppliers.
          </p>
          <Link
            to="/about"
            className="text-green-700 hover:text-green-800 font-semibold inline-flex items-center space-x-1 transition duration-200 group"
          >
            <span>Learn More About Our Mission</span>
            <ArrowRight size={18} className="transform group-hover:translate-x-1 transition-transform" />
          </Link>
        </div>
      </section>

      {/* Call to Action Section */}
      <section id="home-cta" className="py-12 px-4 bg-white border-t border-gray-200">
        <div className="container mx-auto text-center">
          <h2 className="text-2xl md:text-3xl font-semibold text-gray-800 mb-6">Ready to Streamline Your Sourcing?</h2>
          <button
            onClick={() => navigate('/login')} // Or '/register' if you have a separate registration page
            className="px-6 py-3 border border-gray-300 rounded-md font-medium text-gray-700 bg-white hover:bg-gray-100 transition duration-200 inline-flex items-center space-x-2 text-lg shadow-sm hover:shadow"
          >
            <LogIn size={20} /> <span>Log in or Sign Up</span>
          </button>
        </div>
      </section>
    </>
  );
};

export default HomePage;