## Parameters
$ARGUMENTS = API name (e.g., "booking", "auth", "payment")

## Actions
1. Read `.ai/config.yaml` for base package, paths, and naming conventions
2. Check if directories for this API already exist under `payloads/`, `requests/`, `checks/` — abort with message if they do
3. Create directories:
    - `payloads/request/$ARGUMENTS/`
    - `payloads/response/$ARGUMENTS/`
    - `requests/` (if not exists)
    - `checks/` (if not exists)
4. If a spec file matching `$ARGUMENTS` exists in `specs/` — read it and list discovered endpoints
5. Print summary: created directories, discovered endpoints (if any), suggested next steps (`/gen-payload`, `/gen-request`, `/gen-check`)