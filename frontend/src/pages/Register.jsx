import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import ShieldLogo from "../components/ShieldLogo";
import Spinner from "../components/Spinner";
import { registerUser } from "../services/api";
import { useAuth } from "../context/AuthContext";

export default function Register() {
  const [form, setForm] = useState({ name: "", email: "", password: "", confirm: "" });
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  function update(field) {
    return (e) => setForm((f) => ({ ...f, [field]: e.target.value }));
  }

  function validate() {
    const next = {};
    if (!form.name.trim()) next.name = "Enter your full name.";
    if (!form.email.trim()) next.email = "Enter your email address.";
    else if (!/^\S+@\S+\.\S+$/.test(form.email)) next.email = "Enter a valid email address.";
    if (!form.password) next.password = "Choose a password.";
    else if (form.password.length < 6) next.password = "Password must be at least 6 characters.";
    if (form.confirm !== form.password) next.confirm = "Passwords don't match.";
    setErrors(next);
    return Object.keys(next).length === 0;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setServerError("");
    if (!validate()) return;

    setLoading(true);
    try {
      const data = await registerUser({
        name: form.name.trim(),
        email: form.email.trim(),
        password: form.password,
      });
      login(data.token, data.user);
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setServerError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-50 px-4 py-10">
      <div className="w-full max-w-sm">
        <div className="mb-6 flex flex-col items-center">
          <ShieldLogo size={44} />
          <h1 className="mt-3 text-xl font-semibold text-slate-800">Create your account</h1>
          <p className="text-sm text-slate-500">Start scanning messages and links safely.</p>
        </div>

        <form onSubmit={handleSubmit} noValidate className="card">
          {serverError && (
            <div className="mb-4 rounded-xl bg-red-50 px-4 py-2.5 text-sm text-red-700">
              {serverError}
            </div>
          )}

          <div className="mb-4">
            <label className="field-label" htmlFor="name">Full name</label>
            <input
              id="name"
              className="field-input"
              value={form.name}
              onChange={update("name")}
              placeholder="Jane Doe"
              autoComplete="name"
            />
            {errors.name && <p className="field-error">{errors.name}</p>}
          </div>

          <div className="mb-4">
            <label className="field-label" htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              className="field-input"
              value={form.email}
              onChange={update("email")}
              placeholder="you@example.com"
              autoComplete="email"
            />
            {errors.email && <p className="field-error">{errors.email}</p>}
          </div>

          <div className="mb-4">
            <label className="field-label" htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              className="field-input"
              value={form.password}
              onChange={update("password")}
              placeholder="At least 6 characters"
              autoComplete="new-password"
            />
            {errors.password && <p className="field-error">{errors.password}</p>}
          </div>

          <div className="mb-5">
            <label className="field-label" htmlFor="confirm">Confirm password</label>
            <input
              id="confirm"
              type="password"
              className="field-input"
              value={form.confirm}
              onChange={update("confirm")}
              placeholder="Re-enter your password"
              autoComplete="new-password"
            />
            {errors.confirm && <p className="field-error">{errors.confirm}</p>}
          </div>

          <button type="submit" disabled={loading} className="btn-primary w-full">
            {loading ? <Spinner className="text-white" /> : "Create account"}
          </button>
        </form>

        <p className="mt-4 text-center text-sm text-slate-500">
          Already have an account?{" "}
          <Link to="/login" className="font-medium text-teal-600 hover:text-teal-700">
            Log in
          </Link>
        </p>
      </div>
    </div>
  );
}
