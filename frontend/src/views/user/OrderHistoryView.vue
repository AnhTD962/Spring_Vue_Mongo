<template>
  <div class="orders-wrapper">
    <h2>My Orders</h2>
    <div v-if="orders.length" class="orders-table-container">
      <table class="orders-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Order ID</th>
            <th>Status</th>
            <th>Details</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(o, index) in orders" :key="o.id">
            <td>{{ index + 1 }}</td>
            <td>{{ o.orderId }}</td>
            <td>{{ o.status }}</td>
            <td>
              <router-link :to="`/my-orders/${o.id}`" class="order-link">View</router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="no-orders">You haven't placed any orders yet.</div>
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

<style scoped>
.orders-wrapper {
  padding: 40px 20px;
  min-height: 100vh;
  background-color: #f9f9f9;
}

h2 {
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2rem;
  color: #333;
}

.orders-table-container {
  overflow-x: auto;
  max-width: 900px;
  margin: 0 auto;
}

.orders-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  border-radius: 6px;
  overflow: hidden;
}

.orders-table th,
.orders-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.orders-table th {
  background-color: #f0f0f0;
  font-weight: 600;
  color: #555;
}

.orders-table td {
  font-size: 0.95rem;
  color: #333;
}

.order-link {
  color: #7b2ff2;
  font-weight: bold;
  text-decoration: underline;
}

.no-orders {
  text-align: center;
  color: #777;
  font-size: 1.1rem;
}
</style>
