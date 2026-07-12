import { createContext, useState, useEffect } from "react";
import api from "../services/api";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {

  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [role, setRole] = useState(localStorage.getItem("role"));
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem("token"));
  const [loading, setLoading] = useState(true);

  // 🔹 LOGIN
  const login = async (newToken) => {
    const payload = JSON.parse(atob(newToken.split(".")[1]));
    const extractedRole = payload.role.replace("ROLE_", "");

    const expiryTime = payload.exp * 1000; // ✅ JWT expiry use कर

    localStorage.setItem("token", newToken);
    localStorage.setItem("role", extractedRole);
    localStorage.setItem("expiry", expiryTime);

    setToken(newToken);
    setRole(extractedRole);
    setIsAuthenticated(true);

    try {
      const res = await api.get("/auth/profile"); // ❗ header मत डाल
      setUser(res.data);
    } catch (error) {
      console.error("Profile fetch failed:", error);
      logout();
    }
  };

  // 🔹 LOGOUT
  const logout = () => {
    localStorage.clear();
    setToken(null);
    setRole(null);
    setUser(null);
    setIsAuthenticated(false);
  };

  // 🔹 Restore user on refresh
  useEffect(() => {
    const fetchProfile = async () => {
      const token = localStorage.getItem("token");
      const expiry = localStorage.getItem("expiry");

      if (!token || !expiry || Date.now() > Number(expiry)) {
        logout();
        setLoading(false);
        return;
      }

      try {
        const res = await api.get("/auth/profile"); // ✅ interceptor use होगा
        setUser(res.data);
        setIsAuthenticated(true);
      } catch (error) {
        logout();
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  return (
    <AuthContext.Provider
      value={{
        token,
        role,
        user,
        isAuthenticated,
        loading,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

import { useContext } from "react";

export const useAuth = () => {
  return useContext(AuthContext);
};