<template>
  <div class="login-container">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label>Email:</label>
        <input v-model="email" type="email" required />
      </div>
      <div>
        <label>Password:</label>
        <input v-model="password" type="password" required />
      </div>
      <div v-if="error" class="error">{{ error }}</div>
      <button type="submit">Login</button>
    </form>
    <router-link to="/register">regiter now</router-link>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../../store/auth'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const error = ref('')
const auth = useAuthStore()
const router = useRouter()

async function handleLogin() {
  try {
    error.value = ''
    await auth.login({ email: email.value, password: password.value })
    // Redirect based on role
    if (auth.user && auth.user.role === 'ROLE_ADMIN') {
      router.push('/admin')
    } else {
      router.push('/')
    }
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || 'Login failed'
  }
}
</script>

<style scoped>
.login-container {
  max-width: 350px;
  margin: 60px auto;
  background: #181818;
  padding: 2rem;
  border-radius: 8px;
  color: #fff;
}
.error {
  color: #f55;
  margin-bottom: 1rem;
}
button {
  margin-top: 1rem;
  width: 100%;
  padding: 0.5rem;
  background: #7b2ff2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>