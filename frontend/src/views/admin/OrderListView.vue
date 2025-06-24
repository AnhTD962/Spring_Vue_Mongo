<template>
  <div>
    <h2>Admin - Orders</h2>
    <ul>
      <li v-for="o in orders" :key="o.id">
        Order #{{ o.id }} - {{ o.status }}
        <router-link :to="`/admin/orders/${o.id}`">Detail</router-link>
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getOrders } from "../../api/orders";

const orders = ref([]);
onMounted(async () => {
  const { data } = await getOrders();
  orders.value = data;
});
</script>