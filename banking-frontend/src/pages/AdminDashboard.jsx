import { useEffect, useState } from "react";
import api from "../services/api";
import "../styles/admin.css";

const AdminDashboard = () => {

  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchStats = async () => {
    try {
      const res = await api.get("/admin/stats");
      console.log("STATS:", res.data);
      setStats(res.data);
    } catch (err) {
      console.error("Stats error:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStats();
  }, []);

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="container">

      <h2>Admin Dashboard</h2>

      <div className="cards">

        <div className="card">
          <h3>Total Users</h3>
          <p>{stats?.totalUsers}</p>
        </div>

        <div className="card">
          <h3>Active Users</h3>
          <p>{stats?.activeUsers}</p>
        </div>

        <div className="card">
          <h3>Pending Users</h3>
          <p>{stats?.pendingUsers}</p>
        </div>

        <div className="card">
          <h3>Total Transactions</h3>
          <p>{stats?.totalTransactions}</p>
        </div>

        <div className="card">
          <h3>Today's Transactions</h3>
          <p>{stats?.todayTransactions}</p>
        </div>

        <div className="card">
          <h3>Blocked Users</h3>
          <p>{stats?.blockedUsers}</p>
        </div>

        <div className="card">
          <h3>Rejected Users</h3>
          <p>{stats?.rejectedUsers}</p>
        </div>

        <div className="card">
          <h3>Total Balance</h3>
          <p>₹ {stats?.totalBalance}</p>
        </div>

      </div>

      {/* 🔥 RECENT TRANSACTIONS */}
      <div className="table-wrapper">
        <h3>Recent Transactions</h3>

        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Type</th>
              <th>Amount</th>
              <th>Date</th>
            </tr>
          </thead>

          <tbody>
            {stats?.recentTransactions?.map((t) => (
              <tr key={t.transId}>
                <td>{t.transId}</td>
                <td>{t.type}</td>
                <td>₹ {t.amount}</td>
                <td>{t.dateTime}</td>
              </tr>
            ))}
          </tbody>
        </table>

      </div>

    </div>
  );
};

export default AdminDashboard;