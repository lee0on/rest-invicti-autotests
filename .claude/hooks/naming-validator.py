#!/usr/bin/env python3
"""PreToolUse hook: validates file naming conventions per package.

Naming rules from CLAUDE.md:
  checks/            -> {Feature}Test.java
  requests/          -> {ApiName}Api.java  (exceptions: Api.java, AuthHelper.java)
  payloads/response/ -> {Entity}Response.java
  factories/         -> {Name}Factory.java
  utils/             -> {Concern}Utils.java
  mocks/             -> {ApiName}Mock.java
  steps/             -> {Domain}Steps.java
  features/          -> {domain}.feature
"""
import json
import os
import sys


NAMING_RULES = [
    {
        "path": "/payloads/response/",
        "suffix": "Response.java",
        "exceptions": [],
        "message": "Response payloads must end with 'Response.java' (e.g., UserResponse.java)",
    },
    {
        "path": "/checks/",
        "suffix": "Test.java",
        "exceptions": [],
        "message": "Test classes in checks/ must end with 'Test.java' (e.g., BookingTest.java)",
    },
    {
        "path": "/requests/",
        "suffix": "Api.java",
        "exceptions": ["Api.java", "AuthHelper.java"],
        "message": "Request classes in requests/ must end with 'Api.java' (e.g., BookingApi.java)",
    },
    {
        "path": "/factories/",
        "suffix": "Factory.java",
        "exceptions": [],
        "message": "Factory classes must end with 'Factory.java' (e.g., EasyRandomFactory.java)",
    },
    {
        "path": "/utils/",
        "suffix": "Utils.java",
        "exceptions": [],
        "message": "Utility classes must end with 'Utils.java' (e.g., DateUtils.java)",
    },
    {
        "path": "/mocks/",
        "suffix": "Mock.java",
        "exceptions": [],
        "message": "Mock classes must end with 'Mock.java' (e.g., BookingMock.java)",
    },
    {
        "path": "/steps/",
        "suffix": "Steps.java",
        "exceptions": [],
        "message": "Step classes must end with 'Steps.java' (e.g., BookingSteps.java)",
    },
    {
        "path": "/resources/features/",
        "suffix": ".feature",
        "exceptions": [],
        "message": "Feature files must end with '.feature' (e.g., booking.feature)",
    },
]


def main():
    data = json.load(sys.stdin)

    if data.get("tool_name") != "Write":
        sys.exit(0)

    file_path = data.get("tool_input", {}).get("file_path", "")
    filename = os.path.basename(file_path)

    if filename == "CLAUDE.md":
        sys.exit(0)

    if not (file_path.endswith(".java") or file_path.endswith(".feature")):
        sys.exit(0)

    for rule in NAMING_RULES:
        if rule["path"] in file_path:
            if filename in rule["exceptions"]:
                sys.exit(0)
            if not filename.endswith(rule["suffix"]):
                reason = "NAMING CONVENTION VIOLATION:\n  {}\n  Got: {}".format(
                    rule["message"], filename
                )
                print(reason, file=sys.stderr)
                sys.exit(2)
            break

    sys.exit(0)


if __name__ == "__main__":
    main()
