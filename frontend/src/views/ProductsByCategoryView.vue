<template>
  <div class="category-products-wrapper">
    <h2>Products in "{{ route.params.id }}"</h2>
    <div v-if="filteredProducts.length" class="product-grid">
      <div class="product-card" v-for="p in filteredProducts" :key="p.id">
        <router-link :to="`/products/${p.id}`" class="product-link">
          <img :src="p.image ? `/uploads/product_img/${p.image}` : '/default-product.png'" alt="product image" class="product-image" />
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
    </div>
    <div v-else class="no-products">No products found in this category.</div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { getProductByCategory } from "../api/products";

const route = useRoute();
const filteredProducts = ref([]);

const fetchProducts = async () => {
  const categoryName = route.params.id;
  const { data } = await getProductByCategory(categoryName);
  filteredProducts.value = data;
};

onMounted(fetchProducts);
watch(() => route.params.id, fetchProducts);
</script>

<style scoped>
.category-products-wrapper {
  padding: 40px 20px;
  min-height: 100vh;
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
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
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

.no-products {
  text-align: center;
  font-size: 1.2rem;
  color: #777;
  margin-top: 2rem;
}
</style>
