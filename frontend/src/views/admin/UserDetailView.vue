<template>
  <div v-if="user">
    <h2>User Detail</h2>
    <p><b>Name:</b> {{ user.name }}</p>
    <p><b>Email:</b> {{ user.email }}</p>
    <p><b>Status:</b> {{ user.status }}</p>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getUser } from "../../api/users";

const route = useRoute();
const user = ref(null);

onMounted(async () => {
  const { data } = await getUser(route.params.id);
  user.value = data;
});
</script>