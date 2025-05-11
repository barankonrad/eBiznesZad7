import React, { useState, useEffect } from 'react';
import Cart from "./Cart";
import Products from './Products';
import Payment from './Payment';
import Navbar from './Navbar';

const App = () => {
  const [currentPage, setCurrentPage] = useState('products');
  const [cartItems, setCartItems] = useState([]);
  const [products, setProducts] = useState([]);

  useEffect(() => {
    fetch("http://localhost:9000/items")
    .then(res => res.json())
    .then(data => setProducts(data))
    .catch(err => console.error("Error fetching products:", err));
  }, []);

  useEffect(() => {
    fetch("http://localhost:9000/cartItems")
    .then(res => res.json())
    .then(data => setCartItems(data))
    .catch(err => console.error("Error fetching cart items:", err));
  }, []);

  const refreshCart = () => {
    fetch("http://localhost:9000/cartItems")
    .then(res => res.json())
    .then(data => setCartItems(data))
    .catch(err => console.error("Error refreshing cart items:", err));
  };

  const renderPage = () => {
    switch (currentPage) {
      case 'products':
        return <Products products={products} refreshCart={refreshCart} />;
      case 'cart':
        return <Cart cartItems={cartItems} products={products} refreshCart={refreshCart} />;
      case 'payment':
        return <Payment cartItems={cartItems} products={products} refreshCart={refreshCart} />;
      default:
        return <Products products={products} refreshCart={refreshCart} />;
    }
  };

  return (
      <div style={{ fontFamily: 'Arial, sans-serif' }}>
        <Navbar currentPage={currentPage} setCurrentPage={setCurrentPage} />
        <main style={{ padding: '2rem', maxWidth: '800px', margin: '0 auto' }}>
          {renderPage()}
        </main>
      </div>
  );
};

export default App;