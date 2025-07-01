<template>
  <div class="register-wrapper">
    <div class="register-container">
      <h2>Create an Account</h2>
      <form @submit.prevent="submit" enctype="multipart/form-data">
        <input v-model="form.name" placeholder="Full Name" required />
        <input v-model="form.email" type="email" placeholder="Email" required />
        <input v-model="form.password" type="password" placeholder="Password" required />
        <input v-model="form.mobileNumber" placeholder="Phone Number" required />
        <input v-model="form.address" placeholder="Address" required />
        <input v-model="form.city" placeholder="City" required />
        <input v-model="form.state" placeholder="State" required />
        <input type="file" @change="onFileChange" />
        <input v-model="form.pincode" placeholder="Pin Code" required />
        <button type="submit">Register</button>
        <div v-if="message" class="success">{{ message }}</div>
        <div v-if="error" class="error">{{ error }}</div>
      </form>
      <router-link to="/login" class="login-link">Already have an account? Login</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { register } from "../../api/auth";

const router = useRouter();

const form = ref({
  name: "",
  email: "",
  password: "",
  mobileNumber: "",
  address: "",
  city: "",
  state: "",
  pincode: "",
});
const avatar = ref(null);
const error = ref("");
const message = ref("");

function onFileChange(e) {
  avatar.value = e.target.files[0];
}

async function submit() {
  error.value = "";
  message.value = "";
  try {
    const formData = new FormData();
    for (const key in form.value) {
      formData.append(key, form.value[key]);
    }
    if (avatar.value) {
      formData.append("avata", avatar.value);
    }

    await register(formData);

    message.value = "✅ Registration successful! Redirecting to login...";
    form.value = {
      name: "",
      email: "",
      password: "",
      mobileNumber: "",
      address: "",
      city: "",
      state: "",
      pincode: "",
    };
    avatar.value = null;

    setTimeout(() => {
      router.push("/login");
    }, 2000);
  } catch (e) {
    error.value = e.response?.data || "❌ Error during registration";
  }
}
</script>

<style scoped>
.register-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px 20px;
  background-color: #f4f4f4;
}

.register-container {
  background: #181818;
  color: #fff;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);
}

h2 {
  text-align: center;
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

.success {
  color: #00cc66;
  text-align: center;
  margin-top: 0.5rem;
}

.error {
  color: #ff5555;
  text-align: center;
  margin-top: 0.5rem;
}

.login-link {
  display: block;
  text-align: center;
  margin-top: 1rem;
  color: #ccc;
  text-decoration: underline;
  font-size: 0.9rem;
}
</style>
