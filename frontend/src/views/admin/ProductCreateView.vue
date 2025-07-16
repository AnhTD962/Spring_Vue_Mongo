<template>
  <div>
    <router-link to="/admin/products" class="back-btn no-print">← Back to Products</router-link>
    <h2>Create Product</h2>
    <form @submit.prevent="submit">
      <input v-model="product.title" placeholder="Name" required />
      <input v-model="product.description" placeholder="Description" required />

      <select v-model="product.category" required>
        <option disabled value="">Select category</option>
        <option v-for="cat in categories" :key="cat.id" :value="cat.name">
          {{ cat.name }}
        </option>
      </select>

      <input v-model.number="product.price" type="number" placeholder="Price" required />
      <input v-model.number="product.stock" type="number" placeholder="Stock" required />
      <input type="file" @change="e => imageFile.value = e.target.files[0]" required />

      <input v-model.number="product.discount" type="number" placeholder="Discount (%)" />
      <input :value="product.discountPrice.toFixed(2)" placeholder="Discount Price" readonly />

      <button type="submit">Create</button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import { createProduct } from "../../api/products";
import { getCategoryActive } from "@/api/categories";

const router = useRouter();
const product = ref({
  title: "",
  description: "",
  category: "",
  price: 0,
  stock: 0,
  discount: 0,
  discountPrice: 0,
  isActive: true,
});
const categories = ref([]);
const imageFile = ref(null);

onMounted(async () => {
  const res = await getCategoryActive();
  categories.value = res.data;
});

watch(
  () => [product.value.price, product.value.discount],
  () => {
    const price = parseFloat(product.value.price) || 0;
    const discount = parseFloat(product.value.discount) || 0;

    if (discount >= 0 && discount <= 100) {
      product.value.discountPrice = price - (price * discount / 100);
    } else {
      product.value.discountPrice = price;
    }
  }
);

async function submit() {
  const formData = new FormData();
  for (const key in product.value) {
    formData.append(key, product.value[key]);
  }
  if (imageFile.value) {
    formData.append("file", imageFile.value);
  }

  await createProduct(formData);
  router.push("/admin/products");
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

input,
select {
  padding: 0.6rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 1rem;
}

input[readonly] {
  background-color: #f3f3f3;
  cursor: not-allowed;
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
}

button:hover {
  background-color: #6920d4;
}

.back-btn {
  display: inline-block;
  margin-bottom: 1.5rem;
  background-color: #eee;
  color: #333;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  text-decoration: none;
  font-weight: bold;
  transition: background-color 0.2s;
}

.back-btn:hover {
  background-color: #ddd;
}
</style>
