<template>
  <div>
    <h2>Create Product</h2>
    <form @submit.prevent="submit">
      <input v-model="product.name" placeholder="Name" required />
      <input v-model="product.price" type="number" placeholder="Price" required />
      <button type="submit">Create</button>
    </form>
  </div>
</template>
<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { createProduct } from "../../api/products";

const router = useRouter();
const product = ref({ name: "", price: 0 });

async function submit() {
  await createProduct(product.value);
  router.push("/admin/products");
}
</script>