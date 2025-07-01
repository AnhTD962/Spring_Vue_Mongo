<template>
  <div class="product-detail-wrapper" v-if="product">
    <div class="product-card">
      <img v-if="product.image" :src="`/uploads/product_img/${product.image}`" :alt="product.title"
        class="product-image" />
      <h2>{{ product.title }}</h2>
      <div class="price-section">
        <p><strong>Description:</strong>{{ product.description }}</p>
        <p><strong>Category:</strong> {{ product.category }}</p>
        <p>
          <strong>Price:</strong>
          <span v-if="product.discount > 0" style="text-decoration: line-through;">
            ${{ product.price }}
          </span>
          <span v-else>${{ product.price }}</span>
        </p>
        <p v-if="product.discount > 0">
          <strong>Discount:</strong> {{ product.discount }}%
        </p>
        <p v-if="product.discount > 0">
          <strong>Discounted Price:</strong> ${{ product.discountPrice }}
        </p>
      </div>
      <form @submit.prevent="handleAddToCart" class="cart-form">
        <input v-model.number="quantity" type="number" min="1" placeholder="Quantity" />
        <button type="submit">Add to Cart</button>
      </form>
      <p v-if="quantity > 0" class="total-price">
        <strong>Total Price:</strong> ${{ totalPrice.toFixed(2) }}
      </p>
    </div>
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

  if (
    auth.isAuthenticated &&
    route.query.addToCart === "true" &&
    route.query.productId === route.params.id
  ) {
    const qty = Number(route.query.qty) || 1;
    await addToCart({ productId: product.value.id, quantity: qty });
    alert("Added to cart!");
    router.replace({
      query: {
        ...route.query,
        addToCart: undefined,
        productId: undefined,
        qty: undefined
      }
    });
  }
});

const totalPrice = computed(() => {
  if (!product.value) return 0;
  return (Number(product.value.discountPrice) || 0) * (Number(quantity.value) || 0);
});

async function handleAddToCart() {
  if (!auth.isAuthenticated) {
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

  await addToCart({
    productId: product.value.id,
    userId: auth.user.id,
    quantity: quantity.value
  });
  alert("Added to cart!");
}
</script>

<style scoped>
.product-detail-wrapper {
  display: flex;
  justify-content: center;
  padding: 60px 20px;
  background-color: #f9f9f9;
}

.product-card {
  background: #fff;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 480px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 260px;
  object-fit: cover;
  border-radius: 6px;
  margin-bottom: 1.2rem;
  border: 1px solid #eee;
}

h2 {
  margin-bottom: 1.5rem;
  color: #222;
}

.price-section p {
  margin: 0.4rem 0;
  font-size: 1rem;
}

.cart-form {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 1rem;
}

input[type="number"] {
  width: 100px;
  padding: 0.5rem;
  font-size: 1rem;
  border-radius: 4px;
  border: 1px solid #ccc;
}

button {
  padding: 0.5rem 1rem;
  background: #7b2ff2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.3s ease;
}

button:hover {
  background: #6920d4;
}

.total-price {
  margin-top: 1rem;
  font-size: 1.1rem;
  color: #333;
}
</style>
