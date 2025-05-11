import React from "react";

const Cart = ({ cartItems, products, refreshCart }) => {

  const removeFromCart = () => {
    fetch(`http://localhost:9000/cart/clear`, {
      method: "DELETE",
    })
    .then(() => refreshCart())
    .catch((error) => {
      console.error("Error removing item from cart:", error);
    });
  };

  const findProduct = (id) => products.find(p => p.id === id);

  const groupedItems = cartItems.reduce((acc, item) => {
    acc[item.id] = acc[item.id] || { id: item.id, quantity: 0 };
    acc[item.id].quantity += item.quantity;
    return acc;
  }, {});

  const groupedArray = Object.values(groupedItems);

  const totalPrice = groupedArray.reduce((sum, item) => {
    const product = findProduct(item.id);
    return sum + (product ? product.price * item.quantity : 0);
  }, 0);

  return (
      <div style={{ padding: "2rem", fontFamily: "Arial, sans-serif" }}>
        <h2>Shopping Cart</h2>

        {groupedArray.length === 0 ? (
            <p>Your cart is currently empty.</p>
        ) : (
            <ul style={{ listStyle: "none", padding: 0 }}>
              {groupedArray.map((item) => {
                const product = findProduct(item.id);

                return (
                    <li
                        key={item.id}
                        style={{
                          padding: "1rem",
                          marginBottom: "1rem",
                          border: "1px solid #ccc",
                          borderRadius: "8px",
                        }}
                    >
                      {product ? (
                          <>
                            <h3 style={{ marginBottom: "0.25rem" }}>{product.name}</h3>
                            <p style={{ marginTop: 0, marginBottom: "0.5rem", color: "#777" }}>
                              Category ID: {product.categoryId}
                            </p>
                            <p>Unit price: ${product.price.toFixed(2)}</p>
                            <p>Quantity: {item.quantity}</p>
                            <p>Total: ${(product.price * item.quantity).toFixed(2)}</p>
                          </>
                      ) : (
                          <p>Loading product info...</p>
                      )}
                      <button
                          onClick={() => removeFromCart()}
                          style={{
                            marginTop: "0.5rem",
                            backgroundColor: "#e74c3c",
                            color: "white",
                            padding: "0.5rem 1rem",
                            border: "none",
                            borderRadius: "5px",
                            cursor: "pointer",
                          }}
                      >
                        Remove
                      </button>
                    </li>
                );
              })}
            </ul>
        )}

        <hr style={{ margin: "2rem 0" }} />
        <h3>Total cart cost: ${totalPrice.toFixed(2)}</h3>
      </div>
  );
};

export default Cart;