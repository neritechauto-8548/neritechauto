# NeriTech Auto — DESIGN.md

> Machine-readable and human-readable visual contract for the NeriTech Auto application.
> Source of truth: Notion `04.01 UI-MASTER-001` + `04.05 MASTER DESIGN.md — NeriTech Auto para Google Stitch` + `04.06 UX & Design Engineering 2026`.
> This contract exists to keep Google Stitch/Figma artifacts and Angular implementation visually consistent.

## Authority order

When implementing or reviewing a screen, use this order:

1. `UI-MASTER-001` in the official Notion documentation.
2. Canonical specification for the screen (`TELA-*`).
3. This `DESIGN.md`.
4. Approved Google Stitch/Figma artifact for that screen.
5. Existing frontend code.

Existing code is not a visual authority when it conflicts with the design contract. Legacy Ng-Matero, Angular Material, PrimeIcons or old mockups must not override the canonical design.

## Product identity

```yaml
name: NeriTech Auto Design System
version: 1.1
revisionDate: 2026-09-01
locale: pt-BR
theme: light
futureThemes:
  - dark
brand:
  productName: NeriTech Auto
  personality: professional, trustworthy, operational, modern, calm
  visualDirection: Minimal Enterprise SaaS
referenceModel:
  operationalUx: Shopmonkey
  designEngineering: Stripe
  domain: Brazilian automotive workshop management
  rule: reference quality and principles; never copy proprietary identity or layouts
```

The UI must feel like one mature operational product, not a collection of independently generated pages.

## Primary design reference model — Shopmonkey × Stripe

The NeriTech Auto visual/UX direction intentionally combines two different strengths:

### Shopmonkey — operational UX reference

Use Shopmonkey as the main reference for how workshop-management work is organized and accelerated:

- visible workflow and status progression;
- customer + vehicle + order context continuity;
- operational cards and list views;
- contextual creation from customer, vehicle, schedule and search surfaces;
- estimate/inspection/authorization/order/payment continuity;
- digital inspection evidence as operational information;
- technician-friendly mobile/tablet behavior;
- keyboard shortcuts and reduced repetitive navigation;
- dashboards that explain shop state, aging and next actions.

Do not copy US-specific terminology, proprietary layouts, assets or business rules that conflict with the Brazilian domain.

### Stripe — design engineering reference

Use Stripe as the main reference for visual precision and design-system discipline:

- design tokens instead of arbitrary values;
- quiet typography with precise hierarchy;
- predictable spacing and alignment;
- subtle keylines/borders and minimal functional elevation;
- one primary action per context;
- consistent forms, errors, focus and feedback;
- reusable component variants rather than one-off CSS;
- high-quality empty/loading/error states;
- premium feel created by consistency, not decoration.

Do not reproduce Stripe branding, marketing gradients or financial-product-specific layouts.

### NeriTech formula

`NeriTech Auto = Shopmonkey operational UX + Stripe design discipline + Brazilian workshop domain + NeriTech identity`

Every screen review should answer two questions:

1. **Shopmonkey check:** does this reduce steps, preserve workshop context and make the next operational action obvious?
2. **Stripe check:** does this feel precise, consistent, token-driven and intentionally composed as part of one product?

## Frontend implementation mapping

Current implementation direction for the rebuild:

```yaml
frontend:
  framework: Angular 20.x
  componentLibrary: PrimeNG 20.x
  styling: Tailwind CSS
  primeThemeStrategy: NeriTech custom preset via PrimeNG design tokens
  tokenStandard: DTCG 2025.10
  iconFamily: Tabler Icons
  angular20IconIntegration: local NeriTech wrapper using official Tabler SVG geometry
  targetIconPackage: '@tabler/icons-angular when Angular 21+ compatibility is approved'
```

Rules:

- PrimeNG provides accessible interactive component primitives where it is a good fit.
- Tailwind CSS is the primary layout/spacing/responsive utility layer and must express the canonical tokens below.
- PrimeNG must be visually themed to NeriTech tokens. Its default visual identity is not the product design.
- Use a NeriTech `definePreset` theme rather than broad CSS overrides of PrimeNG internals.
- Custom Angular components are allowed when a PrimeNG primitive cannot express the documented UX cleanly.
- SCSS/CSS is allowed for component-specific behavior or tokens that are clearer outside utilities, but must not create a second visual system.
- Do not introduce new Angular Material or Ng-Matero UI dependencies/components into rebuilt screens.
- Existing Material/Matero code is migration input only. When a touched feature is rebuilt, prefer the canonical stack.
- Do not mix Tailwind major-version conventions. Normalize the project to one Tailwind toolchain in a dedicated technical change before broad visual migration.
- Tailwind and PrimeNG must consume the same semantic values; do not maintain parallel product palettes.

