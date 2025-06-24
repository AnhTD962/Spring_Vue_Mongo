<template>
  <div>
    <h2>Reset Password</h2>
    <form @submit.prevent="submit">
      <input v-model="password" type="password" placeholder="New password" required />
      <button type="submit">Set New Password</button>
      <div v-if="message">{{ message }}</div>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
    <router-link to="/login">Back to Login</router-link>
  </div>
</template>
<script setup>
import { ref } from "vue";
import { useRoute } from "vue-router";
import { resetPassword } from "../../api/auth";

const route = useRoute();
const password = ref("");
const message = ref("");
const error = ref("");

async function submit() {
  message.value = "";
  error.value = "";
  try {
    await resetPassword({ token: route.query.token, password: password.value });
    message.value = "Password set! You can now log in.";
  } catch {
    error.value = "Could not reset password.";
  }
}
</script>