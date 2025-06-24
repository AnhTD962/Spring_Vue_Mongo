<template>
  <div v-if="product">
    <h2>Product Detail</h2>
    <p><b>Name:</b> {{ product.name }}</p>
    <p><b>Price:</b> {{ product.price }}</p>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getProduct } from "../../api/products";

const route = useRoute();
const product = ref(null);

onMounted(async () => {
  const { data } = await getProduct(route.params.id);
  product.value = data;
});
</script>