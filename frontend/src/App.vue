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

      <main class="main-content">
        <CategoriesView v-if="route.path === '/home'" />
        <ProductListView v-if="route.path === '/home'" />
        <router-view v-if="route.path !== '/home'" />
      </main>
    </template>
  </div>
</template>

<script setup>
import { useAuthStore } from './store/auth'
import { useRoute } from 'vue-router'
import AdminNavbar from './components/admin/AdminNavbar.vue'
import UserNavbar from './components/user/UserNavbar.vue'
import ProductListView from './views/ProductListView.vue'
import CategoriesView from './views/CategoriesView.vue'

const auth = useAuthStore()
const route = useRoute()
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
</style>
