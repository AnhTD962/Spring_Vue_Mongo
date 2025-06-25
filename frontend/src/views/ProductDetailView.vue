<template>
  <div v-if="product">
    <h2>{{ product.title }}</h2>
    <p>Price: {{ product.price }}</p>
    <p>Discount: {{ product.discount }}</p>
    <p>Discounted Price: {{ product.discountPrice }}</p>
    <form @submit.prevent="handleAddToCart">
      <input v-model.number="quantity" type="number" min="1" placeholder="Quantity" />
      <button type="submit">Add to Cart</button>
    </form>
    <p v-if="quantity > 0">
      Total Price: {{ totalPrice }}
    </p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getViewProductDetail } from "../api/products";
import { addToCart } from "../api/cart";
import { useAuthStore } from "../store/auth";

const route = useRoute();
const router = useRouter();
const product = ref(null);
const quantity = ref(1);

const auth = useAuthStore();

onMounted(async () => {
  const { data } = await getViewProductDetail(route.params.id);
  product.value = data;

  // If redirected from login with add-to-cart info in query, auto add to cart
  if (
    auth.isAuthenticated &&
    route.query.addToCart === "true" &&
    route.query.productId === route.params.id
  ) {
    const qty = Number(route.query.qty) || 1;
    await addToCart({ productId: product.value.id, quantity: qty });
    alert("Added to cart!");
    // Clean up query params after adding
    router.replace({ query: { ...route.query, addToCart: undefined, productId: undefined, qty: undefined } });
  }
});

const totalPrice = computed(() => {
  if (!product.value) return 0;
  return (Number(product.value.discountPrice) || 0) * (Number(quantity.value) || 0);
});

async function handleAddToCart() {
  if (!auth.isAuthenticated) {
    // Redirect to login with add-to-cart info in query
    router.push({
      path: "/login",
      query: {
        redirect: route.fullPath,
        addToCart: "true",
        productId: product.value.id,
        qty: quantity.value
      }
    });
    return;
  }
  await addToCart({ productId: product.value.id, quantity: quantity.value });
  alert("Added to cart!");
}
</script>