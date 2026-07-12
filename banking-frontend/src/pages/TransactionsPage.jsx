import { useEffect, useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";
import "../styles/transactions.css";

const TransactionsPage = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [accno, setAccno] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchProfileAndTransactions();
  }, []);

  const fetchProfileAndTransactions = async () => {
    try {
      const profileRes = await api.get("/auth/profile");
      const userAccno = profileRes.data.accno;
      setAccno(userAccno);

      const txRes = await api.get(`/transactions/${userAccno}`);
      setTransactions(txRes.data);
    } catch (err) {
      console.error("Transaction fetch error:", err.response?.data || err.message);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateTime) => {
    return new Date(dateTime).toLocaleString();
  };

  return (
    <div className="transactions-container">
      <div className="transactions-card">
        <div className="transactions-header">
  <h2>Transaction History</h2>

  <button
    className="back-btn"
    onClick={() => navigate("/dashboard")}
  >
    ← Back
  </button>
</div>
        {loading ? (
          <div className="loader">Loading transactions...</div>
        ) : transactions.length === 0 ? (
          <p>No transactions found.</p>
        ) : (
          <table className="transactions-table">
            <thead>
              <tr>
                <th>Type</th>
                <th>Amount</th>
                <th>Details</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((tx, index) => {
                const isCredit =
                  tx.receiverAccno === accno && tx.type !== "WITHDRAW";

                return (
                  <tr key={index}>
                    <td>{tx.type}</td>
                    <td className={isCredit ? "credit" : "debit"}>
                      {isCredit ? "+" : "-"} ₹{tx.amount}
                    </td>
                    <td>
                      {tx.type === "TRANSFER" && (
                        <>
                          From: {tx.senderAccno} → To: {tx.receiverAccno}
                        </>
                      )}
                      {tx.type === "DEPOSIT" && "Self Deposit"}
                      {tx.type === "WITHDRAW" && "Self Withdrawal"}
                    </td>
                    <td>{formatDate(tx.dateTime)}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default TransactionsPage;