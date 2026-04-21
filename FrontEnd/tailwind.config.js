/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: ['class', '.theme-dark'],
  content: [
    './src/**/*.{html,ts,scss}',
    './src/index.html',
    './public/**/*.{html,ts}',
  ],
  theme: {
    extend: {
      colors: {
        // Primary (NeriTech Blue)
        primary: {
          DEFAULT: '#2563EB',
          50: '#EFF6FF',
          100: '#DBEAFE',
          200: '#BFDBFE',
          300: '#93C5FD',
          400: '#60A5FA',
          500: '#2563EB',
          600: '#1D4ED8', // Hover
          700: '#1E40AF', // Active
          800: '#1E3A8A',
          900: '#172554',
        },
        // Neutral Scale (Gray)
        slate: {
          50: '#F8FAFC',
          100: '#F1F5F9',
          200: '#E2E8F0',
          300: '#CBD5E1',
          400: '#94A3B8',
          500: '#64748B',
          600: '#475569',
          700: '#334155',
          800: '#1E293B', // Dark Bg
          900: '#0F172A', // Darker Bg
        },
        // Indigo Scale (Stripe-like Sidebar)
        indigo: {
          50: '#f5f7ff',
          100: '#ebf0fe',
          200: '#ced9fb',
          300: '#9db4f7',
          400: '#6283f0',
          500: '#3f5efb',
          600: '#2c44e8',
          700: '#2334ca',
          800: '#1d2ca3',
          900: '#1e2882',
          950: '#12174d', // Deep Navy/Indigo
        },
        violet: {
          50: '#f5f3ff',
          500: '#8b5cf6',
          600: '#7c3aed',
        },
        // Semantic
        success: {
          DEFAULT: '#10b981',
          50: '#ecfdf5',
          500: '#10b981',
          600: '#059669',
        },
        warning: {
          DEFAULT: '#f59e0b',
          50: '#fffbeb',
          500: '#f59e0b',
          600: '#d97706',
        },
        danger: {
          DEFAULT: '#ef4444',
          50: '#fef2f2',
          500: '#ef4444',
          600: '#dc2626',
        },
        info: {
          DEFAULT: '#3b82f6',
          50: '#eff6ff',
          500: '#3b82f6',
          600: '#2563eb',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        'soft': '0px 2px 4px rgba(0, 0, 0, 0.02), 0px 4px 12px rgba(0, 0, 0, 0.05)',
        'soft-md': '0px 4px 6px -1px rgba(0, 0, 0, 0.05), 0px 2px 4px -1px rgba(0, 0, 0, 0.03)',
        'soft-lg': '0px 10px 25px -5px rgba(0, 0, 0, 0.05), 0px 8px 10px -6px rgba(0, 0, 0, 0.01)',
        'soft-xl': '0px 20px 25px -5px rgba(0, 0, 0, 0.05), 0px 10px 10px -5px rgba(0, 0, 0, 0.02)',
      }
    },
  },
  plugins: [],
};
