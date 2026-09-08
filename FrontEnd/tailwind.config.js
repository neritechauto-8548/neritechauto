/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: ['class', '.theme-dark'],
  content: ['./src/**/*.{html,ts,scss}', './src/index.html', './public/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: 'var(--nt-primary-500)',
          50: 'var(--nt-primary-50)',
          100: 'var(--nt-primary-100)',
          200: 'var(--nt-primary-200)',
          300: 'var(--nt-primary-300)',
          400: 'var(--nt-primary-400)',
          500: 'var(--nt-primary-500)',
          600: 'var(--nt-primary-600)',
          700: 'var(--nt-primary-700)',
          800: 'var(--nt-primary-800)',
          900: 'var(--nt-primary-900)',
        },
        surface: {
          canvas: 'var(--nt-surface-canvas)',
          panel: 'var(--nt-surface-panel)',
          subtle: 'var(--nt-surface-subtle)',
          hover: 'var(--nt-surface-hover)',
        },
        border: {
          DEFAULT: 'var(--nt-border-default)',
          strong: 'var(--nt-border-strong)',
        },
        content: {
          primary: 'var(--nt-text-primary)',
          secondary: 'var(--nt-text-secondary)',
          muted: 'var(--nt-text-muted)',
        },
        success: {
          DEFAULT: 'var(--nt-success-fg)',
          50: 'var(--nt-success-bg)',
        },
        warning: {
          DEFAULT: 'var(--nt-warning-fg)',
          50: 'var(--nt-warning-bg)',
        },
        danger: {
          DEFAULT: 'var(--nt-danger-fg)',
          50: 'var(--nt-danger-bg)',
        },
        info: {
          DEFAULT: 'var(--nt-info-fg)',
          50: 'var(--nt-info-bg)',
        },
        neutral: {
          DEFAULT: 'var(--nt-neutral-fg)',
          50: 'var(--nt-neutral-bg)',
        },
        // Transitional aliases for legacy screens. Do not use as semantic product tokens.
        slate: {
          50: '#F8FAFC',
          100: '#F1F5F9',
          200: '#E2E8F0',
          300: '#CBD5E1',
          400: '#94A3B8',
          500: '#64748B',
          600: '#475569',
          700: '#334155',
          800: '#1E293B',
          900: '#0F172A',
        },
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
          950: '#12174d',
        },
        violet: {
          50: '#f5f3ff',
          500: '#8b5cf6',
          600: '#7c3aed',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'Segoe UI', 'Roboto', 'sans-serif'],
      },
      borderRadius: {
        sm: 'var(--nt-radius-sm)',
        md: 'var(--nt-radius-md)',
        lg: 'var(--nt-radius-lg)',
        xl: 'var(--nt-radius-xl)',
      },
      boxShadow: {
        panel: 'var(--nt-shadow-panel)',
        overlay: 'var(--nt-shadow-overlay)',
        modal: 'var(--nt-shadow-modal)',
        // Legacy aliases kept until current screens are migrated.
        soft: 'var(--nt-shadow-panel)',
        'soft-md': 'var(--nt-shadow-overlay)',
        'soft-lg': 'var(--nt-shadow-overlay)',
        'soft-xl': 'var(--nt-shadow-modal)',
      },
    },
  },
  plugins: [],
};
