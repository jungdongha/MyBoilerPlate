---
name: refactor
description: Improve code structure, readability, or performance without changing external behavior. Use when user says "리팩토링", "코드 개선", "/refactor", or wants to improve code quality without adding features.
---

# Refactor Skill

You are a Code Quality Engineer for the MyBoilerPlate project.

## Purpose

Improve code structure, readability, or performance without changing external behavior or API contracts.

## Core Principle

**Zero Behavior Change:** Input/Output must remain EXACTLY the same. API contracts, business logic results, and side effects must not change.

## Execution Flow

### Phase 1: Context Loading

**MUST READ:**
1. `CLAUDE.md` - Project overview
2. `core/essential-rules.yaml` - Non-negotiable rules
3. `references/conventions/coding-style.yaml` - Code style standards
4. Target file(s) to be refactored

### Phase 2: Analysis

**Common issues to look for:**
- Layer violations (Controller calling Repository directly)
- Field injection (@Autowired) instead of constructor injection
- Missing @RequiredArgsConstructor
- @Setter usage on Entity
- Physical delete instead of softDelete()
- Direct DTO constructor call instead of static factory `from()`
- Long methods (>30 lines)
- Deep nesting (>3 levels)
- Duplicate logic across services

**Safety check before refactoring:**
```bash
./gradlew test --tests "*{TargetClass}*"
```
All tests must be GREEN before starting.

### Phase 3: Refactoring

**Step-by-step (each step must keep tests green):**
1. Apply change
2. Run: `./gradlew compileJava`
3. Run: `./gradlew test --tests "*{TargetClass}*"`
4. Proceed to next step only if green

**Common refactoring patterns:**

Extract Method:
```java
// Before
public ApiResponse<MemberResponse> create(...) {
    // 20 lines of logic
}

// After
public ApiResponse<MemberResponse> create(...) {
    validate(request);
    Member member = buildMember(request);
    return ApiResponse.response(HttpStatus.CREATED, "생성 성공", MemberResponse.from(save(member)));
}
```

Fix layer violation:
```java
// Before (Controller → Repository — WRONG)
memberRepository.findById(id)

// After (Controller → Service — CORRECT)
memberService.findById(id)
```

### Phase 4: Verification

```bash
./gradlew test          # All tests must pass
./gradlew build         # Full build must succeed
```

## Validation Checklist

- [ ] All tests pass BEFORE refactoring
- [ ] No behavior change (same inputs → same outputs)
- [ ] All tests pass AFTER refactoring
- [ ] Build successful
- [ ] No new features added

## Forbidden Actions

- Change external behavior or API contracts
- Skip pre-refactoring test run
- Add new features during refactoring
- Complete with failing tests or build
