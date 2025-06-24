<template>
  <div class="auth-layout">
    <main class="auth-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
</script>

<style scoped>
.auth-layout {
  min-height: 100vh;
  background: #f2f5fa;
  display: flex;
  align-items: center;
  justify-content: center;
}
.auth-main {
  width: 100%;
  max-width: 400px;
  margin: 2rem auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 24px #0001;
  padding: 2rem;
}
</style>