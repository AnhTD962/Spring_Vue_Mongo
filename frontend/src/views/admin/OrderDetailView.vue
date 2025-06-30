<template>
  <div v-if="order" class="p-6 max-w-3xl mx-auto bg-white shadow rounded-lg">
    <h2 class="text-2xl font-bold mb-4">Order Detail</h2>

    <div class="mb-4">
      <p><strong>Order ID:</strong> {{ order.orderId }}</p>
      <p><strong>Status:</strong> {{ order.status }}</p>
      <p><strong>Date:</strong> {{ formatDate(order.orderDate) }}</p>
      <p><strong>Total:</strong> ${{ order.total.toFixed(2) }}</p>
      <p><strong>Payment:</strong> {{ order.paymentType }}</p>
    </div>

    <div class="mb-4">
      <h3 class="text-xl font-semibold mb-2">Shipping Address</h3>
      <p>{{ order.orderAddress.firstName }} {{ order.orderAddress.lastName }}</p>
      <p>{{ order.orderAddress.address }}, {{ order.orderAddress.city }}, {{ order.orderAddress.state }}</p>
      <p>{{ order.orderAddress.email }} | {{ order.orderAddress.mobileNo }}</p>
    </div>

    <div>
      <h3 class="text-xl font-semibold mb-2">Items</h3>
      <ul>
        <li
          v-for="item in order.items"
          :key="item.product.id"
          class="flex justify-between items-center py-2 border-b"
        >
          <div>
            <p class="font-medium">{{ item.product.title }}</p>
            <p class="text-sm text-gray-500">
              {{ item.quantity }} × ${{ item.price.toFixed(2) }} : ${{ item.total.toFixed(2) }}
            </p>
          </div>
        </li>
      </ul>
    </div>
  </div>

  <div v-else class="p-6 text-center text-red-500">
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
