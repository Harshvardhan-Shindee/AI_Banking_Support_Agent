import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useState } from "react";
import Layout from "./layout/Layout";
import AdminLayout from "./layout/AdminLayout";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import TransactionsPage from "./pages/TransactionsPage";
import AdminInactive from "./pages/AdminInactive";
import AdminDashboard from "./pages/AdminDashboard";
import AdminUsers from "./pages/AdminUsers";
import AdminTransactions from "./pages/AdminTransactions";
import AdminPending from "./pages/AdminPending";
import AdminTickets from "./pages/AdminTickets";
import ChatBotPanel from "./components/ChatBotPanel";

import ProtectedRoute from "./utils/ProtectedRoute";
import { useAuth } from "./context/AuthContext";

function App() {
  const { token, role } = useAuth();

  const [toast, setToast] = useState({
    message: "",
    type: "success",
  });

  const showToast = (msg, type = "success") => {
    setToast({ message: msg, type });

    setTimeout(() => {
      setToast({ message: "", type: "success" });
    }, 2000);
  };

  return (
    <BrowserRouter>

      {/* GLOBAL TOAST */}
      {toast.message && (
        <div className={`toast ${toast.type}`}>
          {toast.message}
        </div>
      )}

      <Routes>

        {/* ROOT */}
        <Route
          path="/"
          element={
            token
              ? role === "ADMIN"
                ? <Navigate to="/admin" />
                : <Navigate to="/dashboard" />
              : <Navigate to="/login" />
          }
        />

        {/* AUTH */}
        <Route
          path="/login"
          element={
            token
              ? role === "ADMIN"
                ? <Navigate to="/admin" />
                : <Navigate to="/dashboard" />
              : <Login />
          }
        />

        <Route path="/register" element={<Register />} />

        {/* ================= USER ================= */}
        <Route element={<Layout />}>

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard showToast={showToast} />
              </ProtectedRoute>
            }
          />

          <Route
            path="/transactions"
            element={
              <ProtectedRoute>
                <TransactionsPage showToast={showToast} />
              </ProtectedRoute>
            }
          />

        </Route>

        {/* ================= ADMIN ================= */}
        <Route element={<AdminLayout />}>

          <Route
            path="/admin"
            element={
              <ProtectedRoute roleRequired="ADMIN">
                <AdminDashboard />
              </ProtectedRoute>
            }
          />

          <Route
            path="/admin/users"
            element={
              <ProtectedRoute roleRequired="ADMIN">
                <AdminUsers showToast={showToast} />
              </ProtectedRoute>
            }
          />

          {/* ✅ NEW: PENDING ROUTE */}
          <Route
            path="/admin/pending"
            element={
              <ProtectedRoute roleRequired="ADMIN">
                <AdminPending showToast={showToast} />
              </ProtectedRoute>
            }
          />

          <Route
            path="/admin/transactions/:accno"
            element={
              <ProtectedRoute roleRequired="ADMIN">
                <AdminTransactions />
              </ProtectedRoute>
            }
          />

          <Route
            path="/admin/inactive"
            element={
              <ProtectedRoute roleRequired="ADMIN">
                <AdminInactive showToast={showToast} />
              </ProtectedRoute>
            }
          />

          <Route
            path="/admin/tickets"
            element={
              <ProtectedRoute roleRequired="ADMIN">
                <AdminTickets />
              </ProtectedRoute>
            }
          />

        </Route>

      </Routes>

      {token && role !== "ADMIN" && <ChatBotPanel />}
    </BrowserRouter>
  );
}

export default App;