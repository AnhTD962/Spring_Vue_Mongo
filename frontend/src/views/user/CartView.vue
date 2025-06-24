<template>
  <div>
    <h2>Your Cart</h2>
    <ul>
      <li v-for="item in items" :key="item.productId">
        {{ item.productName }} x {{ item.quantity }}
        <button @click="remove(item.productId)">Remove</button>
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getCart, removeCartItem } from "../../api/cart";

const items = ref([]);
async function fetch() {
  const { data } = await getCart();
  items.value = data;
}
onMounted(fetch);

async function remove(id) {
  await removeCartItem(id);
  fetch();
}
</script>