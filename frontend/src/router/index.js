import { createRouter, createWebHistory } from 'vue-router'

// Lazy-loaded views
const LoginView = () => import("../views/auth/LoginView.vue");
const RegisterView = () => import("../views/auth/RegisterView.vue");
const ForgotPasswordView = () => import("../views/auth/ForgotPasswordView.vue");
const ResetPasswordView = () => import("../views/auth/ResetPasswordView.vue");

const HomeView = () => import("../views/user/UserHomeView.vue");
const UserProductListView = () => import("../views/user/ProductListView.vue");
const UserProductDetailView = () => import("../views/user/ProductDetailView.vue");
const CartView = () => import("../views/user/CartView.vue");
const OrderHistoryView = () => import("../views/user/OrderHistoryView.vue");
const UserProfileView = () => import("../views/user/ProfileView.vue");
const ChangePasswordView = () => import("../views/user/ChangePasswordView.vue");

const AdminDashboardView = () => import("../views/admin/DashboardView.vue");
const AdminProductListView = () => import("../views/admin/ProductListView.vue");
const AdminProductDetailView = () => import("../views/admin/ProductDetailView.vue");
const AdminProductCreateView = () => import("../views/admin/ProductCreateView.vue");
const AdminProductEditView = () => import("../views/admin/ProductEditView.vue");
const AdminCategoryListView = () => import("../views/admin/CategoryListView.vue");
const AdminCategoryCreateView = () => import("../views/admin/CategoryCreateView.vue");
const AdminCategoryEditView = () => import("../views/admin/CategoryEditView.vue");
const AdminUserListView = () => import("../views/admin/UserListView.vue");
const AdminUserDetailView = () => import("../views/admin/UserDetailView.vue");
const AdminOrderListView = () => import("../views/admin/OrderListView.vue");
const AdminOrderDetailView = () => import("../views/admin/OrderDetailView.vue");

const NotFoundView = () => import("../views/NotFoundView.vue");

const routes = [
    // Auth
    { path: "/login", component: LoginView, meta: { guest: true } },
    { path: "/register", component: RegisterView, meta: { guest: true } },
    { path: "/forgot-password", component: ForgotPasswordView, meta: { guest: true } },
    { path: "/reset-password", component: ResetPasswordView, meta: { guest: true } },

    // User
    { path: "/", redirect: "/home" },
    { path: "/home", component: HomeView, meta: { requiresAuth: true } },
    { path: "/products", component: UserProductListView, meta: { requiresAuth: true } },
    { path: "/products/:id", component: UserProductDetailView, meta: { requiresAuth: true } },
    { path: "/cart", component: CartView, meta: { requiresAuth: true } },
    { path: "/my-orders", component: OrderHistoryView, meta: { requiresAuth: true } },
    { path: "/profile", component: UserProfileView, meta: { requiresAuth: true } },
    { path: "/change-password", component: ChangePasswordView, meta: { requiresAuth: true } },

    // Admin
    { path: "/admin", component: AdminDashboardView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/products", component: AdminProductListView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/products/create", component: AdminProductCreateView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/products/:id", component: AdminProductDetailView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/products/:id/edit", component: AdminProductEditView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/categories", component: AdminCategoryListView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/categories/create", component: AdminCategoryCreateView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/categories/:id/edit", component: AdminCategoryEditView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/users", component: AdminUserListView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/users/:id", component: AdminUserDetailView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/orders", component: AdminOrderListView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin/orders/:id", component: AdminOrderDetailView, meta: { requiresAuth: true, requiresAdmin: true } },

    // 404
    { path: "/:pathMatch(.*)*", component: NotFoundView }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// Route Guards
import { useAuthStore } from '../store/auth'
router.beforeEach((to, from, next) => {
    const auth = useAuthStore();

    // Redirect if route requires authentication
    if (to.meta.requiresAuth && !auth.isAuthenticated) {
        return next('/login');
    }

    // Prevent logged-in users from accessing guest routes
    if (to.meta.guest && auth.isAuthenticated) {
        // redirect to home or admin dashboard based on role
        if (auth.user && auth.user.role === 'ROLE_ADMIN') {
            return next('/admin');
        } else {
            return next('/home');
        }
    }

    // Admin route check
    if (to.meta.requiresAdmin && (!auth.user || auth.user.role !== 'ROLE_ADMIN')) {
        return next('/login');
    }

    next();
});

export default router;