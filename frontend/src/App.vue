<template>
  <div id="app">
    <template v-if="route.path === '/login'">
      <router-view />
    </template>
    <template v-else>
      <div>
        <div v-if="!auth.isAuthenticated">
          <router-link to="/home">| Home </router-link>
          <router-link to="/products">| Products </router-link>
          <router-link to="/categories">| Categories </router-link>
          <router-link to="/cart">| Cart </router-link>
          <router-link to="/login">| Login </router-link>
        </div>
        <AdminNavbar v-if="auth.userRole === 'ROLE_ADMIN'" />
        <UserNavbar v-else-if="auth.userRole === 'ROLE_USER'" />
        <span v-if="auth.isAuthenticated">
          | Welcome: <b>{{ auth.user?.email }}</b>
        </span>
        <!-- Only show these when on home -->
        <ProductListView v-if="route.path === '/home'" />
        <CategoriesView v-if="route.path === '/home'" />
        <!-- Show routed component for other pages -->
        <router-view v-if="route.path !== '/home'" />
      </div>
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