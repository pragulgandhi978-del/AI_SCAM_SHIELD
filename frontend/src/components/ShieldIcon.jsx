import React from 'react'

/**
 * Inline SVG shield icon — brand mark for AI Scam Shield.
 * color: any Tailwind class or hex string passed as `className`
 */
export default function ShieldIcon({ size = 64, className = '' }) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 64 64"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      className={className}
      aria-hidden="true"
    >
      {/* Shield body */}
      <path
        d="M32 4L8 14v16c0 14.4 10.4 27.8 24 31.2C45.6 57.8 56 44.4 56 30V14L32 4z"
        fill="#0EA5A4"
        opacity="0.15"
      />
      <path
        d="M32 4L8 14v16c0 14.4 10.4 27.8 24 31.2C45.6 57.8 56 44.4 56 30V14L32 4z"
        stroke="#0EA5A4"
        strokeWidth="2.5"
        strokeLinejoin="round"
      />
      {/* Check mark */}
      <path
        d="M22 32l7 7 13-13"
        stroke="#0EA5A4"
        strokeWidth="3"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  )
}
