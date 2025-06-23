// useAuthStore.js
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('user')) || null);
  const router = useRouter();

  const isAuthenticated = computed(() => !!user.value);
  const isAdmin = computed(() => user.value?.role === 'ROLE_ADMIN');

  async function login(credentials) {
    console.log('Attempting login with credentials:', credentials);
    
    try {
      // Prepare the request payload as JSON to match backend's SigninRequestDTO
      const loginData = {
        email: credentials.email, // Backend expects 'email' not 'username'
        password: credentials.password
      };

      console.log('Sending login request to /api/signin with data:', JSON.stringify(loginData));
      
      // Send login credentials with JSON content type
      const response = await axios.post('/signin', loginData, {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        withCredentials: true // Important for cookies/session
      });

      console.log('Login response:', response);

      if (response.data) {
        console.log('Login successful, response data:', response.data);
        
        // Extract user data from response
        const userData = response.data.user || {};
        
        // Store user data in the store
        user.value = {
          email: userData.email || credentials.email,
          name: userData.name || userData.email?.split('@')[0] || 'User',
          roles: userData.roles || [],
          // If using JWT token, it might come in the response
          token: response.data.token || response.data.accessToken
        };
        
        console.log('Processed user data:', user.value);
        
        // Persist user data to localStorage
        localStorage.setItem('user', JSON.stringify(user.value));
        
        // Set default auth header for subsequent requests if token is available
        if (user.value.token) {
          axios.defaults.headers.common['Authorization'] = `Bearer ${user.value.token}`;
          console.log('Authorization header set');
        } else {
          console.warn('No token received in login response');
        }
        
        return true;
      } else {
        const error = new Error('Login failed: No user data received');
        console.error(error.message);
        throw error;
      }
    } catch (error) {
      console.error('Login failed:', error);

      // Clear any existing user data
      user.value = null;
      localStorage.removeItem('user');

      // Extract error message from response
      let errorMessage = 'Login failed. Please try again.';
      
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        console.error('Response data:', error.response.data);
        console.error('Response status:', error.response.status);
        console.error('Response headers:', error.response.headers);
        
        if (error.response.data) {
          if (error.response.data.message) {
            errorMessage = error.response.data.message;
          } else if (error.response.data.error) {
            errorMessage = error.response.data.error;
          } else if (typeof error.response.data === 'string') {
            errorMessage = error.response.data;
          }
        }
        
        // Handle specific HTTP status codes
        if (error.response.status === 401) {
          errorMessage = 'Invalid email or password';
        } else if (error.response.status === 403) {
          errorMessage = 'Access denied. Please contact support.';
        } else if (error.response.status >= 500) {
          errorMessage = 'Server error. Please try again later.';
        }
      } else if (error.request) {
        // The request was made but no response was received
        console.error('No response received:', error.request);
        errorMessage = 'No response from server. Please check your connection.';
      } else {
        // Something happened in setting up the request
        console.error('Request setup error:', error.message);
        errorMessage = `Request error: ${error.message}`;
      }
      
      throw new Error(errorMessage);
    }
  }

  async function logout() {
    console.log('Logging out...');
    
    try {
      // Call the backend logout endpoint
      await axios.post('/signout', {}, {
        withCredentials: true // Important for session cookies
      });
      console.log('Logout successful on server');
    } catch (error) {
      console.error('Error during logout:', error);
      // Continue with client-side cleanup even if server logout fails
    } finally {
      // Clear client-side state
      user.value = null;
      localStorage.removeItem('user');
      delete axios.defaults.headers.common['Authorization'];
      
      console.log('Client-side logout complete');
      
      // Redirect to login page
      router.push('/login');
    }
  }

  async function checkAuth() {
    console.log('Checking authentication status...');
    
    try {
      // Try to get user data from localStorage first
      const storedUser = localStorage.getItem('user');
      
      if (!storedUser) {
        console.log('No stored user found');
        return false;
      }
      
      const userData = JSON.parse(storedUser);
      console.log('Found stored user data:', userData);
      
      // Set the token in the axios headers if it exists
      if (userData.token) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${userData.token}`;
        console.log('Authorization header set from stored token');
      }
      
      try {
        // Verify the session with the backend
        const response = await axios.get('/api/user/me', {
          withCredentials: true // Important for session cookies
        });
        
        if (response.data) {
          console.log('User is authenticated:', response.data);
          
          // Merge stored data with fresh data from the server
          const updatedUserData = {
            ...userData,
            ...response.data,
            // Preserve the token from storage unless we get a new one
            token: response.data.token || userData.token
          };
          
          // Update the store and localStorage
          user.value = updatedUserData;
          localStorage.setItem('user', JSON.stringify(updatedUserData));
          
          // Update the auth header if we got a new token
          if (response.data.token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
          }
          
          return true;
        }
      } catch (error) {
        console.error('Session verification failed:', error);
        // If we get a 401, the session is invalid
        if (error.response && error.response.status === 401) {
          console.log('Session expired or invalid');
          // Clear the stored user data
          user.value = null;
          localStorage.removeItem('user');
          delete axios.defaults.headers.common['Authorization'];
          return false;
        }
        // For other errors, we'll assume the session is still valid
        console.log('Assuming session is still valid despite error');
        return true;
      }
      
      // If we get here, something went wrong
      console.log('No valid authentication found');
      return false;
      
    } catch (error) {
      console.error('Error checking authentication status:', error);
      return false;
    }
  }

  return {
    user,
    isAuthenticated,
    isAdmin,
    login,
    logout,
    checkAuth
  };
});