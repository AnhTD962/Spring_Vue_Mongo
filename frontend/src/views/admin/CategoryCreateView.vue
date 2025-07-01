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
<style scoped>
h2 {
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
}

form {
  max-width: 400px;
  margin: 0 auto;
  background: #fff;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

input[type="text"],
input[type="file"] {
  padding: 0.6rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 0.6rem 1.2rem;
  background-color: #7b2ff2;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #6920d4;
}
</style>
