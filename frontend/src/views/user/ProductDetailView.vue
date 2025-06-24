<template>
  <div v-if="product">
    <h2>{{ product.name }}</h2>
    <p>{{ product.price }}</p>
    <form @submit.prevent="addToCart">
      <input v-model.number="quantity" type="number" min="1" placeholder="Quantity" />
      <button type="submit">Add to Cart</button>
    </form>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getProduct } from "../../api/products";
import { addToCart } from "../../api/cart";

const route = useRoute();
const product = ref(null);
const quantity = ref(1);

onMounted(async () => {
  const { data } = await getProduct(route.params.id);
  product.value = data;
});

async function addToCart() {
  await addToCart({ productId: product.value.id, quantity: quantity.value });
  alert("Added to cart!");
}
</script>