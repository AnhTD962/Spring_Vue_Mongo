<template>
  <div class="admin-products-wrapper">
    <h2>Admin - Products</h2>

    <!-- Create Button -->
    <div class="header">
      <router-link to="/admin/products/create" class="create-link">+ Create Product</router-link>
    </div>

    <!-- 🔍 Search Bar -->
    <div class="search-bar">
      <input v-model="searchQuery" type="text" placeholder="Search by title or description..." class="search-input" />
    </div>

    <!-- Product Table -->
    <div class="table-wrapper" v-if="filteredProducts.length">
      <table class="products-table">
        <thead>
          <tr>
            <th>Image</th>
            <th>Title</th>
            <th>Description</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Discount</th>
            <th>Active</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in filteredProducts" :key="p.id">
            <td>
              <img :src="p.image ? `/uploads/product_img/${p.image}` : '/default-product.png'" alt="product image"
                class="product-thumbnail" />
            </td>
            <td>{{ p.title }}</td>
            <td>{{ p.description }}</td>
            <td>{{ p.category }}</td>
            <td>${{ p.price }}</td>
            <td>{{ p.stock }}</td>
            <td>{{ p.discount }}%</td>
            <td>{{ p.isActive ? 'Yes' : 'No' }}</td>
            <td>
              <router-link :to="`/admin/products/${p.id}/edit`" class="icon-button" title="Edit">
                <i class="fas fa-edit"></i>
              </router-link>
              <button @click="remove(p.id)" class="icon-button delete" title="Delete">
                <i class="fas fa-trash-alt"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <!-- Pagination -->
      <div class="pagination" v-if="totalPages > 1">
        <button :disabled="currentPage === 0" @click="fetch(currentPage - 1)">Previous</button>
        <span>Page {{ currentPage + 1 }} / {{ totalPages }}</span>
        <button :disabled="currentPage === totalPages - 1" @click="fetch(currentPage + 1)">Next</button>
      </div>
    </div>

    <!-- No Results -->
    <div v-else class="no-products">No products found.</div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getProducts, deleteProduct } from '../../api/products'

const products = ref([])
const searchQuery = ref('')
const currentPage = ref(0)
const totalPages = ref(0)

// Fetch data
async function fetch(page = 0) {
  const { data } = await getProducts(page, 5)
  products.value = data.content;
  totalPages.value = data.totalPages;
  currentPage.value = data.number;
}
onMounted(fetch)

// Search filter
const filteredProducts = computed(() => {
  const query = searchQuery.value.toLowerCase().trim()
  if (!query) return products.value
  return products.value.filter(p =>
    p.title.toLowerCase().includes(query) ||
    p.description?.toLowerCase().includes(query)
  )
})

// Delete product
async function remove(id) {
  if (confirm('Are you sure you want to delete this product?')) {
    await deleteProduct(id)
    fetch()
  }
}
</script>

<style scoped>
.admin-products-wrapper {
  padding: 40px 20px;
  background: #f8f8f8;
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
  padding: 0.5rem 1rem;
  background: #7b2ff2;
  color: #fff;
  border-radius: 4px;
  text-decoration: none;
  font-weight: bold;
}

.create-link:hover {
  background: #6920d4;
}

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

.table-wrapper {
  overflow-x: auto;
}

.products-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.product-thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.products-table th,
.products-table td {
  padding: 0.75rem;
  border: 1px solid #ddd;
  text-align: center;
}

.products-table th {
  background: #f0f0f0;
  color: #333;
  font-weight: 600;
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

.no-products {
  text-align: center;
  font-size: 1.1rem;
  color: #777;
  margin-top: 2rem;
}
</style>
