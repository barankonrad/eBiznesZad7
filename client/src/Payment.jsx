import React, { useState } from "react";

const Payment = ({ cartItems, products, refreshCart }) => {
  const [cardNumber, setCardNumber] = useState("");
  const [paymentStatus, setPaymentStatus] = useState("");

  const getTotalPrice = () => {
    return cartItems.reduce((sum, item) => {
      const product = products.find(p => p.id === item.id);
      return sum + (product ? product.price * item.quantity : 0);
    }, 0);
  };

  const handlePayment = () => {
    if (!cardNumber) {
      setPaymentStatus("Please enter a card number.");
      return;
    }

    const paymentData = {
      id: Math.floor(Math.random() * 1000000),
      cardNumber,
      amount: getTotalPrice(),
      timestamp: new Date().toISOString(),
      status: "pending",
    };

    fetch("http://localhost:9000/payment", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(paymentData),
    })
    .then(res => res.json())
    .then(() => {
      setPaymentStatus("Payment successful!");
      clearCart();
    })
    .catch((error) => {
      console.error("Payment failed:", error);
      setPaymentStatus("Payment failed. Please try again.");
    });
  };

  const clearCart = () => {
    fetch("http://localhost:9000/cart/clear", { method: "DELETE" })
    .then(() => refreshCart())
    .catch((error) => {
      console.error("Error clearing cart:", error);
    });
  };

  const total = getTotalPrice();

  return (
      <div style={{ padding: "2rem", fontFamily: "Arial, sans-serif" }}>
        <h2>Payment</h2>
        <p>Total: ${total.toFixed(2)}</p>

        <div style={{ marginBottom: "1rem" }}>
          <label>
            Card Number:
            <input
                type="text"
                value={cardNumber}
                onChange={(e) => setCardNumber(e.target.value)}
                placeholder="1234 5678 9876 5432"
                style={{ padding: "0.5rem", marginLeft: "0.5rem", width: "250px" }}
            />
          </label>
        </div>

        <button
            onClick={handlePayment}
            style={{
              padding: "0.75rem 1.5rem",
              backgroundColor: "#4CAF50",
              color: "white",
              border: "none",
              borderRadius: "5px",
              cursor: "pointer",
            }}
        >
          Pay Now
        </button>

        {paymentStatus && (
            <p
                style={{
                  marginTop: "1rem",
                  color: paymentStatus === "Payment successful!" ? "green" : "red",
                }}
            >
              {paymentStatus}
            </p>
        )}
      </div>
  );
};

export default Payment;