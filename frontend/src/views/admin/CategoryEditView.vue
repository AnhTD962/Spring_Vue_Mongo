<template>
  <div>
    <h2>Edit Category</h2>
    <form v-if="category" @submit.prevent="submit">
      <div>
        <label>Image:</label>
        <input type="file" @change="onFileChange" />
      </div>
      <div>
        <label>Name:</label>
        <input v-model="category.name" type="text" placeholder="Name" required />
      </div>
      <div>
        <label>Active:</label>
        <select v-model="category.isActive">
          <option :value="true">Active</option>
          <option :value="false">Inactive</option>
        </select>
      </div>
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
const file = ref(null);

onMounted(async () => {
  const { data } = await getCategory(route.params.id);
  category.value = data;
});

function onFileChange(e) {
  file.value = e.target.files[0];
}

async function submit() {
  const formData = new FormData();
  formData.append("name", category.value.name);
  formData.append("isActive", category.value.isActive ?? true);
  if (file.value) {
    formData.append("file", file.value);
  }

  await updateCategory(route.params.id, formData);
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

label {
  font-weight: 500;
  margin-bottom: 0.4rem;
  display: block;
}

input[type="text"],
input[type="file"],
select {
  padding: 0.6rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
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
