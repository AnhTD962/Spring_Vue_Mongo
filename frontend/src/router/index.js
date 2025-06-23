import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

// Layouts
import AdminLayout from '@/views/admin/AdminLayout.vue';

// Views
import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/auth/LoginView.vue';
import ProfileView from '@/views/user/ProfileView.vue';
import ProductsView from '@/views/ProductsView.vue';
import CategoriesView from '@/views/admin/CategoriesView.vue';
import AdminProductsView from '@/views/admin/ProductsView.vue';
import UsersView from '@/views/admin/UsersView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { guestOnly: true }
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView,
    meta: { requiresAuth: true }
  },
  {
    path: '/products',
    name: 'products',
    component: ProductsView
  },
  // Admin routes
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: 'categories',
        name: 'admin-categories',
        component: CategoriesView
      },
      {
        path: 'products',
        name: 'admin-products',
        component: AdminProductsView
      },
      {
        path: 'users',
        name: 'admin-users',
        component: () => import('@/views/admin/UsersView.vue')
      },
      // Redirect /admin to /admin/categories
      {
        path: '',
        redirect: { name: 'admin-categories' }
      }
    ]
  },
  // Redirect to home for any other route
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { top: 0 };
  }
});

// Navigation guard
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();
  
  // Check authentication status if we have a saved user
  if (!authStore.isAuthenticated && localStorage.getItem('user')) {
    try {
      const isAuthenticated = await authStore.checkAuth();
      if (!isAuthenticated) {
        // If checkAuth fails, clear the stored user
        authStore.logout();
        next('/login');
        return;
      }
    } catch (error) {
      console.error('Auth check failed:', error);
      authStore.logout();
      next('/login');
      return;
    }
  }

  // For non-authenticated routes (like login, register)
  if (to.matched.some(record => record.meta.guestOnly)) {
    if (authStore.isAuthenticated) {
      // If user is already logged in, redirect to home or intended URL
      const redirectPath = from.query.redirect || '/';
      next(redirectPath);
    } else {
      next();
    }
  } 
  // For authenticated routes
  else if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!authStore.isAuthenticated) {
      // Redirect to login with the intended URL
      next({
        name: 'login',
        query: { redirect: to.fullPath }
      });
    } else if (to.matched.some(record => record.meta.requiresAdmin) && !authStore.isAdmin) {
      // If admin route but not admin, redirect to home
      next({ name: 'home' });
    } else {
      next();
    }
    // Public route, allow access
    next();
  }
});

export default router;
