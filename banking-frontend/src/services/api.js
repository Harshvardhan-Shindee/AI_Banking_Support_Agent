import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    const expiry = localStorage.getItem("expiry");

    if (token && expiry && Date.now() < Number(expiry)) {
      config.headers.Authorization = `Bearer ${token}`;
    } else {
      // ❌ expired → clear + force logout
      localStorage.clear();
      window.location.href = "/login";
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// 🔥 RESPONSE INTERCEPTOR
api.interceptors.response.use(
  (response) => response,
  (error) => {

    if (error.response && error.response.status === 401) {
      console.log("❌ Unauthorized / Token expired");

      localStorage.clear();
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

export default api;