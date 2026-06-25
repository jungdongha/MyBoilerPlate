---
name: myboilerplate
description: >
  MyBoilerPlate 프로젝트에서 코드 작업 시 반드시 사용. 새 도메인/기능 추가, 엔티티 생성,
  API 개발, 예외 처리, 서비스 작성 등 이 프로젝트와 관련된 모든 Java/Spring 작업에 이 스킬을 사용할 것.
  "도메인 추가해줘", "API 만들어줘", "엔티티 만들어줘", "서비스 작성해줘" 등의 요청이 오면 이 스킬을 사용.
---

# MyBoilerPlate 프로젝트 가이드

## 프로젝트 개요

Spring Boot 4.0.5 기반 백엔드 보일러플레이트. Java 17, Gradle(Kotlin DSL) 사용.
Spring AI를 통해 Claude(Anthropic), Gemini(Google), Groq(OpenAI-compatible) 세 가지 AI 모델을 지원.
인증은 JWT + Redis(RefreshToken 저장) 방식, DB는 H2(인메모리), 문서화는 Swagger(springdoc).

**Base package**: `com.back.myboilerplate`

---

## 디렉토리 구조

```
src/main/java/com/back/myboilerplate/
├── MyBoilerPlateApplication.java
├── domain/
│   └── {domainName}/
│       ├── controller/
│       │   ├── {Domain}Controller.java      # 실제 컨트롤러
│       │   └── {Domain}Swagger.java         # Swagger 인터페이스
│       ├── dto/
│       │   ├── request/
│       │   │   └── {Domain}CreateRequest.java
│       │   └── response/
│       │       └── {Domain}Response.java
│       ├── entity/
│       │   └── {Domain}.java
│       ├── exception/
│       │   ├── {Domain}Exception.java
│       │   └── {Domain}ExceptionInformation.java
│       ├── repository/
│       │   └── {Domain}Repository.java
│       └── service/
│           ├── {Domain}Service.java         # CUD (쓰기)
│           └── {Domain}GetService.java      # Read (조회)
└── global/
    ├── advisor/                             # Spring AI 로깅 어드바이저
    ├── common/
    │   ├── entity/BaseEntity.java
    │   ├── exception/
    │   │   ├── BaseException.java
    │   │   ├── ErrorCode.java
    │   │   ├── ErrorDetail.java
    │   │   ├── ExceptionInformation.java    # 인터페이스
    │   │   └── GlobalExceptionHandler.java
    │   └── response/
    │       ├── ApiResponse.java
    │       ├── PageResponse.java
    │       └── SliceResponse.java
    ├── config/                              # AiConfig, SecurityConfig, RedisConfig 등
    ├── properties/                          # AiProperties, JwtProperties
    └── security/
        ├── CustomUserDetails.java
        ├── CustomUserDetailsService.java
        ├── SecurityConfig.java
        ├── jwt/
        │   ├── JwtFilter.java
        │   └── JwtProvider.java
        └── redis/
            └── RefreshTokenRepository.java
```

---

## 새 도메인 생성 순서

새 도메인을 추가할 때는 반드시 아래 순서대로 생성한다.

**Entity → Repository → ExceptionInformation → Exception → GetService → Service → DTO → SwaggerInterface → Controller**

---

## 코드 패턴 (반드시 준수)

### 1. Entity

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@Table(name = "{table_name}s")
public class {Domain} extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 필드들...

    // 변경 메서드는 엔티티 안에 정의 (setter 사용 금지)
    public void update{Field}({Type} value) {
        this.{field} = value;
    }
}
```

- `BaseEntity` 상속 필수 (createdAt, updatedAt 자동 관리)
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 필수
- `@SuperBuilder` 사용 (`@Builder` 아님)
- setter 대신 도메인 메서드 정의

### 2. Repository

```java
public interface {Domain}Repository extends JpaRepository<{Domain}, Long> {
    // 필요한 쿼리 메서드
}
```

### 3. ExceptionInformation (예외 정보 Enum)

에러 코드 형식: `{도메인 약어 대문자}-{3자리 숫자}` (예: MEM-001, AUTH-001)

```java
@Getter
@AllArgsConstructor
public enum {Domain}ExceptionInformation implements ExceptionInformation {

