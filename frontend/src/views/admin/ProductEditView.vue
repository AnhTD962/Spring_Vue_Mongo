<template>
  <div>
    <h2>Edit Product</h2>
    <form v-if="product" @submit.prevent="submit">
      <input v-model="product.title" placeholder="Title" required />
      <input v-model="product.description" placeholder="Description" required />

      <select v-model="product.category" required>
        <option disabled value="">Select category</option>
        <option v-for="cat in categories" :key="cat.id" :value="cat.name">
          {{ cat.name }}
        </option>
      </select>

      <input v-model.number="product.price" type="number" placeholder="Price" required />
      <input v-model.number="product.discount" type="number" placeholder="Discount (%)" />
      <input :value="product.discountPrice.toFixed(2)" placeholder="Discount Price" readonly />

      <input v-model.number="product.stock" type="number" placeholder="Stock" required />
      <input type="file" @change="e => imageFile.value = e.target.files[0]" />

      <label>
        <input type="checkbox" v-model="product.isActive" />
        Active
      </label>

      <button type="submit">Save</button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getProduct, updateProduct} from "../../api/products";
import { getCategoryActive } from "@/api/categories";

const router = useRouter();
const route = useRoute();

const product = ref(null);
const categories = ref([]);
const imageFile = ref(null);

onMounted(async () => {
  const [{ data: productData }, { data: categoryData }] = await Promise.all([
    getProduct(route.params.id),
    getCategoryActive()
  ]);
  product.value = productData;
  categories.value = categoryData;
});

// Auto-calculate discountPrice
watch(
  () => [product.value?.price, product.value?.discount],
  () => {
    if (!product.value) return;
    const price = parseFloat(product.value.price) || 0;
    const discount = parseFloat(product.value.discount) || 0;
    if (discount >= 0 && discount <= 100) {
      product.value.discountPrice = price - (price * discount / 100);
    }
  }
);

async function submit() {
  await updateProduct(route.params.id, product.value, imageFile.value);
  router.push("/admin/products");
}
</script>
