import { useEffect, useState } from "react";
import api from "../services/api";
import "../styles/dashboard.css";
import DepositModal from "../components/DepositModal";
import WithdrawModal from "../components/WithdrawModal";
import TransferModal from "../components/TransferModal";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const Dashboard = ({ showToast }) => {
  const [profile, setProfile] = useState(null);
  const [showDeposit, setShowDeposit] = useState(false);
  const [showWithdraw, setShowWithdraw] = useState(false);
  const [showTransfer, setShowTransfer] = useState(false);
  const navigate = useNavigate();
  const { token, role } = useAuth();

  useEffect(() => {
    if (!token) return;

    if (role === "ADMIN") {
      navigate("/admin");
      return;
    }

    fetchProfile();
  }, [token, role]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchProfile = async () => {
    try {
      const res = await api.get("/auth/profile");
      setProfile(res.data);
    } catch (err) {
      setError("Failed to load profile");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;
  if (role === "ADMIN") return null;
  return (
    <div className="dashboard-container">
      <div className="dashboard-wrapper">

        <div className="balance-card">
          <h2>Current Balance</h2>
          <div className="balance-amount">
            ₹ {profile ? profile.balance : "..."}
          </div>
        </div>

        <div className="action-card" onClick={() => setShowDeposit(true)}>
          <h3>Deposit</h3>
          <p>Add money to your account</p>
        </div>

        <div className="action-card" onClick={() => setShowWithdraw(true)}>
          <h3>Withdraw</h3>
          <p>Withdraw funds securely</p>
        </div>

        <div className="action-card" onClick={() => setShowTransfer(true)}>
          <h3>Transfer</h3>
          <p>Send money to another account</p>
        </div>

        <div className="action-card" onClick={() => navigate("/transactions")}>
          <h3>Transactions</h3>
          <p>View your transaction history</p>
        </div>

      </div>
      {showDeposit && (
        <DepositModal
          close={() => setShowDeposit(false)}
          refresh={fetchProfile}
          accno={profile?.accno}
          showToast={showToast}
        />
      )}
      {showWithdraw && (
        <WithdrawModal
          close={() => setShowWithdraw(false)}
          refresh={fetchProfile}
          accno={profile?.accno}
          showToast={showToast}
        />
      )}
      {showTransfer && (
        <TransferModal
          close={() => setShowTransfer(false)}
          refresh={fetchProfile}
          accno={profile?.accno}
          showToast={showToast}
        />
      )}
    </div>
  );
};

export default Dashboard;