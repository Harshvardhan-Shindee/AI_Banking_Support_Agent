import { useEffect, useState } from "react";
import api from "../services/api";
import "../styles/admin.css";

const AdminInactive = ({ showToast }) => {

    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const fetchUsers = async () => {
        try {
            const res = await api.get("/admin/inactive-users", {
                params: { page, size: 5 }
            });

            setUsers(res.data.content || []);
            setTotalPages(res.data.totalPages || 0);

        } catch (err) {
            console.error("INACTIVE ERROR:", err);
            showToast("Failed to load users", "error");
        }
    };

    useEffect(() => {
        fetchUsers();
    }, [page]);

    // 🔥 ACTION HANDLERS

    const handleUnblock = async (accno) => {
        await api.put(`/admin/unblock/${accno}`);
        showToast("User Unblocked");
        fetchUsers();
    };

    const handleApprove = async (accno) => {
        await api.put(`/admin/approve/${accno}`);
        showToast("User Approved");
        fetchUsers();
    };

    return (
        <div className="container">

            <h2>Blocked & Rejected Users</h2>

            <div className="table-wrapper">

                {users.length === 0 ? (
                    <p className="no-data">No users found</p>
                ) : (

                    <table className="table">
                        <thead>
                            <tr>
                                <th>Acc No</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            {users.map((u) => (
                                <tr key={u.accno}>
                                    <td>{u.accno}</td>
                                    <td>{u.custname}</td>
                                    <td>{u.email}</td>

                                    <td>
                                        <span className={`status ${u.status.toLowerCase()}`}>
                                            {u.status}
                                        </span>
                                    </td>

                                    <td>

                                        {u.status === "BLOCKED" && (
                                            <button
                                                className="btn success"
                                                onClick={() => handleUnblock(u.accno)}
                                            >
                                                Unblock
                                            </button>
                                        )}

                                        {u.status === "REJECTED" && (
                                            <button
                                                className="btn primary"
                                                onClick={() => handleApprove(u.accno)}
                                            >
                                                Approve
                                            </button>
                                        )}

                                    </td>

                                </tr>
                            ))}
                        </tbody>
                    </table>

                )}
            </div>

            <div className="pagination">
                <button disabled={page === 0} onClick={() => setPage(page - 1)}>
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

export default AdminInactive;