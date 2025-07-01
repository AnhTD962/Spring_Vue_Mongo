<template>
  <div class="forgot-wrapper">
    <div class="forgot-container">
      <h2>Reset Your Password</h2>
      <form @submit.prevent="submit">
        <input v-model="email" type="email" placeholder="Your Email" required />
        <button type="submit">Send New Password</button>
        <div v-if="message" class="msg">{{ message }}</div>
        <div v-if="error" class="error">{{ error }}</div>
      </form>
      <router-link to="/login" class="login-link">← Back to Login</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { forgotPassword } from "../../api/auth";

const email = ref("");
const message = ref("");
const error = ref("");
const router = useRouter();

async function submit() {
  message.value = "";
  error.value = "";

  try {
    await forgotPassword(email.value);
    message.value = "✅ New password sent! Redirecting to login...";
    setTimeout(() => {
      router.push("/login");
    }, 2000);
  } catch (err) {
    error.value = err.response?.data || "❌ Could not send new password.";
  }
}
</script>

<style scoped>
.forgot-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px 20px;
  background-color: #f4f4f4;
}

.forgot-container {
  background: #181818;
  color: #fff;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 360px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);
  text-align: center;
}

h2 {
  margin-bottom: 1.5rem;
}

form {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

input {
  padding: 0.6rem;
  border-radius: 4px;
  border: none;
  font-size: 1rem;
}

button {
  padding: 0.6rem;
  background: #7b2ff2;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s ease;
}

button:hover {
  background: #6920d4;
}

.msg {
  color: #00cc66;
  margin-top: 0.5rem;
}

.error {
  color: #ff5555;
  margin-top: 0.5rem;
}

.login-link {
  display: block;
  margin-top: 1.5rem;
  color: #ccc;
  text-decoration: underline;
  font-size: 0.9rem;
}
</style>
