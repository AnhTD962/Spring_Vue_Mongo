<template>
  <div class="admin-layout">
    <AdminNavbar @logout="logout" />
    <div class="admin-content">
      <AdminSidebar />
      <main class="admin-main">
        <router-view />
      </main>
    </div>
  </div>
</template>
<script setup>
import AdminNavbar from '../components/admin/AdminNavbar.vue'
import AdminSidebar from '../components/admin/AdminSidebar.vue'
import { useAuthStore } from '../store/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()
function logout() {
  auth.logout()
  router.push('/login')
}
</script>
<style scoped>
.admin-layout {
  min-height: 100vh;
  background: #f7f7fa;
}
.admin-content {
  display: flex;
}
.admin-main {
  flex: 1;
  padding: 2rem;
  background: #fff;
  min-height: 100vh;
}
</style>