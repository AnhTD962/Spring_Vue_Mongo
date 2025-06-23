import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useCartStore = defineStore('cart', () => {
  const items = ref(JSON.parse(localStorage.getItem('cartItems')) || []);

  // Computed properties
  const itemCount = computed(() => 
    items.value.reduce((total, item) => total + item.quantity, 0)
  );

  const cartTotal = computed(() =>
    items.value.reduce((total, item) => total + (item.price * item.quantity), 0)
  );

  // Actions
  function addToCart(product) {
    const existingItem = items.value.find(item => item.id === product.id);
    
    if (existingItem) {
      if (existingItem.quantity < (existingItem.stock || 999)) {
        existingItem.quantity++;
      }
    } else {
      items.value.push({ 
        ...product, 
        quantity: 1 
      });
    }
    
    saveCartToLocalStorage();
  }

  function removeFromCart(productId) {
    const index = items.value.findIndex(item => item.id === productId);
    if (index > -1) {
      items.value.splice(index, 1);
      saveCartToLocalStorage();
    }
  }

  function updateQuantity(productId, quantity) {
    const item = items.value.find(item => item.id === productId);
    if (item) {
      const newQuantity = Math.max(1, Math.min(quantity, item.stock || 999));
      item.quantity = newQuantity;
      saveCartToLocalStorage();
    }
  }

  function clearCart() {
    items.value = [];
    saveCartToLocalStorage();
  }

  function saveCartToLocalStorage() {
    localStorage.setItem('cartItems', JSON.stringify(items.value));
  }

  // Initialize from localStorage on store creation
  function initializeFromLocalStorage() {
    const savedCart = localStorage.getItem('cartItems');
    if (savedCart) {
      items.value = JSON.parse(savedCart);
    }
  }

  // Call initialization
  initializeFromLocalStorage();

  return {
    items,
    itemCount,
    cartTotal,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart
  };
});
