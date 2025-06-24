<template>
  <aside class="user-sidebar">
    <router-link to="/profile">Profile</router-link>
    <router-link to="/change-password">Change Password</router-link>
    <router-link to="/my-orders">My Orders</router-link>
    <router-link to="/cart">Cart</router-link>
  </aside>
</template>

<script setup>
</script>

<style scoped>
.user-sidebar {
  min-width: 160px;
  background: #eef;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  height: 100vh;
}
</style>