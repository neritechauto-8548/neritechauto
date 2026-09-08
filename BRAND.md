# NeriTech Auto — BRAND.md

> Institutional and marketing design contract for NeriTech Auto.
> This file governs the landing page, public marketing pages, campaigns, launch surfaces and high-level brand storytelling.
> It does **not** override the internal application `DESIGN.md`.

## Authority and separation of surfaces

NeriTech uses a multi-surface design architecture:

1. **Brand / Marketing** → this `BRAND.md`.
2. **Product / ERP** → root `DESIGN.md` + UI-MASTER-001 + canonical screen specs.
3. **Developer Experience / API documentation** → future DX/documentation contract.

Shared DNA: NeriTech brand, accessibility, semantic tokens, typography quality, Tabler icon family and product truth.

The degree of expression changes by surface.

## Institutional reference model

Stripe is the principal reference for **institutional design quality**, not a template to copy.

Learn from Stripe's ability to make complex B2B infrastructure feel modern, trustworthy and desirable through:

- strong editorial hierarchy;
- high-quality product storytelling;
- expressive but controlled gradients;
- smooth micro-interactions;
- detailed product demonstrations;
- precise typography and spacing;
- transitions that explain the product rather than decorate the page;
- strong continuity between marketing promise and actual product UI.

Never reproduce Stripe's proprietary brand, exact layouts, copy, assets, logo, gradient composition or product screenshots.

## NeriTech institutional personality

```yaml
brand:
  name: NeriTech Auto
  category: automotive workshop management SaaS
  personality:
    - professional
    - technological
    - trustworthy
    - operational
    - approachable
    - ambitious
  promise: modern workshop management without losing operational clarity
  primaryAudience:
    - mechanical workshops
    - automotive centers
    - body shops
    - motorcycle workshops
    - workshop owners and managers
```

## Visual model

### Core brand color

Blue remains the NeriTech anchor.

```yaml
brandPrimary:
  50: '#EFF6FF'
  100: '#DBEAFE'
  500: '#2563EB'
  600: '#1D4ED8'
  700: '#1E40AF'
```

### Institutional accent field

Marketing surfaces may use a richer accent spectrum than the ERP:

```yaml
institutionalAccents:
  electricBlue: '#2563EB'
  indigo: '#4F46E5'
  violet: '#7C3AED'
  magenta: '#C026D3'
  warmOrange: '#F97316'
  cyan: '#0891B2'
```

These are **brand-expression accents**, not additional semantic product colors.

Do not use magenta/orange/violet to encode operational states in the ERP.

## Gradient system

Gradient mesh is allowed on institutional/marketing surfaces.

Rules:

- NeriTech blue is always an anchor.
- Use 2–4 related color fields, not rainbow gradients.
- Prefer large soft fields with subtle transitions.
- Keep text contrast independently valid; never rely on a gradient to create readable copy.
- Avoid permanent full-page high-saturation backgrounds.
- Gradients should frame hero/product moments, not every section.
- Never copy Stripe's exact mesh positions or color recipe.

Suggested NeriTech families:

### Aurora Blue

`blue → indigo → violet` with restrained cyan highlights.

Use for:
- hero;
- product launch;
- AI/platform storytelling.

### Workshop Energy

`blue → violet` with a small warm orange accent.

Use for:
- conversion moments;
- growth/automation storytelling;
- campaign artwork.

### Clean Product Light

White/very light blue background with blurred blue/indigo fields behind product frames.

Use for:
- screenshots;
- module showcases;
- customer stories.

## Product storytelling

The landing page should sell the **workflow**, not a collection of features.

Preferred narrative:

1. workshop arrives / schedule;
2. customer + vehicle;
3. inspection and diagnosis;
4. estimate and approval;
5. work order and execution;
6. finance/fiscal/payment;
7. delivery and post-sale;
8. management intelligence.

Use real product screens whenever implementation quality is sufficient.

Never create a marketing screenshot that promises a workflow the product does not support.

## Hero design

A high-quality NeriTech hero should contain:

- one strong value proposition;
- one short proof-oriented supporting sentence;
- one primary CTA;
- one lower-emphasis secondary CTA when needed;
- a real or faithful product visualization;
- restrained trust indicators/social proof;
- optional animated product state demonstration.

Avoid:

- multiple colorful CTAs;
- giant abstract slogans without product evidence;
- stock imagery as the main proof of software quality;
- overloading the first viewport with feature cards.

## Motion and micro-interaction

Marketing motion may be more expressive than ERP motion, but remains functional.

Use motion to:

- demonstrate a workflow transition;
- connect UI before/after states;
- reveal product layers;
- illustrate synchronization or automation;
- direct attention to a CTA or proof point.

Requirements:

- respect `prefers-reduced-motion`;
- no motion required to understand content;
- avoid long blocking intros;
- animations must remain performant;
- no decorative continuous movement behind long-form reading.

## Typography

The goal is Stripe-level typographic quality, not copying a proprietary typeface.

Canonical operational family remains:

`Inter, system-ui, Segoe UI, Roboto, sans-serif`

Marketing may use stronger size/weight contrast while maintaining the same family until a separately licensed brand typeface is formally selected.

Do not introduce Söhne or any proprietary font merely to imitate Stripe.

## Product frames and screenshots

- Use the canonical NeriTech application shell.
- Never invent a different sidebar/topbar for a marketing screenshot.
- Marketing crops may remove irrelevant chrome only when clearly presented as a focused product detail.
- Use realistic Brazilian names, vehicles, BRL values and workshop scenarios.
- Avoid Lorem Ipsum.
- Do not expose real PII or production data.

## Illustration and 3D

Allowed when it explains product architecture or brand narrative.

Possible uses:

- connected workshop workflow;
- NeriTech Platform relationships;
- integrations;
- NeriTech AI/copilot;
- payment/fiscal/data flows.

Avoid generic floating glass cards, random 3D spheres and generic AI imagery without product meaning.

## Institutional components

Create reusable Figma/Stitch variants for:

- MarketingHeader
- Hero
- ProductFrame
- GradientField
- TrustBar
- LogoCloud
- FeatureNarrative
- WorkflowStory
- MetricProof
- Testimonial
- IntegrationGrid
- PricingSection
- FAQ
- FinalCTA
- MarketingFooter

Each component must have responsive variants.

## Accessibility and performance

Marketing quality includes performance.

Field targets when measurable:

- LCP ≤ 2.5s p75
- INP ≤ 200ms p75
- CLS ≤ 0.1 p75

Also require:

- WCAG 2.2 AA for product/marketing copy and interaction;
- visible focus;
- semantic headings;
- meaningful alt text;
- reduced-motion support;
- no text embedded only inside images;
- responsive behavior down to mobile.

## Relationship to Stripe Dashboard design

Do not transfer the expressive marketing layer into the internal ERP.

Inside NeriTech Auto:

- no decorative gradient mesh;
- almost no decorative animation;
- neutral working surfaces;
- thin borders/keylines;
- high operational density;
- one primary action;
- precise states and errors;
- predictable component behavior.

This separation is intentional:

`Marketing sells the vision → Product executes the work → Documentation accelerates implementation.`

## Quality gate

Before approving an institutional page, ask:

1. Does it look unmistakably modern without looking like a Stripe clone?
2. Is the NeriTech product itself the hero?
3. Does every animation explain or reinforce something?
4. Does the page remain excellent with motion disabled?
5. Does the visual promise match implemented functionality?
6. Does it feel related to the actual NeriTech ERP when a user signs in?
7. Is performance still within the expected budget?
8. Does mobile preserve hierarchy and conversion without becoming a stacked wall of cards?
