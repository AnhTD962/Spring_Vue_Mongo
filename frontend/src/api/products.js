import api from "./axios";

export function getProducts(page = 0, size = 6) {
    return api.get(`/products?page=${page}&size=${size}`);
}

export function createProduct(payload) {
    return api.post("/products", payload);
}

export function updateProduct(id, product, imageFile) {
  const formData = new FormData();
  for (const key in product) {
    formData.append(key, product[key]);
  }
  if (imageFile) {
    formData.append("file", imageFile);
  }
  return api.put(`/products/${id}`, formData);
}

export function deleteProduct(id) {
    return api.delete(`/products/${id}`);
}

export function getViewProducts(page = 0, size = 6){
    return api.get(`/products/all?page=${page}&size=${size}`);
}

export function getViewProductDetail(id){
    return api.get(`/products/id/${id}`);
}

export function getProductByCategory(name, page = 0, size = 6) {
  return api.get(`/products/category/${name}?page=${page}&size=${size}`);
}