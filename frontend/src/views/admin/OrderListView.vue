<template>
  <div class="admin-orders-wrapper">
    <h2>Admin - Orders</h2>

    <!-- 🔍 Filter Dropdown -->
    <div class="order-filter">
      <label>
        Filter by Status:
        <select v-model="statusFilter" @change="handleFilter">
          <option value="">All</option>
          <option v-for="(label, key) in statusLabels" :key="key" :value="key">
            {{ label }}
          </option>
        </select>
      </label>
    </div>

    <!-- 📂 Bulk Update Button -->
    <div class="bulk-update">
      <button @click="bulkUpdateFilteredOrders" :disabled="orders.length === 0">
        Bulk Update Filtered Orders
      </button>
    </div>

    <!-- 📋 Order Table -->
    <table v-if="orders.length" class="orders-table">
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
        <tr v-for="o in orders" :key="o.id">
          <td>#{{ o.orderId }}</td>
          <td>${{ o.total.toFixed(2) }}</td>
          <td>{{ statusLabels[o.status] }}</td>
          <td>
            <select v-model="tempStatus[o.id]" @change="changeStatus(o)">
              <option v-for="(enumName, id) in filteredStatusOptions(o.status)" :key="id" :value="enumName">
                {{ statusLabels[enumName] }}
              </option>
            </select>
          </td>
          <td>
            <router-link :to="`/admin/orders/${o.orderId}`" class="icon-link" title="View Order">
              <i class="fas fa-eye"></i>
            </router-link>
          </td>

        </tr>
      </tbody>
    </table>

    <div v-else class="no-orders">No orders found.</div>

    <!-- Pagination -->
    <div class="pagination" v-if="totalPages > 1">
      <button @click="prevPage" :disabled="currentPage === 0">Prev</button>
      <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage + 1 >= totalPages">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { getOrders, updateOrderStatus, bulkUpdateStatusByIds } from '../../api/orders';

const statusOptions = {
  1: 'IN_PROGRESS',
  2: 'ORDER_RECEIVED',
  3: 'PRODUCT_PACKED',
  4: 'OUT_FOR_DELIVERY',
  5: 'DELIVERED',
  6: 'CANCELLED',
  7: 'SUCCESS',
};

const statusLabels = {
  IN_PROGRESS: 'In Progress',
  ORDER_RECEIVED: 'Order Received',
  PRODUCT_PACKED: 'Product Packed',
  OUT_FOR_DELIVERY: 'Out for Delivery',
  DELIVERED: 'Delivered',
  CANCELLED: 'Cancelled',
  SUCCESS: 'Success',
};

function getStatusIdByName(enumName) {
  return Object.entries(statusOptions).find(([, name]) => name === enumName)?.[0] ?? null;
}

function getNextStatus(current) {
  const ids = Object.keys(statusOptions);
  const index = ids.findIndex(id => statusOptions[id] === current);
  return index >= 0 && index + 1 < ids.length ? statusOptions[ids[index + 1]] : null;
}

function filteredStatusOptions(currentEnum) {
  const currentId = parseInt(getStatusIdByName(currentEnum));
  return Object.fromEntries(
    Object.entries(statusOptions).filter(([id]) => parseInt(id) >= currentId)
  );
}

const orders = ref([]);
const tempStatus = ref({});
const statusFilter = ref('');
const currentPage = ref(0);
const pageSize = ref(5);
const totalPages = ref(0);

async function fetchOrders() {
  try {
    const { data } = await getOrders(currentPage.value, pageSize.value, statusFilter.value);
    orders.value = data.content || [];
    totalPages.value = data.page?.totalPages || 0; // ✅ Fix here
    orders.value.forEach(o => (tempStatus.value[o.id] = o.status));
  } catch (e) {
    console.error('Failed to fetch orders', e);
  }
}

function handleFilter() {
  currentPage.value = 0;
  fetchOrders();
}

function nextPage() {
  if (currentPage.value + 1 < totalPages.value) {
    currentPage.value++;
    fetchOrders();
  }
}

function prevPage() {
  if (currentPage.value > 0) {
    currentPage.value--;
    fetchOrders();
  }
}

async function changeStatus(order) {
  const newStatus = tempStatus.value[order.id];
  if (!newStatus || newStatus === order.status) return;
  try {
    const statusId = getStatusIdByName(newStatus);
    await updateOrderStatus(order.id, statusId);
    order.status = newStatus;
  } catch (e) {
    console.error(`Failed to update order ${order.orderId}`, e);
    tempStatus.value[order.id] = order.status;
  }
}

async function bulkUpdateFilteredOrders() {
  const currentStatus = statusFilter.value;
  if (!currentStatus) {
    alert('Please filter a status first.');
    return;
  }
  const nextStatus = getNextStatus(currentStatus);
  if (!nextStatus) {
    alert('No next status available.');
    return;
  }
  const idsToUpdate = orders.value.map(o => o.id);
  try {
    await bulkUpdateStatusByIds(idsToUpdate, nextStatus);
    alert(`Updated ${idsToUpdate.length} orders to ${statusLabels[nextStatus]}`);
    fetchOrders();
  } catch (e) {
    console.error('Bulk update failed', e);
    alert('Bulk update failed');
  }
}

onMounted(fetchOrders);
watch(statusFilter, () => {
  currentPage.value = 0;
  fetchOrders();
});

watch(currentPage, () => {
  fetchOrders();
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

.order-filter {
  text-align: right;
  max-width: 1000px;
  margin: 0 auto 1rem;
}

.bulk-update {
  text-align: right;
  margin: 1rem auto;
  max-width: 1000px;
}

.bulk-update button {
  padding: 8px 16px;
  background-color: #ff9800;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.bulk-update button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.order-filter select,
select {
  padding: 0.4rem;
  font-size: 0.95rem;
  border-radius: 4px;
  border: 1px solid #ccc;
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

.icon-link {
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  margin: 0 5px;
  color: #2196f3;
  margin: auto;
}

.icon-link:hover {
  color: #1976d2;
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
