<template>
  <div>
    <router-link to="/admin/users" class="back-btn no-print">← Back to Users</router-link>
    <h2>Add Admin</h2>
    <form @submit.prevent="submit">
      <div>
        <label for="name">Name:</label>
        <input v-model="user.name" type="text" id="name" placeholder="Name" required />
      </div>
      <div>
        <label for="email">Email:</label>
        <input v-model="user.email" type="email" id="email" placeholder="Email" required />
      </div>
      <div>
        <label for="password">Password:</label>
        <input v-model="user.password" type="password" id="password" placeholder="Password" required /> 
      </div>
      <div>
        <label for="image">Profile Image:</label>
        <input type="file" id="image" accept="image/*" @change="onFileChange" />
      </div>
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
  gap: 1rem;
}

form div {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

form div>label,
form div>input,
form div>select {
  width: 100%;
}

input[type="text"],
input[type="email"],
input[type="password"],
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

p {
  text-align: center;
  margin-top: 1rem;
  font-weight: bold;
}

.back-btn {
  display: inline-block;
  margin-bottom: 1.5rem;
  background-color: #eee;
  color: #333;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  text-decoration: none;
  font-weight: bold;
  transition: background-color 0.2s;
}

.back-btn:hover {
  background-color: #ddd;
}
</style>
