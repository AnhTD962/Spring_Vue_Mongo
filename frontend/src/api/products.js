import api from "./axios";

export function getProducts(params) {
    return api.get("/admin/products", { params });
}

export function createProduct(payload) {
    return api.post("/admin/products", payload);
}

export function getProduct(id){
    return api.get(`/admin/products/${id}`);
}

export function updateProduct(id, product, imageFile) {
  const formData = new FormData();
  for (const key in product) {
    formData.append(key, product[key]);
  }
  if (imageFile) {
    formData.append("file", imageFile);
  }
  return api.put(`/admin/products/${id}`, formData);
}

export function deleteProduct(id) {
    return api.delete(`/admin/products/${id}`);
}

export function getViewProducts(){
    return api.get("/products");
}

export function getViewProductDetail(id, payload){
    return api.get(`/products/${id}`, payload);
}