<template>
  <div>
    <h2>Admin - Categories</h2>
    <router-link to="/admin/categories/create">Create Category</router-link>
    <ul>
      <li v-for="c in categories" :key="c.id">
        {{ c.name }} - {{ c.isActive }}
        <router-link :to="`/admin/categories/${c.id}/edit`">Edit</router-link>
        <button @click="remove(c.id)">Delete</button>
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getCategories, deleteCategory } from "../../api/categories";

const categories = ref([]);
async function fetch() {
  const { data } = await getCategories();
  categories.value = data;
}
onMounted(fetch);

async function remove(id) {
  await deleteCategory(id);
  fetch();
}
</script>