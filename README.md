# Spring WebFlux 리액티브 프로그래밍 학습 프로젝트

## 프로젝트 개요

이 프로젝트는 Spring WebFlux를 활용한 리액티브 프로그래밍의 핵심 개념과 실무 적용 방법을 학습하기 위한 실습 프로젝트입니다. 전통적인 블로킹 방식과 논블로킹 리액티브 방식의 차이를 이해하고, R2DBC를 활용한 리액티브 데이터베이스 액세스부터 WebClient를 통한 HTTP 통신까지 전반적인 리액티브 스택을 다룹니다.

## 주요 학습 내용

### 1. 리액티브 vs 전통적 방식 비교 (sec01)

**학습 목표**: 블로킹 방식과 논블로킹 방식의 근본적인 차이점 이해

- **전통적인 RestClient** (`TraditionalController.java:24-49`)
  - `RestClient`와 `JdkClientHttpRequestFactory` 사용
  - 동기 방식으로 응답을 대기하며 스레드 블로킹
  - 요청이 완료될 때까지 스레드가 대기 상태로 유지됨

- **리액티브 WebClient** (`ReactiveWebController.java:17-54`)
  - 비동기 논블로킹 방식으로 동작
  - `Flux<Product>`를 반환하여 여러 데이터를 스트림으로 처리
  - `MediaType.TEXT_EVENT_STREAM_VALUE`를 활용한 Server-Sent Events 구현
  - `onErrorComplete()`로 에러 발생 시에도 스트림이 정상 종료되도록 처리

**핵심 차이점**:
- 전통적 방식: 요청 → 대기(블로킹) → 응답 → 처리
- 리액티브 방식: 요청 → 다른 작업 수행 → 응답 도착 시 콜백 처리

---

### 2. R2DBC를 활용한 리액티브 데이터베이스 액세스 (sec02)

**학습 목표**: 리액티브 환경에서 데이터베이스와 상호작용하는 방법 습득

#### 2.1 기본 CRUD 연산

- **엔티티 설계** (`Customer.java`, `Product.java`, `CustomerOrder.java`)
  - Spring Data R2DBC의 `@Table`, `@Id` 애노테이션 활용
  - Lombok을 사용한 보일러플레이트 코드 제거
  - UUID를 주키로 사용하는 방법 (`CustomerOrder.java:19`)

- **ReactiveCrudRepository** (`CustomerRepository.java:9`)
  - `ReactiveCrudRepository<Customer, Integer>` 인터페이스 확장
  - 리액티브 타입(`Mono`, `Flux`)을 반환하는 CRUD 메서드
  - 커스텀 쿼리 메서드: `findByName()`, `findByEmailEndingWith()`

#### 2.2 쿼리 메서드 작성 패턴

- **메서드 네이밍 규칙** (`ProductRepository.java:12-14`)
  - `findByPriceBetween(Integer low, Integer high)`: 범위 검색
  - `findBy(Pageable pageable)`: 페이징 처리

- **페이징 처리** (`ProductRepositoryTest.java:41`)
  ```java
  PageRequest.of(0, 3).withSort(Sort.by("price").ascending())
  ```

#### 2.3 복잡한 쿼리 처리

- **@Query 애노테이션 활용** (`CustomerOrderRepository.java:16-40`)
  - JOIN을 활용한 복잡한 쿼리 작성
  - 네이티브 SQL 쿼리 사용
  - DTO 프로젝션: `OrderDetails` 레코드로 결과 매핑

**실습 포인트**:
- `Lec01CustomRepositoryTest.java`에서 `StepVerifier`를 활용한 리액티브 테스트 작성 방법
- `doOnNext()`를 통한 사이드 이펙트 처리 및 로깅
- `flatMap()`, `then()` 등의 연산자 활용

---

### 3. 서비스 레이어 구현 (sec03)

**학습 목표**: 리액티브 환경에서 비즈니스 로직을 처리하는 서비스 레이어 구축

#### 3.1 DTO 패턴

- **Record 타입 활용** (`CustomerDto.java:5-22`)
  - 불변 객체로 데이터 전송
  - 정적 팩토리 메서드 패턴: `fromEntity()`, `toEntity()`
  - 엔티티와 DTO 간의 명확한 분리

#### 3.2 서비스 계층 설계

- **기본 CRUD 연산** (`CustomerService.java:17-49`)
  - `getAllCustomers()`: 전체 조회 후 DTO 변환
  - `getAllCustomers(page, size)`: 페이징 처리된 조회
  - `getCustomerById()`: 단건 조회
  - `save()`: Mono 타입의 DTO를 받아서 저장
  - `update()`: ID 기반 업데이트
  - `delete()`: 삭제 후 Boolean 반환

