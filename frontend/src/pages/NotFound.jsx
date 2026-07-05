import { Link } from "react-router-dom";
import ShieldLogo from "../components/ShieldLogo";

export default function NotFound() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-white px-4 text-center">
      <ShieldLogo size={44} />
      <h1 className="mt-4 text-3xl font-semibold text-slate-800">Page not found</h1>
      <p className="mt-2 text-slate-500">The page you're looking for doesn't exist.</p>
      <Link to="/dashboard" className="btn-primary mt-6">
        Back to dashboard
      </Link>
    </div>
  );
}
