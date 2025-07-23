<template>
  <div class="products-wrapper">
    <h2>Our Products</h2>
    <div class="product-grid">
      <div class="product-card" v-for="p in products" :key="p.id">
        <router-link :to="`/products/${p.id}`" class="product-link">
          <img :src="p.image ? `/uploads/product_img/${p.image}` : '/default-product.png'" alt="product image"
            class="product-image" />
          <div class="product-info">
            <h3>{{ p.title }}</h3>

            <template v-if="p.discount && p.discount > 0">
              <p>
                <strong>Discount:</strong> {{ p.discount }}%
              </p>
              <p>
                <strong>Price:</strong>
                <span class="line-through text-gray-500">${{ p.price }}</span>
              </p>
              <p>
                <strong>Discounted:</strong> ${{ p.discountPrice }}
              </p>
            </template>

            <template v-else>
              <p>
                <strong>Price:</strong> ${{ p.price }}
              </p>
            </template>
          </div>
        </router-link>
      </div>
      <div class="pagination" v-if="totalPages > 1">
        <button :disabled="currentPage === 0" @click="fetchProducts(currentPage - 1)">Previous</button>
        <span>Page {{ currentPage + 1 }} / {{ totalPages }}</span>
        <button :disabled="currentPage === totalPages - 1" @click="fetchProducts(currentPage + 1)">Next</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getViewProducts } from "../api/products";

const products = ref([]);
const currentPage = ref(0);
const totalPages = ref(0);

const fetchProducts = async (page = 0) => {
  const { data } = await getViewProducts(page, 5);
  products.value = data.content;
  totalPages.value = data.totalPages;
  currentPage.value = data.number;
};

onMounted(() => {
  fetchProducts();
});
</script>

<style scoped>
.products-wrapper {
  padding: 40px 20px;
  background-color: #f8f8f8;
  color: #333;
}

h2 {
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2rem;
}

.product-grid {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
}

.product-card {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease;
}

.product-card:hover {
  transform: translateY(-4px);
}

.product-link {
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.product-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-bottom: 1px solid #eee;
}

.product-info {
  padding: 1rem;
}

.product-info h3 {
  margin: 0 0 0.5rem;
  font-size: 1.1rem;
  color: #222;
}

.product-info p {
  margin: 0.3rem 0;
  font-size: 0.9rem;
}

.view-link {
  margin-top: 0.8rem;
  color: #7b2ff2;
  text-decoration: underline;
  font-weight: bold;
}

.line-through {
  text-decoration: line-through;
  color: #999;
}

.text-gray-500 {
  color: #777;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
  align-items: center;
}

.pagination button {
  padding: 8px 16px;
  background-color: #7b2ff2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
