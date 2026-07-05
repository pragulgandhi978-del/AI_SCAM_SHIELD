export default function ShieldLogo({ size = 32, className = "", animated = false }) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 48 48"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      className={`${animated ? "shield-pop" : ""} ${className}`}
      aria-hidden="true"
    >
      <path
        d="M24 3L8 9.5V22C8 32.6 14.6 40.9 24 44C33.4 40.9 40 32.6 40 22V9.5L24 3Z"
        fill="#0EA5A4"
      />
      <path
        d="M24 6.3L11 11.5V22C11 30.9 16.4 37.9 24 40.7C31.6 37.9 37 30.9 37 22V11.5L24 6.3Z"
        fill="#f0fafa"
        fillOpacity="0.15"
      />
      <path
        d="M17 24.5L21.5 29L31.5 18.5"
        stroke="white"
        strokeWidth="3"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}
