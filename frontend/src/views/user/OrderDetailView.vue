<template>
  <div v-if="order">
    <h2>Order Detail</h2>
    <p><b>Order ID:</b> {{ order.orderId }}</p>
    <p><b>Status:</b> {{ order.status }}</p>

    <h3>User Info</h3>
    <p><b>Name:</b> {{ order.user.name }}</p>
    <p><b>Email:</b> {{ order.user.email }}</p>

    <h3>Shipping Address</h3>
    <p>{{ order.address }}</p>

    <h3>Items</h3>
    <ul>
      <li v-for="item in order.items" :key="item.productId">
        {{ item.productName }} - {{ item.price }} x {{ item.quantity }}
        = {{ item.price * item.quantity }}
      </li>
    </ul>

    <h3>Total Price: {{ order.total.toFixed(2) }}</h3>
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
