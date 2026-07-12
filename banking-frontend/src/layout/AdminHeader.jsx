import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import "../styles/header.css";

const AdminHeader = () => {
  const { logout, user } = useAuth();
  const navigate = useNavigate();

  return (
    <header className="header">
      <div className="header-left">
        <h2 className="logo" onClick={() => navigate("/admin")}>
          SecureBank Admin
        </h2>

        <nav className="nav-links">
          <span onClick={() => navigate("/admin")}>Dashboard</span>
          <span onClick={() => navigate("/admin/users")}>Users</span>
          <span onClick={() => navigate("/admin/pending")}>Pending</span>
          <span onClick={() => navigate("/admin/inactive")}>Inactive</span>
          <span onClick={() => navigate("/admin/tickets")}>Tickets</span>
        </nav>
      </div>

      <div className="profile-section">
        <span className="admin-name">
          {user?.custname || "Admin"}
        </span>

        <button className="logout-btn" onClick={logout}>
          Logout
        </button>
      </div>
    </header>
  );
};

export default AdminHeader;