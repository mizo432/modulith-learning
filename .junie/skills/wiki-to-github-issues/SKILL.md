---
name: wiki-to-github-issues
description: >
  Analyze GitHub Wiki requirements and create related GitHub Issues by
  decomposing each requirement into Frontend and Backend implementation work.
  Use this skill when converting GitHub Wiki requirements into actionable
  GitHub Issues.
---

# GitHub Wiki to Frontend / Backend Issues

## Purpose

Convert requirements described in a GitHub Wiki into actionable GitHub Issues.

The skill analyzes the Wiki, identifies the business capability, decomposes
the work into Frontend and Backend responsibilities, defines the API Contract,
checks existing GitHub Issues for duplicates, and creates GitHub Issues through
GitHub MCP only after explicit user approval.

The Wiki itself must never be modified.

---

# 1. Overall Workflow

Follow this workflow strictly.

```text
GitHub Wiki
    |
    v
Read Wiki
    |
    v
Analyze Requirements
    |
    v
Epic / Feature / User Story
    |
    v
Decompose Implementation
    |
    +----------------------+
    |                      |
    v                      v
Frontend Issue        Backend Issue
    |                      |
    +----------+-----------+
               |
               v
         API Contract
               |
               v
      Existing Issue Check
               |
               v
       Present Issue Plan
               |
               v
       Explicit User Approval
               |
               v
     Create GitHub Issues
               |
               v
       Link Related Issues
               |
               v
       Report Created Issues
