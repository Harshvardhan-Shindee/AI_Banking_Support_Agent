import AdminHeader from "./AdminHeader";
import { Outlet } from "react-router-dom";

const AdminLayout = () => {
  return (
    <>
      <AdminHeader />
      <div className="layout-content">
        <Outlet />
      </div>
    </>
  );
};

export default AdminLayout;