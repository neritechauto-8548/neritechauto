export type AppTheme = 'light' | 'dark' | 'auto';

export interface AppSettings {
  navPos: 'side' | 'top';
  dir: 'ltr' | 'rtl';
  theme: AppTheme;
  showHeader: boolean;
  headerPos: 'fixed' | 'static' | 'above';
  showUserPanel: boolean;
  sidenavOpened: boolean;
  sidenavCollapsed: boolean;
  language: string;
  primaryColor: string;
  primaryColorValue: string;
  surfaceColor: string;
  surfaceColorValue: string;
  menuThemeClass: string;
  topbarThemeClass: string;
  presetTheme: string;
}

export const defaults: AppSettings = {
  navPos: 'side',
  dir: 'ltr',
  theme: 'light',
  showHeader: true,
  headerPos: 'above',
  showUserPanel: true,
  sidenavOpened: true,
  sidenavCollapsed: true,
  language: 'pt-BR',
  primaryColor: 'indigo',
  primaryColorValue: '{indigo}',
  surfaceColor: 'slate',
  surfaceColorValue: '{slate}',
  menuThemeClass: 'bg-slate-800',
  topbarThemeClass: 'bg-blue-600',
  presetTheme: 'Aura',
};
