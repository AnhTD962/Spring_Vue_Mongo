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
        <input v-model="category.name" placeholder="Name" required />
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
