<template>
  <div id="app">
    <template v-if="['/login', '/register', '/forgot-password'].includes(route.path)">
      <router-view />
    </template>
    <template v-else>
      <header class="navbar">
        <div v-if="!auth.isAuthenticated" class="navbar-layout">
          <div class="nav-center">
            <router-link to="/home">Home</router-link>
            <router-link to="/products">Products</router-link>
            <router-link to="/categories">Categories</router-link>
            <router-link to="/cart">Cart</router-link>
          </div>
          <div class="nav-right">
            <router-link to="/login">Login</router-link>
          </div>
        </div>

        <div v-if="auth.isAuthenticated">
          <AdminNavbar v-if="auth.userRole === 'ROLE_ADMIN'" />
          <UserNavbar v-else-if="auth.userRole === 'ROLE_USER'" />
        </div>
      </header>

      <!-- 🔍 Search Bar with Dropdown -->
      <div class="search-bar-wrapper" v-if="route.path === '/home'">
        <input type="text" v-model="searchQuery" placeholder="Search products or categories..." class="search-input"
          @focus="showDropdown = true" @blur="() => setTimeout(() => showDropdown = false, 200)" />

        <ul v-if="showDropdown && searchSuggestions.length" class="search-dropdown">
          <li v-for="item in searchSuggestions" :key="item.id">
            <router-link :to="`/products/${item.id}`" class="search-item-link">
              <img :src="item.image ? `/uploads/product_img/${item.image}` : '/default-product.png'" alt="product image"
                class="dropdown-image" />
              <span class="dropdown-title">{{ item.title }}</span>
            </router-link>
          </li>
        </ul>
      </div>

      <main class="main-content">
        <CategoriesView v-if="route.path === '/home'" :searchQuery="searchQuery" />
        <ProductListView v-if="route.path === '/home'" :searchQuery="searchQuery" />
        <router-view v-if="route.path !== '/home'" />
      </main>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useAuthStore } from './store/auth'
import { useRoute } from 'vue-router'
import AdminNavbar from './components/admin/AdminNavbar.vue'
import UserNavbar from './components/user/UserNavbar.vue'
import ProductListView from './views/ProductListView.vue'
import CategoriesView from './views/CategoriesView.vue'
import { getViewProducts } from './api/products'

const auth = useAuthStore()
const route = useRoute()

// Search logic
const searchQuery = ref('')
const showDropdown = ref(false)
const products = ref([])

onMounted(async () => {
  const { data } = await getViewProducts()
  products.value = data
})

const searchSuggestions = computed(() => {
  if (!searchQuery.value.trim()) return []
  const q = searchQuery.value.toLowerCase()
  return products.value.filter(p =>
    p.title.toLowerCase().includes(q)
  ).slice(0, 5) // limit to 5 results
})
</script>

<style scoped>
#app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

.navbar {
  background-color: #f7f7f7;
  padding: 15px 25px;
  border-bottom: 1px solid #ddd;
}

.navbar-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.nav-center {
  display: flex;
  gap: 20px;
}

.nav-right {
  position: absolute;
  right: 0;
}

.nav-center a,
.nav-right a {
  text-decoration: none;
  color: #007BFF;
  font-weight: 500;
}

.nav-center a:hover,
.nav-right a:hover {
  text-decoration: underline;
}

.main-content {
  padding: 30px 20px;
}

/* 🔍 Search Styles */
.search-bar-wrapper {
  padding: 20px;
  position: relative;
  width: 50%;
  margin: 0 auto;
}

.search-input {
  width: 100%;
  padding: 10px 15px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.search-dropdown {
  position: absolute;
  width: 100%;
  background: white;
  border: 1px solid #ddd;
  border-top: none;
  z-index: 10;
  max-height: 200px;
  overflow-y: auto;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.search-dropdown li {
  padding: 10px 15px;
  cursor: pointer;
}

.search-item-link {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: inherit;
}

.dropdown-image {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.dropdown-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.search-dropdown li:hover {
  background-color: #f2f2f2;
}
</style>
