import React from 'react';
import { Building, User, ShieldCheck, Users, Zap } from 'lucide-react'; // Add Users and Zap icons if used from prev example
// Example Leaf Icon component (can replace with lucide or other library)
const LeafIcon = (props) => (
    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" {...props}><path d="M11 20A7 7 0 0 1 9.8 6.1C15.5 5 17 4.48 19 2c1 2 2 4.18 2 8 0 5.5-4.78 10-10 10Z"/><path d="M2 21c0-3 1.85-5.36 5.08-6C9.5 14.52 12 13 13 12"/></svg>
);

/**
 * About Us page component.
 */
const AboutPage = () => {
  // Placeholder team members array
  const teamMembers = [
    { name: 'Alex Green', title: 'CEO & Founder', img: 'Team+1' },
    { name: 'Sam Farmer', title: 'Head of Operations', img: 'Team+2' },
    { name: 'Jamie Root', title: 'Lead Developer', img: 'Team+3' },
  ];

  return (
    <section id="about-page" className="py-16 px-4 bg-gradient-to-br from-green-50 via-white to-blue-50 min-h-[calc(100vh-150px)]">
      <div className="container mx-auto max-w-4xl space-y-12">
        {/* Page Title */}
        <h1 className="text-4xl md:text-5xl font-bold text-green-800 mb-8 text-center">About GoLocal</h1>

        {/* Mission Section */}
        <div className="bg-white p-8 rounded-lg shadow-md border border-gray-200">
          <h2 className="text-2xl font-semibold text-gray-800 mb-4 flex items-center">
            <Building size={24} className="mr-3 text-green-600 flex-shrink-0" /> Our Mission
          </h2>
          <p className="text-gray-700 leading-relaxed">
            Our mission is to revolutionize the B2B organic product marketplace by providing a seamless, secure, and efficient platform
            that connects trusted suppliers with businesses seeking high-quality organic goods. We aim to foster transparency, promote sustainability, and build
            long-lasting relationships within the organic trade community, making local and organic sourcing easier than ever.
          </p>
        </div>

        {/* Values Section */}
        <div className="bg-white p-8 rounded-lg shadow-md border border-gray-200">
          <h2 className="text-2xl font-semibold text-gray-800 mb-5">Our Core Values</h2>
          <ul className="space-y-4 text-gray-700">
            <li className="flex items-start">
              <ShieldCheck size={24} className="mr-3 mt-1 text-green-600 flex-shrink-0" />
              <div><span className="font-semibold text-green-700">Quality & Trust:</span> We are committed to sourcing the best organic products and building a reliable platform through secure transactions and thoroughly verified partners.</div>
            </li>
            <li className="flex items-start">
              <LeafIcon className="mr-3 mt-1 text-green-600 flex-shrink-0" />
              <div><span className="font-semibold text-green-700">Sustainability:</span> We actively promote environmentally friendly practices and support the growth and accessibility of the organic industry for businesses of all sizes.</div>
            </li>
            <li className="flex items-start">
              <Users size={24} className="mr-3 mt-1 text-green-600 flex-shrink-0" />
              <div><span className="font-semibold text-green-700">Community & Connection:</span> We believe in fostering strong, collaborative relationships between buyers, suppliers, and producers to strengthen the local food ecosystem.</div>
            </li>
            <li className="flex items-start">
              <Zap size={24} className="mr-3 mt-1 text-green-600 flex-shrink-0" />
              <div><span className="font-semibold text-green-700">Efficiency & Innovation:</span> We continuously strive to improve our platform technology, making B2B organic trade simpler, faster, and more intuitive.</div>
            </li>
          </ul>
        </div>

        {/* Team Section */}
        <div className="bg-white p-8 rounded-lg shadow-md border border-gray-200">
          <h2 className="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
            <User size={24} className="mr-3 text-green-600 flex-shrink-0" /> Meet the Team (Placeholder)
          </h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 text-center">
            {teamMembers.map(member => (
              <div key={member.name} className="p-4">
                <img
                  src={`https://placehold.co/150x150/EBF5EB/1B5E20?text=${member.img}`}
                  alt={member.name}
                  className="w-32 h-32 rounded-full mx-auto mb-3 shadow-md border-2 border-green-100"
                  loading="lazy"
                 />
                <h3 className="font-semibold text-lg text-gray-800">{member.name}</h3>
                <p className="text-sm text-gray-600">{member.title}</p>
              </div>
            ))}
          </div>
          <p className="text-center text-gray-600 mt-6 text-sm italic">
            Our passionate team is dedicated to making GoLocal the leading platform for B2B organic trade.
          </p>
        </div>
      </div>
    </section>
  );
};

export default AboutPage;