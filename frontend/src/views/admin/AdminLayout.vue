<template>
  <div class="min-h-screen bg-gray-100">
    <!-- Sidebar -->
    <div class="bg-indigo-700 text-white w-64 fixed h-full">
      <div class="p-4">
        <h1 class="text-2xl font-bold">Admin Panel</h1>
      </div>
      <nav class="mt-6">
        <router-link 
          to="/admin/categories" 
          class="flex items-center px-6 py-3 text-indigo-200 hover:bg-indigo-600"
          :class="{ 'bg-indigo-800': $route.path.includes('categories') }"
        >
          <span class="mx-3">Categories</span>
        </router-link>
        <router-link 
          to="/admin/products" 
          class="flex items-center px-6 py-3 text-indigo-200 hover:bg-indigo-600"
          :class="{ 'bg-indigo-800': $route.path.includes('products') }"
        >
          <span class="mx-3">Products</span>
        </router-link>
        <router-link 
          to="/admin/users" 
          class="flex items-center px-6 py-3 text-indigo-200 hover:bg-indigo-600"
          :class="{ 'bg-indigo-800': $route.path.includes('users') }"
        >
          <span class="mx-3">Users</span>
        </router-link>
      </nav>
    </div>

    <!-- Main Content -->
    <div class="ml-64 p-6">
      <!-- Top Navigation -->
      <header class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-semibold text-gray-800">{{ pageTitle }}</h2>
        <div class="flex items-center space-x-4">
          <span class="text-gray-600">Welcome, {{ user?.name }}</span>
          <button 
            @click="logout"
            class="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition-colors"
          >
            Logout
          </button>
        </div>
      </header>

      <!-- Page Content -->
      <main class="bg-white rounded-lg shadow p-6">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const user = computed(() => authStore.user);

const pageTitle = computed(() => {
  if (route.path.includes('categories')) return 'Categories Management';
  if (route.path.includes('products')) return 'Products Management';
  if (route.path.includes('users')) return 'Users Management';
  return 'Dashboard';
});

const logout = () => {
  authStore.logout();
  router.push('/login');
};
</script>
