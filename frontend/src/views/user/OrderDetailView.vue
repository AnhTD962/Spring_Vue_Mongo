<template>
  <div class="order-detail-wrapper" v-if="order">
    <div class="order-card">
      <h2>Order Detail</h2>
      <div class="section">
        <p><strong>Order ID:</strong> {{ order.orderId }}</p>
        <p><strong>Status:</strong> {{ order.status }}</p>
      </div>

      <div class="section">
        <h3>User Info</h3>
        <p><strong>Name:</strong> {{ order.user.name }}</p>
        <p><strong>Email:</strong> {{ order.user.email }}</p>
      </div>

      <div class="section">
        <h3>Shipping Address</h3>
        <p>{{ order.address }}</p>
      </div>

      <div class="section">
        <h3>Items</h3>
        <table class="items-table">
          <thead>
            <tr>
              <th>Product</th>
              <th>Price</th>
              <th>Qty</th>
              <th>Subtotal</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in order.items" :key="item.productId">
              <td>{{ item.productName }}</td>
              <td>${{ item.price.toFixed(2) }}</td>
              <td>{{ item.quantity }}</td>
              <td>${{ (item.price * item.quantity).toFixed(2) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="section total">
        <h3>Total: ${{ order.total.toFixed(2) }}</h3>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getMyOrderDetail } from "../../api/orders";

const route = useRoute();
const order = ref(null);

onMounted(async () => {
  const { data } = await getMyOrderDetail(route.params.id);
  order.value = data;
});
</script>

<style scoped>
.order-detail-wrapper {
  padding: 40px 20px;
  background: #f9f9f9;
  min-height: 100vh;
  display: flex;
  justify-content: center;
}

.order-card {
  background: #fff;
  border-radius: 10px;
  padding: 2rem;
  max-width: 700px;
  width: 100%;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.section {
  margin-bottom: 1.5rem;
}

h2 {
  margin-bottom: 1rem;
  text-align: center;
  color: #333;
}

h3 {
  margin-bottom: 0.5rem;
  color: #555;
}

.items-table {
  width: 100%;
  border-collapse: collapse;
}

.items-table th,
.items-table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: center;
}

.items-table th {
  background: #f2f2f2;
  font-weight: 600;
  color: #333;
}

.total {
  text-align: right;
  font-size: 1.2rem;
  font-weight: bold;
  color: #000;
}
</style>
