<template>
  <div class="login-wrapper">
    <router-link to="/" class="back-link">← Back to Homepage</router-link>
    <div class="login-container">
      <h2>Login</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Email</label>
          <input v-model="email" type="email" required />
        </div>
        <div class="form-group">
          <label>Password</label>
          <input v-model="password" type="password" required />
        </div>
        <div v-if="error" class="error">{{ error }}</div>
        <button type="submit">Login</button>
      </form>
      <div class="form-links">
        <router-link to="/register">Register now</router-link>
        <router-link to="/forgot-password">Forgot password?</router-link>
      </div>
    </div>
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
    if (auth.user?.role === 'ROLE_ADMIN') {
      router.push('/admin/orders')
    } else {
      router.push('/')
    }
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || 'Login failed'
  }
}
</script>

<style scoped>
.login-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60px 20px;
  background-color: #f4f4f4;
}

.back-link {
  align-self: flex-start;
  margin-bottom: 20px;
  color: #555;
  text-decoration: none;
}

.login-container {
  background: #181818;
  color: #fff;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 360px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 0.6rem;
  border-radius: 4px;
  border: none;
  font-size: 1rem;
  box-sizing: border-box;
}

.error {
  color: #f55;
  margin: 1rem 0;
  text-align: center;
}

button {
  width: 100%;
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

.form-links {
  margin-top: 1rem;
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
}

.form-links a {
  color: #ccc;
  text-decoration: underline;
}
</style>
