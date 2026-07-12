import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "../styles/admin.css";

const AdminTransactions = () => {

  const { accno } = useParams();
  const navigate = useNavigate();
  const [transactions, setTransactions] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // 🔥 FETCH
  const fetchTransactions = async () => {
    try {
      const res = await api.get(`/transactions/user/${accno}`, {
        params: { page, size: 5 }
      });

      setTransactions(res.data.content || []);
      setTotalPages(res.data.totalPages || 0);

    } catch (err) {
      console.error("ERROR:", err);
    }
  };

  useEffect(() => {
    fetchTransactions();
  }, [accno, page]);

  // 🔥 DETAILS LOGIC
  const getDetails = (t) => {
    if (t.type === "DEPOSIT") return "Self Deposit";
    if (t.type === "WITHDRAW") return "Self Withdrawal";

    if (t.type === "TRANSFER") {
      if (t.senderAccno == accno) {
        return `To: ${t.receiverAccno}`;
      } else {
        return `From: ${t.senderAccno}`;
      }
    }

    return "-";
  };

  // 🔥 AMOUNT LOGIC
  const getAmount = (t) => {
    if (t.type === "DEPOSIT") return { text: `+ ₹${t.amount}`, cls: "credit" };
    if (t.type === "WITHDRAW") return { text: `- ₹${t.amount}`, cls: "debit" };

    if (t.type === "TRANSFER") {
      if (t.senderAccno == accno) {
        return { text: `- ₹${t.amount}`, cls: "debit" };
      } else {
        return { text: `+ ₹${t.amount}`, cls: "credit" };
      }
    }
  };

  return (
    <div className="container">

      <div className="header-row">
  <h2>User Transactions (Acc: {accno})</h2>

  <button
    className="back-btn"
    onClick={() => navigate(-1)}
  >
    ← Back
  </button>
</div>

      <div className="table-wrapper">

        {transactions.length === 0 ? (
          <p className="no-data">No transactions found</p>
        ) : (

          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Amount</th>
                <th>Details</th>
                <th>Date</th>
              </tr>
            </thead>

            <tbody>
              {transactions.map((t) => {
                const amt = getAmount(t);

                return (
                  <tr key={t.transId}>
                    <td>{t.transId}</td>
                    <td>{t.type}</td>

                    {/* 🔥 COLOR BASED AMOUNT */}
                    <td className={amt.cls}>
                      {amt.text}
                    </td>

                    {/* 🔥 DETAILS */}
                    <td>{getDetails(t)}</td>

                    <td>
                      {new Date(t.dateTime).toLocaleString()}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>

        )}
      </div>

      {/* 🔥 PAGINATION */}
      <div className="pagination">

        <button
          disabled={page === 0}
          onClick={() => setPage(page - 1)}
        >
          Prev
        </button>

        <span>Page {page + 1} of {totalPages}</span>

        <button
          disabled={page + 1 >= totalPages}
          onClick={() => setPage(page + 1)}
        >
          Next
        </button>

      </div>

    </div>
  );
};

export default AdminTransactions;