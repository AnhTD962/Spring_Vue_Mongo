<template>
  <div class="admin-orders-wrapper">
    <h2>Admin - Orders</h2>

    <table v-if="paginatedOrders.length" class="orders-table">
      <thead>
        <tr>
          <th>Order ID</th>
          <th>Total (USD)</th>
          <th>Status</th>
          <th>Update</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="o in paginatedOrders" :key="o.id">
          <td>#{{ o.orderId }}</td>
          <td>${{ o.total.toFixed(2) }}</td>
          <td>{{ statusLabels[o.status] }}</td>
          <td>
            <select v-model="tempStatus[o.id]" @change="changeStatus(o)">
              <option
                v-for="(enumName, id) in filteredStatusOptions(o.status)"
                :key="id"
                :value="enumName"
              >
                {{ statusLabels[enumName] }}
              </option>
            </select>
          </td>
          <td>
            <router-link
              :to="`/admin/orders/${o.orderId}`"
              class="detail-link"
            >
              View
            </router-link>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else class="no-orders">No orders found.</div>

    <!-- Pagination buttons -->
    <div class="pagination" v-if="totalPages > 1">
      <button @click="prevPage" :disabled="currentPage === 0">Prev</button>
      <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage + 1 >= totalPages">
        Next
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { getOrders, updateOrderStatus } from "../../api/orders";

const statusOptions = {
  1: "IN_PROGRESS",
  2: "ORDER_RECEIVED",
  3: "PRODUCT_PACKED",
  4: "OUT_FOR_DELIVERY",
  5: "DELIVERED",
  6: "CANCELLED",
  7: "SUCCESS"
};

const statusLabels = {
  IN_PROGRESS: "In Progress",
  ORDER_RECEIVED: "Order Received",
  PRODUCT_PACKED: "Product Packed",
  OUT_FOR_DELIVERY: "Out for Delivery",
  DELIVERED: "Delivered",
  CANCELLED: "Cancelled",
  SUCCESS: "Success"
};

function getStatusIdByName(enumName) {
  return (
    Object.entries(statusOptions).find(([, name]) => name === enumName)?.[0] ?? null
  );
}

function filteredStatusOptions(currentEnum) {
  const currentId = parseInt(getStatusIdByName(currentEnum));
  return Object.fromEntries(
    Object.entries(statusOptions).filter(([id]) => parseInt(id) >= currentId)
  );
}

const allOrders = ref([]);
const tempStatus = ref({});

// Pagination setup
const currentPage = ref(0);
const pageSize = ref(5);

const totalPages = computed(() =>
  Math.ceil(allOrders.value.length / pageSize.value)
);

const paginatedOrders = computed(() => {
  const start = currentPage.value * pageSize.value;
  return allOrders.value.slice(start, start + pageSize.value);
});

function nextPage() {
  if (currentPage.value + 1 < totalPages.value) currentPage.value++;
}

function prevPage() {
  if (currentPage.value > 0) currentPage.value--;
}

async function changeStatus(order) {
  const newStatus = tempStatus.value[order.id];
  if (!newStatus || newStatus === order.status) return;

  try {
    const statusId = getStatusIdByName(newStatus);
    await updateOrderStatus(order.id, statusId);
    order.status = newStatus;
    console.log(`✅ Order ${order.orderId} status updated to ${newStatus}`);
  } catch (e) {
    console.error(`❌ Failed to update order ${order.orderId}`, e);
    tempStatus.value[order.id] = order.status; // revert
  }
}

onMounted(async () => {
  try {
    const { data } = await getOrders();
    allOrders.value = data.content || [];

    data.content.forEach((o) => {
      tempStatus.value[o.id] = o.status;
    });
  } catch (e) {
    console.error("Failed to fetch orders", e);
  }
});
</script>


<style scoped>
.admin-orders-wrapper {
  padding: 40px 20px;
  background-color: #f9f9f9;
  min-height: 100vh;
}

h2 {
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
}

.orders-table {
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
  border-collapse: collapse;
  background: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.orders-table th,
.orders-table td {
  padding: 0.75rem;
  border: 1px solid #eee;
  text-align: center;
}

.orders-table th {
  background-color: #f0f0f0;
  font-weight: 600;
}

select {
  padding: 0.4rem;
  font-size: 0.95rem;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.detail-link {
  color: #7b2ff2;
  font-weight: bold;
  text-decoration: underline;
}

.no-orders {
  text-align: center;
  font-size: 1.1rem;
  color: #777;
  margin-top: 2rem;
}
.pagination {
  text-align: center;
  margin-top: 1.5rem;
}

.pagination button {
  margin: 0 0.5rem;
  padding: 6px 12px;
  background: #7b2ff2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

</style>
