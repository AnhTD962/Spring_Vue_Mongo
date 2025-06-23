<template>
  <div class="container mx-auto p-4">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">Categories</h1>
      <button 
        @click="showAddModal = true"
        class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded"
      >
        Add Category
      </button>
    </div>

    <!-- Categories Table -->
    <div class="bg-white rounded-lg shadow overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="category in categories" :key="category.id">
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="flex items-center">
                <div class="flex-shrink-0 h-10 w-10">
                  <img class="h-10 w-10 rounded-full" :src="getImageUrl(category.imageName)" alt="">
                </div>
                <div class="ml-4">
                  <div class="text-sm font-medium text-gray-900">{{ category.name }}</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <span 
                :class="{
                  'px-2 inline-flex text-xs leading-5 font-semibold rounded-full': true,
                  'bg-green-100 text-green-800': category.isActive,
                  'bg-red-100 text-red-800': !category.isActive
                }"
              >
                {{ category.isActive ? 'Active' : 'Inactive' }}
              </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
              <button 
                @click="editCategory(category)"
                class="text-indigo-600 hover:text-indigo-900 mr-3"
              >
                Edit
              </button>
              <button 
                @click="confirmDelete(category)"
                class="text-red-600 hover:text-red-900"
              >
                Delete
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Add/Edit Category Modal -->
    <div v-if="showAddModal || editingCategory" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
      <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h2 class="text-xl font-bold mb-4">{{ editingCategory ? 'Edit' : 'Add' }} Category</h2>
        
        <form @submit.prevent="editingCategory ? updateCategory() : createCategory()">
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
              Name
            </label>
            <input
              id="name"
              v-model="formData.name"
              type="text"
              required
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            >
          </div>

          <div class="mb-4">
            <label class="flex items-center">
              <input 
                type="checkbox" 
                v-model="formData.isActive"
                class="rounded text-blue-500"
              >
              <span class="ml-2 text-sm text-gray-700">Active</span>
            </label>
          </div>

          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="image">
              Image
            </label>
            <input
              id="image"
              type="file"
              @change="handleImageUpload"
              class="w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
            >
          </div>

          <div class="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              @click="closeModal"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              {{ editingCategory ? 'Update' : 'Create' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
      <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h2 class="text-xl font-bold mb-4">Confirm Delete</h2>
        <p class="mb-6">Are you sure you want to delete "{{ categoryToDelete?.name }}"?</p>
        
        <div class="flex justify-end space-x-3">
          <button
            @click="showDeleteModal = false"
            class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Cancel
          </button>
          <button
            @click="deleteCategory"
            class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'vue-toastification';
import categoryService from '@/services/category.service';

const toast = useToast();
const categories = ref([]);
const showAddModal = ref(false);
const showDeleteModal = ref(false);
const editingCategory = ref(null);
const categoryToDelete = ref(null);

const formData = ref({
  name: '',
  isActive: true,
  image: null
});

// Fetch categories
const fetchCategories = async () => {
  try {
    const data = await categoryService.getAllCategories();
    categories.value = data;
  } catch (error) {
    toast.error('Failed to load categories');
  }
};

// Handle image upload
const handleImageUpload = (event) => {
  formData.value.image = event.target.files[0];
};

// Get image URL
const getImageUrl = (imageName) => {
  return imageName 
    ? `http://localhost:8080/img/category_img/${imageName}`
    : 'https://via.placeholder.com/40';
};

// Create category
const createCategory = async () => {
  try {
    await categoryService.createCategory(formData.value);
    toast.success('Category created successfully');
    closeModal();
    fetchCategories();
  } catch (error) {
    toast.error(error.response?.data?.message || 'Failed to create category');
  }
};

// Edit category
const editCategory = (category) => {
  editingCategory.value = category;
  formData.value = {
    name: category.name,
    isActive: category.isActive,
    image: null
  };
  showAddModal.value = true;
};

// Update category
const updateCategory = async () => {
  try {
    await categoryService.updateCategory(editingCategory.value.id, formData.value);
    toast.success('Category updated successfully');
    closeModal();
    fetchCategories();
  } catch (error) {
    toast.error(error.response?.data?.message || 'Failed to update category');
  }
};

// Confirm delete
const confirmDelete = (category) => {
  categoryToDelete.value = category;
  showDeleteModal.value = true;
};

// Delete category
const deleteCategory = async () => {
  try {
    await categoryService.deleteCategory(categoryToDelete.value.id);
    toast.success('Category deleted successfully');
    showDeleteModal.value = false;
    fetchCategories();
  } catch (error) {
    toast.error('Failed to delete category');
  }
};

// Close modal
const closeModal = () => {
  showAddModal.value = false;
  editingCategory.value = null;
  formData.value = { name: '', isActive: true, image: null };
};

// Initialize
onMounted(() => {
  fetchCategories();
});
</script>
