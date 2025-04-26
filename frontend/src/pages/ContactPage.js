import React, { useState } from 'react';
import { MapPin, Phone, Mail, Send } from 'lucide-react';
import LoadingSpinner from '../components/Common/LoadingSpinner';
import ErrorMessage from '../components/Common/ErrorMessage';

/**
 * Contact Us page component with information and a contact form.
 */
const ContactPage = () => {
  // State for form data
  const [formData, setFormData] = useState({ name: '', email: '', subject: '', message: '' });
  // State for submission status
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitStatus, setSubmitStatus] = useState(null); // 'success', 'error', or null
  const [submitError, setSubmitError] = useState(null); // Specific error message

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({ ...prevState, [name]: value }));
  };

  // Handle form submission (placeholder - replace with actual API call if needed)
  const handleContactSubmit = async (e) => {
    e.preventDefault();
    setSubmitStatus(null); // Reset status
    setSubmitError(null);

    // Basic client-side validation (can be more extensive)
    if (!formData.name || !formData.email || !formData.subject || !formData.message) {
      setSubmitError('Please fill in all required fields.');
      return;
    }

    setIsSubmitting(true);

    // --- Placeholder for actual submission logic ---
    console.log('Contact form submitted:', formData);
    // Example: Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1500));
    // Simulate success/error (replace with actual API response check)
    const success = Math.random() > 0.2; // 80% chance of success for demo
    // --- End Placeholder ---

    setIsSubmitting(false);
    if (success) {
      setSubmitStatus('success');
      setFormData({ name: '', email: '', subject: '', message: '' }); // Clear form on success
    } else {
      setSubmitStatus('error');
      setSubmitError('Sorry, there was an error sending your message. Please try again later or contact us directly.');
    }
  };

  return (
    <section id="contact-page" className="py-16 px-4 bg-gray-50 min-h-[calc(100vh-150px)]">
      <div className="container mx-auto max-w-5xl">
        {/* Page Title */}
        <h1 className="text-4xl md:text-5xl font-bold text-green-800 mb-10 text-center">Contact Us</h1>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-10 md:gap-16">
          {/* Contact Information Column */}
          <div className="bg-white p-8 rounded-lg shadow-md border border-gray-200 flex flex-col">
            <h2 className="text-2xl font-semibold text-gray-800 mb-6">Get in Touch</h2>
            <p className="text-gray-700 mb-8 leading-relaxed">
              Have questions, feedback, or need assistance with the platform? We're here to help! Reach out using the details below, or fill out the contact form, and we'll get back to you as soon as possible.
            </p>
            {/* Contact Details */}
            <div className="space-y-5 text-gray-700 mt-auto">
              <div className="flex items-start">
                <MapPin size={20} className="mr-4 mt-1 text-green-600 flex-shrink-0" />
                <span>123 Organic Lane,<br />Green Valley, GV 45678</span>
              </div>
              <div className="flex items-center">
                <Phone size={20} className="mr-4 text-green-600 flex-shrink-0" />
                <a href="tel:+11234567890" className="hover:text-green-700">(123) 456-7890</a>
              </div>
              <div className="flex items-center">
                <Mail size={20} className="mr-4 text-green-600 flex-shrink-0" />
                <a href="mailto:support@golocal.example" className="hover:text-green-700 break-all">support@golocal.example</a>
              </div>
            </div>
          </div>

          {/* Contact Form Column */}
          <div className="bg-white p-8 rounded-lg shadow-md border border-gray-200">
            <h2 className="text-2xl font-semibold text-gray-800 mb-6">Send Us a Message</h2>

            {/* Display Form Error */}
             <ErrorMessage message={submitError} className="mb-4" onDismiss={() => setSubmitError(null)} />

             {/* Display Success Message */}
             {submitStatus === 'success' && (
                <div className="mb-4 p-3 bg-green-50 border border-green-300 text-green-700 rounded-md text-sm text-center">
                   Thank you! Your message has been sent successfully. We'll be in touch soon.
                </div>
             )}


            {/* Hide form on success or show it */}
            {submitStatus !== 'success' && (
              <form onSubmit={handleContactSubmit} className="space-y-5">
                {/* Name Field */}
                <div>
                  <label htmlFor="name-contact" className="block text-sm font-medium text-gray-700 mb-1">Your Name</label>
                  <input
                    type="text" name="name" id="name-contact" required
                    value={formData.name} onChange={handleChange} disabled={isSubmitting}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm disabled:bg-gray-100"
                    placeholder="e.g., Jane Doe" />
                </div>
                {/* Email Field */}
                <div>
                  <label htmlFor="email-contact" className="block text-sm font-medium text-gray-700 mb-1">Your Email</label>
                  <input
                    type="email" name="email" id="email-contact" required
                    value={formData.email} onChange={handleChange} disabled={isSubmitting}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm disabled:bg-gray-100"
                    placeholder="e.g., jane.doe@company.com" />
                </div>
                {/* Subject Field */}
                <div>
                  <label htmlFor="subject-contact" className="block text-sm font-medium text-gray-700 mb-1">Subject</label>
                  <input
                    type="text" name="subject" id="subject-contact" required
                    value={formData.subject} onChange={handleChange} disabled={isSubmitting}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm disabled:bg-gray-100"
                    placeholder="e.g., Question about an order" maxLength={200} />
                </div>
                {/* Message Field */}
                <div>
                  <label htmlFor="message-contact" className="block text-sm font-medium text-gray-700 mb-1">Message</label>
                  <textarea
                    name="message" id="message-contact" rows="5" required
                    value={formData.message} onChange={handleChange} disabled={isSubmitting}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm disabled:bg-gray-100"
                    placeholder="Enter your message here..." maxLength={5000}
                  ></textarea>
                </div>
                {/* Submit Button */}
                <div>
                  <button
                    type="submit"
                    disabled={isSubmitting}
                    className={`w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-700 hover:bg-green-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-150 ease-in-out ${
                      isSubmitting ? 'opacity-50 cursor-not-allowed' : ''
                    }`}
                  >
                    {isSubmitting ? (
                      <>
                        <LoadingSpinner size="h-5 w-5" color="text-white" className="mr-2" /> Sending...
                      </>
                    ) : (
                      <>
                        <Send size={16} className="mr-2" /> Send Message
                      </>
                    )}
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      </div>
    </section>
  );
};

export default ContactPage;