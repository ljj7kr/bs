# 개요
카카오뱅크 서버개발과제 '블로그 검색 서비스' 기능 구현

# 핵심 문제해결 전략 및 분석
- 주문/결제 시스템 특성상 명확한 구조와 높은 신뢰도를 요구하고, 추후 기능 확장시 데이터 수정이 예상되므로 RDB 선택
- 데이터 수집 플랫폼으로 주문내역 전송시 실패하더라도 로그를 남기고 나머지가 성공이라면 주문을 성공처리함, 전송 실패건 재처리 필요
- WebClient 사용
- 연관된 비즈니스 로직 transaction 처리
- REST API 제약 조건 추가
- QueryDSL 동적쿼리 사용으로 추후 확장 고려
- 다수 서버에 다수 인스턴스 사용 가능하도록 개발`

# 요구사항
- 블로그 검색
    - [x] 키워드를 통해 블로그를 검색할 수 있어야 합니다.
    - [x] 검색 결과에서 Sorting(정확도순, 최신순) 기능을 지원해야 합니다.
    - [x] 검색 결과는 Pagination 형태로 제공해야 합니다.
    - [x] 검색 소스는 카카오 API의 키워드로 블로그 검색 활용합니다.
    - [x] 추후 카카오 API 이외에 새로운 검색 소스가 추가될 수 있음을 고려해야 합니다.
- 인기 검색어 목록
    - [x] 사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드를 제공합니다.
    - [x] 검색어 별로 검색된 횟수도 함께 표기해 주세요.
- 추가
    - [x] 멀티 모듈 구성 및 모듈간 의존성 제약
    - [x] 트래픽이 많고, 저장되어 있는 데이터가 많음을 염두에 둔 구현
    - [x] 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터 제공

# DB
### SEARCH_HISTORY (검색 내역)
| 컬럼      | 타입       | 설명     |
|---------|----------|--------|
| ID      | Long     | 식별값    |
| KEYWORD | String   | 검색 키워드 |
| SERVER  | String   | 원격지 서버 |

### POPULAR_KEYWORD_HOUR (인기 검색어 집계)
| 컬럼         | 타입            | 설명      |
|------------|---------------|---------|
| ID         | Long          | 식별값     |
| KEYWORD    | String        | 검색 키워드  |
| COUNT      | Long          | 집계 숫자   |
| START_TIME | LocalDateTime | 집계 시작시간 |
| END_TIME   | LocalDateTime | 집계 종료시간 |

### 공통 컬럼
| 컬럼          | 타입            | 설명      |
|-------------|---------------|---------|
| CREATED_AT  | LocalDateTime | 생성시간    |
| MODIFIED_AT | LocalDateTime | 수정시간    |

# 주요 기능
* 메뉴 조회
* 포인트 충전
* 커피 주문, 결제 및 주문 내역을 데이터 수집 플랫폼으로 전송
* 인기 메뉴 목록 조회

# 환경
* Kotlin: 1.7.22
* Java: 17
* SpringBoot: 3.0.4
* SpringFramework: 6.0.6
* JUnit 5
* QueryDSL 5.0.0
* Gradle - Kotlin
* H2: http://localhost:8080/v1/h2-console
* Swagger: http://localhost:8080/v1/swagger-ui/index.html

# 실행
* blog-search-api 모듈 실행
