#!/usr/bin/env python3
"""PreToolUse hook: validates three-layer architecture separation.

Layers:
  checks/   -> NEVER imports REST Assured or makes HTTP calls
  requests/ -> NEVER contains assertions
  payloads/ -> pure data classes, no HTTP/test/business logic
  utils/    -> no direct HTTP calls
"""
import json
import sys
import re


LAYER_RULES = {
    "/checks/": {
        "forbidden": [
            (r"import\s+(static\s+)?io\.restassured", "checks/ must NOT import REST Assured — use requests/ layer for HTTP calls"),
        ],
    },
    "/requests/": {
        "forbidden": [
            (r"import\s+(static\s+)?org\.junit\.jupiter\.api\.Assertions", "requests/ must NOT import JUnit assertions — assertions belong in checks/"),
            (r"\b(assertEquals|assertTrue|assertFalse|assertNotNull|assertThat|assertAll)\s*\(", "requests/ must NOT contain assertion calls — return Response, assert in checks/"),
        ],
    },
    "/payloads/": {
        "forbidden": [
            (r"import\s+(static\s+)?io\.restassured", "payloads/ must be pure data classes — no REST Assured imports"),
            (r"import\s+(static\s+)?org\.junit", "payloads/ must be pure data classes — no JUnit imports"),
            (r"import\s+com\.github\.tomakehurst\.wiremock", "payloads/ must be pure data classes — no WireMock imports"),
        ],
    },
    "/utils/": {
        "forbidden": [
            (r"import\s+(static\s+)?io\.restassured", "utils/ must NOT import REST Assured — HTTP logic belongs in requests/"),
        ],
    },
}


def get_content(tool_name, tool_input):
    if tool_name == "Write":
        return tool_input.get("content", "")
    elif tool_name == "Edit":
        return tool_input.get("new_string", "")
    return ""


def main():
    data = json.load(sys.stdin)
    tool_input = data.get("tool_input", {})
    file_path = tool_input.get("file_path", "")

    if not file_path.endswith(".java"):
        sys.exit(0)

    content = get_content(data.get("tool_name", ""), tool_input)
    if not content:
        sys.exit(0)

    violations = []
    for layer_path, rules in LAYER_RULES.items():
        if layer_path in file_path:
            for pattern, message in rules["forbidden"]:
                if re.search(pattern, content):
                    violations.append(message)
            break

    if violations:
        reason = "ARCHITECTURE VIOLATION in {}:\n{}".format(
            file_path.split("/")[-1],
            "\n".join("  • " + v for v in violations),
        )
        print(reason, file=sys.stderr)
        sys.exit(2)

    sys.exit(0)


if __name__ == "__main__":
    main()
