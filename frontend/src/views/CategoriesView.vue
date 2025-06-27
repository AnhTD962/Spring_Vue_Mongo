<template>
  <div>
    <h2>Categories</h2>
    <ul>
      <li v-for="c in categories" :key="c.id">
        <router-link :to="`/categories/${c.name}`">
          <img v-if="c.image" :src="getCategoryImgUrl(c.image)" alt="Category Image"
            style="width: 120px; height: auto;" />
          <div>Name: {{ c.name }}</div>
        </router-link>
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getViewCategories } from "../api/categories";

const categories = ref([]);
async function fetch() {
  const { data } = await getViewCategories();
  categories.value = data;
  console.log("category", categories);
}
onMounted(fetch);
</script>