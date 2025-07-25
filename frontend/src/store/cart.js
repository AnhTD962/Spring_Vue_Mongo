import { defineStore } from "pinia";
import { getCart, addToCart, updateCartQuantity, removeCartItem } from "../api/cart";

export const useCartStore = defineStore("cart", {
    state: () => ({
        items: [],
    }),
    actions: {
        async fetchCart() {
            const { data } = await getCart();
            this.items = data;
        },
        async addItem(payload) {
            await addToCart(payload);
            await this.fetchCart();
        },
        async updateCartQuantity(id, payload) {
            await updateCartQuantity(id, payload);
            await this.fetchCart();
        },
        async removeItem(id) {
            await removeCartItem(id);
            await this.fetchCart();
        }
    }
});