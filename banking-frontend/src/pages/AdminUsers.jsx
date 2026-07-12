import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "../styles/admin.css";

const AdminUsers = ({ showToast }) => {

  const [keyword, setKeyword] = useState("");
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const navigate = useNavigate();

  // 🔥 FETCH USERS
  const fetchUsers = async () => {
    try {
      setLoading(true);

      const res = await api.get("/admin/all-users", {
        params: {
          page,
          size: 5,
          keyword   // ✅ FIXED
        }
      });

      setUsers(res.data.content || []);
      setTotalPages(res.data.totalPages || 0);

    } catch (err) {
      console.error(err);
      showToast("Failed to load users", "error");
    } finally {
      setLoading(false);
    }
  };

  // 🔍 SEARCH + PAGINATION
  useEffect(() => {
    const delay = setTimeout(() => {
      fetchUsers();
    }, 400);

    return () => clearTimeout(delay);

  }, [keyword, page]); // ✅ FIXED

  // 🔥 RESET PAGE ON SEARCH
  useEffect(() => {
    setPage(0);
  }, [keyword]);

  // 🔥 BLOCK / UNBLOCK (INSTANT UI UPDATE)
  const toggleBlock = async (user) => {
    try {

      const url =
        user.status === "BLOCKED"
          ? `/admin/unblock/${user.accno}`
          : `/admin/block/${user.accno}`;

      await api.put(url);

      // ✅ INSTANT UI CHANGE (NO REFETCH)
      setUsers((prev) =>
        prev.map((u) =>
          u.accno === user.accno
            ? {
                ...u,
                status: user.status === "BLOCKED" ? "ACTIVE" : "BLOCKED",
              }
            : u
        )
      );

      showToast(
        user.status === "BLOCKED"
          ? "User Unblocked"
          : "User Blocked"
      );

    } catch (err) {
      console.error(err);
      showToast("Action failed", "error");
    }
  };

  // 📊 TRANSACTIONS
  const handleTransactions = (accno) => {
    navigate(`/admin/transactions/${accno}`);
  };

  return (
    <div className="container">

      <h2>All Users</h2>

      {/* 🔍 SEARCH */}
      <input
        className="search-bar"
        placeholder="Search by name, email or account no..."
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
      />

      <div className="table-wrapper">

        {loading ? (
          <div className="loading">Loading users...</div>
        ) : (
          <table className="table">

            <thead>
              <tr>
                <th>DP</th>
                <th>Acc No</th>
                <th>Name</th>
                <th>Email</th>
                <th>Type</th>
                <th>Balance</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>

              {users.length === 0 ? (
                <tr>
                  <td colSpan="8" className="no-data">
                    No users found
                  </td>
                </tr>
              ) : (

                users.map((u) => (
                  <tr key={u.accno}>

                    <td>
                      {u.dp ? (
                        <img
                          src={u.dp}
                          alt="dp"
                          className="dp"
                          onError={(e) => {
                            e.target.style.display = "none";
                          }}
                        />
                      ) : (
                        <div className="dp-placeholder">
                          {u.custname?.charAt(0)?.toUpperCase() || "U"}
                        </div>
                      )}
                    </td>

                    <td>{u.accno}</td>
                    <td>{u.custname}</td>
                    <td>{u.email}</td>
                    <td>{u.acctype}</td>
                    <td>₹ {u.balance}</td>

                    <td>
                      <span className={`status ${u.status?.toLowerCase()}`}>
                        {u.status}
                      </span>
                    </td>

                    <td className="action-btns">

                      {/* 🔥 SINGLE BUTTON TOGGLE */}
                      <button
                        className={`btn ${
                          u.status === "BLOCKED" ? "success" : "danger"
                        }`}
                        onClick={() => toggleBlock(u)}
                      >
                        {u.status === "BLOCKED" ? "Unblock" : "Block"}
                      </button>

                      <button
                        className="btn primary"
                        onClick={() => handleTransactions(u.accno)}
                      >
                        Transactions
                      </button>

                    </td>

                  </tr>
                ))

              )}

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

export default AdminUsers;