    {DOMAIN}_NOT_FOUND(HttpStatus.NOT_FOUND, "{PREFIX}-001", "존재하지 않는 {도메인명}입니다."),
    // 추가 예외들...
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
```

### 4. Exception

```java
public class {Domain}Exception extends BaseException {
    public {Domain}Exception({Domain}ExceptionInformation info) {
        super(info.getHttpStatus(), info.getCode(), info.getMessage());
    }
}
```

### 5. GetService (조회 전용)

```java
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class {Domain}GetService {
    private final {Domain}Repository {domain}Repository;

    public {Domain} get{Domain}(Long id) {
        return {domain}Repository.findById(id)
                .orElseThrow(() -> new {Domain}Exception({Domain}ExceptionInformation.{DOMAIN}_NOT_FOUND));
    }

    public {Domain}Response get{Domain}Response(Long id) {
        return {Domain}Response.from(get{Domain}(id));
    }
}
```

### 6. Service (쓰기 전용)

```java
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class {Domain}Service {
    private final {Domain}Repository {domain}Repository;
    private final {Domain}GetService {domain}GetService;

    public {Domain}Response create{Domain}({Domain}CreateRequest request) {
        // 중복 체크 등 비즈니스 로직
        {Domain} entity = {Domain}.builder()
                // 필드 매핑
                .build();
        return {Domain}Response.from({domain}Repository.save(entity));
    }
}
```

### 7. DTO (Java Record 사용)

**Request:**
```java
public record {Domain}CreateRequest(
        String field1,
        String field2
) {}
```

**Response:**
```java
public record {Domain}Response(
        Long id,
        String field1,
        String field2
) {
    public static {Domain}Response from({Domain} entity) {
        return new {Domain}Response(
                entity.getId(),
                entity.getField1(),
                entity.getField2()
        );
    }
}
```

### 8. Swagger 인터페이스

```java
@Tag(name = "{Domain}", description = "{도메인 설명} API")
public interface {Domain}Swagger {

    @Operation(summary = "생성", description = "새로운 {도메인}을 등록합니다.")
    ApiResponse<{Domain}Response> create{Domain}(@RequestBody {Domain}CreateRequest request);

    @Operation(summary = "조회", description = "{도메인} 정보를 조회합니다.")
    ApiResponse<{Domain}Response> get{Domain}(@PathVariable Long id);
}
```

### 9. Controller

```java
@RestController
@RequestMapping("/api/{domains}")
@RequiredArgsConstructor
@Tag(name = "{Domain}", description = "{도메인} API")
public class {Domain}Controller implements {Domain}Swagger {
    private final {Domain}Service {domain}Service;
    private final {Domain}GetService {domain}GetService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<{Domain}Response> create{Domain}(@RequestBody {Domain}CreateRequest request) {
        return ApiResponse.response(HttpStatus.CREATED, "생성 성공", {domain}Service.create{Domain}(request));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<{Domain}Response> get{Domain}(@PathVariable Long id) {
        return ApiResponse.response(HttpStatus.OK, "조회 성공", {domain}GetService.get{Domain}Response(id));
    }
}
```

---

## ApiResponse 사용법

```java
// 데이터 있는 응답
ApiResponse.response(HttpStatus.OK, "조회 성공", data)
ApiResponse.response(HttpStatus.CREATED, "생성 성공", data)

// 데이터 없는 응답 (204 No Content 등)
ApiResponse.response(HttpStatus.NO_CONTENT, "삭제 성공")

// 에러 응답 (GlobalExceptionHandler에서 자동 처리)
ApiResponse.response(HttpStatus.NOT_FOUND, "MEM-001", "존재하지 않는 회원입니다.")
```

---

## 인증이 필요한 API

로그인한 사용자 정보가 필요할 때 `@AuthenticationPrincipal CustomUserDetails userDetails` 파라미터를 추가한다.

```java
@GetMapping("/me")
public ApiResponse<{Domain}Response> getMyInfo(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
    Long memberId = userDetails.getMemberId();
    // ...
}
```

---

## 기술 스택 요약

| 항목 | 내용 |
|------|------|
| Framework | Spring Boot 4.0.5 |
| Language | Java 17 |
| Build | Gradle (Kotlin DSL) |
| DB | H2 (인메모리, 개발용) |
| Cache | Redis (RefreshToken 저장) |
| Security | Spring Security + JWT |
| Docs | Swagger (springdoc-openapi 2.8.9) |
| AI | Spring AI 2.0.0-M3 (Claude, Gemini, Groq) |
| Util | Lombok, Jasypt (프로퍼티 암호화) |

---

## 환경변수 (.env)

`bootRun` 시 프로젝트 루트의 `.env` 파일을 자동으로 읽는다. 필요한 키:

```
GROQ_API_KEY=...
CLAUDE_API_KEY=...
GEMINI_API_KEY=...
JWT_SECRET=...
```

---

## 주의사항

- setter 절대 사용 금지 — 엔티티 변경은 도메인 메서드로
- `@Builder` 대신 `@SuperBuilder` 사용 (BaseEntity 상속 때문)
- 조회 서비스는 `@Transactional(readOnly = true)` 필수
- Service와 GetService를 반드시 분리 (CUD / Read)
- Controller는 항상 Swagger 인터페이스를 구현(`implements`)
- DTO는 Java Record 사용
- 예외 발생 시 도메인별 Exception 클래스 사용 (직접 RuntimeException 던지지 말 것)
