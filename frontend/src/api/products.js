import api from "./axios";

export function getProducts() {
    return api.get("/products");
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

export function getViewProducts(){
    return api.get("/products/all");
}

export function getViewProductDetail(id){
    return api.get(`/products/id/${id}`);
}

export function getProductByCategory(name) {
  return api.get(`/products/category/${name}`);
}