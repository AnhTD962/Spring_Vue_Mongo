<template>
  <div class="admin-users-wrapper">
    <h2>Admin - Users</h2>

    <div class="users-header">
      <label>
        Filter:
        <select v-model="filter" @change="fetchUsers">
          <option value="">All</option>
          <option value="1">User</option>
          <option value="2">Admin</option>
        </select>
      </label>

      <router-link to="/admin/add-new-admin">
        <button class="add-btn">+ Add New Admin</button>
      </router-link>
    </div>

    <table class="users-table" v-if="users.length">
      <thead>
        <tr>
          <th>Photo</th>
          <th>Name</th>
          <th>Email</th>
          <th>Role</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="u in users" :key="u.id">
          <td>
            <img :src="`/uploads/profile_img/${u.profileImage}`" alt="Profile" />
          </td>
          <td>{{ u.name }}</td>
          <td>{{ u.email }}</td>
          <td>{{ u.role }}</td>
          <td :style="{ color: u.isEnable ? 'green' : 'red' }">
            {{ u.isEnable ? 'Active' : 'Inactive' }}
          </td>
          <td>
            <button @click="toggleStatus(u)">
              {{ u.isEnable ? 'Deactivate' : 'Activate' }}
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else class="no-users">No users found.</div>
  </div>
</template>


<script setup>
import { ref, onMounted } from "vue";
import { getUsers, toggleUserStatus } from "../../api/users";

const users = ref([]);
const filter = ref("");

async function fetchUsers() {
  const { data } = await getUsers(filter.value);
  users.value = data;
}

async function toggleStatus(user) {
  await toggleUserStatus(user.id, !user.isEnable);
  await fetchUsers();
}

onMounted(fetchUsers);
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
  max-width: 900px;
  margin: 0 auto 1rem;
}

select {
  padding: 0.4rem 0.6rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 0.95rem;
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
</style>
