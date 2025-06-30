<template>
  <div>
    <h2>Create Category</h2>
    <form @submit.prevent="submit">
      <input type="file" @change="onFileChange" required />
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
const file = ref(null);

function onFileChange(e) {
  file.value = e.target.files[0];
}

async function submit() {
  const formData = new FormData();
  formData.append("name", category.value.name);
  if (file.value) {
    formData.append("file", file.value);
  }

  await createCategory(formData);
  router.push("/admin/categories");
}
</script>
