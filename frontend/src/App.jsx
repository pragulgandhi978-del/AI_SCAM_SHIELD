import { Routes, Route, Navigate } from "react-router-dom";
import Splash from "./pages/Splash";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import ScanMessage from "./pages/ScanMessage";
import ScanUrl from "./pages/ScanUrl";
import History from "./pages/History";
import ReportScam from "./pages/ReportScam";
import NotFound from "./pages/NotFound";
import ProtectedRoute from "./components/ProtectedRoute";
import { useAuth } from "./context/AuthContext";

function PublicOnlyRoute({ children }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <Navigate to="/dashboard" replace /> : children;
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Splash />} />

      <Route
        path="/login"
        element={
          <PublicOnlyRoute>
            <Login />
          </PublicOnlyRoute>
        }
      />
      <Route
        path="/register"
        element={
          <PublicOnlyRoute>
            <Register />
          </PublicOnlyRoute>
        }
      />

      <Route element={<ProtectedRoute />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/scan-message" element={<ScanMessage />} />
        <Route path="/scan-url" element={<ScanUrl />} />
        <Route path="/history" element={<History />} />
        <Route path="/report-scam" element={<ReportScam />} />
      </Route>

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
