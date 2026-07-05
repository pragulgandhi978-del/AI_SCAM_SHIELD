/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx}",
  ],
  theme: {
    extend: {
      colors: {
        teal: {
          500: '#0EA5A4',
          600: '#0c9190',
          700: '#0a7d7c',
          50:  '#f0fafa',
          100: '#ccf0f0',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
