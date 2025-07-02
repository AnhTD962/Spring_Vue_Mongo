<template>
  <div class="orders-wrapper">
    <h2>My Orders</h2>
    <div v-if="paginatedOrders.length" class="orders-table-container">
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
          <tr v-for="(o, index) in paginatedOrders" :key="o.id">
            <td>{{ index + 1 + currentPage * pageSize }}</td>
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

    <div class="pagination" v-if="totalPages > 1">
      <button @click="prevPage" :disabled="currentPage === 0">Prev</button>
      <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage + 1 >= totalPages">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { getMyOrders } from "../../api/orders";

const allOrders = ref([]);
const pageSize = 5;
const currentPage = ref(0);

const totalPages = computed(() => Math.ceil(allOrders.value.length / pageSize));

const paginatedOrders = computed(() => {
  const start = currentPage.value * pageSize;
  return allOrders.value.slice(start, start + pageSize);
});

function nextPage() {
  if (currentPage.value + 1 < totalPages.value) currentPage.value++;
}

function prevPage() {
  if (currentPage.value > 0) currentPage.value--;
}

onMounted(async () => {
  const { data } = await getMyOrders();
  allOrders.value = data || [];
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
