/** @type {import('tailwindcss').Config} */
export default {
  content: ["./src/main/resources/templates/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: [function ({ addVariant }) {
    console.log('Files being processed by Tailwind:');
  },
],
  darkMode: "selector",
}

