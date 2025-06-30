<template>
  <div>
    <h2>Admin - Orders</h2>
    <ul v-if="orders.length">
      <li v-for="o in orders" :key="o.id" class="mb-4 border-b pb-2">
        <div>
          <strong>Order #{{ o.orderId }}</strong> - {{ o.total.toFixed(2) }} USD
        </div>

        <div class="mt-1">
          <label>Status:</label>
          <select v-model="tempStatus[o.id]" @change="changeStatus(o)">
            <option
              v-for="(enumName, id) in filteredStatusOptions(o.status)"
              :key="id"
              :value="enumName"
            >
              {{ statusLabels[enumName] }}
            </option>
          </select>

          <router-link
            :to="`/admin/orders/${o.orderId}`"
            class="ml-4 text-blue-600 underline"
          >
            Detail
          </router-link>
        </div>
      </li>
    </ul>
    <div v-else>No orders found.</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getOrders, updateOrderStatus } from "../../api/orders";

// Mapping of status IDs to enum names (from backend)
const statusOptions = {
  1: "IN_PROGRESS",
  2: "ORDER_RECEIVED",
  3: "PRODUCT_PACKED",
  4: "OUT_FOR_DELIVERY",
  5: "DELIVERED",
  6: "CANCELLED",
  7: "SUCCESS"
};

// Human-readable labels for display
const statusLabels = {
  IN_PROGRESS: "In Progress",
  ORDER_RECEIVED: "Order Received",
  PRODUCT_PACKED: "Product Packed",
  OUT_FOR_DELIVERY: "Out for Delivery",
  DELIVERED: "Delivered",
  CANCELLED: "Cancelled",
  SUCCESS: "Success"
};

// Reverse map enum -> ID
function getStatusIdByName(enumName) {
  return (
    Object.entries(statusOptions).find(([, name]) => name === enumName)?.[0] ??
    null
  );
}

// Allow current status and forward transitions only
function filteredStatusOptions(currentEnum) {
  const currentId = parseInt(getStatusIdByName(currentEnum));
  return Object.fromEntries(
    Object.entries(statusOptions).filter(([id]) => parseInt(id) >= currentId)
  );
}

const orders = ref([]);
const tempStatus = ref({});

// When dropdown is changed
async function changeStatus(order) {
  const newStatus = tempStatus.value[order.id];
  if (!newStatus || newStatus === order.status) return;

  try {
    const statusId = getStatusIdByName(newStatus);
    await updateOrderStatus(order.id, statusId);

    // Reflect new value in UI after successful update
    order.status = newStatus;
    console.log(`✅ Order ${order.orderId} status updated to ${newStatus}`);
  } catch (e) {
    console.error(`❌ Failed to update order ${order.orderId}`, e);
    tempStatus.value[order.id] = order.status; // revert
  }
}

// Initial load
onMounted(async () => {
  try {
    const { data } = await getOrders();
    orders.value = data.content;

    data.content.forEach((o) => {
      tempStatus.value[o.id] = o.status;
    });
  } catch (e) {
    console.error("Failed to fetch orders", e);
  }
});
</script>