#### 3.3 컨트롤러 레이어

- **REST API 엔드포인트** (`CustomerController.java:20-60`)
  - `@GetMapping`: `Flux<CustomerDto>` 직접 반환
  - `@GetMapping("/paginated")`: `Mono<List<CustomerDto>>` 반환
  - `@PostMapping`: `Mono<CustomerDto>` 요청 본문으로 받기
  - `@PutMapping`, `@DeleteMapping`: `ResponseEntity` 활용

- **ResponseEntity 활용** (`CustomerController.java:35-39`)
  ```java
  .map(ResponseEntity::ok)
  .defaultIfEmpty(ResponseEntity.notFound().build())
  ```

#### 3.4 WebTestClient를 활용한 통합 테스트

- **테스트 구성** (`CustomerControllerTest.java:15`)
  - `@AutoConfigureWebTestClient`: WebTestClient 자동 설정
  - `@SpringBootTest(properties = "sec=sec03")`: 특정 섹션만 활성화

- **테스트 검증** (`CustomerControllerTest.java:27-34`)
  - `.expectStatus().isOk()`: HTTP 상태 검증
  - `.expectHeader().contentType()`: 헤더 검증
  - `.expectBodyList()`: 응답 본문 타입 검증
  - `.value()`: 커스텀 검증 로직

---

### 4. 예외 처리 및 검증 (sec04)

**학습 목표**: 리액티브 스트림에서 에러를 안전하게 처리하는 방법

#### 4.1 커스텀 예외 정의

- **도메인 예외** (`CustomerNotFoundException.java:3-11`)
  - 커스텀 예외 메시지 포맷팅
  - RuntimeException 확장

- **검증 예외** (`InvalidInputException.java`)
  - 입력 데이터 검증 실패 시 발생

#### 4.2 글로벌 예외 핸들러

- **@RestControllerAdvice** (`ApplicationExceptionHandler.java:8`)
  - `@ExceptionHandler`로 특정 예외 타입 처리
  - `ProblemDetail` RFC 7807 표준 에러 응답 사용
  - HTTP 상태 코드와 상세 메시지 설정

#### 4.3 리액티브 검증

- **RequestValidator** (`RequestValidator.java:13-27`)
  - `UnaryOperator<Mono<CustomerDto>>` 타입 사용
  - `filter()` + `switchIfEmpty()` 패턴으로 검증
  - Predicate를 활용한 검증 로직 분리
  - 이메일 형식 검증: `dto.email().contains("@")`

- **컨트롤러에서 검증 적용** (`sec04/CustomerController.java:45`)
  ```java
  mono.transform(RequestValidator.validate())
      .as(customerService::save)
  ```

#### 4.4 에러 처리 패턴

- **ApplicationExceptions** (`sec05/ApplicationExceptions.java:6-17`)
  - 정적 팩토리 메서드로 에러 생성
  - `Mono.error()`를 반환하여 리액티브 체인 유지
  - 타입 파라미터 `<T>`로 모든 타입에 적용 가능

---

### 5. 필터 및 인증/인가 (sec05)

**학습 목표**: WebFilter를 활용한 횡단 관심사(Cross-cutting Concerns) 처리

#### 5.1 인증 필터

- **AuthenticationFilter** (`AuthenticationFilter.java:15-37`)
  - `@Order(1)`: 필터 실행 순서 지정
  - 헤더에서 `auth-token` 추출
  - 토큰 검증 후 사용자 카테고리를 `exchange.getAttributes()`에 저장
  - 인증 실패 시 `HttpStatus.UNAUTHORIZED` 반환

- **토큰-카테고리 매핑** (`AuthenticationFilter.java:19-22`)
  ```java
  Map.of(
      "sec123", Category.STANDARD,
      "sec345", Category.PRIME
  )
  ```

#### 5.2 인가 필터

- **AuthorizationWebFilter** (`AuthorizationWebFilter.java:13-38`)
  - `@Order(2)`: 인증 필터 다음에 실행
  - Switch Expression을 활용한 역할 기반 접근 제어
  - STANDARD 사용자: GET 요청만 허용
  - PRIME 사용자: 모든 요청 허용

#### 5.3 카테고리 정의

- **Category Enum** (`Category.java:3-8`)
  - 사용자 등급 구분
  - STANDARD, PRIME 두 가지 레벨

**핵심 패턴**:
- `ServerWebExchange`를 통한 요청/응답 컨텍스트 공유
- `WebFilterChain.filter()`로 다음 필터로 요청 전달
- `Mono.fromRunnable()`로 상태 코드 설정 후 응답 종료

---

### 6. 파일 업로드/다운로드 (sec06)

**학습 목표**: 대용량 데이터 스트리밍 처리

