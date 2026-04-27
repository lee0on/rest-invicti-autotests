## Parameters
$ARGUMENTS = path to the updated spec file (e.g., "specs/booking-api-v2.yaml")
Optionally: path to the old spec for explicit diff ("specs/booking-api-v1.yaml specs/booking-api-v2.yaml")

## Actions
1. If two specs provided — diff them directly. If one spec provided — diff against the spec path registered in `.ai/config.yaml` for this API
2. Categorize each change:
    - **New endpoint**: method + path not present in old spec
    - **Removed endpoint**: method + path no longer in new spec
    - **Modified schema**: fields added, removed, renamed, or type-changed
    - **Modified endpoint**: URL, parameters, or response codes changed
3. For each new endpoint — run `/gen-full-suite` for that endpoint
4. For each modified schema — update affected payload classes, then check if request methods and test assertions need adjustment. Apply changes.
5. For each modified endpoint — update the corresponding request class method (URL, params, headers). Update affected checks.
6. For each removed endpoint — list affected files (request methods, checks, steps, mocks). Ask for confirmation before deleting.
7. Run Code Reviewer agent on all modified files
8. Verify full compilation and run affected tests
9. Print migration report: changes detected, files created, files modified, files suggested for deletion, test results