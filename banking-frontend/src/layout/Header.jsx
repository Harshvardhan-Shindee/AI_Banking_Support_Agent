import { useState, useEffect, useRef } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import ProfileModal from "../components/ProfileModal";
import "../styles/header.css";

const Header = () => {
  const { role, logout, user } = useAuth();
  const navigate = useNavigate();
  const [showMenu, setShowMenu] = useState(false);
  const [showProfileModal, setShowProfileModal] = useState(false);
  const dropdownRef = useRef(null);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  // ✅ Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target)
      ) {
        setShowMenu(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <header className="header">
      <div className="header-left">
        <h2
          className="logo"
          onClick={() => {
            if (role === "ADMIN") {
              navigate("/admin");
            } else {
              navigate("/dashboard");
            }
          }}
        >
          SecureBank
        </h2>

        {role === "CUSTOMER" && (
          <nav className="nav-links">
            <span onClick={() => navigate("/dashboard")}>Dashboard</span>
            <span onClick={() => navigate("/transactions")}>Transactions</span>
          </nav>
        )}

        {role === "ADMIN" && (
          <nav className="nav-links">
            <span onClick={() => navigate("/admin")}>Admin Panel</span>
          </nav>
        )}
      </div>

      {role && (
        <div className="profile-section" ref={dropdownRef}>
          <div
            className="profile-circle"
            onClick={() => setShowMenu(!showMenu)}
          >
            {user?.dp ? (
              <img
                src={user.dp}
                alt="Profile"
                className="profile-img"
              />
            ) : (
              <span>
                {user?.custname?.charAt(0)?.toUpperCase() || "U"}
              </span>
            )}
          </div>

          {showMenu && (
            <div className="profile-dropdown" ref={dropdownRef}>
              <div
                className="profile-info clickable-profile"
                onClick={() => {
                  setShowMenu(false);
                  setShowProfileModal(true);
                }}
              >
                {user?.dp ? (
                  <img src={user.dp} alt="Profile" className="dropdown-img" />
                ) : (
                  <div className="dropdown-fallback">
                    {user?.custname?.charAt(0)?.toUpperCase() || "U"}
                  </div>
                )}

                <div>
                  <p className="profile-name">
                    {user?.custname || "User"}
                  </p>
                  <p className="profile-role">{role}</p>
                </div>
              </div>

              <button className="logout-btn" onClick={handleLogout}>
                Logout
              </button>
            </div>
          )}
        </div>

      )}
      {showProfileModal && (
        <ProfileModal
          user={user}
          onClose={() => setShowProfileModal(false)}
          refreshUser={() => window.location.reload()}
        />
      )}
    </header>
  );
};

export default Header;