#### 6.1 대량 데이터 업로드

- **NDJSON (Newline Delimited JSON)** (`ProductController.java:26`)
  - `consumes = MediaType.APPLICATION_NDJSON_VALUE`
  - 각 JSON 객체가 한 줄씩 전송됨
  - 메모리 효율적인 대용량 데이터 처리

- **스트리밍 저장** (`ProductService.java:17-21`)
  ```java
  flux.map(ProductDto::toEntity)
      .as(productRepository::saveAll)
      .map(ProductDto::from)
  ```
  - `as()` 연산자로 전체 Flux를 변환
  - Repository의 `saveAll()`로 배치 저장
  - 응답으로 저장된 개수와 확인 ID 반환

#### 6.2 대량 데이터 다운로드

- **NDJSON 스트리밍 응답** (`ProductController.java:36`)
  - `produces = MediaType.APPLICATION_NDJSON_VALUE`
  - 클라이언트가 데이터를 받으면서 즉시 처리 가능
  - 메모리에 전체 데이터를 로드하지 않음

#### 6.3 테스트

- **업로드 테스트** (`ProductUploadDownloadTest.java:20-46`)
  - 단일 아이템 지연 업로드: `delayElements(Duration.ofSeconds(10))`
  - 대량 데이터 업로드: `Flux.range(1, 1_000)`
  - `StepVerifier`로 비동기 완료 검증

**실무 적용**:
- CSV, Excel 파일 대량 업로드/다운로드
- 실시간 로그 스트리밍
- 대용량 데이터 Export/Import

---

### 7. Server-Sent Events (SSE) 및 실시간 데이터 스트리밍 (sec07)

**학습 목표**: 서버에서 클라이언트로 실시간 데이터 푸시

#### 7.1 Sinks를 활용한 데이터 방출

- **Sink 설정** (`SinkConfig.java:12-14`)
  ```java
  Sinks.many().replay().limit(2)
  ```
  - `replay()`: 새로운 구독자에게 이전 데이터 재전송
  - `limit(2)`: 최근 2개의 아이템만 보관

#### 7.2 실시간 데이터 생성

- **DataSetUpService** (`DataSetUpService.java:15-27`)
  - `CommandLineRunner` 구현: 애플리케이션 시작 시 자동 실행
  - 1초마다 새로운 Product 생성
  - `ThreadLocalRandom`으로 랜덤 가격 생성
  - Sink에 데이터 방출: `sinks::tryEmitNext`

#### 7.3 SSE 엔드포인트

- **스트리밍 API** (`ProductController.java:27-31`)
  - `produces = MediaType.TEXT_EVENT_STREAM_VALUE`
  - `maxPrice` 파라미터로 필터링
  - `sinks.asFlux()`로 Flux 변환
  - `.filter(dto -> dto.price() <= maxPrice)`: 스트림에서 필터링

**사용 시나리오**:
- 실시간 주식 시세 표시
- 라이브 스포츠 점수 업데이트
- 실시간 알림 시스템
- 대시보드 실시간 모니터링

**핵심 개념**:
- 여러 클라이언트가 동시에 같은 데이터 스트림 구독 가능
- 각 클라이언트가 서로 다른 필터 조건 적용 가능
- Sink를 통한 멀티캐스팅 구현

---

### 8. WebClient 고급 활용 (sec08 & webclient)

**학습 목표**: 리액티브 HTTP 클라이언트의 다양한 기능 습득

#### 8.1 기본 사용법

- **Mono 요청** (`Lec1MonoTest.java:15-27`)
  - 단일 객체 조회
  - `bodyToMono(Product.class)`
  - 비동기 논블로킹 방식으로 여러 요청 동시 처리

- **Flux 스트리밍** (`Lec02FluxTest.java:17-62`)
  - `bodyToFlux()`: 스트림 데이터 수신
  - `take(Duration.ofSeconds(3))`: 시간 기반 제한
  - Server-Sent Events 수신

#### 8.2 헤더 및 인증

- **기본 헤더 설정** (`HeaderTest.java:9`)
  ```java
  b.defaultHeader("caller-id", "order-service")
  ```
  - 모든 요청에 공통 헤더 자동 추가
  - 요청별로 오버라이드 가능

- **Basic 인증** (`BasicAuthTest.java:18`)
  ```java
  b.defaultHeaders(h -> h.setBasicAuth("java", "secret"))
  ```

- **Bearer 토큰** (`BasicAuthTest.java:43`)
  ```java
  .headers(h -> h.setBearerAuth("token"))
  ```

#### 8.3 Exchange Filter

- **동적 토큰 생성** (`BasicAuthTest.java:76-88`)
  - 각 요청마다 새로운 토큰 생성
  - `ClientRequest.from()`: 요청 불변성 유지
  - 토큰 로깅 및 검증 로직 추가

