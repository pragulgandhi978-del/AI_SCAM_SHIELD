const STYLES = {
  Low: "bg-green-50 text-green-700 border-green-200",
  Safe: "bg-green-50 text-green-700 border-green-200",
  Medium: "bg-yellow-50 text-yellow-700 border-yellow-200",
  High: "bg-red-50 text-red-700 border-red-200",
  Malicious: "bg-red-50 text-red-700 border-red-200",
};

export default function RiskBadge({ level }) {
  const style = STYLES[level] || "bg-slate-100 text-slate-600 border-slate-200";
  return (
    <span
      className={`inline-flex items-center rounded-full border px-3 py-1 text-sm font-semibold ${style}`}
    >
      {level}
    </span>
  );
}
