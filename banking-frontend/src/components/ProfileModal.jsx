import { useState } from "react";
import api from "../services/api";
import "../styles/profileModal.css";

const ProfileModal = ({ user, onClose, refreshUser }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(user?.custname || "");

  const handleImageUpload = async (e) => {
  const file = e.target.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append("file", file);

  try {
    await api.post("/auth/upload-dp", formData);
    await refreshUser();
  } catch {
    alert("Upload failed");
  }
};

const handleDeleteImage = async () => {
  try {
    await api.delete("/auth/delete-dp");
    await refreshUser();
  } catch {
    alert("Delete failed");
  }
};

  const handleSave = async () => {
    try {
      await api.put("/customers/update-profile", {
        custname: name,
      });
      await refreshUser();
      setIsEditing(false);
    } catch {
      alert("Update failed");
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-container">

        <div className="modal-header">
          <h3>Profile</h3>
          <span className="close-btn" onClick={onClose}>✕</span>
        </div>

        <div className="profile-image-wrapper">

  <img
    src={user?.dp || "/default-avatar.png"}
    alt="Profile"
    className="profile-big-img"
  />

  {isEditing && (
    <>
      <label className="upload-icon">
        +
        <input
          type="file"
          hidden
          accept="image/*"
          onChange={handleImageUpload}
        />
      </label>

      {user?.dp && (
        <button
          className="delete-icon"
          onClick={handleDeleteImage}
        >
          ×
        </button>
      )}
    </>
  )}

</div>

        <div className="profile-fields">
          <div className="field">
            <label>Name</label>
            <input
              value={name}
              disabled={!isEditing}
              onChange={(e) => setName(e.target.value)}
            />
          </div>

          <div className="field">
            <label>Email</label>
            <input value={user?.email} disabled />
          </div>

          <div className="field">
            <label>Account No</label>
            <input value={user?.accno} disabled />
          </div>
        </div>

        {!isEditing ? (
          <button className="primary-btn" onClick={() => setIsEditing(true)}>
            Edit Profile
          </button>
        ) : (
          <button className="primary-btn" onClick={handleSave}>
            Save Changes
          </button>
        )}

      </div>
    </div>
  );
};

export default ProfileModal;