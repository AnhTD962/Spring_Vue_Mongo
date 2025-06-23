<template>
  <div class="min-h-screen bg-gray-100">
    <!-- Hero Section -->
    <div class="bg-indigo-700 text-white">
      <div class="container mx-auto px-6 py-16 text-center">
        <h1 class="text-4xl md:text-6xl font-bold mb-4">Welcome to Admin Dashboard</h1>
        <p class="text-xl mb-8">Manage your application with ease</p>
        
        <div class="flex flex-wrap justify-center gap-4">
          <router-link 
            to="/admin/categories" 
            class="px-6 py-3 bg-white text-indigo-700 font-semibold rounded-lg hover:bg-gray-100 transition-colors"
          >
            Manage Categories
          </router-link>
          <router-link 
            to="/admin/products" 
            class="px-6 py-3 border-2 border-white text-white font-semibold rounded-lg hover:bg-indigo-600 transition-colors"
          >
            Manage Products
          </router-link>
        </div>
      </div>
    </div>


    <!-- Stats Section -->
    <div class="container mx-auto px-6 py-16">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <!-- Categories Card -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-indigo-100 text-indigo-600 mr-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
            </div>
            <div>
              <p class="text-gray-500 text-sm">Total Categories</p>
              <p class="text-2xl font-bold">{{ stats.categories || 0 }}</p>
            </div>
          </div>
        </div>

        <!-- Products Card -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-green-100 text-green-600 mr-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
              </svg>
            </div>
            <div>
              <p class="text-gray-500 text-sm">Total Products</p>
              <p class="text-2xl font-bold">{{ stats.products || 0 }}</p>
            </div>
          </div>
        </div>

        <!-- Users Card -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-purple-100 text-purple-600 mr-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
            </div>
            <div>
              <p class="text-gray-500 text-sm">Total Users</p>
              <p class="text-2xl font-bold">{{ stats.users || 0 }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Recent Activity -->
    <div class="container mx-auto px-6 pb-16">
      <div class="bg-white rounded-lg shadow overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h2 class="text-lg font-semibold text-gray-800">Recent Activity</h2>
        </div>
        <div class="divide-y divide-gray-200">
          <div v-for="(activity, index) in recentActivities" :key="index" class="px-6 py-4 hover:bg-gray-50">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <div class="h-10 w-10 rounded-full bg-indigo-100 flex items-center justify-center">
                  <span class="text-indigo-600">{{ activity.initials }}</span>
                </div>
              </div>
              <div class="ml-4">
                <p class="text-sm font-medium text-gray-900">{{ activity.title }}</p>
                <p class="text-sm text-gray-500">{{ activity.description }}</p>
              </div>
              <div class="ml-auto text-sm text-gray-500">
                {{ activity.time }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();

// Sample data - replace with actual API calls
const stats = ref({
  categories: 0,
  products: 0,
  users: 0,
  orders: 0
});

const recentActivities = ref([
  {
    title: 'New category added',
    description: 'Electronics category was created',
    time: '2 hours ago',
    initials: 'EC'
  },
  {
    title: 'Product updated',
    description: 'Smartphone X was updated',
    time: '5 hours ago',
    initials: 'PU'
  },
  {
    title: 'New user registered',
    description: 'john.doe@example.com',
    time: '1 day ago',
    initials: 'NU'
  }
]);

// Fetch dashboard stats
const fetchStats = async () => {
  try {
    // Replace with actual API calls
    // const response = await axios.get('/api/dashboard/stats');
    // stats.value = response.data;
    
    // Mock data for now
    stats.value = {
      categories: 12,
      products: 87,
      users: 45,
      orders: 156
    };
  } catch (error) {
    console.error('Error fetching dashboard stats:', error);
  }
};

onMounted(() => {
  fetchStats();
});
</script>
