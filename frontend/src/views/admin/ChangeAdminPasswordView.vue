<template>
  <div>
    <h2>Change Password</h2>
    <form @submit.prevent="change">
      <input v-model="currentPassword" type="password" placeholder="Current password" required />
      <input v-model="newPassword" type="password" placeholder="New password" required />
      <input v-model="confirmPassword" type="password" placeholder="Confirm new password" required />
      <button type="submit">Change Password</button>
      <div v-if="msg">{{ msg }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { changePassword} from "../../api/users"; 
import { useAuthStore } from '../../store/auth';

const router = useRouter();
const auth = useAuthStore();

const currentPassword = ref("");
const newPassword = ref("");
const confirmPassword = ref("");
const msg = ref("");

async function change() {
  if (newPassword.value !== confirmPassword.value) {
    msg.value = "New passwords do not match!";
    return;
  } else if (newPassword.value === currentPassword.value) {
    msg.value = "New password must be different from current password!";
    return;
  }

  try {
    await changePassword({
      currentPassword: currentPassword.value,
      newPassword: newPassword.value,
    });

    msg.value = "Password changed! Logging out...";
    auth.logout();
    router.push("/");

  } catch (err) {
    msg.value = err.response?.data || "Error changing password.";
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

input {
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

div[v-if="msg"] {
  text-align: center;
  margin-top: 1rem;
  font-weight: bold;
  color: green;
}
</style>
