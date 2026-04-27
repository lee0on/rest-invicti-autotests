## Parameters
$ARGUMENTS = API or endpoint scope plus options:
- "booking" — full suite for entire Booking API
- "POST /users --with-mocks --with-bdd" — single endpoint with mocks and BDD
- "specs/payment-api.yaml --with-mocks" — full API from spec with mocks

Flags: `--with-mocks` (generate WireMock stubs), `--with-bdd` (generate feature + steps)

## Actions
1. Read `.ai/config.yaml` and root `CLAUDE.md`
2. If spec file provided — use OpenAPI Parsing skill to extract all endpoints and schemas
3. Check what already exists for this API (payloads, requests, checks, mocks, features)
4. Execute in order, skipping what already exists:
    - Step 1: `/gen-payload` for each request and response schema
    - Step 2: `/gen-request` for each endpoint
    - Step 3: `/gen-check` for each endpoint
    - Step 4 (if `--with-mocks`): `/gen-mock` for each endpoint
    - Step 5 (if `--with-bdd`): `/gen-feature` for the API domain
5. Run Code Reviewer agent on all generated files
6. Fix any issues found by the reviewer
7. Verify full compilation
8. Print summary: total files generated, files per category, any warnings