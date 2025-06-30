<template>
  <div>
    <h2>Products in Category</h2>
    <div v-if="filteredProducts.length">
      <ul>
        <li v-for="p in filteredProducts" :key="p.id">
          {{ p.title }} - {{ p.description }} - {{ p.category }} - {{ p.discount }} - {{ p.price }} - {{ p.discountPrice }}
          <router-link :to="`/products/${p.id}`">View</router-link>
        </li>
      </ul>
    </div>
    <div v-else>No products found in this category.</div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { getProductByCategory } from "../api/categories";

const route = useRoute();
const filteredProducts = ref([]);

const fetchProducts = async () => {
  const categoryName = route.params.id; // e.g., "Electronic", "Clother"
  const { data } = await getProductByCategory(categoryName); // pass category name
  filteredProducts.value = data;
};

onMounted(fetchProducts);

// Optional: refetch when route changes (e.g., user navigates to another category)
watch(() => route.params.id, fetchProducts);
</script>

