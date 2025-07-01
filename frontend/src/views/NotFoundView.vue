<template>
  <div class="not-found-wrapper">
    <div class="not-found-container">
      <h1>404</h1>
      <p>Sorry, the page you’re looking for doesn’t exist.</p>
      <router-link to="/" class="home-link">← Go to Homepage</router-link>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.not-found-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f3f4f6;
  text-align: center;
  padding: 20px;
}

.not-found-container {
  background: #fff;
  padding: 2rem 2.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

h1 {
  font-size: 4rem;
  color: #7b2ff2;
  margin-bottom: 0.5rem;
}

p {
  font-size: 1.2rem;
  color: #555;
  margin-bottom: 1.5rem;
}

.home-link {
  color: #7b2ff2;
  text-decoration: underline;
  font-weight: bold;
  font-size: 1rem;
}
</style>
