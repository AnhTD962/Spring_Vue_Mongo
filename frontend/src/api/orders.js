import api from "./axios";

export function getOrders() {
  return api.get("/orders");
}

export function getOrder(orderId) {
    return api.get(`/orders/${orderId}`);
}

export function updateOrderStatus(orderId, status) {
    return api.put(`/orders/${orderId}/status`, null, { params: { st: status } });
}

export function bulkUpdateStatusByIds(orderIds, newStatus) {
  return api.put("/orders/bulk-update-status-by-ids", {
    orderIds,
    newStatus
  });
}

export function searchOrder(orderId) {
  return api.get("/orders/search", { params: { orderId } });
}

// For users to see their own orders
export function getMyOrders() {
    return api.get("/orders/my-orders");
}

export function checkoutCart(payload){
    return api.post("/orders/place-order", payload);
}

export function getMyOrderDetail(id) {
  return api.get(`/orders/my-orders/${id}`)
}