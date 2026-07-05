import { NavLink, useNavigate } from "react-router-dom";
import ShieldLogo from "./ShieldLogo";
import { useAuth } from "../context/AuthContext";

const links = [
  { to: "/dashboard", label: "Dashboard" },
  { to: "/scan-message", label: "Scan Message" },
  { to: "/scan-url", label: "Scan URL" },
  { to: "/history", label: "History" },
  { to: "/report-scam", label: "Report Scam" },
];

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate("/login", { replace: true });
  }

  return (
    <header className="border-b border-slate-100 bg-white">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-3 sm:px-6">
        <div className="flex items-center gap-2">
          <ShieldLogo size={28} />
          <span className="text-lg font-semibold text-slate-800">AI Scam Shield</span>
        </div>

        <nav className="hidden items-center gap-1 md:flex">
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className={({ isActive }) =>
                `rounded-xl px-3 py-2 text-sm font-medium transition ${
                  isActive
                    ? "bg-teal-50 text-teal-700"
                    : "text-slate-600 hover:bg-slate-50 hover:text-slate-800"
                }`
              }
            >
              {link.label}
            </NavLink>
          ))}
        </nav>

        <div className="flex items-center gap-3">
          <span className="hidden text-sm text-slate-500 sm:inline">{user?.name}</span>
          <button onClick={handleLogout} className="btn-secondary !px-4 !py-1.5 text-sm">
            Log out
          </button>
        </div>
      </div>

      {/* Mobile nav */}
      <nav className="flex gap-1 overflow-x-auto border-t border-slate-100 px-4 py-2 md:hidden">
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) =>
              `whitespace-nowrap rounded-xl px-3 py-1.5 text-sm font-medium ${
                isActive ? "bg-teal-50 text-teal-700" : "text-slate-600"
              }`
            }
          >
            {link.label}
          </NavLink>
        ))}
      </nav>
    </header>
  );
}
