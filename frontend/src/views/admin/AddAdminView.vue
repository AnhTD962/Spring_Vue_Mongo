<template>
  <div>
    <h2>Add Admin</h2>
    <form @submit.prevent="submit">
      <input v-model="user.name" placeholder="Name" required />
      <input v-model="user.email" placeholder="Email" required />
      <input v-model="user.password" type="password" placeholder="Password" required />
      <input type="file" accept="image/*" @change="onFileChange" />
      <button type="submit">Create</button>
    </form>
    <p v-if="errorMessage" style="color: red">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { addAdmin } from "../../api/users";

const router = useRouter();

const user = ref({
  name: "",
  email: "",
  password: ""
});

const file = ref(null);
const errorMessage = ref("");

function onFileChange(e) {
  file.value = e.target.files[0] || null;
}

async function submit() {
  errorMessage.value = "";

  try {
    const formData = new FormData();
    formData.append("name", user.value.name);
    formData.append("email", user.value.email);
    formData.append("password", user.value.password);

    if (file.value) {
      formData.append("img", file.value);
    }

    await addAdmin(formData);
    router.push("/admin/users");
  } catch (err) {
    if (err.response && err.response.data) {
      errorMessage.value = err.response.data; 
    } else {
      errorMessage.value = "Failed to create admin. Please try again.";
    }
  }
}
</script>
