# NeriTech Platform — DX.md

> Developer Experience contract for NeriTech APIs, integrations, webhooks, SDK guidance, technical documentation and debugging surfaces.
> Stripe is the principal reference for DX quality and predictability; NeriTech remains domain-specific and implementation-independent.

## Mission

A developer integrating NeriTech should be able to:

1. understand the domain without reverse-engineering the database;
2. authenticate safely;
3. discover the correct endpoint/event;
4. test without touching production;
5. copy a valid example;
6. understand errors without opening a support ticket;
7. retry safely;
8. trace what happened;
9. migrate versions deliberately;
10. reach production with clear operational confidence.

The API is a product, not a transport layer accidentally exposed by the backend.

## Reference principles

Learn from Stripe's developer-first model:

- predictable resource-oriented concepts;
- consistent HTTP semantics;
- clear test/live separation;
- executable examples;
- strong error feedback;
- high-quality API reference;
- versioning discipline;
- webhook-first integration guidance;
- copy/paste-friendly documentation;
- documentation that follows developer tasks, not backend package structure.

Do not copy Stripe endpoint naming or financial domain where it does not fit NeriTech.

## Domain-first API design

Public/integration contracts should use workshop domain concepts:

- Customer
- Vehicle
- Appointment
- Inspection / Checklist
- Estimate
- Authorization
- Work Order
- Service
- Part
- Inventory Movement
- Receivable / Payable
- Payment
- Invoice / Fiscal Document
- Notification
- Audit Event

Internal persistence entities must not automatically become public API resources.

## Resource model

Prefer stable resource-oriented URLs and representations.

Examples of intended style, not final endpoint contracts:

```text
GET    /v1/customers/{id}
GET    /v1/customers/{id}/vehicles
POST   /v1/estimates
GET    /v1/estimates/{id}
POST   /v1/estimates/{id}/authorization-requests
GET    /v1/work-orders/{id}
POST   /v1/work-orders/{id}/complete
```

Rules:

- nouns represent resources;
- commands/actions are used only when a domain transition is clearer than CRUD;
- avoid leaking database table names;
- avoid RPC-style endpoint explosion;
- collections use consistent pagination/filtering conventions;
- IDs are opaque and never encode tenant authority.

## Multi-tenancy

Tenant security is part of DX.

- Authority comes from authenticated server context and authorized tenant membership.
- Browser/client-supplied tenant IDs never grant access.
- Tenant selection, where supported, must select only among already-authorized tenants.
- Cross-tenant access must not leak resource existence.
- API documentation must explain tenant behavior clearly.

## Authentication

Documentation must separately describe:

- human application authentication;
- server-to-server integration authentication;
- OAuth/integration authorization when introduced;
- webhook signature verification;
- credential rotation;
- environment separation.

Never document insecure shortcuts as development conveniences.

## Environments

Developer-facing NeriTech should distinguish at minimum:

- local development;
- test/sandbox;
- production.

The UI and documentation must make the current environment obvious.

Test activity should not generate production financial/fiscal consequences.

## Idempotency

Mutating integration endpoints that can reasonably be retried should support an explicit idempotency strategy.

Prioritize idempotency for:

- payment/fiscal operations;
- externally triggered creation;
- webhook-driven commands;
- message/send operations;
- transitions where duplicate execution creates business harm.

Document retry behavior and retention expectations.

## Errors are product surfaces

Use a stable error envelope.

Illustrative contract:

```json
{
  "error": {
    "code": "estimate_already_approved",
    "message": "O orçamento já possui aprovação válida.",
    "type": "business_conflict",
    "requestId": "req_...",
    "fieldErrors": [],
    "details": {}
  }
}
```

Every public error code should document:

- meaning;
- likely cause;
- whether retry is safe;
- whether user action is required;
- relevant fields;
- HTTP status;
- example;
- correlation/request ID behavior.

Do not expose stack traces, SQL, secrets or cross-tenant facts.

## HTTP semantics

Use statuses consistently.

Typical intent:

- `200` successful read/update response;
- `201` resource created;
- `202` asynchronous processing accepted;
- `204` successful operation with no response body;
- `400` malformed request;
- `401` unauthenticated;
- `403` authenticated but forbidden when policy permits disclosure;
- `404` unavailable/not found, including non-enumeration policies;
- `409` domain/version conflict;
- `422` semantically invalid command when the chosen API convention requires it;
- `429` rate limited;
- `5xx` unexpected/server dependency failure.

The project must choose and document one consistent validation/error convention rather than mixing status meanings per controller.

## Request correlation and supportability

Every externally observable request should support correlation.

Expose a safe `requestId`/correlation ID in:

- error responses;
- logs;
- support/debug screens;
- webhook deliveries where appropriate.

A user should be able to report an error without copying sensitive payloads.

## Pagination

Choose one canonical pagination contract per API generation.

Prefer cursor pagination for large/changeable integration collections; offset/page pagination remains acceptable for bounded internal lists where documented.

Document:

- default/max page size;
- stable ordering;
- cursor lifetime/meaning;
- filters;
- supported sort fields.

## Filtering and expansion

Do not create a unique query language for every resource.

Define consistent conventions for:

- equality filters;
- date ranges;
- status lists;
- search;
- sorting;
- pagination;
- optional expansions/includes.

