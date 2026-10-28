'use client';

import { createContext, useContext, useEffect, useState, type ReactNode } from 'react';
import { Moon, Sun } from 'lucide-react';

export type MarketingTheme = 'light' | 'dark';
const storageKey = 'neritech-marketing-theme';
type ThemeContext = { theme: MarketingTheme; chooseTheme: (theme: MarketingTheme) => void };
const Context = createContext<ThemeContext | null>(null);

export function MarketingThemeProvider({ children }: { children: ReactNode }) {
  const [theme, setTheme] = useState<MarketingTheme>('light');

  useEffect(() => {
    const sync = () => {
      setTheme(document.documentElement.dataset.theme === 'dark' ? 'dark' : 'light');
    };
    const onStorage = (event: StorageEvent) => {
      if (event.key !== storageKey && event.key !== null) return;
      const next: MarketingTheme = event.newValue === 'dark' ? 'dark' : 'light';
      document.documentElement.dataset.theme = next;
      setTheme(next);
    };
    sync();
    window.addEventListener('storage', onStorage);
    return () => window.removeEventListener('storage', onStorage);
  }, []);

  const chooseTheme = (next: MarketingTheme) => {
    document.documentElement.dataset.theme = next;
    setTheme(next);
    try { window.localStorage.setItem(storageKey, next); } catch { /* preferência apenas na aba atual */ }
  };

  return <Context.Provider value={{ theme, chooseTheme }}>{children}</Context.Provider>;
}

export function useMarketingTheme() {
  const context = useContext(Context);
  if (!context) throw new Error('MarketingThemeProvider ausente');
  return context;
}

export function ThemeToggle({ className = '' }: { className?: string }) {
  const { theme, chooseTheme } = useMarketingTheme();
  const dark = theme === 'dark';
  return <button
    type="button"
    className={'marketing-theme-toggle ' + className}
    role="switch"
    aria-label="Modo escuro"
    aria-checked={dark}
    title={dark ? 'Ativar modo claro' : 'Ativar modo escuro'}
    onClick={() => chooseTheme(dark ? 'light' : 'dark')}
  >
    {dark ? <Sun size={17} aria-hidden="true" /> : <Moon size={17} aria-hidden="true" />}
  </button>;
}
