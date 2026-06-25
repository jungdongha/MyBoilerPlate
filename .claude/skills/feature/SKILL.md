---
name: feature
description: Automate complete feature development from context loading through design to implementation and testing. Use when user says "새 기능 추가", "기능 개발", "/feature", or provides a feature specification.
---

# Feature Development Skill

You are a Backend Developer for the MyBoilerPlate project.

## Purpose

Automate the complete feature development workflow from context loading through design to implementation and testing.

## Trigger Scenarios

- User requests a new feature
- User says "새 기능 추가", "기능 개발", "기능 구현"
- User invokes `/feature [FeatureName]`
- User provides a feature specification file
- User describes business requirements

## Execution Flow

### Phase 1: Context Loading

**MUST READ:**
1. `CLAUDE.md` - Project overview
2. `core/essential-rules.yaml` - Non-negotiable rules
3. `references/data-model/{domain}/*.yaml` - Target domain entity

**CONDITIONAL READ:**
- `references/conventions/coding-style.yaml` - When unsure about style
- `references/conventions/testing-guide.yaml` - When writing tests

**FORBIDDEN:**
- Do NOT load unrelated domain YAMLs
- Do NOT read entire codebase

### Phase 2: Feature Design

**IF** spec file provided:
1. Read spec file → validate completeness → **SKIP** to implementation

**ELSE:**
1. Use template from `.claude/docs/templates/feature-spec.yaml`
2. Create spec → present to user → **WAIT FOR APPROVAL** before implementing

**Constraints (CRITICAL):**
- Do NOT modify provided spec without user permission
- Implement ONLY what's in the spec (No over-engineering)
- Follow layer architecture: Controller → Service → Repository

### Phase 3: Implementation

> 반드시 이 순서대로 생성: **Entity → Repository → ExceptionInformation → Exception → GetService → Service → DTO → SwaggerInterface → Controller**

**Entity:**
```java
@Entity
@Table(name = "{name}s")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder                          // @Builder 아님! BaseEntity 상속 때문
public class {Name} extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fieldName;

    public void update{Field}(String value) {   // setter 금지, 도메인 메서드로
        this.fieldName = value;
    }
}
```

**Repository:**
```java
public interface {Name}Repository extends JpaRepository<{Name}, Long> {
}
```

**ExceptionInformation (에러 코드 enum):**
```java
@Getter
@AllArgsConstructor
public enum {Name}ExceptionInformation implements ExceptionInformation {
    {NAME}_NOT_FOUND(HttpStatus.NOT_FOUND, "{PREFIX}-001", "존재하지 않는 {name}입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
```
> 에러 코드 형식: `{PREFIX}-{3자리}` (예: MEM-001, AUTH-001)

**Exception (throw 시 사용):**
```java
public class {Name}Exception extends BaseException {
    public {Name}Exception({Name}ExceptionInformation info) {
        super(info.getHttpStatus(), info.getCode(), info.getMessage());
    }
}
```

**GetService (조회 전용 — readOnly):**
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class {Name}GetService {
    private final {Name}Repository {name}Repository;

    public {Name} get{Name}(Long id) {
        return {name}Repository.findById(id)
                .orElseThrow(() -> new {Name}Exception({Name}ExceptionInformation.{NAME}_NOT_FOUND));
    }

    public {Name}Response get{Name}Response(Long id) {
        return {Name}Response.from(get{Name}(id));
    }
}
```

**Service (CUD 전용 — write):**
```java
@Service
@RequiredArgsConstructor
@Transactional
public class {Name}Service {
    private final {Name}Repository {name}Repository;
    private final {Name}GetService {name}GetService;

    public {Name}Response create{Name}({Name}CreateRequest request) {
        {Name} entity = {Name}.builder()
                .fieldName(request.fieldName())
                .build();
        return {Name}Response.from({name}Repository.save(entity));
    }
}
```

**DTO (Java Record 필수):**
```java
// Request
public record {Name}CreateRequest(String fieldName) {}

// Response — static factory from() 필수
public record {Name}Response(Long id, String fieldName) {
    public static {Name}Response from({Name} entity) {
        return new {Name}Response(entity.getId(), entity.getFieldName());
    }
}
```

**Swagger Interface:**
```java
@Tag(name = "{Name}", description = "{Name} API")
public interface {Name}Swagger {
    @Operation(summary = "생성")
    ApiResponse<{Name}Response> create{Name}(@RequestBody {Name}CreateRequest request);

    @Operation(summary = "조회")
    ApiResponse<{Name}Response> get{Name}(@PathVariable Long id);
}
```

**Controller (Swagger implements 필수):**
```java
@RestController
@RequestMapping("/api/{names}")
@RequiredArgsConstructor
public class {Name}Controller implements {Name}Swagger {
    private final {Name}Service {name}Service;
    private final {Name}GetService {name}GetService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<{Name}Response> create{Name}(@RequestBody {Name}CreateRequest request) {
        return ApiResponse.response(HttpStatus.CREATED, "생성 성공", {name}Service.create{Name}(request));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<{Name}Response> get{Name}(@PathVariable Long id) {
        return ApiResponse.response(HttpStatus.OK, "조회 성공", {name}GetService.get{Name}Response(id));
    }
}
```

**Package locations:**
```
com.back.myboilerplate.domain.{domain}/
  controller/{Name}Controller.java       ← Swagger 구현
  controller/{Name}Swagger.java          ← Swagger 인터페이스
  dto/request/{Name}CreateRequest.java
  dto/response/{Name}Response.java
  entity/{Name}.java
  repository/{Name}Repository.java
  service/{Name}Service.java             ← CUD
  service/{Name}GetService.java          ← Read
  exception/{Name}ExceptionInformation.java
  exception/{Name}Exception.java
```

### Phase 4: Tests

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("{Name}Service")
class {Name}ServiceTest {

    @Mock
    private {Name}Repository {name}Repository;

    @InjectMocks
    private {Name}Service {name}Service;

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("유효한 요청이 주어지면 {name}을 저장하고 반환한다")
        void creates{Name}Successfully() {
            // given / when / then
        }
    }
}
```

**Run tests:**
```bash
./gradlew test --tests "*{Name}*"
./gradlew build
```

## Validation Checklist

- [ ] Entity → Repository → Service → Controller 순서 구현
- [ ] ApiResponse<T> 사용
- [ ] Response DTO에 static factory `from()` 메서드 사용
- [ ] Exception enum이 ExceptionInformation 구현
- [ ] @RequiredArgsConstructor 사용 (필드 주입 금지)
- [ ] softDelete() 사용 (물리 삭제 금지)
- [ ] 테스트 작성 및 통과
- [ ] 빌드 성공: `./gradlew build`

## Report Format

```
✅ Feature Implemented: [FeatureName]

📋 Specification:
  - Domain: [Domain]
  - API: [Method] [Path]
  - Business Logic: [Brief description]

📁 Files Created:
  - controller/{Name}Controller.java
  - service/{Name}Service.java
  - entity/{Name}.java
  - repository/{Name}Repository.java
  - dto/request/Create{Name}Request.java
  - dto/response/{Name}Response.java
  - exception/{Name}Exception.java
  - test/{Name}ServiceTest.java

🧪 Test: ✅ PASSED
🏗️ Build: ✅ SUCCESS
```
