---
mode: tester-agent
description: "Generate, run, and improve tests using JaCoCo coverage feedback."
model: GPT-5.2
tools:
  - read
  - edit
  - execute
  - search
  - se333-MCP-server/spec_testgen
---

## Follow instructions below: ##(Phase 3)
1. Write or improve JUnit tests for this Maven project.
2. Run `mvn test`.
3. If tests fail, debug and fix the issue.
4. Find `target/site/jacoco/jacoco.xml`.
5. Identify uncovered methods, lines, or branches.
6. Add tests targeting those gaps.
7. Re-run tests and compare coverage.
8. Repeat until coverage improves.

---

## Git + Trunk-Based Branching Model (Phase 4 — Required)

### Repo + Branch Rules
- Trunk branch is `main`.
- NEVER commit directly to `main`.
- Every change must happen in a short-lived feature branch named: `feature/<short-description>`.

### Required Workflow for ANY code/test change
1. Verify Git repository:
- If the current directory is not a Git repository, initialize it.
2. Configure remote repository:
- Ensure the GitHub repository `furqansaddal23/assign_1` is set as the `origin` remote.
- If `origin` exists but points elsewhere, replace it.
3. Ensure trunk branch:
- Ensure the trunk branch is named `main`.
- Ensure work starts from `main`, but do not commit to it.
4. Create a short-lived feature branch:
- Create and switch to `feature/<short-description>`.
5. Make changes:
- Implement test/code updates.
- Run `mvn clean test`.
- Confirm JaCoCo XML exists at `target/site/jacoco/jacoco.xml`.
6. Commit and push:
- Commit with a meaningful message describing the change.

- Push the feature branch to GitHub.
7. Create Pull Request:
- Create a PR from the feature branch into `main`.
- Include a short summary + test/coverage result.
8. Merge to trunk (policy below):
- Default: DO NOT auto-merge.
- Wait for human approval unless explicitly instructed otherwise.

### Automation Policy (Design Choice + Justification)
- PRs are created **immediately** (not batched) to maximize auditability and reduce risk.
- Merges require **manual approval** to keep human oversight and prevent accidental regressions.
- Quality gate: require `mvn clean test` locally before PR creation (CI gating can be added later).
- Autonomy level: agent can create branches/commits/PRs automatically, but merging is human-controlled by default.

---

## Phase 5 — Specification-Based Test Generation (ECP + BVA)
When adding or improving tests:
1) Call `se333-MCP-server/spec_testgen` with a short plain-English spec.
2) Convert returned cases into JUnit tests (one test per case).
3) Run `mvn clean test` and confirm `target/site/jacoco/jacoco.xml` exists.
4) Track changes via the Phase 4 trunk workflow (feature branch → commit → PR).
