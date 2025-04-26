import React from 'react';
import { Loader2 } from 'lucide-react'; // Using lucide icon for spinner

/**
 * A simple loading spinner component.
 * @param {string} [size='h-8 w-8'] - Tailwind size class for the spinner.
 * @param {string} [color='text-green-600'] - Tailwind text color class.
 * @param {string} [className=''] - Additional CSS classes.
 */
const LoadingSpinner = ({ size = 'h-8 w-8', color = 'text-green-600', className = '' }) => {
  return (
    <div className={`flex justify-center items-center ${className}`}>
      <Loader2 className={`animate-spin ${size} ${color}`} />
    </div>
  );
};

export default LoadingSpinner;