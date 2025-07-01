<template>
  <div class="admin-products-wrapper">
    <h2>Admin - Products</h2>
    <div class="header">
      <router-link to="/admin/products/create" class="create-link">+ Create Product</router-link>
    </div>
    <div class="table-wrapper" v-if="products.length">
      <table class="products-table">
        <thead>
          <tr>
            <th>Image</th>
            <th>Title</th>
            <th>Description</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Discount</th>
            <th>Active</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in products" :key="p.id">
            <td>
              <img :src="p.image ? `/uploads/product_img/${p.image}` : '/default-product.png'" alt="product image"
                class="product-thumbnail" />
            </td>
            <td>{{ p.title }}</td>
            <td>{{ p.description }}</td>
            <td>{{ p.category }}</td>
            <td>${{ p.price }}</td>
            <td>{{ p.stock }}</td>
            <td>{{ p.discount }}%</td>
            <td>{{ p.isActive ? 'Yes' : 'No' }}</td>
            <td>
              <router-link :to="`/admin/products/${p.id}/edit`" class="edit-link">Edit</router-link>
              <button @click="remove(p.id)" class="delete-btn">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="no-products">No products available.</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getProducts, deleteProduct } from "../../api/products";

const products = ref([]);
async function fetch() {
  const { data } = await getProducts();
  products.value = data;
}
onMounted(fetch);

async function remove(id) {
  if (confirm("Are you sure you want to delete this product?")) {
    await deleteProduct(id);
    fetch();
  }
}
</script>

<style scoped>
.admin-products-wrapper {
  padding: 40px 20px;
  background: #f8f8f8;
  min-height: 100vh;
}

h2 {
  text-align: center;
  margin-bottom: 2rem;
}

.header {
  text-align: right;
  margin-bottom: 1rem;
}

.create-link {
  padding: 0.5rem 1rem;
  background: #7b2ff2;
  color: #fff;
  border-radius: 4px;
  text-decoration: none;
  font-weight: bold;
}

.create-link:hover {
  background: #6920d4;
}

.table-wrapper {
  overflow-x: auto;
}

.products-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.product-thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.products-table th,
.products-table td {
  padding: 0.75rem;
  border: 1px solid #ddd;
  text-align: center;
}

.products-table th {
  background: #f0f0f0;
  color: #333;
  font-weight: 600;
}

.edit-link {
  color: #007bff;
  text-decoration: underline;
  margin-right: 1rem;
}

.delete-btn {
  padding: 0.4rem 0.8rem;
  background-color: #d9534f;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.delete-btn:hover {
  background-color: #c9302c;
}

.no-products {
  text-align: center;
  font-size: 1.1rem;
  color: #777;
}
</style>
