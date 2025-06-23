<template>
  <div class="profile-view">
    <div class="container">
      <h1>User Profile</h1>
      <div class="card">
        <div class="card-body">
          <div class="row">
            <div class="col-md-4">
              <div class="text-center">
                <img src="https://via.placeholder.com/150" class="rounded-circle img-fluid" alt="Profile Picture">
                <h5 class="my-3">{{ user?.username || 'User' }}</h5>
                <p class="text-muted mb-1">{{ user?.email || 'user@example.com' }}</p>
                <p class="text-muted mb-4">Member since {{ user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A' }}</p>
              </div>
            </div>
            <div class="col-md-8">
              <div class="card mb-3">
                <div class="card-body">
                  <h5 class="card-title">Personal Information</h5>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <p class="mb-0">Full Name</p>
                    </div>
                    <div class="col-sm-9">
                      <p class="text-muted mb-0">{{ user?.fullName || 'Not set' }}</p>
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <p class="mb-0">Email</p>
                    </div>
                    <div class="col-sm-9">
                      <p class="text-muted mb-0">{{ user?.email || 'Not set' }}</p>
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <p class="mb-0">Role</p>
                    </div>
                    <div class="col-sm-9">
                      <span class="badge bg-primary">
                        {{ user?.role || 'USER' }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="card">
                <div class="card-body">
                  <h5 class="card-title">Account Settings</h5>
                  <button class="btn btn-primary">Edit Profile</button>
                  <button class="btn btn-outline-danger ms-2">Change Password</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';

export default {
  name: 'ProfileView',
  setup() {
    const authStore = useAuthStore();
    const user = ref(null);

    onMounted(() => {
      // Load user data when the component is mounted
      user.value = authStore.user;
    });

    return {
      user
    };
  }
};
</script>

<style scoped>
.profile-view {
  padding: 20px 0;
}

.card {
  border-radius: 10px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.card-body {
  padding: 30px;
}

h1 {
  margin-bottom: 30px;
  color: #333;
  font-weight: 600;
}

.badge {
  font-size: 0.9em;
  padding: 0.5em 0.8em;
}

.btn {
  padding: 8px 20px;
  border-radius: 5px;
  font-weight: 500;
}

.text-muted {
  color: #6c757d !important;
}
</style>
