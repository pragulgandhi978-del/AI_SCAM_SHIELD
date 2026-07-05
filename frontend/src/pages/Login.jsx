import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import ShieldLogo from "../components/ShieldLogo";
import Spinner from "../components/Spinner";
import { loginUser } from "../services/api";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  function validate() {
    const next = {};
    if (!email.trim()) next.email = "Enter your email address.";
    else if (!/^\S+@\S+\.\S+$/.test(email)) next.email = "Enter a valid email address.";
    if (!password) next.password = "Enter your password.";
    setErrors(next);
    return Object.keys(next).length === 0;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setServerError("");
    if (!validate()) return;

    setLoading(true);
    try {
      const data = await loginUser({ email: email.trim(), password });
      login(data.token, data.user);
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setServerError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-50 px-4">
      <div className="w-full max-w-sm">
        <div className="mb-6 flex flex-col items-center">
          <ShieldLogo size={44} />
          <h1 className="mt-3 text-xl font-semibold text-slate-800">Welcome back</h1>
          <p className="text-sm text-slate-500">Log in to check your latest scans.</p>
        </div>

        <form onSubmit={handleSubmit} noValidate className="card">
          {serverError && (
            <div className="mb-4 rounded-xl bg-red-50 px-4 py-2.5 text-sm text-red-700">
              {serverError}
            </div>
          )}

          <div className="mb-4">
            <label className="field-label" htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              className="field-input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="you@example.com"
              autoComplete="email"
            />
            {errors.email && <p className="field-error">{errors.email}</p>}
          </div>

          <div className="mb-5">
            <label className="field-label" htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              className="field-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              autoComplete="current-password"
            />
            {errors.password && <p className="field-error">{errors.password}</p>}
          </div>

          <button type="submit" disabled={loading} className="btn-primary w-full">
            {loading ? <Spinner className="text-white" /> : "Log in"}
          </button>
        </form>

        <p className="mt-4 text-center text-sm text-slate-500">
          Don't have an account?{" "}
          <Link to="/register" className="font-medium text-teal-600 hover:text-teal-700">
            Create one
          </Link>
        </p>
      </div>
    </div>
  );
}
