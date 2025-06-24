<template>
  <div>
    <h2>Edit Category</h2>
    <form v-if="category" @submit.prevent="submit">
      <input v-model="category.name" placeholder="Name" required />
      <button type="submit">Save</button>
    </form>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getCategory, updateCategory } from "../../api/categories";

const router = useRouter();
const route = useRoute();
const category = ref(null);

onMounted(async () => {
  const { data } = await getCategory(route.params.id);
  category.value = data;
});

async function submit() {
  await updateCategory(route.params.id, category.value);
  router.push("/admin/categories");
}
</script>