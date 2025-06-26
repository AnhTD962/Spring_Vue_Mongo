<template>
  <div>
    <h2>Your Cart</h2>
    <ul>
      <li v-for="item in items" :key="item.id">
        <div>
          <strong>{{ item.title }}</strong> ({{ item.category }})<br />
          Quantity:
          <input type="number" v-model.number="item.quantity" min="1" @change="updateQuantity(item.id, item.quantity)"
            style="width: 50px" /><br />
          Total: ${{ item.totalPrice?.toFixed(2) || "0.00" }}
        </div>
        <button @click="remove(item.id)">Remove</button>
      </li>
    </ul>
    <hr />
    <h3>Total Cart Price: ${{ totalCartPrice.toFixed(2) }}</h3>
    <button @click="checkout" :disabled="items.length === 0">
      Checkout
    </button>
  </div>
</template>


<script setup>
import { ref, onMounted } from "vue";
import { getCart, removeCartItem, updateCartQuantity } from "../../api/cart";

const items = ref([]);
const totalCartPrice = ref(0);

async function fetch() {
  const { data } = await getCart();
  items.value = data.items;
  totalCartPrice.value = data.totalCartPrice || 0;
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
    await fetch(); // reload cart (sẽ trống sau khi checkout)
  } catch (err) {
    alert("Checkout failed: " + err.response?.data?.message || err.message);
  }
}
</script>