## Iconography — mandatory

```yaml
icons:
  library: Tabler Icons
  source: official Tabler SVG geometry
  defaultStyle: outline
  grid: 24x24
  defaultStroke: 2
  preferredSizes:
    - 16px
    - 20px
    - 24px
  filledUsage: only for explicit selected/state semantics
  mixingLibraries: false
  prohibitedRegularUiLibraries:
    - Lucide
    - PrimeIcons
    - Material Symbols
    - Font Awesome
```

Rules:

- New or rebuilt UI uses Tabler only.
- Angular 20 rebuilt UI uses the local NeriTech Tabler wrapper until the official Angular package compatibility gate is met.
- PrimeNG components that expose templates or icon slots must receive a Tabler icon/component rather than a `pi pi-*` icon whenever technically possible.
- Do not add PrimeIcons to new screens.
- Existing PrimeIcons should be migrated as related screens are rebuilt; do not perform a risky blind global replacement.
- Every collapsed-sidebar navigation icon needs a tooltip.

## Colors

```yaml
colors:
  primary-50: '#EFF6FF'
  primary-100: '#DBEAFE'
  primary-500: '#2563EB'
  primary-600: '#1D4ED8'
  primary-700: '#1E40AF'
  surface-canvas: '#F8FAFC'
  surface-panel: '#FFFFFF'
  surface-subtle: '#F1F5F9'
  border-default: '#E2E8F0'
  text-primary: '#0F172A'
  text-secondary: '#475569'
  text-muted: '#64748B'
  success: '#15803D'
  success-bg: '#F0FDF4'
  warning: '#B45309'
  warning-bg: '#FFFBEB'
  danger: '#B91C1C'
  danger-bg: '#FEF2F2'
  info: '#0369A1'
  info-bg: '#F0F9FF'
  neutral: '#475569'
  neutral-bg: '#F8FAFC'
```

Never use color as the only status indicator. Pair semantic color with text and, when useful, a Tabler icon.

## Typography

```yaml
typography:
  fontFamily: Inter, system-ui, Segoe UI, Roboto, sans-serif
  body-sm: { fontSize: 13px, lineHeight: 18px, fontWeight: 400 }
  body-md: { fontSize: 14px, lineHeight: 20px, fontWeight: 400 }
  body-lg: { fontSize: 16px, lineHeight: 24px, fontWeight: 400 }
  label: { fontSize: 13px, lineHeight: 18px, fontWeight: 500 }
  caption: { fontSize: 12px, lineHeight: 16px, fontWeight: 400 }
  h1: { fontSize: 28px, lineHeight: 36px, fontWeight: 700 }
  h2: { fontSize: 22px, lineHeight: 30px, fontWeight: 650 }
  h3: { fontSize: 18px, lineHeight: 26px, fontWeight: 600 }
```

Financial values and codes should use tabular numerals when helpful.

## Spacing and shape

```yaml
spacing:
  0: 0px
  1: 4px
  2: 8px
  3: 12px
  4: 16px
  5: 20px
  6: 24px
  8: 32px
  10: 40px
  12: 48px
  16: 64px
radius:
  sm: 6px
  md: 8px
  lg: 12px
  xl: 16px
  pill: 999px
border:
  width: 1px
  color: '#E2E8F0'
elevation:
  card: none-or-very-subtle
  dropdown: subtle
  modal: medium
```

Avoid heavy shadows, decorative gradients, glassmorphism and oversized cards with little operational value.

## Application Shell

```yaml
layout:
  sidebarExpanded: 264px
  sidebarCollapsed: 72px
  topbarDesktop: 64px
  topbarMobile: 56px
  contentGutterDesktop: 32px
  contentGutterTablet: 24px
  contentGutterMobile: 16px
breakpoints:
  xl: 1440px+
  lg: 1280-1439px
  md: 1024-1279px
  sm: 768-1023px
  xs: <768px
```

### Sidebar

- Expanded desktop width: 264px.
- Collapsed width: 72px.
- Tablet: collapsed or overlay according to viewport.
- Mobile: overlay; never permanently consume content width.
- Independent vertical scroll.
- Active item: `primary-50` background + blue side indicator + stronger text weight.
- Parent group remains expanded when a child route is active.
- Exactly one destination is active.
- Collapsed icons always have tooltips.

### Official menu order — immutable

1. Gestão de Pátio
2. Home
3. Clientes
4. Operacional
5. Cadastros
6. Movimentação
   - Orçamentos
   - Ordens de Serviço
   - Checklists
   - Aprovações
   - Peças
   - Faturamento
