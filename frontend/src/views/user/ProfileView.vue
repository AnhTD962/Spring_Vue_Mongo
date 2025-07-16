<template>
  <div v-if="profile">
    <h2>My Profile</h2>
    <form @submit.prevent="save">
      <div class="avatar-upload">
        <label for="avatar-input">
          <img :src="avatarPreview" alt="User Avatar" class="avatar" title="Click to change avatar" />
        </label>
        <input id="avatar-input" type="file" @change="onFileChange" accept="image/*" hidden />
      </div>

      <div>Full name:
        <input v-model="profile.name" type="text" placeholder="Name" required />
      </div>

      <div>Email:
        <input v-model="profile.email" type="email" placeholder="Email" disabled />
      </div>

      <div>Phone number:
        <input v-model="profile.mobileNumber" type="text" placeholder="Phone number" />
      </div>

      <div>Address:
        <input v-model="profile.address" type="text" placeholder="Address" />
      </div>

      <div>City:
        <input v-model="profile.city" type="text" placeholder="City" />
      </div>

      <div>State:
        <input v-model="profile.state" type="text" placeholder="State" />
      </div>

      <div>Pin code:
        <input v-model="profile.pincode" type="text" placeholder="Pin code" />
      </div>

      <button type="submit">Save</button>
      <div class="msg" v-if="msg">{{ msg }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getProfile, updateProfile } from "../../api/users";

const profile = ref(null);
const msg = ref("");
const avatarPreview = ref('/default-avatar.png');
const selectedImg = ref(null);

onMounted(async () => {
  try {
    const { data } = await getProfile();
    profile.value = data;
    avatarPreview.value = data.profileImage
      ? `/uploads/profile_img/${data.profileImage}`
      : '/default-avatar.png';
    console.log("Profile:", profile.value)
    console.log("Avatar path:", avatarPreview.value)
  } catch (e) {
    msg.value = "Failed to load profile.";
  }
});

function onFileChange(e) {
  const file = e.target.files[0];
  if (file) {
    selectedImg.value = file;
    avatarPreview.value = URL.createObjectURL(file);
  }
}

async function save() {
  const formData = new FormData();
  const fieldsToSend = ["name", "mobileNumber", "address", "city", "state", "pincode"];
  fieldsToSend.forEach((key) => {
    if (profile.value[key] !== undefined && profile.value[key] !== null) {
      formData.append(key, profile.value[key]);
    }
  });

  if (selectedImg.value) {
    formData.append("img", selectedImg.value);
  }

  try {
    await updateProfile(formData);
    msg.value = "Profile updated!";
  } catch (e) {
    msg.value = "Failed to update profile.";
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
  max-width: 500px;
  margin: 0 auto;
  background: #fff;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

form div {
  display: flex;
  flex-direction: column;
}

input[type="text"],
input[type="email"],
input[type="file"],
input[type="number"] {
  padding: 0.6rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 1rem;
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
  margin-top: 1rem;
}

button:hover {
  background-color: #6920d4;
}

.avatar-upload {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
  cursor: pointer;
}

.avatar {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 50%;
  border: 2px solid #ccc;
  transition: box-shadow 0.3s ease;
}

.avatar:hover {
  box-shadow: 0 0 10px rgba(123, 47, 242, 0.5);
}

.msg {
  text-align: center;
  margin-top: 1rem;
  color: green;
  font-weight: bold;
}
</style>
