<template>
  <div>
    <h2>Create Category</h2>
    <form @submit.prevent="submit">
      <input v-model="category.name" placeholder="Name" required />
      <button type="submit">Create</button>
    </form>
  </div>
</template>
<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { createCategory } from "../../api/categories";

const router = useRouter();
const category = ref({ name: "" });

async function submit() {
  await createCategory(category.value);
  router.push("/admin/categories");
}
</script>