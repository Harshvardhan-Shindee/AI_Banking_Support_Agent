import { useEffect, useState } from "react";
import api from "../services/api";
import "../styles/admin.css";

const AdminTickets = () => {

  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);

  const [filter, setFilter] = useState("ALL");
  const [search, setSearch] = useState("");

  // ================= FETCH =================
  const fetchTickets = async () => {
    try {
      const res = await api.get("/admin/tickets");
      setTickets(res.data);
    } catch (err) {
      console.error("Ticket error:", err);
    } finally {
      setLoading(false);
    }
  };

  // ================= RESOLVE =================
  const resolveTicket = async (id) => {
    try {
      await api.put(`/admin/tickets/resolve/${id}`);
      fetchTickets();
      alert("✅ Ticket resolved");
    } catch (err) {
      console.error("Resolve error:", err);
      alert("❌ Failed to resolve ticket");
    }
  };

  useEffect(() => {
    fetchTickets();
  }, []);

  // ================= FILTER + SEARCH =================
  const filteredTickets = tickets.filter((t) => {

    const matchesFilter =
      filter === "ALL" || t.status === filter;

    const matchesSearch =
      t.issue.toLowerCase().includes(search.toLowerCase()) ||
      String(t.userId).includes(search);

    return matchesFilter && matchesSearch;
  });

  if (loading) return <div className="loading">Loading tickets...</div>;

  return (
    <div className="container">

      {/* ================= HEADER ================= */}
      <div className="header-row">
        <h2>Support Tickets</h2>
      </div>

      {/* ================= SEARCH ================= */}
      <input
        className="search-bar"
        placeholder="Search by user ID or issue..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      {/* ================= FILTER ================= */}
      <div style={{ marginBottom: "15px" }}>
        <div className="ticket-filters">
  <button
    className={`btn ${filter === "ALL" ? "filter-active" : ""}`}
    onClick={() => setFilter("ALL")}
  >
    All
  </button>

  <button
    className={`btn ${filter === "OPEN" ? "filter-active" : ""}`}
    onClick={() => setFilter("OPEN")}
  >
    Open
  </button>

  <button
    className={`btn ${filter === "RESOLVED" ? "filter-active" : ""}`}
    onClick={() => setFilter("RESOLVED")}
  >
    Resolved
  </button>
</div>
      </div>

      {/* ================= TABLE ================= */}
      <div className="table-wrapper">

        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>User ID</th>
              <th>Issue</th>
              <th>Status</th>
              <th>Date</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>

            {filteredTickets.map((t) => (
              <tr key={t.id}>
                <td>{t.id}</td>
                <td>{t.userId}</td>
                <td>{t.issue}</td>

                {/* STATUS */}
                <td>
                  <span className={`status ${t.status.toLowerCase()}`}>
                    {t.status}
                  </span>
                </td>

                {/* DATE */}
                <td>
                  {t.createdAt
                    ? new Date(t.createdAt).toLocaleString()
                    : "—"}
                </td>

                {/* ACTION */}
                <td>
                  {t.status === "OPEN" ? (
                    <button
                      className="btn success"
                      onClick={() => resolveTicket(t.id)}
                    >
                      Resolve
                    </button>
                  ) : (
                    <span style={{ color: "#888", fontSize: "13px" }}>
                      Done
                    </span>
                  )}
                </td>

              </tr>
            ))}

          </tbody>
        </table>

        {/* EMPTY STATE */}
        {filteredTickets.length === 0 && (
          <div className="no-data">No matching tickets</div>
        )}

      </div>

    </div>
  );
};

export default AdminTickets;