7. Financeiro
8. Fiscal
9. Histórico
10. Gráficos
11. Agendamentos
12. Relatórios

Do not rename, reorder or move these top-level destinations in isolated screen work.

### Topbar

Contains, according to permission and viewport:

- navigation trigger on reduced viewports;
- global search for customer, vehicle, plate, estimate and work order;
- company/unit selector;
- notifications;
- support/help;
- user menu.

Global context must not trust browser-provided tenant authority.

## Page anatomy

Use this sequence when applicable:

1. concise breadcrumb;
2. PageHeader;
3. contextual alerts;
4. operational KPIs/summary only when useful;
5. search and filters;
6. main content;
7. pagination/final actions;
8. global toast region.

### PageHeader

- Exactly one H1.
- Short description.
- Optional status/context.
- At most one visually primary action.
- Secondary actions use secondary/ghost/overflow patterns.
- Destructive actions never compete visually with the primary action.

## Workshop operational UX

### Context-first

- Preserve customer + vehicle + current object identity during the main workshop journey.
- Long estimate/OS/inspection journeys may use a compact sticky operational context.
- Do not use oversized identity cards that waste working space.

### Next-action-first

Operational queues and details should make visible when relevant:

- current state;
- pending reason;
- responsible person/role;
- aging/time waiting;
- next safe action.

### Connected flow

Target continuity:

`Scheduling → Customer/Vehicle → Reception → DVI → Diagnosis → Estimate → Authorization → Work Order → Execution/Parts → Finalization → Finance/Fiscal/Payment → Delivery/Post-sale`

Do not require retyping data that already belongs to the same journey.

### Technician surfaces

- Tablet/mobile prioritizes task execution, checklist, camera/evidence and status updates.
- Frequent touch actions should target approximately 44×44px when practical.
- No essential interaction depends on hover or drag.
- Offline/degraded state must explain what is saved, queued or blocked.

## Operational DataTable

Canonical composition:

`TableContext → Search → QuickFilters → AdvancedFilters → SavedView → Density → Columns → ResultCount → DataTable → Pagination`

Rules:

- Preserve filters/sort when opening detail and returning.
- Secondary row actions move to overflow when the row becomes noisy.
- Batch actions only appear when selection exists and the business rule is homogeneous/auditable.
- Use skeletons that preserve table geometry.
- First-use empty, filtered-empty and error are different states.
- Desktop may provide comfortable/compact density.
- Mobile must not compress a wide operational table into unreadable columns; use responsive cards/lists or an essential-column view.

## Responsive behavior

### XL — 1440+

- Sidebar expanded by default.
- Full operational tables.
- Multi-column dashboards are allowed.

### LG — 1280–1439

- Sidebar expanded or collapsed according to preference.
- Preserve full operational tables when space permits.

### MD — 1024–1279

- Sidebar collapsed.
- Advanced filters move to drawer where useful.
- Dashboards use 2–3 columns according to content.

### SM — 768–1023

- Overlay navigation.
- One or two content columns.
- Hide secondary table columns only if the main task remains complete.

### XS — below 768

- One column.
- Dense tables become responsive cards/lists rather than unreadable compressed columns.
- Never rely on hover.
- Preserve search, state, primary CTA and essential actions.

## Canonical reusable components

### Navigation

- AppSidebar
- SidebarGroup
- SidebarItem
- Topbar
- Breadcrumb
- Tabs
- Stepper
- Pagination

### Global context

- GlobalSearch
- CompanyUnitSelector
- NotificationCenter
- UserMenu
- SupportEntry
- CommandPalette

### Actions

- Button / Primary
- Button / Secondary
- Button / Ghost
- Button / Danger
- IconButton
- SplitButton when justified
- OverflowMenu

### Forms

- TextInput
- SearchInput
- TextArea
- Select
- MultiSelect
- Autocomplete
- DatePicker
- DateRange
- TimePicker
- Checkbox
- Radio
- Switch
- CurrencyInput
- QuantityInput
- DocumentInput
- PhoneInput
- Plate/VIN Input
- FormField
- InlineValidation
- HelperText

### Feedback and states

- Toast
- InlineAlert
- Banner
- Badge
- StatusBadge
- Progress
- Skeleton
- EmptyState
- ErrorState
- PermissionState
- OfflineState
- ConflictState

### Data

- DataTable
- TableHeader
- TableRow
- SortControl
- FilterBar
- FilterChip
- SavedViewSelector
- KPI Card
- MetricCard
- Timeline
- ActivityFeed
- AuditEvent
- ChartContainer
- AccessibleChartSummary

### Containers

- Card
- Section
- Accordion
- Drawer
- Modal
- Popover
- Tooltip
- Divider

### Operational identity

