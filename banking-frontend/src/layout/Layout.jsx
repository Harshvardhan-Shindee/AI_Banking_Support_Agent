import Header from "./Header";
import Footer from "./Footer";
import { Outlet } from "react-router-dom";
import "../styles/layout.css";

const Layout = () => {
  return (
    <div className="layout-wrapper">
      <Header />
      <main className="layout-content">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default Layout;