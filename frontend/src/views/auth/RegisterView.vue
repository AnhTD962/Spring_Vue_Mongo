<template>
  <div>
    <h2>Register</h2>
    <form @submit.prevent="submit" enctype="multipart/form-data">
      <input v-model="form.name" placeholder="Name" required />
      <input v-model="form.email" type="email" placeholder="Email" required />
      <input v-model="form.password" type="password" placeholder="Password" required />
      <input v-model="form.phone" placeholder="Phone number" required />
      <input v-model="form.city" placeholder="City" required />
      <input v-model="form.state" placeholder="State" required />
      <input type="file" @change="onFileChange"/>
      <input v-model="form.pinCode" placeholder="Pin Code" required />
      <button type="submit">Register</button>
      <div v-if="message" class="success">{{ message }}</div>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
    <router-link to="/login">Already have an account? Login</router-link>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { register } from "../../api/auth";

const form = ref({
  name: "",
  email: "",
  password: "",
  phone: "",
  city: "",
  state: "",
  pinCode: "",
});
const avatar = ref(null);
const error = ref("");
const message = ref("");

function onFileChange(e) {
  avatar.value = e.target.files[0];
}

async function submit() {
  error.value = "";
  message.value = "";
  try {
    const formData = new FormData();
    for (const key in form.value) {
      formData.append(key, form.value[key]);
    }
    if (avatar.value) {
      formData.append("avata", avatar.value); // field name must match backend
    }
    await register(formData);
    message.value = "Registration successful! Please check your email.";
    form.value = {
      name: "",
      email: "",
      password: "",
      phone: "",
      city: "",
      state: "",
      pinCode: "",
    };
    avatar.value = null;
  } catch (e) {
    error.value = "Error during registration";
  }
}
</script>

<style scoped>
.success { color: green; margin-top: 8px; }
.error { color: red; margin-top: 8px; }
form { display: flex; flex-direction: column; gap: 8px; max-width: 350px; }
</style>