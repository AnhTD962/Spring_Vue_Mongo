<template>
  <div class="my-orders-container">
    <h2>My Orders</h2>
    <div v-if="totalPages > 0">
      <div class="filter-section">
        <label for="statusFilter">Filter by status:</label>
        <select id="statusFilter" v-model="selectedStatus">
          <option value="">All</option>
          <option v-for="(label, key) in statusLabels" :key="key" :value="key">
            {{ label }}
          </option>
        </select>
      </div>

      <table class="orders-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Order ID</th>
            <th>Products</th>
            <th>Status</th>
            <th>Detail</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="orders.length === 0">
            <td colspan="5">No orders found.</td>
          </tr>
          <tr v-for="(o, index) in orders" :key="o.id">
            <td>{{ index + 1 + currentPage * pageSize }}</td>
            <td>{{ o.orderId }}</td>
            <td>
              <ul>
                <li v-for="item in o.items" :key="item.product.id">{{ item.product.title }}</li>
              </ul>
            </td>
            <td>{{ statusLabels[o.status] || o.status }}</td>
            <td>
              <router-link :to="`/my-orders/${o.id}`"  class="icon-link" title="View Order">
                <i class="fas fa-eye"></i>
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pagination" v-if="totalPages > 1">
        <button @click="prevPage" :disabled="currentPage === 0">Previous</button>
        <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>
        <button @click="nextPage" :disabled="currentPage + 1 >= totalPages">Next</button>
      </div>
    </div>
    
  <div v-else>
    <p>No orders found. <router-link to="/products">Browse products</router-link>.</p>
  </div>
  </div>
</template>

<script setup>
import { ref, watchEffect, watch } from 'vue'
import { getMyOrders } from '@/api/orders'

const orders = ref([])
const selectedStatus = ref('')
const currentPage = ref(0)
const pageSize = 5
const totalPages = ref(0)

const statusLabels = {
  IN_PROGRESS: 'In Progress',
  ORDER_RECEIVED: 'Order Received',
  PRODUCT_PACKED: 'Product Packed',
  OUT_FOR_DELIVERY: 'Out for Delivery',
  DELIVERED: 'Delivered',
  CANCELLED: 'Cancelled',
  SUCCESS: 'Success'
}

watch(selectedStatus, () => {
  currentPage.value = 0
})

const fetchOrders = async () => {
  try {
    const res = await getMyOrders(currentPage.value, pageSize, selectedStatus.value)
    orders.value = res.data.content
    totalPages.value = res.data.page?.totalPages || 1
  } catch (err) {
    console.error('Failed to fetch orders', err)
  }
}

watchEffect(() => {
  fetchOrders()
})

const nextPage = () => {
  if (currentPage.value + 1 < totalPages.value) {
    currentPage.value++
  }
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--
  }
}
</script>

<style scoped>
.my-orders-container {
  padding: 20px;
  background-color: #f8f9fa;
}

h2 {
  margin-bottom: 16px;
  color: #333;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-section select {
  padding: 6px 10px;
  font-size: 1rem;
  margin-left: 10px;
}

.orders-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.orders-table th,
.orders-table td {
  padding: 12px;
  border: 1px solid #ccc;
  text-align: center;
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

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 20px;
}

.pagination button {
  padding: 6px 12px;
  font-size: 1rem;
  cursor: pointer;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
