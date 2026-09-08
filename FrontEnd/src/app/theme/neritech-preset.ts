import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

/**
 * Canonical PrimeNG preset for NeriTech Auto.
 *
 * Visual authority: UI-MASTER-001 + DESIGN.md.
 * Aura is used only as a technical base; NeriTech semantic tokens define the product identity.
 * Prefer semantic tokens here over CSS overrides of PrimeNG internals.
 */
const NeriTechPreset = definePreset(Aura, {
  primitive: {
    neritech: {
      50: '#EFF6FF',
      100: '#DBEAFE',
      200: '#BFDBFE',
      300: '#93C5FD',
      400: '#60A5FA',
      500: '#2563EB',
      600: '#1D4ED8',
      700: '#1E40AF',
      800: '#1E3A8A',
      900: '#172554',
      950: '#0F172A',
    },
  },
  semantic: {
    primary: {
      50: '{neritech.50}',
      100: '{neritech.100}',
      200: '{neritech.200}',
      300: '{neritech.300}',
      400: '{neritech.400}',
      500: '{neritech.500}',
      600: '{neritech.600}',
      700: '{neritech.700}',
      800: '{neritech.800}',
      900: '{neritech.900}',
      950: '{neritech.950}',
    },
    focusRing: {
      width: '2px',
      style: 'solid',
      color: '{primary.color}',
      offset: '2px',
    },
    colorScheme: {
      light: {
        surface: {
          0: '#FFFFFF',
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
          950: '#020617',
        },
        primary: {
          color: '{primary.500}',
          inverseColor: '#FFFFFF',
          hoverColor: '{primary.600}',
          activeColor: '{primary.700}',
        },
        highlight: {
          background: '{primary.50}',
          focusBackground: '{primary.100}',
          color: '{primary.700}',
          focusColor: '{primary.700}',
        },
        formField: {
          background: '{surface.0}',
          disabledBackground: '{surface.100}',
          filledBackground: '{surface.50}',
          filledHoverBackground: '{surface.50}',
          filledFocusBackground: '{surface.0}',
          borderColor: '{surface.300}',
          hoverBorderColor: '{surface.400}',
          focusBorderColor: '{primary.color}',
          invalidBorderColor: '#B91C1C',
          color: '{surface.900}',
          disabledColor: '{surface.500}',
          placeholderColor: '{surface.500}',
          invalidPlaceholderColor: '#B91C1C',
        },
      },
    },
  },
});

export default NeriTechPreset;
