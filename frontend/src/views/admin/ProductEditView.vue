<template>
  <div>
    <h2>Edit Product</h2>
    <form v-if="product" @submit.prevent="submit">
      <!-- Image preview -->
      <div class="image-upload">
        <label for="product-image-input">
          <img
            :src="imagePreview"
            alt="Product Image"
            class="product-image"
            title="Click to change product image"
          />
        </label>
        <input id="product-image-input" type="file" accept="image/*" hidden @change="onImageChange" />
      </div>

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
import { getProduct, updateProduct } from "../../api/products";
import { getCategoryActive } from "@/api/categories";

const router = useRouter();
const route = useRoute();

const product = ref(null);
const categories = ref([]);
const imageFile = ref(null);
const imagePreview = ref("/default-product.jpg");

onMounted(async () => {
  const [{ data: productData }, { data: categoryData }] = await Promise.all([
    getProduct(route.params.id),
    getCategoryActive()
  ]);
  product.value = productData;
  categories.value = categoryData;

  if (product.value.image) {
    imagePreview.value = `/uploads/product_img/${product.value.image}?t=${Date.now()}`;
  }
});

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

function onImageChange(e) {
  const file = e.target.files[0];
  if (file) {
    imageFile.value = file;
    imagePreview.value = URL.createObjectURL(file);
  }
}

async function submit() {
  await updateProduct(route.params.id, product.value, imageFile.value);
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

label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 500;
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

.image-upload {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
  cursor: pointer;
}

.product-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border-radius: 10px;
  border: 2px solid #ccc;
  transition: box-shadow 0.3s ease;
}

.product-image:hover {
  box-shadow: 0 0 10px rgba(123, 47, 242, 0.5);
}
</style>
