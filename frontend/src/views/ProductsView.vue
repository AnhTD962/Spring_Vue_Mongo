<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Our Products</h1>
    
    <!-- Category Filter -->
    <div class="mb-8">
      <label for="category" class="block text-sm font-medium text-gray-700 mb-2">Filter by Category</label>
      <select 
        v-model="selectedCategory" 
        @change="fetchProducts"
        class="w-full md:w-64 p-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
      >
        <option value="">All Categories</option>
        <option v-for="category in categories" :key="category.id" :value="category.id">
          {{ category.name }}
        </option>
      </select>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="flex justify-center items-center h-64">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
    </div>

    <!-- Products Grid -->
    <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      <div 
        v-for="product in products" 
        :key="product.id"
        class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300"
      >
        <div class="h-48 bg-gray-200 overflow-hidden">
          <img 
            v-if="product.imageUrl" 
            :src="product.imageUrl" 
            :alt="product.name"
            class="w-full h-full object-cover"
          >
          <div v-else class="w-full h-full flex items-center justify-center bg-gray-100 text-gray-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
        </div>
        <div class="p-4">
          <h3 class="text-lg font-semibold text-gray-800 mb-1">{{ product.name }}</h3>
          <p class="text-gray-600 text-sm mb-2">{{ product.categoryName || 'Uncategorized' }}</p>
          <p class="text-gray-700 font-medium">${{ product.price?.toFixed(2) }}</p>
          <div class="mt-4 flex justify-between items-center">
            <span 
              class="px-2 py-1 text-xs rounded-full"
              :class="product.stock > 0 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
            >
              {{ product.stock > 0 ? 'In Stock' : 'Out of Stock' }}
            </span>
            <button 
              @click="addToCart(product)"
              :disabled="product.stock <= 0"
              class="px-3 py-1 bg-indigo-600 text-white text-sm rounded hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-if="!loading && products.length === 0" class="text-center py-12">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mx-auto text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
      </svg>
      <h3 class="mt-2 text-lg font-medium text-gray-900">No products found</h3>
      <p class="mt-1 text-sm text-gray-500">There are no products available in this category.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '@/stores/cart';
import CategoryService from '@/services/category.service';
import ProductService from '@/services/product.service';

const router = useRouter();
const cartStore = useCartStore();

const products = ref([]);
const categories = ref([]);
const selectedCategory = ref('');
const loading = ref(true);

// Fetch products
const fetchProducts = async () => {
  try {
    loading.value = true;
    if (selectedCategory.value) {
      products.value = await ProductService.getProductsByCategory(selectedCategory.value);
    } else {
      products.value = await ProductService.getAllProducts();
    }
  } catch (error) {
    console.error('Error fetching products:', error);
  } finally {
    loading.value = false;
  }
};

// Fetch categories
const fetchCategories = async () => {
  try {
    categories.value = await CategoryService.getAllCategories();
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};

// Add to cart function
const addToCart = (product) => {
  cartStore.addToCart({
    id: product.id,
    name: product.name,
    price: product.price,
    quantity: 1,
    image: product.imageUrl,
    stock: product.stock
  });
};

// Initialize
onMounted(async () => {
  await Promise.all([fetchProducts(), fetchCategories()]);
});
</script>
