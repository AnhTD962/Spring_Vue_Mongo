<template>
  <div>
    <h2>Admin - Users</h2>

    <label>
      Filter:
      <select v-model="filter" @change="fetchUsers">
        <option value="">All</option>
        <option value="1">User</option>
        <option value="2">Admin</option>
      </select>
    </label>

    <router-link to="/admin/add-new-admin">Add new admin</router-link>

    <ul>
      <li v-for="u in users" :key="u.id">
        <img :src="`/uploads/profile_img/${u.profileImage}`" width="50" />
        {{ u.name }} ({{ u.email }}) -
        <strong>{{ u.role }}</strong> -
        Status:
        <span :style="{ color: u.isEnable ? 'green' : 'red' }">
          {{ u.isEnable ? 'Active' : 'Inactive' }}
        </span>
        <button @click="toggleStatus(u)">
          {{ u.isEnable ? 'Deactivate' : 'Activate' }}
        </button>
      </li>
    </ul>
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
