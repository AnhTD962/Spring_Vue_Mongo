<template>
  <div class="categories-wrapper">
    <h2>Shop by Category</h2>
    <div class="categories-grid">
      <div v-for="c in categories" :key="c.id" class="category-card">
        <router-link :to="`/categories/${c.name}`" class="category-link">
          <img :src="`/uploads/category_img/${c.imageName}`"/>
          <div class="category-name">{{ c.name }}</div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getCategoryActive } from "../api/categories";

const categories = ref([]);

onMounted(async () => {
  const { data } = await getCategoryActive();
  categories.value = data;
});
</script>

<style scoped>
.categories-wrapper {
  padding: 40px 20px;
  background-color: #f9f9f9;
  text-align: center;
}

h2 {
  font-size: 2rem;
  margin-bottom: 2rem;
  color: #333;
}

.categories-grid {
  display: flex;
  gap: 20px;
  justify-content: space-evenly;
  align-items: center;
}

.category-card {
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  width: 100%;
  max-width: 180px;
}

.category-card:hover {
  transform: translateY(-4px);
}

.category-link {
  display: block;
  text-decoration: none;
  color: inherit;
  padding: 1rem;
}

.category-link img {
  width: 60%;
  height: 60%;
  object-fit: cover;
  border-radius: 6px;
}

.category-name {
  margin-top: 0.8rem;
  font-weight: bold;
  font-size: 1rem;
  color: #7b2ff2;
}
</style>
