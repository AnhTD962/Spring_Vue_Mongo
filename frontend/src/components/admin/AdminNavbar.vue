<template>
  <nav>
    <router-link to="/admin/products">Manage Products</router-link> |
    <router-link to="/admin/categories">Manage Categories</router-link> |
    <router-link to="/admin/users">Manage Users</router-link> |
    <router-link to="/admin/orders">Manage Orders</router-link> |
    <router-link to="/admin-profile">Profile</router-link> 
    <button @click="logout" class="logout-btn">Logout</button>
  </nav>
</template>

<script setup>
import { useAuthStore } from '../../store/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function logout() {
  const wasAdmin = auth.userRole === 'ROLE_ADMIN' 
  auth.logout()
  router.push(wasAdmin ? '/home' : '/login') 
}
</script>

<style scoped>
.logout-btn {
  margin-left: 1rem;
  background: #c00;
  color: #fff;
  border: none;
  padding: 0.25rem 1rem;
  cursor: pointer;
  border-radius: 3px;
}
</style>