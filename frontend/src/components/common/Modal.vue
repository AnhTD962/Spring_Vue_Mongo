<template>
  <div class="modal-backdrop" @click.self="$emit('close')">
    <div class="modal-content">
      <slot />
      <button class="close-btn" @click="$emit('close')">Close</button>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}
.modal-content {
  background: #fff;
  padding: 2rem;
  position: relative;
  border-radius: 8px;
  box-shadow: 0 0 12px #0002;
}
.close-btn {
  position: absolute;
  top: 0.5rem; right: 0.5rem;
}
</style>