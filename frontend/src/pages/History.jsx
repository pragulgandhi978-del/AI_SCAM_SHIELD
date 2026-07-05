import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import Spinner from "../components/Spinner";
import RiskBadge from "../components/RiskBadge";
import { getHistory } from "../services/api";

export default function History() {
  const [page, setPage] = useState(0);
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    setLoading(true);
    getHistory(page, 10)
      .then((res) => active && setData(res))
      .catch((err) => active && setError(err.message))
      .finally(() => active && setLoading(false));
    return () => {
      active = false;
    };
  }, [page]);

  const items = data?.content || [];
  const totalPages = data?.totalPages ?? 0;

  return (
    <div className="min-h-screen bg-slate-50">
      <Navbar />
      <main className="mx-auto max-w-3xl px-4 py-8 sm:px-6">
        <h1 className="text-2xl font-semibold text-slate-800">Scan history</h1>
        <p className="mt-1 text-slate-500">Everything you've scanned, in one place.</p>

        {loading && (
          <div className="mt-10 flex justify-center">
            <Spinner size={32} className="text-teal-500" />
          </div>
        )}

        {!loading && error && (
          <div className="mt-6 rounded-xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
        )}

        {!loading && !error && items.length === 0 && (
          <div className="card mt-6 text-center">
            <p className="text-slate-600">No scans yet.</p>
            <p className="mt-1 text-sm text-slate-400">
              Scan a message or URL and it'll show up here.
            </p>
          </div>
        )}

        {!loading && !error && items.length > 0 && (
          <div className="mt-6 space-y-3">
            {items.map((item) => (
              <div key={item.id} className="card">
                <div className="flex items-start justify-between gap-4">
                  <div className="min-w-0">
                    <p className="text-xs font-medium uppercase tracking-wide text-slate-400">
                      {item.contentType}
                    </p>
                    <p className="mt-1 truncate text-sm text-slate-700">{item.contentText}</p>
                    {item.explanation && (
                      <p className="mt-1 text-xs text-slate-500">{item.explanation}</p>
                    )}
                  </div>
                  <RiskBadge level={item.riskScore} />
                </div>
                <p className="mt-2 text-xs text-slate-400">
                  {new Date(item.scannedAt).toLocaleString()}
                </p>
              </div>
            ))}
          </div>
        )}

        {!loading && totalPages > 1 && (
          <div className="mt-6 flex items-center justify-center gap-3">
            <button
              className="btn-secondary !px-4 !py-1.5 text-sm"
              disabled={page === 0}
              onClick={() => setPage((p) => Math.max(0, p - 1))}
            >
              Previous
            </button>
            <span className="text-sm text-slate-500">
              Page {page + 1} of {totalPages}
            </span>
            <button
              className="btn-secondary !px-4 !py-1.5 text-sm"
              disabled={page + 1 >= totalPages}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </button>
          </div>
        )}
      </main>
    </div>
  );
}
