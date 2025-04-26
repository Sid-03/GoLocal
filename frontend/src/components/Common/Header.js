import React, { useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth'; // Import useAuth hook
import { Home, ShoppingBag, Info, Mail, LogIn, LogOut, User, Menu, X } from 'lucide-react'; // Add Menu/X for mobile

/**
 * Header component displaying the logo, navigation links, and login/logout button.
 * Uses NavLink for active styling and useAuth for conditional rendering based on auth state.
 * Includes basic mobile menu functionality.
 */
const Header = ({ currentPath }) => { // Receive currentPath for potential non-NavLink active state if needed
  const { isAuthenticated, user, logout } = useAuth(); // Get auth state and logout function
  const navigate = useNavigate();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    setIsMobileMenuOpen(false); // Close menu on logout
    // Navigation is handled within the logout function in AuthContext
  };

  const handleNavigate = (path) => {
     navigate(path);
     setIsMobileMenuOpen(false); // Close menu on navigation
  }

  // Base style for navigation links
  const linkBaseStyle = "flex items-center space-x-1 px-3 py-2 rounded-md text-sm font-medium transition duration-200";
  // Active style for NavLink
  const activeLinkStyle = "bg-green-100 text-green-800";
  // Inactive style for NavLink
  const inactiveLinkStyle = "text-gray-700 hover:bg-gray-100 hover:text-gray-900";

  // NavLink style function
  const getNavLinkClass = ({ isActive }) =>
    `${linkBaseStyle} ${isActive ? activeLinkStyle : inactiveLinkStyle}`;

  // Mobile link style
  const mobileLinkStyle = "block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-100 hover:text-gray-900";
  const mobileActiveLinkStyle = "block px-3 py-2 rounded-md text-base font-medium bg-green-100 text-green-800";
  const getMobileNavLinkClass = ({ isActive }) => isActive ? mobileActiveLinkStyle : mobileLinkStyle;


  return (
    <header className="bg-white shadow-sm sticky top-0 z-50 border-b border-gray-200">
      <div className="container mx-auto px-4 py-3">
        <div className="flex justify-between items-center">
          {/* Logo */}
          <Link to="/" className="text-2xl font-bold text-green-700 hover:text-green-800 transition-colors">
            GoLocal
          </Link>

          {/* Desktop Navigation Links */}
          <nav className="hidden md:flex space-x-2 items-center">
            <NavLink to="/" className={getNavLinkClass}>
              <Home size={18} /> <span>Home</span>
            </NavLink>
            <NavLink to="/products" className={getNavLinkClass}>
              <ShoppingBag size={18} /> <span>Products</span>
            </NavLink>
            <NavLink to="/about" className={getNavLinkClass}>
              <Info size={18} /> <span>About</span>
            </NavLink>
            <NavLink to="/contact" className={getNavLinkClass}>
              <Mail size={18} /> <span>Contact</span>
            </NavLink>
          </nav>

          {/* Auth Buttons (Desktop) */}
          <div className="hidden md:flex items-center space-x-3">
            {isAuthenticated ? (
              <>
                <span className="text-sm text-gray-600 hidden lg:inline">Welcome, {user?.username || 'User'}!</span>
                {/* Add Profile Link if needed */}
                {/* <button onClick={() => handleNavigate('/profile')} className={`${linkBaseStyle} ${inactiveLinkStyle}`}>
                   <User size={16} /> <span>Profile</span>
                </button> */}
                <button
                  onClick={handleLogout}
                  className="px-4 py-2 border border-red-300 rounded-md text-sm font-medium text-red-700 bg-white hover:bg-red-50 transition duration-200 flex items-center space-x-1"
                >
                  <LogOut size={16} /> <span>Log out</span>
                </button>
              </>
            ) : (
              <button
                onClick={() => handleNavigate('/login')}
                className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-100 transition duration-200 flex items-center space-x-1"
              >
                <LogIn size={16} /> <span>Log in</span>
              </button>
            )}
          </div>

          {/* Mobile Menu Button */}
          <div className="md:hidden flex items-center">
            <button
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              type="button"
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-green-500"
              aria-controls="mobile-menu"
              aria-expanded={isMobileMenuOpen}
            >
              <span className="sr-only">Open main menu</span>
              {isMobileMenuOpen ? (
                <X className="block h-6 w-6" aria-hidden="true" />
              ) : (
                <Menu className="block h-6 w-6" aria-hidden="true" />
              )}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Menu Panel */}
      {isMobileMenuOpen && (
        <div className="md:hidden absolute top-full left-0 right-0 bg-white border-b border-gray-200 shadow-md" id="mobile-menu">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
            <NavLink to="/" className={getMobileNavLinkClass} onClick={() => setIsMobileMenuOpen(false)}>Home</NavLink>
            <NavLink to="/products" className={getMobileNavLinkClass} onClick={() => setIsMobileMenuOpen(false)}>Products</NavLink>
            <NavLink to="/about" className={getMobileNavLinkClass} onClick={() => setIsMobileMenuOpen(false)}>About</NavLink>
            <NavLink to="/contact" className={getMobileNavLinkClass} onClick={() => setIsMobileMenuOpen(false)}>Contact</NavLink>
          </div>
          {/* Mobile Auth Buttons */}
          <div className="pt-4 pb-3 border-t border-gray-200">
             {isAuthenticated ? (
                <>
                  <div className="flex items-center px-5">
                     <User size={20} className="text-gray-500 mr-2"/>
                     <div className="text-base font-medium leading-none text-gray-800">{user?.username || 'User'}</div>
                  </div>
                   <div className="mt-3 px-2 space-y-1">
                      {/* <button onClick={() => handleNavigate('/profile')} className={mobileLinkStyle}>Your Profile</button> */}
                      <button onClick={handleLogout} className={`${mobileLinkStyle} text-red-700 hover:bg-red-50 w-full text-left`}>
                          Sign out
                      </button>
                   </div>
                 </>
             ) : (
                 <div className="mt-3 px-2 space-y-1">
                     <button onClick={() => handleNavigate('/login')} className={mobileLinkStyle}>
                         Log in
                     </button>
                 </div>
             )}
          </div>
        </div>
      )}
    </header>
  );
};

export default Header;