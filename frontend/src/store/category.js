import { defineStore } from "pinia";
import {
  getCategories,
  getCategory,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../api/categories";

export const useCategoryStore = defineStore("category", {
  state: () => ({
    categories: [],
    selectedCategory: null,
    currentPage: 0,
    totalPages: 0,
    totalItems: 0,
    pageSize: 6,
  }),
  actions: {
    async fetchCategories(page = 0) {
      const { data } = await getCategories(page, this.pageSize);
      this.categories = data.content;
      this.currentPage = data.number;
      this.totalPages = data.totalPages;
      this.totalItems = data.totalElements;
    },
    async fetchCategory(id) {
      const { data } = await getCategory(id);
      this.selectedCategory = data;
    },
    async createCategory(payload) {
      await createCategory(payload);
      await this.fetchCategories(this.currentPage);
    },
    async updateCategory(id, payload) {
      await updateCategory(id, payload);
      await this.fetchCategories(this.currentPage);
    },
    async deleteCategory(id) {
      await deleteCategory(id);
      await this.fetchCategories(this.currentPage);
    },
  },
});
