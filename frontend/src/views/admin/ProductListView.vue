<template>
  <div>
    <h2>Admin - Products</h2>
    <router-link to="/admin/products/create">Create Product</router-link>
    <ul>
      <li v-for="p in products" :key="p.id">
        {{ p.name }}
        <router-link :to="`/admin/products/${p.id}`">Detail</router-link>
        <router-link :to="`/admin/products/${p.id}/edit`">Edit</router-link>
        <button @click="remove(p.id)">Delete</button>
      </li>
    </ul>
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
  await deleteProduct(id);
  fetch();
}
</script>