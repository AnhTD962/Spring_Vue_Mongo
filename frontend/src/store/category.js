import { defineStore } from "pinia";
import { getCategories, getCategory, createCategory, updateCategory, deleteCategory } from "../api/categories";

export const useCategoryStore = defineStore("category", {
    state: () => ({
        categories: [],
        selectedCategory: null,
    }),
    actions: {
        async fetchCategories() {
            const { data } = await getCategories();
            this.categories = data;
        },
        async fetchCategory(id) {
            const { data } = await getCategory(id);
            this.selectedCategory = data;
        },
        async createCategory(payload) {
            await createCategory(payload);
            await this.fetchCategories();
        },
        async updateCategory(id, payload) {
            await updateCategory(id, payload);
            await this.fetchCategories();
        },
        async deleteCategory(id) {
            await deleteCategory(id);
            await this.fetchCategories();
        }
    }
});