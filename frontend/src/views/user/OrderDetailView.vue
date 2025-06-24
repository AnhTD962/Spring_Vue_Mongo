<template>
  <div v-if="order">
    <h2>Order Detail</h2>
    <p><b>ID:</b> {{ order.id }}</p>
    <p><b>Status:</b> {{ order.status }}</p>
    <ul>
      <li v-for="item in order.items" :key="item.productId">
        {{ item.productName }} x {{ item.quantity }}
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getOrder } from "../../api/orders";

const route = useRoute();
const order = ref(null);

onMounted(async () => {
  const { data } = await getOrder(route.params.id);
  order.value = data;
});
</script>