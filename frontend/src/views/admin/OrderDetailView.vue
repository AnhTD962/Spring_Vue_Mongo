<template>
  <div v-if="order" class="order-detail-wrapper">
    <div class="order-card" id="print-area">
      <h2>Order Detail</h2>

      <div class="section">
        <p><strong>Order ID:</strong> {{ order.orderId }}</p>
        <p><strong>Status:</strong> {{ order.status }}</p>
        <p><strong>Date:</strong> {{ formatDate(order.orderDate) }}</p>
        <p><strong>Total:</strong> ${{ order.total.toFixed(2) }}</p>
        <p><strong>Payment:</strong> {{ order.paymentType }}</p>
      </div>

      <div class="section">
        <h3>Shipping Address</h3>
        <p>{{ order.orderAddress.firstName }} {{ order.orderAddress.lastName }}</p>
        <p>{{ order.orderAddress.address }}, {{ order.orderAddress.city }}, {{ order.orderAddress.state }}</p>
        <p>{{ order.orderAddress.email }} | {{ order.orderAddress.mobileNo }}</p>
      </div>

      <div class="section">
        <h3>Items</h3>
        <table class="order-items">
          <thead>
            <tr>
              <th>Product</th>
              <th>Quantity</th>
              <th>Price</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in order.items" :key="item.product.id">
              <td>{{ item.product.title }}</td>
              <td>{{ item.quantity }}</td>
              <td>${{ item.price.toFixed(2) }}</td>
              <td>${{ item.total.toFixed(2) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <div v-else>
    <p>Order not found.</p>
  </div>
</template>


<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getOrder } from "../../api/orders";

const order = ref(null);
const route = useRoute();

function formatDate(dateStr) {
  const date = new Date(dateStr);
  return date.toLocaleString();
}

onMounted(async () => {
  try {
    const { data } = await getOrder(route.params.id);
    order.value = data;
  } catch (err) {
    console.error("Failed to fetch order", err);
  }
});
</script>

<style scoped>
.order-detail-wrapper {
  padding: 40px 20px;
  max-width: 800px;
  margin: auto;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

.order-card {
  background: #fff;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.section {
  margin-bottom: 2rem;
}

h2 {
  margin-bottom: 1rem;
  font-size: 1.75rem;
}

h3 {
  margin-bottom: 1rem;
  font-size: 1.2rem;
  color: #7b2ff2;
}

.order-items {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.order-items th,
.order-items td {
  padding: 0.75rem;
  border: 1px solid #ddd;
  text-align: left;
}

.order-items th {
  background-color: #f3f3f3;
  font-weight: 600;
}

.order-items td {
  background-color: #fafafa;
}

.print-btn {
  margin-top: 1.5rem;
  padding: 0.6rem 1.2rem;
  background-color: #7b2ff2;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}

.print-btn:hover {
  background-color: #6920d4;
}

/* Hide non-printable elements */
@media print {
  .no-print {
    display: none !important;
  }

  .order-detail-wrapper {
    padding: 0;
    box-shadow: none;
  }

  .order-card {
    box-shadow: none;
    border: none;
  }

  .order-items th {
    background-color: #eee !important;
  }
}
</style>
