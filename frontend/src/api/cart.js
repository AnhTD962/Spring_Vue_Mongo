import api from "./axios";

export function getCart() {
  return api.get("/user/cart");
}

export function addToCart(payload) {
  return api.post("/user/cart/add", {
    pid: payload.productId,
    uid: payload.userId,
  });
}

export function updateCartQuantity(cartId, quantity) {
  return api.put(`/user/cart/${cartId}?quantity=${quantity}`);
}

export function removeCartItem(cartId) {
  return api.delete(`/user/cart/${cartId}`);
}
