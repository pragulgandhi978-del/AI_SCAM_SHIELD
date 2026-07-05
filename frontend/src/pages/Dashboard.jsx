import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import Spinner from "../components/Spinner";
import RiskBadge from "../components/RiskBadge";
import { getDashboard } from "../services/api";
import { useAuth } from "../context/AuthContext";

function StatCard({ label, value, accent }) {
  return (
    <div className="card">
      <p className="text-sm font-medium text-slate-500">{label}</p>
      <p className={`mt-2 text-3xl font-semibold ${accent || "text-slate-800"}`}>{value}</p>
    </div>
  );
}

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    getDashboard()
      .then((data) => active && setStats(data))
      .catch((err) => active && setError(err.message))
      .finally(() => active && setLoading(false));
    return () => {
      active = false;
    };
  }, []);

  return (
    <div className="min-h-screen bg-slate-50">
      <Navbar />
      <main className="mx-auto max-w-6xl px-4 py-8 sm:px-6">
        <h1 className="text-2xl font-semibold text-slate-800">
          Welcome{user?.name ? `, ${user.name.split(" ")[0]}` : ""}
        </h1>
        <p className="mt-1 text-slate-500">Here's what's happening with your scans.</p>

        {loading && (
          <div className="mt-10 flex justify-center">
            <Spinner size={32} className="text-teal-500" />
          </div>
        )}

        {!loading && error && (
          <div className="mt-6 rounded-xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
        )}

        {!loading && !error && stats && (
          <>
            <div className="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
              <StatCard label="Total Scans" value={stats.totalScans ?? 0} />
              <StatCard label="Safe Messages" value={stats.safeCount ?? 0} accent="text-green-600" />
              <StatCard label="Scam Messages" value={stats.scamCount ?? 0} accent="text-red-600" />
              <div className="card flex flex-col justify-center">
                <p className="text-sm font-medium text-slate-500">Recent Risk Level</p>
                <div className="mt-2">
                  {stats.recentRiskLevel ? (
                    <RiskBadge level={stats.recentRiskLevel} />
                  ) : (
                    <span className="text-sm text-slate-400">No scans yet</span>
                  )}
                </div>
              </div>
            </div>

            <div className="mt-8 flex flex-wrap gap-3">
              <Link to="/scan-message" className="btn-primary">Scan a message</Link>
              <Link to="/scan-url" className="btn-secondary">Check a URL</Link>
              <Link to="/report-scam" className="btn-secondary">Report a scam</Link>
            </div>
          </>
        )}
      </main>
    </div>
  );
}
