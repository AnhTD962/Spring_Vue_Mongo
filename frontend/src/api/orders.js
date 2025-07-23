import api from "./axios";

export function getOrders(page = 0, size = 6, status) {
  return api.get(`/orders`, {
    params: {
      page,
      size,
      status,
    },
  });
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
    newStatus,
  });
}

export function searchOrder(orderId) {
  return api.get("/orders/search", { params: { orderId } });
}

// For users to see their own orders
export function getMyOrders(page = 0, size = 5, status = "") {
  return api.get("/orders/my-orders", {
    params: { page, size, status },
  });
}

export function checkoutCart(payload) {
  return api.post("/orders/place-order", payload);
}

export function getMyOrderDetail(id) {
  return api.get(`/orders/my-orders/${id}`);
}
