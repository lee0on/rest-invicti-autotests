## Parameters
$ARGUMENTS = what to mock. Accepts either:
- endpoint: "POST /api/payment/charge"
- API name: "payment"
- scenario list: "GET /api/payment/status success, timeout, 500 error"

## Actions
1. Read `mocks/CLAUDE.md` and `wiremock/CLAUDE.md` for conventions
2. Parse the input — extract endpoints and desired scenarios
3. Check if a mock class for this API already exists
    - If exists — add new methods without modifying existing ones
    - If not — create new `{ApiName}Mock.java`
4. For each endpoint generate stub methods covering: success, bad request (400), server error (500), timeout
5. If user specified explicit scenarios — generate only those
6. Use realistic response bodies matching the corresponding response payload classes
7. Verify the file compiles