- Avatar
- CustomerIdentity
- VehicleIdentity
- WorkOrderIdentity
- EstimateIdentity
- MoneyValue
- DateTimeValue

## Interaction rules

- Prefer variants of shared components over one-off components.
- Primary button exists only for the main contextual action.
- Disabled actions need an understandable reason when relevant.
- Modal is for short contextual tasks.
- Long journeys use a dedicated route/stepper.
- Drawer is preferred for quick detail and advanced filters.
- Loading uses skeletons where layout is known.
- Empty first-use is different from empty search/filter result.
- Error, partial failure, permission denied, offline and conflict are designed states, not afterthoughts.
- Preserve filters and list context when navigating to details and back.
- `Ctrl/Cmd + K` is the standard command/search acceleration surface, but never replaces visible navigation.

## Accessibility

```yaml
accessibility:
  target: WCAG 2.2 AA
  visibleFocus: true
  keyboardNavigation: true
  colorOnlyStatus: false
  zoom200: true
  minimumPointerTarget: 24x24-or-spacing-equivalent
  frequentTouchTargetRecommended: 44x44
```

- All critical interaction must work by keyboard.
- Focus must remain visible and logical and cannot be hidden behind sticky UI.
- Mobile/touch interactions cannot depend on hover.
- Icon-only buttons need accessible labels/tooltips.
- Charts need an accessible textual summary when they carry business information.
- Drag interactions require a non-drag alternative when they are functionally important.

## Performance UX

When measurable in field data, target p75:

- LCP ≤ 2.5s;
- INP ≤ 200ms;
- CLS ≤ 0.1.

Prefer regional skeleton/loading instead of blocking the whole page when independent data can render progressively.

## Motion

```yaml
motion:
  style: subtle and purposeful
  avoidDecorativeMotion: true
```

Motion communicates state or relationship. It must not be decorative noise.

## Visual direction — do / avoid

### Do

- Strong information hierarchy with operational density.
- Clear surfaces and discreet borders.
- Short objective pt-BR copy.
- One primary action per context.
- Reuse predictable components.
- Keep visual continuity across Cliente → Veículo → Atendimento → Orçamento → OS → Financeiro.
- Use realistic Brazilian demo data in design artifacts, never Lorem Ipsum.
- Use Shopmonkey as a workflow/operational benchmark and Stripe as a design-engineering benchmark.

### Avoid

- Glassmorphism.
- Decorative gradients.
- Heavy shadows.
- Excessive blue everywhere.
- Multiple competing colored buttons.
- Landing-page aesthetics inside the ERP.
- Mixed icon families.
- Generic AI-template appearance.
- Invented functionality to make a mockup look complete.
- Literal copies of Shopmonkey or Stripe UI.

## Screen implementation workflow

Before coding a screen:

1. Read `UI-MASTER-001`.
2. Read the canonical `TELA-*` specification and linked RN/CA/CT.
3. Read the Shopmonkey × Stripe reference rules in this file.
4. Inspect the most recent Google Stitch/Figma artifact if available.
5. Map the artifact to shared NeriTech components and PrimeNG primitives.
6. Implement layout/responsiveness with canonical tokens/Tailwind.
7. Use Tabler icons only.
8. Implement documented states: default, loading, empty, error, permission and responsive variants as applicable.
9. Keep permission/tenant authority in the backend; UI rules are experience only.
10. Compare implementation against the artifact at desktop/tablet/mobile.
11. Add visual/accessibility regression evidence before claiming VISUAL-OK/D5.

## Google Stitch / Figma fidelity rule

Google Stitch/Figma is a visual implementation reference, not business-rule authority.

- Preserve shell dimensions, hierarchy, spacing, typography, component composition and responsive intent as closely as practical.
- Never alter a documented business rule just to match an artifact.
- If the artifact conflicts with UI-MASTER or the screen specification, the documentation wins and the artifact must be flagged for review.
- If the artifact introduces a reusable component not yet in the Design System, implement it as a candidate shared component rather than hardcoding it into one screen.
- Fidelity means system fidelity, not blind pixel matching: semantics, accessibility, component behavior and responsive intent matter as much as geometry.

## Migration policy for the current codebase

The repository contains legacy Ng-Matero/Angular Material/PrimeIcons code. Migration is incremental:

1. Do not introduce more legacy UI into rebuilt screens.
2. Establish canonical shell/tokens/components first.
3. Migrate shared navigation/header/page primitives.
4. Rebuild each touched feature against this contract.
5. Remove legacy dependency only after no production feature depends on it.
6. Migrate Tailwind major versions only as an isolated technical batch with build and visual-regression evidence.

Do not attempt a blind global visual rewrite without tests.
