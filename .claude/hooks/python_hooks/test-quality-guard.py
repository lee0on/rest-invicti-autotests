#!/usr/bin/env python3
"""PostToolUse hook: detects test anti-patterns in check files.

Checks for violations of checks/CLAUDE.md rules:
  - Missing @DisplayName on test methods
  - Missing @Tag on test class
  - assertNotNull without specific field assertions
  - assertTrue(x.contains(...)) pattern
  - Direct REST Assured imports
  - Hardcoded test data instead of factory usage
"""
import json
import os
import re
import sys


HARDCODED_PATTERNS = [
    (r'(?<!")\"test\d*\"', "hardcoded 'test' string"),
    (r'"aaa+"', "hardcoded dummy string"),
    (r'"dummy\w*"', "hardcoded 'dummy' string"),
    (r'"password\d*"', "hardcoded 'password' string"),
    (r'"123\d*"', "hardcoded numeric string"),
    (r'"foo\w*"', "hardcoded 'foo' string"),
    (r'"bar\w*"', "hardcoded 'bar' string"),
]


def analyze_test_file(file_path):
    try:
        with open(file_path, "r") as f:
            content = f.read()
    except (FileNotFoundError, PermissionError):
        return []

    lines = content.split("\n")
    warnings = []

    has_test_methods = bool(re.search(r"@Test\b", content))
    if not has_test_methods:
        return []

    if not re.search(r"@Tag\s*\(", content):
        warnings.append("Missing @Tag on test class — required for categorization (@smoke, @regression, etc.)")

    test_count = len(re.findall(r"@Test\b", content))
    display_count = len(re.findall(r"@DisplayName\s*\(", content))
    if display_count < test_count:
        warnings.append(
            "@DisplayName missing on {}/{} test methods — every test needs a human-readable name".format(
                test_count - display_count, test_count
            )
        )

    for i, line in enumerate(lines, 1):
        stripped = line.strip()

        if "assertNotNull" in stripped and "import" not in stripped:
            ctx_start = max(0, i - 4)
            ctx_end = min(len(lines), i + 4)
            context = "\n".join(lines[ctx_start:ctx_end])
            if not re.search(r"(assertEquals|assertThat|extracting|containsExactly)", context):
                warnings.append("Line {}: assertNotNull without specific field assertions".format(i))

        if re.search(r"assertTrue\s*\(.*\.contains\s*\(", stripped):
            warnings.append(
                "Line {}: assertTrue(x.contains(...)) — use deserialized field comparison".format(i)
            )

    for pattern, desc in HARDCODED_PATTERNS:
        for i, line in enumerate(lines, 1):
            stripped = line.strip()
            if stripped.startswith("//") or stripped.startswith("*") or "@DisplayName" in stripped:
                continue
            if re.search(pattern, stripped, re.IGNORECASE):
                warnings.append(
                    "Line {}: {} detected — use EasyRandomFactory for test data".format(i, desc)
                )
                break

    if re.search(r"import\s+(static\s+)?io\.restassured", content):
        warnings.append("Direct REST Assured import — use requests/ layer for HTTP calls")

    return warnings


def main():
    data = json.load(sys.stdin)
    file_path = data.get("tool_input", {}).get("file_path", "")

    if not file_path.endswith(".java"):
        sys.exit(0)

    if "/checks/" not in file_path:
        sys.exit(0)

    warnings = analyze_test_file(file_path)

    if warnings:
        text = "TEST QUALITY WARNINGS for {}:\n{}".format(
            os.path.basename(file_path),
            "\n".join("  ⚠ " + w for w in warnings),
        )
        json.dump(
            {
                "hookSpecificOutput": {
                    "hookEventName": "PostToolUse",
                    "additionalContext": text,
                }
            },
            sys.stdout,
        )

    sys.exit(0)


if __name__ == "__main__":
    main()
