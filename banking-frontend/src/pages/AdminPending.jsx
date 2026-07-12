import React, { useEffect, useState } from "react";
import axios from "axios";
import "../styles/admin.css";

const AdminPending = () => {
  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
const token = localStorage.getItem("token");

  const fetchPending = async () => {
    try {
      console.log("CALLING API...");

      const res = await axios.get(
  `http://localhost:8001/api/admin/pending?page=${page}&size=5`,
  {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  }
);
      console.log("DATA:", res.data);

      setUsers(res.data.content);
      setTotalPages(res.data.totalPages);

    } catch (err) {
      console.error("ERROR:", err);
    }
  };

  const approveUser = async (accno) => {
  try {
    await axios.put(
      `http://localhost:8001/api/admin/approve/${accno}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    // 🔥 remove user instantly from UI
    setUsers((prev) => prev.filter((u) => u.accno !== accno));

    showToast("User Approved ✅");

  } catch (err) {
    console.error(err);
    showToast("Approve failed ❌", "error");
  }
};


const rejectUser = async (accno) => {
  try {
    await axios.put(
      `http://localhost:8001/api/admin/reject/${accno}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    // 🔥 remove user instantly
    setUsers((prev) => prev.filter((u) => u.accno !== accno));

    showToast("User Rejected ❌");

  } catch (err) {
    console.error(err);
    showToast("Reject failed ❌", "error");
  }
};

  useEffect(() => {
    fetchPending();
  }, [page]);

  return (
   <div className="container">
  <h2>Pending Users</h2>

  <div className="table-wrapper">
    {users.length === 0 ? (
      <p className="no-data">No pending users</p>
    ) : (
      <table className="table">
        <thead>
          <tr>
            <th>DP</th>
            <th>Acc No</th>
            <th>Name</th>
            <th>Email</th>
            <th>Type</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {users.map((u) => (
            <tr key={u.accno}>
              <td>
                {u.dp ? (
                  <img src={u.dp} className="dp" />
                ) : (
                  <div className="dp-placeholder">
                    {u.custname[0]}
                  </div>
                )}
              </td>

              <td>{u.accno}</td>
              <td>{u.custname}</td>
              <td>{u.email}</td>
              <td>{u.acctype}</td>

              <td>
                <button
                  className="btn success"
                  onClick={() => approveUser(u.accno)}
                >
                  Approve
                </button>

                <button
                  className="btn danger"
                  onClick={() => rejectUser(u.accno)}
                >
                  Reject
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    )}
  </div>

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

export default AdminPending;