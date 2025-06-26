<template>
  <div>
    <h2>Your Cart</h2>
    <ul>
      <li v-for="item in items" :key="item.id">
        <div>
          <strong>{{ item.title }}</strong> ({{ item.category }})<br />
          Quantity:
          <input type="number" v-model.number="item.quantity" min="1"
            @change="updateQuantity(item.id, item.quantity)" style="width: 50px" />
          <br />
          Total: ${{ item.totalPrice?.toFixed(2) || "0.00" }}
        </div>
        <button @click="remove(item.id)">Remove</button>
      </li>
    </ul>
    <hr v-if="items.length > 0" />
    <h3 v-if="items.length > 0">
      Total Cart Price: ${{ totalCartPrice.toFixed(2) }}
    </h3>
    <button @click="checkout" :disabled="items.length === 0">
      Checkout
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getCart, removeCartItem, updateCartQuantity } from "@/api/cart";
import { checkoutCart } from "@/api/orders";
import { useCartStore } from "@/store/cart";

const items = ref([]);
const totalCartPrice = ref(0);
const cartStore = useCartStore();

async function fetch() {
  const { data } = await getCart();
  items.value = data.items;
  totalCartPrice.value = data.totalCartPrice || 0;
  cartStore.refreshCount(); // cập nhật số lượng cart
}

onMounted(fetch);

async function remove(cartId) {
  await removeCartItem(cartId);
  await fetch();
}

async function updateQuantity(cartId, quantity) {
  if (quantity < 1) quantity = 1;
  await updateCartQuantity(cartId, quantity);
  await fetch();
}

async function checkout() {
  try {
    await checkoutCart();
    alert("Checkout successful!");
    await fetch(); // reset cart
  } catch (err) {
    alert("Checkout failed: " + err.response?.data?.message || err.message);
  }
}
</script>
