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
        // Semantic
        success: {
          DEFAULT: '#16A34A',
          50: '#F0FDF4',
          500: '#16A34A',
          600: '#15803D',
        },
        warning: {
          DEFAULT: '#F59E0B',
          50: '#FFFBEB',
          500: '#F59E0B',
          600: '#D97706',
        },
        danger: {
          DEFAULT: '#DC2626',
          50: '#FEF2F2',
          500: '#DC2626',
          600: '#B91C1C',
        },
        info: {
          DEFAULT: '#0284C7',
          50: '#F0F9FF',
          500: '#0284C7',
          600: '#0369A1',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        'soft': '0px 4px 12px rgba(0, 0, 0, 0.05)',
        'soft-lg': '0px 10px 25px -5px rgba(0, 0, 0, 0.05), 0px 8px 10px -6px rgba(0, 0, 0, 0.01)',
      }
    },
  },
  plugins: [],
};
