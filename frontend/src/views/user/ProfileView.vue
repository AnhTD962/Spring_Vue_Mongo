<template>
  <div v-if="profile">
    <h2>My Profile</h2>
    <form @submit.prevent="save">
      <input type="file" @change="onFileChange" />
      <input v-model="profile.name" placeholder="Name" required />
      <input v-model="profile.email" type="email" placeholder="Email" disabled />
      <input v-model="profile.mobileNumber" placeholder="Phone number" />
      <input v-model="profile.address" placeholder="Address" />
      <input v-model="profile.city" placeholder="City" />
      <input v-model="profile.state" placeholder="State" />
      <input v-model="profile.pincode" placeholder="Pin code" />
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
const selectedImg = ref(null);

onMounted(async () => {
  const { data } = await getProfile();
  profile.value = data;
});

function onFileChange(e) {
  selectedImg.value = e.target.files[0];
}

async function save() {
  const formData = new FormData();

  // Chỉ gửi các field cần update
  const fieldsToSend = ["name", "mobileNumber", "address", "city", "state", "pincode"];
  fieldsToSend.forEach((key) => {
    if (profile.value[key] !== undefined && profile.value[key] !== null) {
      formData.append(key, profile.value[key]);
    }
  });

  if (selectedImg.value) {
    formData.append("img", selectedImg.value);
  }

  await updateProfile(formData);
  msg.value = "Profile updated!";
}
</script>
