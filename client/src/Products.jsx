import React from "react";

const Products = ({ products, refreshCart }) => {

  const addToCart = (productId) => {
    const cartItem = {
      id: productId,
      quantity: 1
    };

    fetch("http://localhost:9000/cart", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(cartItem)
    })
    .then(res => {
      if (res.ok) {
        refreshCart();
      } else {
        throw new Error("Failed to add item.");
      }
    })
    .catch(err => {
      console.error("Add to cart error:", err);
    });
  };

  return (
      <div style={{ padding: "2rem", fontFamily: "Arial, sans-serif" }}>
        <h2 style={{ marginBottom: "1rem" }}>Product List</h2>
        <div style={{ display: "flex", flexWrap: "wrap", gap: "1rem" }}>
          {products.length > 0 ? (
              products.map(product => (
                  <div
                      key={product.id}
                      style={{
                        border: "1px solid #ccc",
                        borderRadius: "8px",
                        padding: "1rem",
                        width: "200px",
                        boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
                      }}
                  >
                    <h3 style={{ fontSize: "1.1rem", marginBottom: "0.5rem" }}>
                      {product.name}
                    </h3>
                    <p style={{ margin: "0.25rem 0" }}>Price: ${product.price}</p>
                    <p style={{ margin: "0.25rem 0", color: "#777" }}>
                      Category ID: {product.categoryId}
                    </p>
                    <button
                        onClick={() => addToCart(product.id)}
                        style={{
                          marginTop: "0.5rem",
                          padding: "0.5rem 1rem",
                          backgroundColor: "#4CAF50",
                          color: "white",
                          border: "none",
                          borderRadius: "5px",
                          cursor: "pointer",
                        }}
                    >
                      Add to Cart
                    </button>
                  </div>
              ))
          ) : (
              <p>Loading products...</p>
          )}
        </div>
      </div>
  );
};

export default Products;