<template>
  <div class="admin-categories-wrapper">
    <h2>Admin - Categories</h2>

    <!-- Create Button -->
    <div class="header">
      <router-link to="/admin/categories/create" class="create-link">+ Create Category</router-link>
    </div>

    <!-- 🔍 Search Bar -->
    <div class="search-bar">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="Search by category name..."
        class="search-input"
      />
    </div>

    <!-- Categories Table -->
    <table class="categories-table" v-if="paginatedCategories.length">
      <thead>
        <tr>
          <th>Name</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="c in paginatedCategories" :key="c.id">
          <td>{{ c.name }}</td>
          <td>{{ c.isActive ? 'Active' : 'Inactive' }}</td>
          <td>
            <router-link :to="`/admin/categories/${c.id}/edit`" class="edit-link">Edit</router-link>
            <button @click="remove(c.id)" class="delete-btn">Delete</button>
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
import { ref, computed, onMounted } from "vue";
import { getCategories, deleteCategory } from "../../api/categories";

const allCategories = ref([]);
const searchQuery = ref("");
const currentPage = ref(0);
const pageSize = 5;

// Filtered categories by search
const filteredCategories = computed(() => {
  const q = searchQuery.value.toLowerCase().trim();
  if (!q) return allCategories.value;
  return allCategories.value.filter(c =>
    c.name.toLowerCase().includes(q)
  );
});

// Pagination based on filtered list
const totalPages = computed(() =>
  Math.ceil(filteredCategories.value.length / pageSize)
);

const paginatedCategories = computed(() => {
  const start = currentPage.value * pageSize;
  return filteredCategories.value.slice(start, start + pageSize);
});

// Pagination handlers
function nextPage() {
  if (currentPage.value + 1 < totalPages.value) currentPage.value++;
}
function prevPage() {
  if (currentPage.value > 0) currentPage.value--;
}

// Fetch and remove
async function fetch() {
  const { data } = await getCategories();
  allCategories.value = data;
  currentPage.value = 0;
}

async function remove(id) {
  if (confirm("Are you sure you want to delete this category?")) {
    await deleteCategory(id);
    await fetch();
  }
}

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

.edit-link {
  margin-right: 1rem;
  color: #007bff;
  text-decoration: underline;
}

.delete-btn {
  background-color: #d9534f;
  color: #fff;
  border: none;
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  cursor: pointer;
}
.delete-btn:hover {
  background-color: #c9302c;
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
