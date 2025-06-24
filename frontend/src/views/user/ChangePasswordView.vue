<template>
  <div>
    <h2>Change Password</h2>
    <form @submit.prevent="change">
      <input v-model="currentPassword" type="password" placeholder="Current password" required />
      <input v-model="newPassword" type="password" placeholder="New password" required />
      <button type="submit">Change Password</button>
      <div v-if="msg">{{ msg }}</div>
    </form>
  </div>
</template>
<script setup>
import { ref } from "vue";
import { changePassword } from "../../api/auth";

const currentPassword = ref("");
const newPassword = ref("");
const msg = ref("");

async function change() {
  await changePassword({ currentPassword: currentPassword.value, newPassword: newPassword.value });
  msg.value = "Password changed!";
}
</script>