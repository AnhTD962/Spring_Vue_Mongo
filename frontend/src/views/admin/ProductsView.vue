<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-semibold text-gray-800">Products Management</h1>
      <button
        @click="openAddModal"
        class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        Add Product
      </button>
    </div>

    <!-- Products Table -->
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Image
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Title
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Category
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Price
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Stock
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
              </th>
              <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="h-10 w-10 rounded-full overflow-hidden bg-gray-200">
                  <img 
                    :src="getProductImageUrl(product.image)" 
                    :alt="product.title"
                    class="h-full w-full object-cover"
                  >
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{{ product.title }}</div>
                <div class="text-sm text-gray-500 line-clamp-1">{{ product.description }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ getCategoryName(product.category) }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">${{ product.price.toFixed(2) }}</div>
                <div v-if="product.discount > 0" class="text-xs text-red-600">
                  ${{ product.discountPrice.toFixed(2) }}
                  <span class="text-gray-500 line-through">${{ product.price.toFixed(2) }}</span>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="product.stock > 0 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                >
                  {{ product.stock }} in stock
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="product.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                >
                  {{ product.isActive ? 'Active' : 'Inactive' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button 
                  @click="editProduct(product)"
                  class="text-indigo-600 hover:text-indigo-900 mr-3"
                >
                  Edit
                </button>
                <button 
                  @click="confirmDelete(product)"
                  class="text-red-600 hover:text-red-900"
                >
                  Delete
                </button>
              </td>
            </tr>
            <tr v-if="products.length === 0">
              <td colspan="7" class="px-6 py-4 text-center text-sm text-gray-500">
                No products found
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Pagination -->
    <div class="mt-4 flex justify-between items-center">
      <div class="text-sm text-gray-700">
        Showing <span class="font-medium">{{ pagination.from }}</span> to 
        <span class="font-medium">{{ pagination.to }}</span> of 
        <span class="font-medium">{{ pagination.total }}</span> results
      </div>
      <div class="flex space-x-2">
        <button 
          @click="previousPage"
          :disabled="pagination.currentPage === 1"
          class="px-3 py-1 border rounded-md"
          :class="pagination.currentPage === 1 ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'"
        >
          Previous
        </button>
        <button 
          @click="nextPage"
          :disabled="pagination.currentPage === pagination.lastPage"
          class="px-3 py-1 border rounded-md"
          :class="pagination.currentPage === pagination.lastPage ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'"
        >
          Next
        </button>
      </div>
    </div>

    <!-- Add/Edit Product Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-2xl">
        <div class="px-6 py-4 border-b">
          <h2 class="text-lg font-semibold text-gray-900">
            {{ isEditing ? 'Edit Product' : 'Add New Product' }}
          </h2>
        </div>
        <div class="p-6">
          <form @submit.prevent="saveProduct">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="col-span-2">
                <label class="block text-sm font-medium text-gray-700 mb-1">Product Image</label>
                <div class="mt-1 flex items-center">
                  <div class="h-32 w-32 rounded-md overflow-hidden bg-gray-100">
                    <img 
                      v-if="productForm.imagePreview" 
                      :src="productForm.imagePreview" 
                      class="h-full w-full object-cover"
                    >
                    <div v-else class="h-full w-full flex items-center justify-center text-gray-400">
                      <svg class="h-12 w-12" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                      </svg>
                    </div>
                  </div>
                  <div class="ml-4">
                    <input 
                      type="file" 
                      ref="fileInput"
                      @change="handleImageUpload"
                      accept="image/*"
                      class="hidden"
                    >
                    <button
                      type="button"
                      @click="$refs.fileInput.click()"
                      class="px-3 py-1.5 border border-gray-300 rounded-md shadow-sm text-sm leading-4 font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      Change
                    </button>
                    <p class="mt-1 text-xs text-gray-500">
                      JPG, PNG or GIF (max. 2MB)
                    </p>
                  </div>
                </div>
              </div>
              
              <div class="col-span-2">
                <label for="title" class="block text-sm font-medium text-gray-700">Product Title</label>
                <input
                  type="text"
                  id="title"
                  v-model="productForm.title"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                >
              </div>

              <div class="col-span-2">
                <label for="description" class="block text-sm font-medium text-gray-700">Description</label>
                <textarea
                  id="description"
                  v-model="productForm.description"
                  rows="3"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                ></textarea>
              </div>

              <div>
                <label for="category" class="block text-sm font-medium text-gray-700">Category</label>
                <select
                  id="category"
                  v-model="productForm.category"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                >
                  <option value="">Select a category</option>
                  <option v-for="category in categories" :key="category.id" :value="category.id">
                    {{ category.name }}
                  </option>
                </select>
              </div>

              <div>
                <label for="price" class="block text-sm font-medium text-gray-700">Price ($)</label>
                <input
                  type="number"
                  id="price"
                  v-model.number="productForm.price"
                  min="0"
                  step="0.01"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                >
              </div>

              <div>
                <label for="stock" class="block text-sm font-medium text-gray-700">Stock</label>
                <input
                  type="number"
                  id="stock"
                  v-model.number="productForm.stock"
                  min="0"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                >
              </div>

              <div>
                <label for="discount" class="block text-sm font-medium text-gray-700">Discount (%)</label>
                <input
                  type="number"
                  id="discount"
                  v-model.number="productForm.discount"
                  min="0"
                  max="100"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                >
              </div>

              <div class="flex items-center">
                <input
                  type="checkbox"
                  id="isActive"
                  v-model="productForm.isActive"
                  class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                >
                <label for="isActive" class="ml-2 block text-sm text-gray-700">
                  Active
                </label>
              </div>
            </div>

            <div class="mt-6 flex justify-end space-x-3">
              <button
                type="button"
                @click="closeModal"
                class="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Cancel
              </button>
              <button
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                :disabled="isSaving"
              >
                <span v-if="isSaving">Saving...</span>
                <span v-else>{{ isEditing ? 'Update' : 'Save' }}</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-md">
        <div class="px-6 py-4">
          <h3 class="text-lg font-medium text-gray-900">Delete Product</h3>
          <div class="mt-2">
            <p class="text-sm text-gray-500">
              Are you sure you want to delete "{{ selectedProduct?.title }}"? This action cannot be undone.
            </p>
          </div>
        </div>
        <div class="px-6 py-4 bg-gray-50 flex justify-end space-x-3">
          <button
            type="button"
            @click="showDeleteModal = false"
            class="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Cancel
          </button>
          <button
            type="button"
            @click="deleteProduct"
            class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
            :disabled="isDeleting"
          >
            <span v-if="isDeleting">Deleting...</span>
            <span v-else>Delete</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useToast } from 'vue-toastification';
import axios from 'axios';

const toast = useToast();

// Sample data - replace with API calls
const products = ref([
  {
    id: 1,
    title: 'Sample Product 1',
    description: 'This is a sample product description.',
    category: 1,
    price: 99.99,
    stock: 10,
    image: 'product1.jpg',
    discount: 10,
    discountPrice: 89.99,
    isActive: true
  },
  // Add more sample products as needed
]);

const categories = ref([
  { id: 1, name: 'Electronics' },
  { id: 2, name: 'Clothing' },
  { id: 3, name: 'Books' },
  { id: 4, name: 'Home & Kitchen' },
]);

const pagination = ref({
  currentPage: 1,
  perPage: 10,
  total: 100,
  lastPage: 10,
  from: 1,
  to: 10
});

// Modal state
const showModal = ref(false);
const showDeleteModal = ref(false);
const isEditing = ref(false);
const isSaving = ref(false);
const isDeleting = ref(false);
const selectedProduct = ref(null);

// Form data
const productForm = ref({
  title: '',
  description: '',
  category: '',
  price: 0,
  stock: 0,
  discount: 0,
  discountPrice: 0,
  image: null,
  imagePreview: null,
  isActive: true
});

// Computed
const getCategoryName = (categoryId) => {
  const category = categories.value.find(cat => cat.id === categoryId);
  return category ? category.name : 'Uncategorized';
};

const getProductImageUrl = (imageName) => {
  return imageName 
    ? `http://localhost:8080/img/product_img/${imageName}`
    : 'https://via.placeholder.com/150';
};

// Methods
const fetchProducts = async (page = 1) => {
  try {
    // Replace with actual API call
    // const response = await axios.get('/admin/products', { params: { page } });
    // products.value = response.data.data;
    // pagination.value = response.data.meta;
    
    // Mock pagination for now
    pagination.value.currentPage = page;
    pagination.value.from = (page - 1) * pagination.value.perPage + 1;
    pagination.value.to = Math.min(page * pagination.value.perPage, pagination.value.total);
  } catch (error) {
    console.error('Error fetching products:', error);
    toast.error('Failed to fetch products');
  }
};

const fetchCategories = async () => {
  try {
    // Replace with actual API call
    // const response = await axios.get('/categories');
    // categories.value = response.data;
  } catch (error) {
    console.error('Error fetching categories:', error);
    toast.error('Failed to fetch categories');
  }
};

const openAddModal = () => {
  isEditing.value = false;
  productForm.value = {
    title: '',
    description: '',
    category: '',
    price: 0,
    stock: 0,
    discount: 0,
    discountPrice: 0,
    image: null,
    imagePreview: null,
    isActive: true
  };
  showModal.value = true;
};

const editProduct = (product) => {
  isEditing.value = true;
  selectedProduct.value = product;
  
  productForm.value = {
    title: product.title,
    description: product.description,
    category: product.category,
    price: product.price,
    stock: product.stock,
    discount: product.discount || 0,
    discountPrice: product.discountPrice || 0,
    image: product.image,
    imagePreview: getProductImageUrl(product.image),
    isActive: product.isActive
  };
  
  showModal.value = true;
};

const handleImageUpload = (event) => {
  const file = event.target.files[0];
  if (!file) return;
  
  // Check file size (max 2MB)
  if (file.size > 2 * 1024 * 1024) {
    toast.error('Image size should be less than 2MB');
    return;
  }
  
  // Create preview
  const reader = new FileReader();
  reader.onload = (e) => {
    productForm.value.imagePreview = e.target.result;
  };
  reader.readAsDataURL(file);
  
  productForm.value.image = file;
};

const calculateDiscountPrice = () => {
  if (productForm.value.discount > 0) {
    const discountAmount = (productForm.value.price * productForm.value.discount) / 100;
    productForm.value.discountPrice = productForm.value.price - discountAmount;
  } else {
    productForm.value.discountPrice = 0;
  }
};

const saveProduct = async () => {
  try {
    isSaving.value = true;
    
    // Calculate discount price before saving
    calculateDiscountPrice();
    
    const formData = new FormData();
    Object.keys(productForm.value).forEach(key => {
      if (key !== 'imagePreview') {
        formData.append(key, productForm.value[key]);
      }
    });
    
    if (isEditing.value) {
      // Update existing product
      // await axios.put(`/admin/products/${selectedProduct.value.id}`, formData, {
      //   headers: { 'Content-Type': 'multipart/form-data' }
      // });
      toast.success('Product updated successfully');
    } else {
      // Create new product
      // await axios.post('/admin/products', formData, {
      //   headers: { 'Content-Type': 'multipart/form-data' }
      // });
      toast.success('Product created successfully');
    }
    
    closeModal();
    fetchProducts(pagination.value.currentPage);
  } catch (error) {
    console.error('Error saving product:', error);
    toast.error(`Failed to ${isEditing.value ? 'update' : 'create'} product`);
  } finally {
    isSaving.value = false;
  }
};

const confirmDelete = (product) => {
  selectedProduct.value = product;
  showDeleteModal.value = true;
};

const deleteProduct = async () => {
  if (!selectedProduct.value) return;
  
  try {
    isDeleting.value = true;
    
    // Replace with actual API call
    // await axios.delete(`/admin/products/${selectedProduct.value.id}`);
    
    toast.success('Product deleted successfully');
    showDeleteModal.value = false;
    fetchProducts(pagination.value.currentPage);
  } catch (error) {
    console.error('Error deleting product:', error);
    toast.error('Failed to delete product');
  } finally {
    isDeleting.value = false;
  }
};

const closeModal = () => {
  showModal.value = false;
  selectedProduct.value = null;
};

const previousPage = () => {
  if (pagination.value.currentPage > 1) {
    fetchProducts(pagination.value.currentPage - 1);
  }
};

const nextPage = () => {
  if (pagination.value.currentPage < pagination.value.lastPage) {
    fetchProducts(pagination.value.currentPage + 1);
  }
};

// Lifecycle hooks
onMounted(() => {
  fetchProducts();
  fetchCategories();
});
</script>
