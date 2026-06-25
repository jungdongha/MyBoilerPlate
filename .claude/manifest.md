# System Manifest
# Role: AI Agent Bootloader

system:
  name: MyBoilerPlate-Agent
  version: 1.0.0
  mode: strict-layered-mvc

boot_sequence:
  - order: 1
    file: .claude/core/essential-rules.yaml
    purpose: Load constraints & stack
  - order: 2
    file: .claude/CLAUDE.md
    purpose: Load index

resource_map:
  domain_model:
    path: .claude/references/data-model/**/*.yaml

agent_behavior:
  - rule: Always validate against 'essential-rules.yaml'.
  - rule: "Ask Protocol: If ambiguous, STOP and ASK."
  - rule: Load domain data-model only when the related domain is in scope.
