import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ShieldLogo from "../components/ShieldLogo";

export default function Splash() {
  const navigate = useNavigate();

  useEffect(() => {
    const timer = setTimeout(() => navigate("/login", { replace: true }), 1500);
    return () => clearTimeout(timer);
  }, [navigate]);

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-white">
      <ShieldLogo size={72} animated />
      <h1 className="fade-in-up mt-4 text-2xl font-semibold text-slate-800">
        AI Scam Shield
      </h1>
      <p className="fade-in-up mt-1 text-sm text-slate-500">Stay a step ahead of scams.</p>
    </div>
  );
}
