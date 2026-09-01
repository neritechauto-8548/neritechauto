# NeriTech Auto — Codex operating instructions

## Mission
Continue the D5 implementation of NeriTech Auto as a production-grade workshop-management SaaS. Preserve existing working behavior only when it is compatible with the official specification. Do not inflate progress: implementation, tests, build evidence and security guarantees must be distinguishable.

## Mandatory branch discipline
- Work only on `feature/neritech-auto-rebuild` unless the user explicitly asks otherwise.
- Never commit directly to `main`.
- Do not rewrite history or force-push.
- Keep commits focused and descriptive.

## Sources of truth
1. Official Notion root: `NERITECH — Documentação Oficial` (`3bb27279-1906-815d-b711-d225de4c2b06`).
2. For UI/UX specifically: Notion `04.01 UI-MASTER-001`, `04.05 MASTER DESIGN.md`, `04.06 UX & Design Engineering 2026`, the canonical `TELA-*` specification, and root `DESIGN.md`.
3. Approved Google Stitch/Figma artifact for the screen, when available, as visual implementation evidence — never as business-rule authority.
4. Current code on `feature/neritech-auto-rebuild`.
5. `Relatorio_Implementacao_NeriTech.md` for implementation status, but verify code before trusting percentages.
6. Legacy code is reference only; it is not authoritative.

When the exact Notion specification is unavailable, do not invent irreversible business rules. Implement safe, reversible defaults only when they do not conflict with known rules, and mark missing contracts explicitly.

## Product/UX reference model
- **Shopmonkey** is the primary reference for workshop operational UX: connected workflow, customer/vehicle/order continuity, inspection/authorization flow, technician surfaces, workflow cards/list views and reduced repetitive navigation.
- **Stripe** is the primary reference for design engineering: tokens, hierarchy, spacing, typography, subtle borders/elevation, component consistency, polished forms/states and one-primary-action discipline.
- Formula: `NeriTech Auto = Shopmonkey operational UX + Stripe design discipline + Brazilian workshop domain + NeriTech identity`.
- Never copy proprietary identity, assets, copy, exact layouts or third-party business rules.

## Stack
- Backend: Java 21, Spring Boot 3.2.x, Spring Security/JWT, PostgreSQL, Flyway, OpenAPI.
- Frontend: Angular 20.x, standalone components, PrimeNG 20.x as the current component-library direction and Tailwind CSS as the primary layout/spacing/responsive styling layer.
- PrimeNG visual identity must come from the NeriTech custom `definePreset` theme and shared semantic tokens, not Aura defaults or broad CSS overrides.
- Tabler Icons is the canonical icon family. Angular 20 rebuilt UI uses the local NeriTech wrapper with official Tabler SVG geometry; migrate to `@tabler/icons-angular` only when Angular 21+ compatibility is approved.
- Angular Material, Ng-Matero and PrimeIcons are legacy migration inputs only. Do not introduce them into rebuilt UI unless an explicit documented compatibility constraint requires it.
- Tailwind/PrimeNG must share the same semantic token values; do not maintain parallel product palettes.
- Multi-tenant SaaS.
- Reports: JasperReports where applicable.

## Visual implementation contract
- Read root `DESIGN.md` before building or substantially changing application UI.
- Visual authority order: `UI-MASTER-001` → canonical `TELA-*` spec → `DESIGN.md` → approved Stitch/Figma artifact → existing code.
- Existing code never overrides the canonical visual contract merely because it already exists.
- Use PrimeNG for interaction/component primitives where appropriate, but theme and compose it according to NeriTech tokens; PrimeNG default styling is not the product identity.
- Prefer PrimeNG semantic/design tokens and the NeriTech preset over CSS that reaches into PrimeNG internals.
- Use Tailwind CSS for canonical spacing, layout, responsive behavior and utility styling. Do not create a second parallel token system in ad-hoc SCSS.
- For Tailwind v3 + PrimeNG v20, preserve the documented CSS layer order: `tailwind-base, primeng, tailwind-utilities`.
- New/rebuilt screens use Tabler Icons only. Do not add Lucide, PrimeIcons, Material Symbols or Font Awesome to regular product UI.
- Migrate old PrimeIcons/Material/Matero incrementally when their owning screen/shared component is rebuilt; do not perform blind global replacement.
- Preserve Google Stitch/Figma hierarchy, dimensions, spacing, component composition and responsive intent as closely as practical when an approved artifact exists.
- Stitch/Figma cannot override business rules, permissions, tenancy, API contracts, accessibility or canonical screen behavior.
- If a visual artifact conflicts with the UI Master or `TELA-*` spec, flag the artifact for review and implement the documented rule.
- UI fidelity means system fidelity: shell, hierarchy, tokens, grid, spacing, component behavior, states, accessibility and responsive intent — not blind pixel matching.

