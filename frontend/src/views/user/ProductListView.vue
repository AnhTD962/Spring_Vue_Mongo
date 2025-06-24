<template>
  <div>
    <h2>Products</h2>
    <ul>
      <li v-for="p in products" :key="p.id">
        {{ p.name }} - {{ p.price }}
        <router-link :to="`/products/${p.id}`">View</router-link>
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getProducts } from "../../api/products";

const products = ref([]);
onMounted(async () => {
  const { data } = await getProducts();
  products.value = data;
});
</script>