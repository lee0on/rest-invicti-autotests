#!/usr/bin/env python3
"""PostToolUse hook: runs Maven compilation after Java file changes.

Reports compilation errors as feedback to the agent so it can fix them immediately.
"""
import json
import os
import subprocess
import sys


def main():
    data = json.load(sys.stdin)
    file_path = data.get("tool_input", {}).get("file_path", "")

    if not file_path.endswith(".java"):
        sys.exit(0)

    project_dir = os.environ.get("CLAUDE_PROJECT_DIR", "")
    if not project_dir:
        sys.exit(0)

    try:
        result = subprocess.run(
            ["mvn", "compile", "-q"],
            cwd=project_dir,
            capture_output=True,
            text=True,
            timeout=90,
        )
    except subprocess.TimeoutExpired:
        json.dump(
            {
                "hookSpecificOutput": {
                    "hookEventName": "PostToolUse",
                    "additionalContext": "Maven compilation timed out (>90s). Check for dependency issues.",
                }
            },
            sys.stdout,
        )
        sys.exit(0)
    except FileNotFoundError:
        sys.exit(0)

    if result.returncode != 0:
        error_output = (result.stderr + "\n" + result.stdout).strip()
        lines = error_output.split("\n")
        if len(lines) > 40:
            lines = lines[-40:]
        truncated = "\n".join(lines)

        json.dump(
            {
                "hookSpecificOutput": {
                    "hookEventName": "PostToolUse",
                    "additionalContext": "COMPILATION FAILED after editing {}:\n{}\n\nFix compilation errors before continuing.".format(
                        os.path.basename(file_path), truncated
                    ),
                }
            },
            sys.stdout,
        )

    sys.exit(0)


if __name__ == "__main__":
    main()