## Non-negotiable security / tenancy rules
- Tenant authority comes only from the authenticated backend session/token plus persisted user/tenant relationship.
- Browser `localStorage`, query params and headers must never grant tenant access.
- `X-Tenant-Id`, when retained for compatibility, may select only an already-authorized tenant and must never grant authority.
- Backend is the final authority for permissions; frontend guards/menu filtering are UX only.
- Deny by default.
- Cross-tenant access must behave as not found/forbidden according to the established contract and must never leak another tenant's existence.
- Do not introduce `empresaId: 1`, `tenantId` from browser state, implicit ADMIN bypasses, or hardcoded credentials.
- Do not log PII, tokens, credentials, complete payloads containing sensitive customer/vehicle data, or free-text business data unnecessarily.
- Prefer minimized read DTOs; do not send full PII to the browser merely to mask it in HTML.

## Lifecycle and data integrity rules already established
- Customer and vehicle operational deletion is logical deactivation; preserve history and related records.
- Vehicle plate is normalized and unique within tenant; same plate may exist in different tenants.
- Vehicle VIN/chassis, when present, must be normalized and unique within tenant.
- Cross-tenant vehicle/customer mutations are forbidden.
- Odometer regression must not silently overwrite data.
- External vehicle lookup is an optional suggestion only; canonical internal data wins and sensitive identifiers are not returned as suggestions.

## Frontend architecture already established
- Canonical application shell and official sidebar are being rebuilt before deeper modules.
- Authentication uses a reusable Auth shell. Never persist password in browser storage.
- Canonical `PageHeader` is shared.
- Official Home routes: `/home/gerencial`, `/home/financeiro`, `/home/orcamentos`, `/home/operacional`.
- Production navigation must not expose template/demo routes.
- UI must use shared design tokens from `DESIGN.md` and remain visually consistent across modules.
- Sidebar/menu/topbar must follow the canonical UI Master, including official menu order and responsive shell dimensions.
- Operational screens should be context-first and next-action-first: preserve customer/vehicle/object identity, state, pending reason, responsible actor and next safe action when relevant.
- Command/search acceleration may use `Ctrl/Cmd + K`, but visible navigation remains canonical.
- Missing backend/read-model contracts must be shown as unavailable/pending; never fabricate KPIs, graphs, timelines, money, trends or fake data.

## Customer module status and routes
Canonical routes:
- `/clientes`
- `/clientes/novo`
- `/clientes/:id`
- `/clientes/:id/editar`

Implemented direction:
- List uses minimized masked DTOs.
- Create/edit follows Dados básicos -> Contatos -> Endereço -> Preferências.
- Customer detail is a 360-degree read view with Resumo, Contatos, Endereços, Veículos, Histórico, Preferências.
- The detail screen must use minimized read models; editing endpoints may return fuller data only under explicit edit permission.
- Customer inactive records remain consultable; creation actions may be blocked by policy.
- Timeline/overview read models must not be fabricated. Until implemented, show explicit pending/partial state.

## Current known unsafe legacy flows to fix next
### Budget / estimate
Legacy Angular budget creation still contains unsafe behavior such as browser-supplied/hardcoded `empresaId` and client-generated identifiers. Remove tenant authority from the browser and move authoritative numbering/identity generation to backend/domain ownership before exposing contextual "Novo orçamento" actions.

### Scheduling
Legacy scheduling still initializes/supplies `empresaId` in the browser. Remove it as authority and derive tenant in backend before enabling contextual "Agendar" actions from customer detail.

