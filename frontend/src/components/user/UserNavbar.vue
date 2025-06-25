<template>
  <nav>
    <router-link to="/products">Products</router-link> |
    <router-link to="/categories">Categories</router-link> |
    <router-link to="/cart">Cart</router-link> |
    <router-link to="/my-orders">Orders</router-link>
    <button @click="logout" class="logout-btn">Logout</button>
  </nav>
</template>

<script setup>
import { useAuthStore } from '../../store/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function logout() {
  auth.logout()
  router.push('/login')
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