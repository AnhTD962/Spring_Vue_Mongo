<template>
  <div class="user-layout">
    <UserNavbar/>
    <div class="user-content">
      <slot name="sidebar"></slot>
      <main class="user-main">
        <router-view />
      </main>
    </div>
    <UserSidebar/>
  </div>
</template>
<script setup>
import UserNavbar from '../components/user/UserNavbar.vue'
import UserSidebar from '../components/user/UserSidebar.vue'
import { useAuthStore } from '../store/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()
</script>
<style scoped>
.user-layout {
  min-height: 100vh;
  background: #f9f9fc;
}
.user-content {
  display: flex;
}
.user-main {
  flex: 1;
  padding: 2rem;
  background: #fff;
  min-height: 100vh;
}
</style>