<template>
  <div v-if="profile">
    <h2>My Profile</h2>
    <form @submit.prevent="save">
      <input v-model="profile.name" placeholder="Name" required />
      <input v-model="profile.email" type="email" placeholder="Email" required />
      <button type="submit">Save</button>
      <div v-if="msg">{{ msg }}</div>
    </form>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getProfile, updateProfile } from "../../api/auth";

const profile = ref(null);
const msg = ref("");

onMounted(async () => {
  const { data } = await getProfile();
  profile.value = data;
});

async function save() {
  await updateProfile(profile.value);
  msg.value = "Profile updated!";
}
</script>