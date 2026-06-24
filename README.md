# REST API Test Automation Framework

A modular, layered framework for **REST API test automation** in Java, built around a
strict separation of concerns and a clean, extensible design. It exercises a public
REST API end‑to‑end with JUnit 5, drives the same endpoints through Cucumber BDD
scenarios, and isolates error/resilience cases behind a WireMock layer.

> Built as a hands‑on study of production‑grade API automation patterns — covering
> classic JUnit checks, BDD, contract‑faithful mocking, and data generation.

![Java](https://img.shields.io/badge/Java-22-orange)
![Maven](https://img.shields.io/badge/Build-Maven-blue)
![JUnit5](https://img.shields.io/badge/Tests-JUnit%205-green)
![REST Assured](https://img.shields.io/badge/HTTP-REST%20Assured-purple)
![Cucumber](https://img.shields.io/badge/BDD-Cucumber%207-brightgreen)
![WireMock](https://img.shields.io/badge/Mocking-WireMock-red)

---

## Highlights

These are the features that set this framework apart from a typical "tests in one class" project:

- **🧱 Strict three‑layer architecture** — `Payloads → Requests → Checks`. Each layer has a
  single responsibility and talks only to its direct neighbour. Tests never make HTTP calls
  directly, request classes never assert, and payloads carry no logic. The boundary is
  **checked by an automated architecture guard** during development, not just documented.
- **🔀 Dual content‑type coverage (JSON *and* XML)** — every entity is exercised over both
  formats using Jackson's `ObjectMapper` and `XmlMapper`, with dual‑annotated payload POJOs.
- **🎭 Contract‑faithful mocking with WireMock** — stub bodies are **serialized from the same
  payload POJOs the production code deserializes**, so mocks can't silently drift from the
  contract (no brittle hand‑written JSON blobs). The mock layer makes error and resilience
  scenarios — `404`, `500`, slow/timeout, connection reset — **deterministic and offline**,
  which the live demo API cannot reliably reproduce.
- **🥒 BDD on the JUnit 5 Platform** — Cucumber 7 feature files run through a `@Suite` runner.
  Step definitions form a thin, reusable "keyword" library that delegates straight into the
  request layer — Gherkin scenarios are composed purely by recombining existing steps.
- **🎲 Realistic randomized test data** — EasyRandom + JavaFaker factories generate
  domain‑appropriate values (names, emails, lorem text). No hardcoded `"test123"` dummies.
- **🌍 Environment‑driven configuration** — the API base URL is resolved at call time from a
  system property or environment variable, so the *same* suite runs against the live API or a
  WireMock server on a dynamic port — no code changes.
- **🤖 AI‑assisted, self‑documenting layout** — per‑package `CLAUDE.md` specs and an
  `.ai/config.yaml` encode the conventions, enabling consistent AI‑assisted generation of new
  coverage that respects the architecture.

---

## Tech Stack

| Concern          | Technology                          |
|------------------|-------------------------------------|
| Language         | Java 22                             |
| Build            | Maven                               |
| HTTP client      | REST Assured                        |
| Test runner      | JUnit 5 (Jupiter + Platform Suite)  |
| Serialization    | Jackson (JSON + XML)                |
| BDD              | Cucumber 7 + Gherkin                |
| Mocking          | WireMock                            |
| Test data        | EasyRandom + JavaFaker              |

**System under test:** Invicti's public, intentionally‑vulnerable demo API
(`rest.testsparker.com/basic_authentication/api`). The OpenAPI contract lives in
[`specs/invicti.yaml`](specs/invicti.yaml).

---

## Architecture

```
Payloads  →  Requests  →  Checks
 (data)      (HTTP)        (assertions)
```

| Layer        | Responsibility                                              | Rule                                            |
|--------------|-------------------------------------------------------------|-------------------------------------------------|
| `payloads/`  | POJOs for request/response bodies (JSON + XML annotations)  | Pure data — no logic, no HTTP                   |
| `factories/` | EasyRandom + Faker generators                               | One generator per entity                        |
| `requests/`  | REST Assured HTTP abstractions, one class per API           | The **only** place HTTP calls & URLs exist      |
| `checks/`    | JUnit 5 test classes                                        | Call `requests/` only — **never** import REST Assured |
| `steps/`     | Cucumber glue (thin orchestration)                          | Delegate to `requests/`, hold no HTTP logic     |
| `mocks/`     | WireMock stub configurations                                | Register stubs only — no assertions             |

The framework keeps the **reusable core** (`payloads`, `requests`, `factories`, `utils`,
`steps`) under `src/main/java` and all **executable tests** (`checks`, `mocks`, the Cucumber
runner) under `src/test/java`.

---

## Project Structure

```
src/
├── main/java/org/example/
│   ├── payloads/
│   │   ├── request/        # *RequestPayload  — builder + Jackson/XML annotations
│   │   └── response/       # *ResponsePayload — builder + Jackson/XML annotations
│   ├── requests/           # Api (base URL), AuthHelper, {Entity}Api  (REST Assured)
│   ├── factories/          # EasyRandomFactory — Faker‑backed generators
│   ├── steps/              # Cucumber step definitions (glue)
│   └── utils/              # XMLUtils and shared helpers
├── test/
│   ├── java/
│   │   ├── checks/         # JUnit 5 tests (live API + WireMock‑backed)
│   │   ├── mocks/          # WireMock stubs
│   │   └── runners/        # Cucumber @Suite runner
│   └── resources/
│       └── features/       # Gherkin feature files
└── specs/                  # OpenAPI specification (invicti.yaml)
```

---

## Getting Started

### Prerequisites
- JDK 22+
- Maven 3.9+

### Build
```bash
mvn clean compile        # compile the core framework
mvn clean test-compile   # compile tests too
```

---

## Running Tests

```bash
# Run everything (JUnit checks + BDD suite)
mvn clean test

# Offline, deterministic — WireMock‑backed tests only (no network)
mvn test -Dtest=CommentMockTest

# A single JUnit class / method
mvn test -Dtest=CommentTest
mvn test -Dtest=CommentTest#createCommentReturnsCreatedBody

# BDD scenarios via the Cucumber suite
mvn test -Dtest=RunCucumberTest
```

### Coverage at a glance

| Entity   | JUnit checks | BDD | WireMock |
|----------|:------------:|:---:|:--------:|
| User     | 19           |  –  |    –     |
| Post     | 17           |  –  |    –     |
| Comment  | 15           |  ✅ (12 scenarios) | ✅ (4 tests) |

> The BDD and mocking layers are demonstrated in depth on the **Comment** domain as a
> reference implementation of each technique; the JUnit layer spans all three entities.

---

## Configuration

The API base URL is resolved at call time in the following order:

1. `api.baseUrl` system property
2. `API_BASE_URL` environment variable
3. Built‑in default (the Invicti demo site)

```bash
# Point the whole suite at a different environment
API_BASE_URL=https://my-staging.example.com/api/ mvn test
```

This seam is what lets the WireMock tests redirect `CommentApi` to a server on a
dynamically assigned port without touching production code.

---

## A note on the live demo API

The default target is an **intentionally‑vulnerable demo site**, so it does not implement
proper error handling: several negative‑path requests return `200`/`500` where a correct API
would return `404`/`400`. As a result, some live negative checks fail by design of the *target*,
not of the framework. This is precisely why the **WireMock layer** exists — it provides the
deterministic, correct error responses needed to validate the client's handling. CI should run
the offline mock + happy‑path tests; live negative scenarios are best filtered out or pointed
at a conformant environment.

---