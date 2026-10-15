// BASE-04: verifies UI-MASTER palette values and minimum text contrast.
// This is a token contract test, not a substitute for browser/WCAG audit.
const { test } = require('node:test');
const assert = require('node:assert/strict');
const { readFileSync } = require('node:fs');
const { resolve } = require('node:path');
const css = readFileSync(resolve(__dirname, '../src/styles.scss'), 'utf8');

const token = name => {
  const expression = new RegExp('--' + name + ':\\s*(#[0-9a-fA-F]{6})\\s*;');
  const match = css.match(expression);
  assert.ok(match, 'Missing CSS token: ' + name);
  return match[1].toUpperCase();
};
const luminance = hex => {
  const channels = [1, 3, 5].map(i => parseInt(hex.slice(i, i + 2), 16) / 255)
    .map(x => x <= 0.04045 ? x / 12.92 : ((x + 0.055) / 1.055) ** 2.4);
  return channels[0] * 0.2126 + channels[1] * 0.7152 + channels[2] * 0.0722;
};
const contrast = (a, b) => {
  const x = luminance(a);
  const y = luminance(b);
  return (Math.max(x, y) + .05) / (Math.min(x, y) + .05);
};
test('canonical UI-MASTER colors remain centralized in semantic CSS tokens', () => {
  for (const [name, expected] of Object.entries({
    'nt-canvas': '#F2F3FB',
    'nt-surface': '#FFFFFF',
    'nt-surface-subtle': '#F7F8FC',
    'nt-border': '#D9DDEA',
    'nt-text': '#182033',
    'nt-text-secondary': '#4F5870',
    'nt-text-muted': '#687187',
    'nt-primary-500': '#3154FF',
    'nt-primary-600': '#2442DC',
    'nt-primary-700': '#1D34B0',
    'nt-focus': '#3154FF',
  })) {
    assert.equal(token(name), expected, name);
  }
});

test('foreground/background pairs clear WCAG normal-text 4.5:1', () => {
  for (const [label, foreground, background] of [
    ['primary action', token('nt-primary-600'), '#FFFFFF'],
    ['primary hover', token('nt-primary-700'), '#FFFFFF'],
    ['secondary body', token('nt-text-secondary'), token('nt-surface')],
    ['muted body', token('nt-text-muted'), token('nt-surface')],
    ['error', token('nt-danger'), token('nt-danger-surface')],
    ['info', token('nt-info'), token('nt-info-surface')],
  ]) {
    assert.ok(contrast(foreground, background) >= 4.5,
      label + ': contrast ratio ' + contrast(foreground, background).toFixed(2));
  }
});

const dark = (() => {
  const match = css.match(/\\.dark\\s*\\{([\\s\\S]*?)\\n\\}/);
  assert.ok(match, 'Missing dark theme tokens');
  return match[1];
})();
const darkToken = name => {
  const expression = new RegExp('--' + name + ':\\\\s*(#[0-9a-fA-F]{6})\\\\s*;');
  const match = dark.match(expression);
  assert.ok(match, 'Missing dark token: ' + name);
  return match[1].toUpperCase();
};

test('shared state text and keyboard action contrast meet WCAG 2.2 AA in both themes', () => {
  for (const [label, foreground, background] of [
    ['light action', token('nt-state-action-fg'), token('nt-state-action-bg')],
    ['light action hover', token('nt-state-action-fg'), token('nt-state-action-hover')],
    ['dark title', darkToken('nt-text'), darkToken('nt-surface')],
    ['dark secondary', darkToken('nt-text-secondary'), darkToken('nt-surface')],
    ['dark action', darkToken('nt-state-action-fg'), darkToken('nt-state-action-bg')],
    ['dark action hover', darkToken('nt-state-action-fg'), darkToken('nt-state-action-hover')],
    ['dark warning icon', darkToken('nt-warning'), darkToken('nt-warning-surface')],
  ]) {
    const ratio = contrast(foreground, background);
    assert.ok(ratio >= 4.5, label + ' ratio ' + ratio.toFixed(2) + ':1');
  }
});

test('shared data view uses semantic colors, 44px click target and visible keyboard focus', () => {
  const cssState = readFileSync(resolve(__dirname, '../src/app/shared/components/data-view-state/data-view-state.scss'), 'utf8');
  assert.match(cssState, /color:\\s*var\\(--nt-text[,)]/);
  assert.match(cssState, /color:\\s*var\\(--nt-text-secondary[,)]/);
  assert.match(cssState, /min-height:\\s*44px/);
  assert.match(cssState, /:focus-visible\\s*\\{[\\s\\S]*?outline:\\s*3px/);
  assert.match(cssState, /overflow-wrap:\\s*anywhere/);
});
