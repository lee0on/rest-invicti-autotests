## Parameters
$ARGUMENTS = description of what to generate. Accepts either:
- spec reference: "BookingRequest from specs/booking-api.yaml"
- example body: "response payload for POST /users from this JSON: {...}"
- plain description: "request payload for user registration with username, email, password"

## Actions
1. Read `payloads/CLAUDE.md` for conventions and annotation rules
2. Parse the input — extract entity name, direction (request/response), fields, content type
3. If spec file referenced — use OpenAPI Parsing skill to extract schema
4. Check if a payload class for this entity already exists — abort with message if it does
5. Generate the payload class following POJO Generation skill:
    - Apply correct annotations for content type (JSON, XML, or both)
    - Request payloads: include Builder pattern
    - Response payloads: include `@JsonIgnoreProperties(ignoreUnknown = true)`
    - Include `toString()`, `equals()`, `hashCode()`
6. Place file in the correct sub-package: `payloads/request/{api}/` or `payloads/response/{api}/`
7. If this is a request payload — check if a matching factory method exists in `EasyRandomFactory`. If not — generate one with appropriate Faker providers for each field
8. Verify the file compiles