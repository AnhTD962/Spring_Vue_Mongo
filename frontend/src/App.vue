<template>
  <div id="app">
    <nav>
      <router-link to="/products">Products</router-link> |
      <router-link to="/categories">Categories</router-link> 
      <router-link to="/profile" v-if="auth.isAuthenticated"> | Profile</router-link> |
      <router-link to="/orders">Orders</router-link> |
      <router-link to="/cart">Cart</router-link>
      <span v-if="auth.isAuthenticated">
        | Welcome: <b>{{ auth.user?.email }}</b>
        <button @click="handleLogout">Logout</button>
      </span>
      <span v-else>
        | <router-link to="/login">Login</router-link>
      </span>
      <UserSidebar v-if="auth.isAuthenticated"/>
    </nav>
    <router-view />
  </div>
</template>

<script setup>
import { useAuthStore } from './store/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function handleLogout() {
  auth.logout()
  router.push('/login')
}
</script>