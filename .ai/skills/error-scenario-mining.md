# Skill: Error Scenario Mining

## Purpose
Systematically identify error scenarios and edge cases for an API endpoint.
Produces a categorized list of negative test cases.

## Mining Process

### Step 1: HTTP Method Semantics
Identify common errors per HTTP method:
- GET: resource not found, bad query parameters, forbidden access
- POST: validation failure, duplicate/conflict, payload too large
- PUT: validation failure, target not found, version conflict
- PATCH: invalid partial update, target not found, unprocessable content
- DELETE: target not found, dependency conflict, double deletion

### Step 2: Authentication and Authorization
Map out access control scenarios:
- No credentials provided
- Malformed or corrupted credentials
- Expired credentials
- Valid credentials but insufficient role or permissions
- Valid credentials but wrong tenant or resource ownership
- Credentials for a disabled or deleted account

### Step 3: Request Body Validation
For each field in the request schema, enumerate:
- Required fields: absent, null, empty, whitespace-only
- String fields: too long, too short, wrong format, special characters,
  HTML/script content
- Numeric fields: negative, zero, exceeds maximum, wrong type (string
  instead of number), decimal instead of integer
- Date fields: invalid format, logical contradiction (end before start),
  past date when future required
- Enum fields: value not in allowed set, empty string, case mismatch

### Step 4: Path and Query Parameters
- Non-existent resource identifier
- Negative or zero identifier
- Non-numeric identifier when numeric expected
- Very large identifier
- Missing required query parameter
- Invalid query parameter value or type
- Out-of-range pagination values

### Step 5: Business Logic Errors
Domain-specific rules that cause rejection:
- Duplicate resource creation
- Overlapping date ranges with existing records
- Action past deadline or outside allowed time window
- Exceeding limits (credit, quantity, rate)
- Modifying a resource in a terminal state (completed, cancelled)
- Circular or self-referencing relationships

### Step 6: Infrastructure and Resilience
Operational failure modes:
- Request timeout (server too slow)
- Upstream dependency unavailable
- Malformed response from dependency
- Very large request payload
- Concurrent modification conflict
- Content-Type mismatch (sending wrong format)

## Output Format
For each scenario: brief description, expected HTTP status code,
expected error message or behavior. Organized by category (validation,
auth, business, infrastructure). Prioritized by likelihood and impact.
Ready for direct consumption by Check Designer or BDD Writer.