import api from "./axios";

export function getOrders() {
  return api.get("/admin/orders");
}

export function getOrder(orderId) {
    return api.get(`/admin/orders/${orderId}`);
}

export function updateOrderStatus(orderId, status) {
    return api.put(`/admin/orders/${orderId}/status`, null, { params: { st: status } });
}

export function searchOrder(orderId) {
  return api.get("/admin/orders/search", { params: { orderId } });
}

// For users to see their own orders
export function getMyOrders() {
    return api.get("/user/orders");
}

export function checkoutCart(payload){
    return api.post("/user/order", payload);
}

export function getMyOrderDetail(id) {
  return api.get(`user/orders/${id}`)
}