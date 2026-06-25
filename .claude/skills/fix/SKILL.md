---
name: fix
description: Automate bug fix workflow from root cause analysis to fix and regression testing. Use when user says "버그 수정", "에러 해결", "/fix", reports a bug, or describes unexpected behavior.
---

# Bug Fix Skill

You are a Debugger + Fixer for the MyBoilerPlate project.

## Purpose

Automate the bug fix workflow from root cause analysis to fix implementation and regression testing.

## Trigger Scenarios

- User reports a bug or unexpected behavior
- User says "버그 수정", "에러 해결", "오류 고쳐줘"
- User invokes `/fix [BugDescription]`
- User provides error messages or stack traces

## Execution Flow

### Phase 1: Context Loading

**MUST READ:**
1. `CLAUDE.md` - Project overview
2. `core/essential-rules.yaml` - Core rules and tech stack
3. `references/conventions/testing-guide.yaml` - Testing conventions

**CONDITIONAL READ:**
- Target domain entity YAML if bug is entity/data related

### Phase 2: Root Cause Analysis

**Investigation order:** Controller → Service → Repository → Entity

**Output format:**
```
🐛 Bug: [Brief description]
📍 Location: [FilePath:LineNumber]
🔍 Root Cause: [Specific cause, not symptoms]
💡 Hypothesis: [Why this bug occurred]
🎯 Fix Strategy: [Proposed solution]
⚠️ Impact: [Affected features/APIs]
```

**MUST ASK when:**
- Multiple fix strategies exist with trade-offs
- API contract or behavior change needed
- Data cleanup/migration required

### Phase 3: Fix & Test

**Rules:**
- Modify ONLY code directly related to the bug
- Do NOT touch unrelated code
- Fix root cause, not symptoms
- Add regression test that confirms the fix

**Regression test pattern:**
```java
@Test
@DisplayName("버그 재현: [버그 설명]")
void reproduceBug() {
    // given — 버그 재현 조건
    // when — 버그 발생 동작
    // then — 수정 후 기대 결과
}
```

**Verification:**
```bash
./gradlew compileJava
./gradlew test --tests "*{AffectedClass}*"
./gradlew test
```

## Validation Checklist

- [ ] Root cause identified and documented
- [ ] Minimal fix applied (no extra changes)
- [ ] New test added (reproduces bug + verifies fix)
- [ ] Test failed before fix, passes after fix
- [ ] All existing tests pass
- [ ] Build successful: `./gradlew build`

## Report Format

```
✅ Bug Fixed: [Brief description]

🐛 Bug: [What was wrong]
📍 Location: [FilePath:LineNumber]
🔍 Root Cause: [Why it happened]
🔧 Fix Applied: [What was changed]

📝 Files Modified:
  - [file] (lines modified)
  - [test file] (added test)

🧪 Tests: ✅ PASSED
✅ Build: SUCCESS
```

## Forbidden Actions

- Skip root cause analysis
- Include unrelated refactoring
- Fix without adding test
- Complete with build/test failures
- Use English test names (must use Korean @DisplayName)
