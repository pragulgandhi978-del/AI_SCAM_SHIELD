import { useState } from "react";
import Navbar from "../components/Navbar";
import Spinner from "../components/Spinner";
import { reportScam } from "../services/api";

const CATEGORIES = ["Phishing", "Fake Job", "Lottery Scam", "Other"];

export default function ReportScam() {
  const [contentText, setContentText] = useState("");
  const [category, setCategory] = useState(CATEGORIES[0]);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSuccess(false);

    if (!contentText.trim()) {
      setError("Paste the scam message, email, or URL you want to report.");
      return;
    }

    setLoading(true);
    try {
      await reportScam({ contentText: contentText.trim(), category });
      setSuccess(true);
      setContentText("");
      setCategory(CATEGORIES[0]);
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
        <h1 className="text-2xl font-semibold text-slate-800">Report a scam</h1>
        <p className="mt-1 text-slate-500">
          Found something we missed? Share it and help protect other users.
        </p>

        <form onSubmit={handleSubmit} className="card mt-6">
          {success && (
            <div className="mb-4 rounded-xl bg-green-50 px-4 py-2.5 text-sm text-green-700">
              Report submitted successfully. Thank you for helping the community.
            </div>
          )}
          {error && (
            <div className="mb-4 rounded-xl bg-red-50 px-4 py-2.5 text-sm text-red-700">
              {error}
            </div>
          )}

          <label className="field-label" htmlFor="category">Category</label>
          <select
            id="category"
            className="field-input mb-4"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
          >
            {CATEGORIES.map((c) => (
              <option key={c} value={c}>{c}</option>
            ))}
          </select>

          <label className="field-label" htmlFor="content">Message, email, or URL</label>
          <textarea
            id="content"
            rows={6}
            className="field-input resize-none"
            placeholder="Paste the scam content here..."
            value={contentText}
            onChange={(e) => setContentText(e.target.value)}
          />

          <button type="submit" disabled={loading} className="btn-primary mt-4 w-full sm:w-auto">
            {loading ? <Spinner className="text-white" /> : "Submit report"}
          </button>
        </form>
      </main>
    </div>
  );
}
