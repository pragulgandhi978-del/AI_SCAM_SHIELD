import { useState } from "react";
import Navbar from "../components/Navbar";
import Spinner from "../components/Spinner";
import RiskBadge from "../components/RiskBadge";
import { scanMessage } from "../services/api";

const MAX_LENGTH = 2000;

export default function ScanMessage() {
  const [text, setText] = useState("");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleScan(e) {
    e.preventDefault();
    setError("");
    setResult(null);

    if (!text.trim()) {
      setError("Enter a message to scan.");
      return;
    }

    setLoading(true);
    try {
      const data = await scanMessage(text.trim());
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
        <h1 className="text-2xl font-semibold text-slate-800">Scan a message</h1>
        <p className="mt-1 text-slate-500">
          Paste an SMS or email you're unsure about. We'll tell you how risky it looks and why.
        </p>

        <form onSubmit={handleScan} className="card mt-6">
          <label className="field-label" htmlFor="message">Message text</label>
          <textarea
            id="message"
            rows={6}
            maxLength={MAX_LENGTH}
            className="field-input resize-none"
            placeholder="Congratulations! You won ₹5,00,000. Click here to claim..."
            value={text}
            onChange={(e) => setText(e.target.value)}
          />
          <div className="mt-1 flex items-center justify-between">
            <span className="text-xs text-slate-400">
              {text.length} / {MAX_LENGTH}
            </span>
          </div>

          {error && (
            <div className="mt-3 rounded-xl bg-red-50 px-4 py-2.5 text-sm text-red-700">
              {error}
            </div>
          )}

          <button type="submit" disabled={loading} className="btn-primary mt-4 w-full sm:w-auto">
            {loading ? <Spinner className="text-white" /> : "Scan message"}
          </button>
        </form>

        {result && (
          <div className="card mt-6">
            <div className="flex items-center justify-between">
              <h2 className="font-semibold text-slate-800">Result</h2>
              <RiskBadge level={result.riskLevel} />
            </div>
            <p className="mt-3 text-sm leading-relaxed text-slate-600">{result.explanation}</p>
          </div>
        )}
      </main>
    </div>
  );
}