## Permission approach
Persisted permission codes currently use legacy-style values such as:
- `GERAL_USUARIO`
- `CLIENTE_CRIAR`
- `CLIENTE_EDITAR`
- `CLIENTE_EXCLUIR`
- `VEICULO_CRIAR`
- `VEICULO_EDITAR`
- `VEICULO_EXCLUIR`

Do not invent new persisted permission codes merely because conceptual D4 capabilities have different names. Add a formal mapping/migration only when the specification and persistence model are reconciled.

## D5 progress policy
Do not mark work complete because files were edited. Increase D5 only for implemented and verifiable behavior.

Stable weighting:
- 0–12% Foundation/security/auth/multi-tenancy/testing base
- 12–18% Angular shell/menu/navigation/route permissions/design-system foundation
- 18–28% Customers + vehicles + CRM base
- 28–38% Budgets
- 38–52% Work Orders/checklists/core operational flow
- 52–62% Inventory/services/kits/areas/accessories
- 62–76% Finance + cash + purchasing
- 76–84% Fiscal + POS
- 84–90% Calendar/alerts/marketing/CRM
- 90–95% Yard + Customer Portal
- 95–98% Reports/Admin/Audit/Subscriptions
- 98–100% E2E tests, hardening, CI/build verification, traceability/evidence

Current reported D5 baseline is 11%. Do not raise it without evidence.

## Definition of done for each task
1. Read the relevant specification, `DESIGN.md` when UI is involved, and current implementation.
2. Identify divergence and security/tenancy implications.
3. Inspect the current approved Stitch/Figma artifact when available for the screen.
4. Apply the Shopmonkey operational check and Stripe design-engineering check for UI work.
5. Implement the smallest coherent production-quality batch.
6. Add or update tests for happy path plus negative tenant/permission cases where relevant.
7. Run the relevant formatter/linter/type-check/tests/build when the environment allows it.
8. Fix failures caused by the change.
9. Do not report CI/build as green unless actually observed.
10. Compare UI work against documented desktop/tablet/mobile behavior before calling it visually complete.
11. Update implementation status only with verifiable evidence.
12. Commit only related files.

## Immediate task queue
1. Reconcile the Angular Application Shell/shared UI foundation with `UI-MASTER-001` + `DESIGN.md`: official menu, shell dimensions, tokens, PrimeNG/Tailwind mapping and Tabler icon migration for touched shared components.
2. Validate the new customer 360 detail implementation and minimized read contracts; maintain the canonical visual contract while adding tests that prevent fallback to full PII endpoints.
3. Reconcile customer create/edit backend DTO validation with the documented optional CPF/CNPJ/address behavior without weakening identity rules.
4. Fix Budget/Estimate multi-tenancy: remove browser-controlled/hardcoded company id and client-generated authoritative numbers; add tenant-safe tests.
5. Rebuild Budget screens against their canonical `TELA-AUTO-ORC-*` specs and latest Stitch/Figma artifacts, starting from list/new/detail composition.
6. Fix Scheduling multi-tenancy the same way; then enable contextual actions from customer detail only if permissions and inactive-customer policy allow it.
7. Return to the pending Flyway vehicle uniqueness migration before declaring the 0–12% foundation milestone complete.

## Coding quality
- Prefer existing project patterns where safe; refactor instead of duplicating components/services.
- Keep Angular strict typing and `strictTemplates` compatibility.
- Avoid `any` unless bridging a documented legacy boundary, and isolate that boundary.
- Avoid N+1 calls and browser-side aggregation when a read model belongs in the backend.
- Do not silently delete/merge duplicate production data in Flyway migrations; fail clearly or implement an explicit reviewed remediation path.
- Never expose secrets from repository history.
- For UI code, prefer shared canonical components/tokens over screen-local reinvention.

## Communication / task result
At the end of each Codex task, report:
- files changed and commits;
- tests/build commands actually executed and results;
- visual contract/artifact used for UI work;
- security/tenancy implications;
- what remains genuinely blocked;
- D5 general percentage and current block percentage only if justified by verifiable completion.
