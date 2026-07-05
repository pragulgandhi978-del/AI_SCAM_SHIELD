import { useState } from "react";
import Navbar from "../components/Navbar";
import Spinner from "../components/Spinner";
import RiskBadge from "../components/RiskBadge";
import { scanUrl } from "../services/api";

function isValidUrl(value) {
  try {
    const url = new URL(value);
    return url.protocol === "http:" || url.protocol === "https:";
  } catch {
    return false;
  }
}

export default function ScanUrl() {
  const [url, setUrl] = useState("");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleScan(e) {
    e.preventDefault();
    setError("");
    setResult(null);

    const trimmed = url.trim();
    if (!trimmed) {
      setError("Enter a URL to check.");
      return;
    }
    if (!isValidUrl(trimmed)) {
      setError("Enter a valid URL, starting with http:// or https://");
      return;
    }

    setLoading(true);
    try {
      const data = await scanUrl(trimmed);
      setResult(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <Navbar />
      <main className="mx-auto max-w-2xl px-4 py-8 sm:px-6">
        <h1 className="text-2xl font-semibold text-slate-800">Check a URL</h1>
        <p className="mt-1 text-slate-500">
          Paste a link before you click it. We'll check it against known malicious sites.
        </p>

        <form onSubmit={handleScan} className="card mt-6">
          <label className="field-label" htmlFor="url">Website URL</label>
          <input
            id="url"
            type="text"
            className="field-input"
            placeholder="https://example.com"
            value={url}
            onChange={(e) => setUrl(e.target.value)}
          />

          {error && (
            <div className="mt-3 rounded-xl bg-red-50 px-4 py-2.5 text-sm text-red-700">
              {error}
            </div>
          )}

          <button type="submit" disabled={loading} className="btn-primary mt-4 w-full sm:w-auto">
            {loading ? <Spinner className="text-white" /> : "Check URL"}
          </button>
        </form>

        {result && (
          <div className="card mt-6 flex items-center justify-between">
            <span className="text-sm text-slate-600">Result for this link</span>
            <RiskBadge level={result.status} />
          </div>
        )}
      </main>
    </div>
  );
}
