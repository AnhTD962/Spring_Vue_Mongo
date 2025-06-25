<template>
  <section>
    <div class="container mt-5 p-5">
      <div class="row">
        <div class="col-md-6 offset-md-3 text-center">
          <i class="fa-solid fa-circle-check fa-5x text-success"></i>
          <p class="fs-3">Product Ordered Successfully</p>
          <p class="fs-4">Product will be delivered within 7 days</p>
          <div class="text-center">
            <router-link to="/" class="btn btn-primary">Home</router-link>
            <router-link to="/user/user-orders" class="btn btn-primary">Your Order</router-link>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'OrderSuccess'
}
</script>

<style scoped>
/* Add any custom styles here if needed */
</style>