Minimized default representations are preferred when full nested data would expose unnecessary PII or create large payloads.

## Versioning

Version APIs deliberately.

Current baseline uses `/v1` where appropriate.

Breaking changes require:

- migration notes;
- compatibility window;
- deprecation timeline;
- changed examples;
- affected webhook/event contracts;
- test environment availability before production enforcement.

Do not silently repurpose existing fields.

## Webhooks and event contracts

Events should represent business facts, not internal implementation details.

Good conceptual examples:

```text
estimate.created
estimate.authorization_requested
estimate.approved
estimate.rejected
work_order.created
work_order.completed
payment.received
invoice.issued
```

Rules:

- past-tense/fact-oriented where applicable;
- immutable event identity;
- event timestamp;
- tenant/account context without granting authority;
- event version/schema discipline;
- signature verification;
- retry policy;
- duplicate delivery expectation;
- idempotent consumer guidance;
- delivery logs/replay tooling when the platform matures.

## API documentation information architecture

Target Stripe-quality usability with NeriTech identity.

### Global shell

- persistent navigation;
- global docs search;
- product/API version selector;
- environment/test context;
- language/SDK selector when SDKs exist;
- account/auth context where safe.

### Endpoint/reference page

Prefer a layout that makes these simultaneously understandable:

1. purpose and domain explanation;
2. request/parameter reference;
3. working code example;
4. response example;
5. errors;
6. related events/webhooks;
7. next step.

A multi-column technical layout is appropriate on large screens when it remains accessible and does not force horizontal reading on mobile.

## Documentation types

Do not mix all technical content into API reference.

Maintain distinct surfaces:

### Guides
Task-oriented journeys such as:

- authenticate an integration;
- create an estimate;
- request customer approval;
- listen for work-order completion;
- integrate payment status;
- consume vehicle/customer updates.

### API Reference
Exact resource/parameter/schema behavior.

### Webhooks
Event catalog, signatures, retries and examples.

### Recipes
Cross-resource workflows.

### Error catalog
Stable searchable error codes and recovery.

### Changelog
Developer-impacting changes with dates and migration instructions.

## Examples

Examples must be:

- complete enough to run;
- consistent with current version;
- safe by default;
- realistic;
- free of real customer PII;
- available in relevant official languages/SDKs only when maintained.

Never show pseudo-code as if it were production-ready code.

## Copy and test interactions

Developer docs should support:

- copy endpoint;
- copy request;
- copy code;
- copy response/error ID;
- test with sandbox credentials when safe;
- toggle language without losing page position;
- deep links to parameters/error codes.

## SDK philosophy

Do not create SDKs before the API contract is stable enough to justify their maintenance.

When SDKs are introduced:

- generated types may assist but do not replace hand-designed developer ergonomics;
- naming should feel native to the target language;
- retries/timeouts should be documented;
- version support policy must be explicit;
- examples must be tested in CI where practical.

## OpenAPI

OpenAPI is a contract input, not the complete developer experience.

Use it to support:

- reference generation;
- validation;
- typed clients where appropriate;
- schema diffing;
- contract tests.

Human-authored guides, errors, workflows and examples remain necessary.

## Sandbox/test data

Provide deterministic demo/test paths when the platform is ready.

Useful capabilities include:

- demo customers/vehicles;
- known estimate/work-order states;
- predictable webhook triggers;
- simulated failure scenarios;
- test payment/fiscal stubs where legally/technically appropriate.

Developers need to test failure, not only success.

## Failure-mode design

Document and test scenarios such as:

- expired/invalid credentials;
- missing permission;
- tenant mismatch/non-enumeration;
- validation failure;
- duplicate/idempotent request;
- stale version conflict;
- rate limiting;
- downstream integration unavailable;
- webhook signature invalid;
- webhook duplicate delivery;
- partial/asynchronous processing.

## AI-assisted documentation

NeriTech docs may expose AI assistance, but source-of-truth behavior remains the versioned documentation/API contract.

AI can:

- answer navigation questions;
- explain an error;
- build a sample request;
- summarize a migration.

AI must not invent unsupported endpoints/fields or hide uncertainty.

## Observability for integrators

Long-term platform surfaces should provide, by permission:

- API request logs;
- webhook delivery logs;
- integration health;
- recent errors;
- retry/replay capability;
- environment markers;
- correlation IDs.

Sensitive request/response data must be minimized/redacted.

## Developer accessibility

Docs are product UI and must meet accessibility expectations:

- keyboard navigation;
- visible focus;
- semantic tables/headings;
- code blocks readable at zoom;
- copy controls with accessible labels;
- mobile layouts that do not depend on three columns;
- contrast-compliant syntax themes.

## DX quality gate

Before calling an integration surface production-ready:

1. Can a developer complete the happy path from documentation alone?
2. Can they deliberately trigger and understand common failures?
3. Are sandbox and production impossible to confuse casually?
4. Are tenant/security semantics explicit?
5. Are retry and idempotency rules clear?
6. Can support trace a failure using a safe request ID?
7. Are relevant webhooks documented and testable?
8. Is the OpenAPI/schema synchronized with actual implementation?
9. Are code examples current and executable?
10. Is the migration/version story documented before a breaking change ships?
