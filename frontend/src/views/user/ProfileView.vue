<template>
  <div v-if="profile">
    <h2>My Profile</h2>
    <form @submit.prevent="save">
      <div>Avata: <input type="file" @change="onFileChange" /></div>
      <div>Full name: <input v-model="profile.name" placeholder="Name" required /></div>
      <div>Email: <input v-model="profile.email" type="email" placeholder="Email" disabled /></div>
      <div>Phone number: <input v-model="profile.mobileNumber" placeholder="Phone number" /></div>
      <div>Address: <input v-model="profile.address" placeholder="Address" /></div>
      <div>City: <input v-model="profile.city" placeholder="City" /></div>
      <div>State: <input v-model="profile.state" placeholder="State" /></div>
      <div>Pin code: <input v-model="profile.pincode" placeholder="Pin code" /></div>
      <button type="submit">Save</button>
      <div>
        <router-link to="/user-change-password">
          <button type="button">Change Password</button>
        </router-link>
      </div>
      <div v-if="msg">{{ msg }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getUserProfile, updateUserProfile } from "../../api/auth";

const profile = ref(null);
const msg = ref("");
const selectedImg = ref(null);

onMounted(async () => {
  const { data } = await getUserProfile();
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

  await updateUserProfile(formData);
  msg.value = "Profile updated!";
}
</script>
