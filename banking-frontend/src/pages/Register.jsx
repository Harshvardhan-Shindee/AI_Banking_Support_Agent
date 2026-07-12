import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/auth.css";

const Register = () => {

  const navigate = useNavigate();

  const [form, setForm] = useState({
  custname: "",
  acctype: "",
  email: "",
  password: "",
  confirmPassword: ""
});
const passwordsMatch = form.password === form.confirmPassword;

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    if (!passwordsMatch) {
  setError("Passwords do not match");
  setLoading(false);
  return;
}

    try {
      await axios.post(
        "http://localhost:8001/api/auth/register",
        form
      );

      alert("Registered successfully! Wait for admin approval.");
      navigate("/login");

    } catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="center-wrapper">
      <div className="auth-card">

        <h2>Create Account</h2>
        <p className="subtitle">Register to start banking securely</p>

        {error && <div className="error-box">{error}</div>}

        <form onSubmit={handleSubmit}>

          <input
            type="text"
            name="custname"
            placeholder="Full Name"
            value={form.custname}
            onChange={handleChange}
            required
          />

          <select
            name="acctype"
            value={form.acctype}
            onChange={handleChange}
            required
          >
            <option value="">Select Account Type</option>
            <option value="SAVINGS">Savings</option>
            <option value="CURRENT">Current</option>
          </select>

          <input
            type="email"
            name="email"
            placeholder="Email address"
            value={form.email}
            onChange={handleChange}
            required
          />

          <input
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            required
          />

          <input
  type="password"
  name="confirmPassword"
  placeholder="Re-enter Password"
  value={form.confirmPassword}
  onChange={handleChange}
  required
/>
{form.confirmPassword && !passwordsMatch && (
  <div className="error-box">Passwords do not match</div>
)}
          <button type="submit" disabled={loading || !passwordsMatch}>
            {loading ? "Creating..." : "Register"}
          </button>

        </form>

        <div className="auth-footer">
          Already have an account?
          <span onClick={() => navigate("/login")}>
            Login
          </span>
        </div>

      </div>
    </div>
  );
};

export default Register;