<template>
  <div class="admin-users-wrapper">
    <h2>Admin - Users</h2>

    <!-- Header: Filter & Add -->
    <div class="users-header">
      <div class="filters">
        <label>
          Role:
          <select v-model="selectedRole" @change="handleFilterChange">
            <option value="">All</option>
            <option value="1">User</option>
            <option value="2">Admin</option>
          </select>
        </label>
        <input type="text" v-model="searchInput" placeholder="Search..." class="search-input" />
      </div>

      <router-link to="/admin/add-new-admin">
        <button class="add-btn">+ Add New Admin</button>
      </router-link>
    </div>

    <!-- Users Table -->
    <table class="users-table">
      <thead>
        <tr>
          <th>#</th>
          <th>Photo</th>
          <th>Name</th>
          <th>Email</th>
          <th>Role</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(user, index) in filteredUsers" :key="user.id">
          <td>{{ index + 1 + currentPage * pageSize }}</td>
          <td>
            <img :src="`/uploads/profile_img/${user.profileImage}`" alt="Profile" />
          </td>
          <td>{{ user.name }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
          <td :style="{ color: user.isEnable ? 'green' : 'red' }">
            {{ user.isEnable ? 'Active' : 'Inactive' }}
          </td>
          <td>
            <button @click="changeStatus(user)">
              {{ user.isEnable ? 'Deactivate' : 'Activate' }}
            </button>
          </td>
        </tr>
        <tr v-if="filteredUsers.length === 0">
          <td colspan="7">No users found</td>
        </tr>
      </tbody>
    </table>

    <!-- Pagination -->
    <div class="pagination" v-if="totalPages > 1">
      <button @click="prevPage" :disabled="currentPage === 0">Prev</button>
      <span>Page {{ currentPage + 1 }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage + 1 >= totalPages">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { getUsers, toggleUserStatus } from '@/api/users';

const users = ref([])
const currentPage = ref(0)
const totalPages = ref(0)
const pageSize = 5
const selectedRole = ref('')
const searchInput = ref('')

const loadUsers = async () => {
  try {
    const res = await getUsers(currentPage.value, pageSize, selectedRole.value)
    users.value = res.content
    totalPages.value = res.page.totalPages
  } catch (e) {
    console.error('Failed to load users:', e)
  }
}

const changeStatus = async (user) => {
  try {
    await toggleUserStatus(user.id, !user.isEnable)
    user.isEnable = !user.isEnable // Optimistic UI update
  } catch (e) {
    console.error('Failed to change user status:', e)
  }
}

const filteredUsers = computed(() => {
  const search = searchInput.value.toLowerCase();
  if (!search) return users.value;

  return users.value.filter(user =>
    user.name.toLowerCase().includes(search) ||
    user.email.toLowerCase().includes(search)
  );
});

const handleFilterChange = () => {
  currentPage.value = 0
  loadUsers()
}

const nextPage = () => {
  if (currentPage.value + 1 < totalPages.value) {
    currentPage.value++
    loadUsers()
  }
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--
    loadUsers()
  }
}

onMounted(() => {
  loadUsers()
})
</script>




<style scoped>
.admin-users-wrapper {
  padding: 40px 20px;
  min-height: 100vh;
  background-color: #f9f9f9;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
}

.users-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  max-width: 1000px;
  margin: 0 auto 1rem;
}

.filters {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}

select {
  padding: 0.4rem 0.6rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 0.95rem;
}

.search-input {
  padding: 0.4rem 0.6rem;
  font-size: 0.95rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 220px;
}

.add-btn {
  padding: 0.5rem 1rem;
  background-color: #7b2ff2;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}

.add-btn:hover {
  background-color: #6920d4;
}

.users-table {
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
  border-collapse: collapse;
  background-color: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.users-table th,
.users-table td {
  padding: 0.75rem;
  text-align: center;
  border: 1px solid #eee;
}

.users-table th {
  background-color: #f0f0f0;
  color: #333;
  font-weight: 600;
}

.users-table img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

button {
  padding: 0.4rem 0.8rem;
  border: none;
  border-radius: 4px;
  background-color: #7b2ff2;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

button:hover {
  background-color: #6920d4;
}

.no-users {
  text-align: center;
  margin-top: 2rem;
  color: #666;
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
