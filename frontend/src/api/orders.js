import api from "./axios";

export function getOrders(params) {
    return api.get("/admin/orders", { params });
}

export function getOrder(id) {
    return api.get(`/admin/orders/${id}`);
}

export function updateOrderStatus(id, status) {
    return api.put(`/admin/orders/${id}/status`, null, { params: { st: status } });
}

// For users to see their own orders
export function getMyOrders() {
    return api.get("/user/orders");
}

export function checkoutCart(payload){
    return api.post("/user/order", payload);
}