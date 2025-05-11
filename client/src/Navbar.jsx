import React from 'react';

const Navbar = ({ currentPage, setCurrentPage }) => {
  const navStyle = (page) => ({
    marginRight: '1rem',
    padding: '0.5rem 1rem',
    cursor: 'pointer',
    backgroundColor: currentPage === page ? '#4CAF50' : '#eee',
    color: currentPage === page ? 'white' : 'black',
    border: 'none',
    borderRadius: '4px',
  });

  return (
      <nav style={{ padding: '1rem', borderBottom: '1px solid #ccc' }}>
        <button style={navStyle('products')} onClick={() => setCurrentPage('products')}>
          Products
        </button>
        <button style={navStyle('cart')} onClick={() => setCurrentPage('cart')}>
          Cart
        </button>
        <button style={navStyle('payment')} onClick={() => setCurrentPage('payment')}>
          Payment
        </button>
      </nav>
  );
};

export default Navbar;