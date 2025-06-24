<template>
  <div>
    <h2>Forgot Password</h2>
    <form @submit.prevent="submit">
      <input v-model="email" type="email" placeholder="Your email" required />
      <button type="submit">Send Reset Link</button>
      <div v-if="message">{{ message }}</div>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
    <router-link to="/login">Back to Login</router-link>
  </div>
</template>
<script setup>
import { ref } from "vue";
import { forgotPassword } from "../../api/auth";

const email = ref("");
const message = ref("");
const error = ref("");

async function submit() {
  message.value = "";
  error.value = "";
  try {
    await forgotPassword(email.value);
    message.value = "Reset link sent! Please check your email.";
  } catch {
    error.value = "Could not send reset link.";
  }
}
</script>