#### 8.4 에러 처리

- **기본 에러 처리** (`ErrorResponseTest.java:23-38`)
  - `onErrorReturn()`: 에러 발생 시 기본값 반환
  - 스트림이 정상적으로 완료됨

- **타입별 에러 처리** (`ErrorResponseTest.java:43-60`)
  - `doOnError(WebClientResponseException.class)`: 에러 로깅
  - `ProblemDetail` 파싱
  - 특정 예외 타입만 처리

- **Exchange 기반 처리** (`ErrorResponseTest.java:64-87`)
  - `.exchangeToMono()`: 저수준 응답 처리
  - 상태 코드 기반 분기 처리
  - 4xx 에러 시 커스텀 로직 수행

#### 8.5 연결 풀링

- **ConnectionProvider 설정** (`ConnectionPoolingTest.java:19-31`)
  ```java
  ConnectionProvider.builder("vins")
      .lifo()  // Last-In-First-Out
      .maxConnections(1000)
      .pendingAcquireMaxCount(5000)
      .build()
  ```
  - 기본 500개 제한을 1000개로 증가
  - 대량 동시 요청 처리 가능
  - `keepAlive(true)`: 연결 재사용

- **동시 요청 테스트** (`ConnectionPoolingTest.java:36-47`)
  - `flatMap(this::getProduct, max)`: 최대 동시성 지정
  - 501개 이상의 동시 요청 처리

#### 8.6 HTTP/2 지원

- **H2C 프로토콜** (`Http2Test.java:26`)
  ```java
  .protocol(HttpProtocol.H2C)
  ```
  - 단일 연결로 다중 요청 처리
  - `maxConnections(1)`로도 501개 요청 처리 가능
  - 멀티플렉싱을 통한 성능 향상


---

## 핵심 학습 포인트

### 1. Mono vs Flux
- **Mono**: 0개 또는 1개의 요소를 방출
- **Flux**: 0개 이상의 요소를 방출
- 적절한 타입 선택이 API 설계의 핵심

### 2. 주요 연산자
- **map()**: 각 요소 변환
- **flatMap()**: 비동기 변환 (Mono/Flux 반환)
- **filter()**: 조건에 맞는 요소만 통과
- **switchIfEmpty()**: 빈 스트림일 때 대체 값
- **doOnNext()**: 사이드 이펙트 (로깅 등)
- **as()**: 전체 Publisher 변환
- **then()**: 완료 신호만 전달
- **transform()**: Publisher 변환 함수 적용

### 3. 에러 처리 전략
- **onErrorReturn()**: 기본값 반환
- **onErrorResume()**: 대체 Publisher 반환
- **doOnError()**: 에러 로깅
- **switchIfEmpty()**: 빈 값 처리
- **Mono.error()**: 명시적 에러 발생

### 4. 테스트 방법
- **StepVerifier**: 리액티브 스트림 검증
- **WebTestClient**: REST API 통합 테스트
- **expectNext()**: 다음 값 검증
- **expectComplete()**: 정상 완료 검증
- **verifyComplete()**: 검증 실행 및 완료 대기

### 5. 백프레셔 (Backpressure)
- 구독자가 처리 가능한 속도로 데이터 요청
- 생산자-소비자 속도 불일치 문제 해결
- Reactor는 자동으로 백프레셔 관리

---

## 다음 학습 방향

1. **Spring Security WebFlux**: 리액티브 환경에서의 보안
2. **Reactive Redis**: 캐싱 및 세션 관리
3. **Reactive Kafka**: 이벤트 스트리밍
4. **Reactive MongoDB**: NoSQL 데이터베이스 연동
5. **WebSocket + Reactive**: 양방향 실시간 통신
6. **Resilience4j**: Circuit Breaker, Retry, Rate Limiter
7. **Spring Cloud Gateway**: 리액티브 API Gateway

---

## 주요 기술 스택

- Spring Boot 3.x
- Spring WebFlux
- R2DBC (Reactive Relational Database Connectivity)
- H2 (리액티브 드라이버)
- Project Reactor
- Lombok
- JUnit 5
- Reactor Test (StepVerifier)

---

## 결론

이 프로젝트를 통해 리액티브 프로그래밍의 기초 개념을 전반적으로 학습했습니다 :

- 블로킹 vs 논블로킹 방식의 실질적 차이
- R2DBC를 활용한 효율적인 데이터베이스 액세스
- 리액티브 스트림에서의 에러 처리와 검증
- WebFilter를 통한 횡단 관심사 처리
- Server-Sent Events와 실시간 데이터 스트리밍
- WebClient의 다양한 활용법과 최적화 기법

