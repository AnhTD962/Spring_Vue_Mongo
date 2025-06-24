import { defineStore } from "pinia";
import { getProducts, getProduct, createProduct, updateProduct, deleteProduct } from "../api/products";

export const useProductStore = defineStore("product", {
    state: () => ({
        products: [],
        selectedProduct: null,
    }),
    actions: {
        async fetchProducts(params) {
            const { data } = await getProducts(params);
            this.products = data;
        },
        async fetchProduct(id) {
            const { data } = await getProduct(id);
            this.selectedProduct = data;
        },
        async createProduct(payload) {
            await createProduct(payload);
            await this.fetchProducts();
        },
        async updateProduct(id, payload) {
            await updateProduct(id, payload);
            await this.fetchProducts();
        },
        async deleteProduct(id) {
            await deleteProduct(id);
            await this.fetchProducts();
        }
    }
});