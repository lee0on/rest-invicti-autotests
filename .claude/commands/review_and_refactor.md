## Parameters
$ARGUMENTS = scope of review. Accepts either:
- file path: "src/test/java/org/example/checks/BookingTest.java"
- directory: "src/main/java/org/example/requests/"
- API name: "booking" (reviews all files related to this API)
- "all" (entire framework)

## Actions

### Phase 1: Review
1. Collect all files in scope
2. Run Code Reviewer agent checklist against each file:
    - Architecture: layer separation, no hardcoded URLs, no misplaced logic
    - Naming: class/method/variable naming follows conventions
    - Test quality: Arrange-Act-Assert, specific assertions, independence, tags, DisplayName
    - Compatibility: imports, compilation, no conflicts
3. Run Refactoring Advisor agent analysis:
    - Duplication across files
    - Unused code (unreferenced payloads, uncalled methods, dead steps)
    - Missing coverage (endpoints without checks, missing negative scenarios)
    - Extraction opportunities (repeated logic → utils or factory methods)
4. Produce review report: each finding with severity (ERROR, WARNING, INFO), location, description

### Phase 2: Refactor
5. Present the review report and ask for confirmation to proceed
6. Apply fixes for all ERROR findings automatically
7. Apply fixes for WARNING findings automatically
8. For each fix: modify the file, verify it still compiles
9. Re-run the review on modified files to confirm all ERRORs and WARNINGs are resolved
10. Print changelog: per file, what was changed and why