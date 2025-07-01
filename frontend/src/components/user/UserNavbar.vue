<template>
  <div class="user-navbar">
    <div class="nav-links">
      <router-link to="/home">Home</router-link>
      <router-link to="/products">Products</router-link>
      <router-link to="/categories">Categories</router-link>
      <router-link to="/cart">Cart</router-link>
      <router-link to="/my-orders">Orders</router-link>
      <router-link to="/user-profile">Profile</router-link>
    </div>

    <div class="profile-dropdown" @click="toggleDropdown">
      <span class="welcome-msg">Welcome: <b>{{ auth.user?.email }}</b></span>
      <div v-if="showDropdown" class="dropdown-menu">
        <router-link to="/user-change-password">Change Password</router-link>
        <a href="#" @click.prevent="logout">Logout</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../../store/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()
const showDropdown = ref(false)

function logout() {
  auth.logout()
  router.push('/login')
}

function toggleDropdown() {
  showDropdown.value = !showDropdown.value
}
</script>

<style scoped>
.user-navbar {
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between; 
  align-items: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  flex-wrap: wrap;
}

.nav-links {
  flex: 1;
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.nav-links a {
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.nav-links a:hover {
  color: #7b2ff2;
}

.profile-dropdown {
  position: relative;
  cursor: pointer;
  margin-left: auto; 
  text-align: right;
}

.welcome-msg {
  font-size: 14px;
  color: #333;
}

.dropdown-menu {
  position: absolute;
  top: 120%;
  right: 0;
  background-color: white;
  border: 1px solid #ddd;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  min-width: 150px;
  z-index: 10;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  text-align: left;
}

.dropdown-menu a,
.dropdown-menu router-link {
  color: #333;
  text-decoration: none;
  font-weight: 500;
}

.dropdown-menu a:hover {
  color: #c00;
}
</style>
