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
import { changePassword, logout } from "../../api/auth"; 
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
