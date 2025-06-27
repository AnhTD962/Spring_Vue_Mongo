import { createRouter, createWebHistory } from 'vue-router'

// Lazy-loaded views
const LoginView = () => import("../views/auth/LoginView.vue");
const RegisterView = () => import("../views/auth/RegisterView.vue");
const ForgotPasswordView = () => import("../views/auth/ForgotPasswordView.vue");

const UserProductListView = () => import("../views/ProductListView.vue");
const UserProductDetailView = () => import("../views/ProductDetailView.vue");
const CartView = () => import("../views/user/CartView.vue");
const OrderHistoryView = () => import("../views/user/OrderHistoryView.vue");
const OrderConfirmationView = () => import("../views/user/OrderConfirmationView.vue");
const UserProfileView = () => import("../views/user/ProfileView.vue");
const ChangePasswordView = () => import("../views/user/ChangePasswordView.vue");    
const OrderDetailView = () => import("../views/user/OrderDetailView.vue")
const ProductsByCategoryView = () => import('../views/ProductsByCategoryView.vue');

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
const AdminProfileView = () => import("../views/admin/AdminProfileView.vue");
const AdminChangePasswordView = () => import("../views/admin/ChangeAdminPasswordView.vue")
const CategoriesView = () => import("../views/CategoriesView.vue");

const NotFoundView = () => import("../views/NotFoundView.vue");

const routes = [
    // Auth
    { path: "/login", component: LoginView, meta: { guest: true } },
    { path: "/register", component: RegisterView, meta: { guest: true } },
    { path: "/forgot-password", component: ForgotPasswordView, meta: { guest: true } },

    // User
    { path: "/", redirect: "/home" },
    { path: "/products", component: UserProductListView},
    { path: "/products/:id", component: UserProductDetailView},
    { path: "/cart", component: CartView, meta: { requiresAuth: true } },
    { path: "/my-orders", component: OrderHistoryView, meta: { requiresAuth: true } },
    { path: "/user-profile", component: UserProfileView, meta: { requiresAuth: true } },
    { path: "/user-change-password", component: ChangePasswordView, meta: { requiresAuth: true } },
    { path: "/success", component: OrderConfirmationView, meta: {requiresAuth: true}},
    { path: "/my-orders/:id", component: OrderDetailView, meta: {requiresAuth: true}},
    { path: "/categories", component: CategoriesView},
    { path: '/categories/:id', component: ProductsByCategoryView},
    

    // Admin
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
    { path: "/admin-change-password", component: AdminChangePasswordView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: "/admin-profile", component: AdminProfileView, meta: { requiresAuth: true, requiresAdmin: true } },

    // 404
    { path: "/:pathMatch(.*)*", component: NotFoundView }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

import { useAuthStore } from '../store/auth'
router.beforeEach(async (to, from, next) => {
    const auth = useAuthStore()

    if (!auth.user && auth.loadUser) {
        await auth.loadUser() 
    }

    if (to.meta.requiresAuth && !auth.isAuthenticated) {
        return next('/login');
    }

    if (to.meta.guest && auth.isAuthenticated) {
        if (auth.user && auth.user.role === 'ROLE_ADMIN') {
            return next('/admin/orders');
        } else {
            return next('/home');
        }
    }

    if (to.meta.requiresAdmin && (!auth.user || auth.user.role !== 'ROLE_ADMIN')) {
        return next('/login');
    }

    next()
});

export default router;