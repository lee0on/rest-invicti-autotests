# Skill: Error Scenario Mining

## Purpose
Systematically identify error scenarios and edge cases for an API endpoint.

## Mining Process

### Step 1: HTTP Method Semantics
- GET: not found, bad query params, forbidden
- POST: validation failure, duplicate/conflict, payload too large
- PUT: validation failure, not found, version conflict
- PATCH: invalid partial update, not found, unprocessable
- DELETE: not found, dependency conflict, double deletion

### Step 2: Authentication and Authorization
- No credentials / malformed / expired
- Valid but insufficient role or wrong tenant/ownership
- Disabled or deleted account

### Step 3: Request Body Validation
Per field in schema:
- Required: absent, null, empty, whitespace
- String: too long/short, wrong format, special chars, HTML/script
- Numeric: negative, zero, exceeds max, wrong type, decimal vs integer
- Date: invalid format, logical contradiction, past when future required
- Enum: outside set, empty, case mismatch

### Step 4: Path and Query Parameters
- Non-existent / negative / zero / non-numeric / very large resource ID
- Missing required query param, invalid value/type
- Out-of-range pagination

### Step 5: Business Logic Errors
- Duplicate creation, overlapping date ranges
- Action past deadline or outside time window
- Exceeding limits (credit, quantity, rate)
- Modifying terminal-state resource
- Circular/self-referencing relationships

### Step 6: Infrastructure and Resilience
- Timeout, upstream unavailable, malformed dependency response
- Large payload, concurrent modification, Content-Type mismatch

## Output Format
Per scenario: description, expected HTTP status, expected error message/behavior.
Organized by category, prioritized by likelihood and impact.
