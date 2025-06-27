<template>
  <div>
    <h2 v-if="items.length > 0">Your Cart</h2>
    <h2 v-else><router-link to="/products">Buy now</router-link></h2>
    <ul>
      <li v-for="item in items" :key="item.id">
        <div>
          <strong>{{ item.title }}</strong> ({{ item.category }})<br />
          Quantity:
          <input type="number" v-model.number="item.quantity" min="1" @change="updateQuantity(item.id, item.quantity)"
            style="width: 50px" />
          <br />
          Total: ${{ item.totalPrice?.toFixed(2) || "0.00" }}
        </div>
        <button @click="remove(item.id)">Remove</button>
      </li>
    </ul>
    <hr v-if="items.length > 0" />
    <h3 v-if="items.length > 0">
      Total Cart Price: ${{ totalCartPrice.toFixed(2) }}
    </h3>
    <button @click="openConfirmPopup" v-if="items.length > 0">
      Checkout
    </button>

    <!-- Confirm Modal -->
    <div v-if="showPopup" class="modal">
      <div class="modal-content">
        <h3>Confirm Order Information</h3>
        <form @submit.prevent="checkout">
          <input v-model="order.firstName" placeholder="First Name" />
          <input v-model="order.lastName" placeholder="Last Name" />
          <input v-model="order.email" placeholder="Email" />
          <input v-model="order.mobileNo" placeholder="Mobile No" />
          <input v-model="order.address" placeholder="Address" />
          <input v-model="order.city" placeholder="City" />
          <input v-model="order.state" placeholder="State" />
          <select v-model="order.paymentType">
            <option value="CASH_ON_DELIVERY">Cash on Delivery</option>
            <option value="ONLINE">Online Payment</option>
          </select>

          <h4>Order Items</h4>
          <ul>
            <li v-for="item in items" :key="item.id">
              {{ item.title }} - Quantity: {{ item.quantity }} - Total: ${{ item.totalPrice.toFixed(2) }}
            </li>
          </ul>

          <h4>Total Order Price: ${{ totalCartPrice.toFixed(2) }}</h4>

          <button type="submit">Confirm</button>
          <button type="button" @click="showPopup = false">Cancel</button>
        </form>
      </div>
    </div>
  </div>
</template>


<script setup>
import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { getCart, removeCartItem, updateCartQuantity } from "@/api/cart";
import { checkoutCart } from "@/api/orders";
import api from "@/api/axios"; // bạn cần endpoint /user/profile

const items = ref([]);
const totalCartPrice = ref(0);
const showPopup = ref(false);
const router = useRouter();

const order = ref({
  firstName: "",
  lastName: "",
  email: "",
  mobileNo: "",
  address: "",
  city: "",
  state: "",
  paymentType: "CASH_ON_DELIVERY",
});

async function fetch() {
  const { data } = await getCart();
  items.value = data.items;
  totalCartPrice.value = data.totalCartPrice || 0;
}

onMounted(async () => {
  await fetch();
  await loadUserInfo();
});

async function loadUserInfo() {
  const res = await api.get("/user/profile"); // cần tạo API này nếu chưa có
  const user = res.data;
  order.value.firstName = user.firstName;
  order.value.lastName = user.lastName;
  order.value.email = user.email;
  order.value.mobileNo = user.mobileNumber;
  order.value.address = user.address || "";
  order.value.city = user.city || "";
  order.value.state = user.state || "";
}

async function remove(cartId) {
  await removeCartItem(cartId);
  await fetch();
}

async function updateQuantity(cartId, quantity) {
  if (quantity < 1) quantity = 1;
  await updateCartQuantity(cartId, quantity);
  await fetch();
}

function openConfirmPopup() {
  showPopup.value = true;
}

async function checkout() {
  try {
    await checkoutCart(order.value);
    alert("Checkout successful!");
    showPopup.value = false;
    await fetch();
    router.push("/success");
  } catch (err) {
    alert("Checkout failed: " + (err.response?.data?.message || err.message));
  }
}
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  width: 400px;
  max-height: 90vh;
  overflow-y: auto;
}
</style>