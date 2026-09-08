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
  // UI Master: desktop inicia com navegação persistente expandida (264 px).
  sidenavCollapsed: false,
  language: 'pt-BR',
  primaryColor: 'blue',
  primaryColorValue: '{blue}',
  surfaceColor: 'slate',
  surfaceColorValue: '{slate}',
  // Shell canônico usa superfícies neutras; cor primária fica para ações e estados.
  menuThemeClass: '',
  topbarThemeClass: '',
  presetTheme: 'Aura',
};
