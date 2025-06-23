import axios from 'axios';

const API_URL = 'http://localhost:8080/api/admin/categories';

class CategoryService {
  async getAllCategories() {
    try {
      const response = await axios.get(API_URL);
      return response.data;
    } catch (error) {
      console.error('Error fetching categories:', error);
      throw error;
    }
  }

  async createCategory(categoryData) {
    try {
      const formData = new FormData();
      formData.append('name', categoryData.name);
      formData.append('isActive', categoryData.isActive || true);
      
      if (categoryData.image) {
        formData.append('file', categoryData.image);
      }

      const response = await axios.post(API_URL, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      return response.data;
    } catch (error) {
      console.error('Error creating category:', error);
      throw error;
    }
  }

  async updateCategory(id, categoryData) {
    try {
      const formData = new FormData();
      formData.append('name', categoryData.name);
      formData.append('isActive', categoryData.isActive);
      
      if (categoryData.image) {
        formData.append('file', categoryData.image);
      }

      const response = await axios.put(`${API_URL}/${id}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      return response.data;
    } catch (error) {
      console.error('Error updating category:', error);
      throw error;
    }
  }

  async deleteCategory(id) {
    try {
      await axios.delete(`${API_URL}/${id}`);
      return true;
    } catch (error) {
      console.error('Error deleting category:', error);
      throw error;
    }
  }
}

export default new CategoryService();
