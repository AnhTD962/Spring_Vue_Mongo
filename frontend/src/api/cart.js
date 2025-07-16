import api from "./axios";

export function getCart() {
  return api.get("/cart");
}

export function addToCart(payload) {
  return api.post("/cart", {
    pid: payload.productId,
    quantity: payload.quantity
  });
}

export function updateCartQuantity(cartId, quantity) {
  return api.put(`/cart/${cartId}?quantity=${quantity}`);
}

export function removeCartItem(cartId) {
  return api.delete(`/cart/${cartId}`);
}

