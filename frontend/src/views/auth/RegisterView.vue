<template>
  <div>
    <h2>Register</h2>
    <form @submit.prevent="submit">
      <input v-model="form.name" placeholder="Name" required />
      <input v-model="form.email" type="email" placeholder="Email" required />
      <input v-model="form.password" type="password" placeholder="Password" required />
      <button type="submit">Register</button>
      <div v-if="message">{{ message }}</div>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
    <router-link to="/login">Already have an account? Login</router-link>
  </div>
</template>
<script setup>
import { ref } from "vue";
import { register } from "../../api/auth";

const form = ref({ name: "", email: "", password: "" });
const error = ref("");
const message = ref("");

async function submit() {
  error.value = "";
  message.value = "";
  try {
    await register(form.value);
    message.value = "Registration successful! Please check your email.";
  } catch {
    error.value = "Error during registration";
  }
}
</script>