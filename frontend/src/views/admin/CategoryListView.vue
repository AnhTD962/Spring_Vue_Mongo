<template>
  <div class="admin-categories-wrapper">
    <h2>Admin - Categories</h2>

    <!-- Create Button -->
    <div class="header">
      <router-link to="/admin/categories/create" class="create-link">+ Create Category</router-link>
    </div>

    <!-- 🔍 Search Bar -->
    <div class="search-bar">
      <input v-model="searchQuery" type="text" placeholder="Search by category name..." class="search-input" />
    </div>

    <!-- Categories Table -->
    <table class="categories-table" v-if="filteredCategories.length">
      <thead>
        <tr>
          <th>Image</th>
          <th>Name</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="c in filteredCategories" :key="c.id">
          <td>
            <img :src="`/uploads/category_img/${c.imageName}`" alt="Category Image" class="category-image" />
          </td>
          <td>{{ c.name }}</td>
          <td>{{ c.isActive ? 'Active' : 'Inactive' }}</td>
          <td>
            <router-link :to="`/admin/categories/${c.id}/edit`" class="icon-button" title="Edit">
              <i class="fas fa-edit"></i>
            </router-link>
            <button @click="remove(c.id)" class="icon-button delete" title="Delete">
              <i class="fas fa-trash-alt"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else class="no-categories">No categories found.</div>

    <!-- Pagination -->
    <div v-if="totalPages > 1" class="pagination">
      <button @click="prevPage" :disabled="currentPage === 0">Prev</button>
      <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage + 1 >= totalPages">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from "vue";
import { getCategories, deleteCategory } from "../../api/categories";

const categories = ref([]);
const searchQuery = ref("");
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = 5;
const filteredCategories = ref([]);

async function fetch() {
  const { data } = await getCategories(currentPage.value, pageSize);
  categories.value = data.content; // Only current page data
  totalPages.value = data.totalPages;
  applySearch(); // search after update
}

function applySearch() {
  const q = searchQuery.value.trim().toLowerCase();
  if (!q) {
    filteredCategories.value = categories.value;
  } else {
    filteredCategories.value = categories.value.filter(c =>
      c.name.toLowerCase().includes(q)
    );
  }
}

function nextPage() {
  if (currentPage.value + 1 < totalPages.value) {
    currentPage.value++;
    fetch();
  }
}

function prevPage() {
  if (currentPage.value > 0) {
    currentPage.value--;
    fetch();
  }
}

async function remove(id) {
  if (confirm("Are you sure you want to delete this category?")) {
    await deleteCategory(id);
    await fetch();
  }
}

// Watch search input
watch(searchQuery, applySearch);

// Initial fetch
onMounted(fetch);
</script>


<style scoped>
.admin-categories-wrapper {
  padding: 40px 20px;
  background: #f9f9f9;
  min-height: 100vh;
}

h2 {
  text-align: center;
  margin-bottom: 2rem;
}

.header {
  text-align: right;
  margin-bottom: 1rem;
}

.create-link {
  background-color: #7b2ff2;
  color: #fff;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  text-decoration: none;
  font-weight: bold;
}

.create-link:hover {
  background-color: #6920d4;
}

/* 🔍 Search */
.search-bar {
  margin-bottom: 1rem;
  text-align: right;
}

.search-input {
  padding: 0.5rem 1rem;
  width: 300px;
  max-width: 100%;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.categories-table {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  border-collapse: collapse;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.categories-table th,
.categories-table td {
  padding: 0.75rem;
  border: 1px solid #ddd;
  text-align: center;
}

.categories-table th {
  background: #f0f0f0;
  font-weight: 600;
}

.category-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  margin: 0 5px;
  color: #2196f3;
}

.icon-button.delete {
  color: #d9534f;
}

.icon-button:hover {
  opacity: 0.7;
}

.no-categories {
  text-align: center;
  margin-top: 2rem;
  color: #666;
}

.pagination {
  text-align: center;
  margin-top: 1.5rem;
}

.pagination button {
  margin: 0 0.5rem;
  padding: 6px 12px;
  background: #7b2ff2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
