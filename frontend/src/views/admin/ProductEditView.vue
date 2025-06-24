<template>
  <div>
    <h2>Edit Product</h2>
    <form v-if="product" @submit.prevent="submit">
      <input v-model="product.name" placeholder="Name" required />
      <input v-model="product.price" type="number" placeholder="Price" required />
      <button type="submit">Save</button>
    </form>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getProduct, updateProduct } from "../../api/products";

const router = useRouter();
const route = useRoute();
const product = ref(null);

onMounted(async () => {
  const { data } = await getProduct(route.params.id);
  product.value = data;
});

async function submit() {
  await updateProduct(route.params.id, product.value);
  router.push("/admin/products");
}
</script>