import axios from "axios";

const baseURL = import.meta.env.VITE_API_URL || "/api";

const api = axios.create({ baseURL });

// Attach JWT to every outgoing request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("scamshield_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// One place to react to 401s (expired/invalid token) — handled by AuthContext,
// which registers this callback so we avoid a circular import.
let onUnauthorized = () => {};
export function registerUnauthorizedHandler(fn) {
  onUnauthorized = fn;
}

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      onUnauthorized();
    }
    return Promise.reject(error);
  }
);

function extractError(error, fallback) {
  return error.response?.data?.error || fallback;
}

export async function registerUser(payload) {
  try {
    const { data } = await api.post("/register", payload);
    return data;
  } catch (error) {
    throw new Error(extractError(error, "Registration failed. Please try again."));
  }
}

export async function loginUser(payload) {
  try {
    const { data } = await api.post("/login", payload);
    return data;
  } catch (error) {
    throw new Error(extractError(error, "Invalid email or password."));
  }
}

export async function scanMessage(text) {
  try {
    const { data } = await api.post("/scan-message", { text });
    return data;
  } catch (error) {
    if (error.response?.status === 429) {
      throw new Error("You've hit the scan limit for now. Try again in a bit.");
    }
    if (error.response?.status === 503) {
      throw new Error("Scan service is temporarily unavailable. Please try again.");
    }
    throw new Error(extractError(error, "Could not scan this message. Please try again."));
  }
}

export async function scanUrl(url) {
  try {
    const { data } = await api.post("/scan-url", { url });
    return data;
  } catch (error) {
    if (error.response?.status === 429) {
      throw new Error("You've hit the scan limit for now. Try again in a bit.");
    }
    if (error.response?.status === 503) {
      throw new Error("Scan service is temporarily unavailable. Please try again.");
    }
    throw new Error(extractError(error, "Could not check this URL. Please try again."));
  }
}

export async function reportScam(payload) {
  try {
    const { data } = await api.post("/report-scam", payload);
    return data;
  } catch (error) {
    throw new Error(extractError(error, "Could not submit report. Please try again."));
  }
}

export async function getHistory(page = 0, size = 10) {
  try {
    const { data } = await api.get("/history", { params: { page, size } });
    return data;
  } catch (error) {
    throw new Error(extractError(error, "Could not load scan history."));
  }
}

export async function getDashboard() {
  try {
    const { data } = await api.get("/dashboard");
    return data;
  } catch (error) {
    throw new Error(extractError(error, "Could not load dashboard data."));
  }
}

export default api;
