import React from 'react';
import { Link } from 'react-router-dom';

/**
 * Simple footer component for the application.
 */
const Footer = () => {
  return (
    <footer className="bg-gray-800 text-gray-400 py-8 px-4 mt-auto"> {/* mt-auto pushes footer down in flex layout */}
      <div className="container mx-auto text-center text-sm">
        {/* Logo or Name */}
        <p className="text-lg font-semibold text-white mb-2">GoLocal</p>

        {/* Copyright */}
        <p>© {new Date().getFullYear()} GoLocal App. All rights reserved.</p>
        <p className="mt-1">Connecting Organic Businesses Sustainably.</p>

        {/* Basic Footer Links */}
        <div className="mt-4 space-x-4">
          <Link to="/about" className="hover:text-white transition-colors duration-200">About Us</Link>
          <span className="text-gray-600">|</span>
          <Link to="/contact" className="hover:text-white transition-colors duration-200">Contact</Link>
          <span className="text-gray-600">|</span>
          {/* Replace # with actual links if Privacy/Terms pages exist */}
          <a href="#privacy" onClick={(e) => e.preventDefault()} className="hover:text-white transition-colors duration-200">Privacy Policy</a>
          <span className="text-gray-600">|</span>
          <a href="#terms" onClick={(e) => e.preventDefault()} className="hover:text-white transition-colors duration-200">Terms of Service</a>
        </div>

         {/* Optional Social Links */}
         {/* <div className="mt-4 space-x-3">
             <a href="#" aria-label="Facebook" className="hover:text-white"><i className="fab fa-facebook-f"></i></a>
             <a href="#" aria-label="Twitter" className="hover:text-white"><i className="fab fa-twitter"></i></a>
             <a href="#" aria-label="Instagram" className="hover:text-white"><i className="fab fa-instagram"></i></a>
         </div> */}
      </div>
    </footer>
  );
};

export default Footer;