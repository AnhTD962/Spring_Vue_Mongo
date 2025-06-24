import { defineStore } from "pinia";
import { getOrders, getOrder, updateOrderStatus, getMyOrders } from "../api/orders";

export const useOrderStore = defineStore("order", {
    state: () => ({
        orders: [],
        myOrders: [],
        selectedOrder: null,
    }),
    actions: {
        async fetchOrders(params) {
            const { data } = await getOrders(params);
            this.orders = data;
        },
        async fetchOrder(id) {
            const { data } = await getOrder(id);
            this.selectedOrder = data;
        },
        async updateOrderStatus(id, status) {
            await updateOrderStatus(id, status);
            await this.fetchOrders();
        },
        async fetchMyOrders() {
            const { data } = await getMyOrders();
            this.myOrders = data;
        }
    }
});