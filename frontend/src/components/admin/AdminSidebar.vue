<template>
  <aside class="admin-sidebar">
    <router-link to="/admin/products">Manage Products</router-link>
    <router-link to="/admin/categories">Manage Categories</router-link>
    <router-link to="/admin/users">Manage Users</router-link>
    <router-link to="/admin/orders">Manage Orders</router-link>
  </aside>
</template>

<script setup>
</script>

<style scoped>
.admin-sidebar {
  min-width: 180px;
  background: #eef;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  height: 100vh;
}
</style>