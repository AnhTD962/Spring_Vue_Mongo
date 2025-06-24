import api from "./axios";

export function getCart() {
    return api.get("/cart");
}

export function addToCart(payload) {
    return api.post("/cart", payload);
}

export function updateCartItem(id, payload) {
    return api.put(`/cart/${id}`, payload);
}

export function removeCartItem(id) {
    return api.delete(`/cart/${id}`);
}