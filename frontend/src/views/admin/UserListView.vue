<template>
  <div>
    <h2>Admin - Users</h2>
    <ul>
      <li v-for="u in users" :key="u.id">
        {{ u.name }} ({{ u.email }})
        <router-link :to="`/admin/users/${u.id}`">Detail</router-link>
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getUsers } from "../../api/users";

const users = ref([]);
onMounted(async () => {
  const { data } = await getUsers();
  users.value = data;
});
</script>