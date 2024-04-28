/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{ts,tsx}'
  ],
  theme: {
    extend: {
      transitionProperty: {
        bottom: 'bottom',
        height: 'height'
      }
    },
  },
  plugins: [],
};

