<template>
  <div>
    <h2>Products</h2>
    <div>
      <ul>
        <li v-for="p in products" :key="p.id">
          {{ p.title }} - {{ p.description }} - {{ p.category }} - {{ p.discount }} - {{ p.price }} - {{ p.discountPrice }}
          <router-link :to="`/products/${p.id}`">View</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getViewProducts } from "../api/products";

const products = ref([]);
onMounted(async () => {
  const { data } = await getViewProducts();
  products.value = data;
});
</script>