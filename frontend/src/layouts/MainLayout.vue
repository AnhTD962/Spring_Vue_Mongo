<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Navigation -->
    <nav class="bg-white shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex">
            <div class="flex-shrink-0 flex items-center">
              <router-link to="/" class="text-xl font-bold text-indigo-600">ShopApp</router-link>
            </div>
            <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
              <router-link 
                to="/" 
                class="border-indigo-500 text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium"
                :class="{ 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700': $route.path !== '/' }"
              >
                Home
              </router-link>
              <router-link 
                to="/products" 
                class="border-indigo-500 text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium"
                :class="{ 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700': $route.path !== '/products' }"
              >
                Products
              </router-link>
            </div>
          </div>
          <div class="hidden sm:ml-6 sm:flex sm:items-center">
            <div v-if="authStore.isAuthenticated" class="ml-3 relative">
              <div class="flex items-center space-x-4">
                <router-link 
                  to="/profile" 
                  class="text-gray-700 hover:text-indigo-600 px-3 py-2 text-sm font-medium"
                >
                  My Profile
                </router-link>
                <button 
                  @click="logout"
                  class="text-gray-700 hover:text-indigo-600 px-3 py-2 text-sm font-medium"
                >
                  Logout
                </button>
              </div>
            </div>
            <div v-else>
              <router-link 
                to="/login" 
                class="text-gray-700 hover:text-indigo-600 px-3 py-2 text-sm font-medium"
              >
                Login
              </router-link>
              <router-link 
                to="/register" 
                class="ml-4 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700"
              >
                Sign up
              </router-link>
            </div>
          </div>
          <!-- Mobile menu button -->
          <div class="-mr-2 flex items-center sm:hidden">
            <button 
              @click="isMobileMenuOpen = !isMobileMenuOpen"
              type="button" 
              class="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500"
              aria-controls="mobile-menu" 
              :aria-expanded="isMobileMenuOpen"
            >
              <span class="sr-only">Open main menu</span>
              <svg class="h-6 w-6" :class="{'hidden': isMobileMenuOpen, 'block': !isMobileMenuOpen}" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
              </svg>
              <svg class="h-6 w-6" :class="{'block': isMobileMenuOpen, 'hidden': !isMobileMenuOpen}" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Mobile menu, show/hide based on menu state. -->
      <div v-if="isMobileMenuOpen" class="sm:hidden" id="mobile-menu">
        <div class="pt-2 pb-3 space-y-1">
          <router-link 
            to="/" 
            @click="isMobileMenuOpen = false"
            class="block pl-3 pr-4 py-2 border-l-4 text-base font-medium"
            :class="$route.path === '/' ? 'bg-indigo-50 border-indigo-500 text-indigo-700' : 'border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-gray-700'"
          >
            Home
          </router-link>
          <router-link 
            to="/products" 
            @click="isMobileMenuOpen = false"
            class="block pl-3 pr-4 py-2 border-l-4 text-base font-medium"
            :class="$route.path === '/products' ? 'bg-indigo-50 border-indigo-500 text-indigo-700' : 'border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-gray-700'"
          >
            Products
          </router-link>
          <template v-if="authStore.isAuthenticated">
            <router-link 
              to="/profile" 
              @click="isMobileMenuOpen = false"
              class="block pl-3 pr-4 py-2 border-l-4 text-base font-medium border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-gray-700"
            >
              My Profile
            </router-link>
            <button 
              @click="logout"
              class="block w-full text-left pl-3 pr-4 py-2 border-l-4 text-base font-medium border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-gray-700"
            >
              Logout
            </button>
          </template>
          <template v-else>
            <router-link 
              to="/login" 
              @click="isMobileMenuOpen = false"
              class="block pl-3 pr-4 py-2 border-l-4 text-base font-medium border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-gray-700"
            >
              Login
            </router-link>
            <router-link 
              to="/register" 
              @click="isMobileMenuOpen = false"
              class="block pl-3 pr-4 py-2 border-l-4 text-base font-medium border-transparent text-indigo-600 hover:bg-gray-50 hover:border-indigo-300 hover:text-indigo-800"
            >
              Sign up
            </router-link>
          </template>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <main class="py-6">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();
const isMobileMenuOpen = ref(false);

const logout = async () => {
  await authStore.logout();
  router.push('/login');
};
</script>
