<template>
  <div>
    <h2>My Orders</h2>
    <ul>
      <li v-for="o in orders" :key="o.id">
        Order #{{ o.id }} - {{ o.status }}
        <router-link :to="`/my-orders/${o.id}`">Detail</router-link>
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getMyOrders } from "../../api/orders";

const orders = ref([]);
onMounted(async () => {
  const { data } = await getMyOrders();
  orders.value = data;
});
</script>