# POST-IT

## 📌 프로젝트 개요

> **Flutter 기반 웹 프론트엔드 + Spring Boot 백엔드**로 개발한 **게시판 웹 애플리케이션**입니다.
> 
> 회원가입, 로그인, 게시글 및 댓글 작성/조회/수정/삭제, 조회수 기능을 제공하며, 대규모 트래픽 대응을 고려한 아키텍처와 **성능 최적화**를 목표로 개발하였습니다.
> 
> Redis를 이용한 Refresh Token 관리, QueryDSL을 활용한 복잡한 데이터 조회 최적화, AWS EC2 서버 운영, Github Action-Docker 기반 자동 배포까지 전체 개발-운영 과정을 직접 수행했습니다.

## 🧱 Software Architecture

![architecture](https://github.com/user-attachments/assets/be45a92a-8e74-4416-91bc-24d8def4db23)

## 🔗 ERD

[//]: # (![erd]&#40;https://github.com/user-attachments/assets/09af3014-54db-458a-822c-ba73cd5320a7&#41;)

<img src="https://github.com/user-attachments/assets/09af3014-54db-458a-822c-ba73cd5320a7" width=70%/>

## 🧱 시스템 설계 및 기술 스택

| 영역       | 기술                                   |
| -------- | ------------------------------------ |
| Backend  | Java 21, Spring Boot 3.2             |
| ORM      | Spring Data JPA + QueryDSL           |
| 인증       | JWT + Redis + OAuth2                 |
| Database | MySQL                                |
| Infra    | AWS EC2, Docker, CloudWatch          |
| 배포       | GitHub Actions + Docker *(자동 배포 구현)* |

### 🔧 설계 선택 요약

- Stateless 인증 구조를 위해 JWT + Redis 채택
- 게시글 목록 조회 시 QueryDSL과 Column Projection을 활용하여 `id`, `title`, `viewCount`만 조회
- 페이징 처리(`Pageable`)로 전송 데이터와 DB 부하 최소화
- 컬럼 인덱싱을 통해 트래픽 상황에서도 안정적인 조회 성능 확보
- 조회수 업데이트는 캐시로 처리하여 DB 부하를 분산
- GitHub Actions를 이용하여 애플리케이션 자동 배포 구성

---

## 🚀 주요 기능 (Key Features)
- **JWT 기반 로그인 / 인증 시스템**
  - Access Token / Refresh Token 구조
  - Redis를 이용한 Refresh Token 저장 및 검증

- **Google OAuth 2.0 로그인**
	- 구글 소셜 계정 연동을 통한 간편 로그인
	
- 게시글 목록 조회 최적화
	- QueryDSL과 Projection을 이용해 필요한 필드(`id`, `title`, `viewCount`)만 조회
	- Pageable을 활용한 페이징 처리로 성능 최적화

- **조회수 캐싱 + 일괄 저장 전략**
  - Spring Cache (ConcurrentMapCacheManager) 사용
  - 트래픽 분산을 위해 일정 주기마다 DB에 일괄 업데이트
- CRUD 구현
	- 게시글 작성(Create), 조회(Read), 수정(Update), 삭제(Delete) 기능 제공. 

---
## ⚙️ CI/CD 구성

- **GitHub Actions 기반의 자동 배포 파이프라인 구성**
  - `master` 브랜치에 push 시, GitHub Actions에서 애플리케이션을 빌드하고 Docker 이미지 생성
  - EC2 서버에 SSH 접속 후 기존 컨테이너를 종료하고 새로운 컨테이너를 실행하는 방식으로 재배포 수행
  - 현재는 테스트 단계는 포함되지 않았으며, **무중단 배포는 아님**
  - `.github/workflows/deploy.yml`에 배포 워크플로우 정의 [보러가기](https://github.com/hr79/post-it/blob/main/.github/workflows/main.yml)
   
> ✅ 추후 개선 방향
> - 테스트 단계 자동화 (예: `./gradlew test`)
> - 무중단 배포를 위한 Blue-Green 또는 Rolling Update 구조 도입

---

## 📈 성능 개선 및 최적화



- ✅ **목록 조회 최적화 (페이징 + Projection)**
  - [링크: Column Projection](https://www.notion.so/Column-Projection-164e74104e0880069a40edf8ec5b3b4a?pvs=21)

- ✅ **컬럼 인덱싱 적용**
  - [링크: Column Indexing](https://www.notion.so/Column-Indexing-username-220e74104e08807cbed0e2762e08d14c?source=copy_link)

---

## 🔍 트러블슈팅

- ✅ 조회수 업데이트 누락: **조회수 캐시 & 일괄 업데이트**
	- 원인: 동시에 여러 사용자가 같은 글을 조회하면 `view_count++`가 경쟁 상태(Race Condition) 발생
	- 해결: `ConcurrentMapCache`에 누적 후 주기적으로 DB에 일괄 반영 (캐시 전략 도입 배경)
	- [링크: Caching & Update 전략](https://www.notion.so/DB-222e74104e088053a5bcc0e30de99df7?source=copy_link)
- ✅ **JPA N+1 문제 해결**: EntityGraph 사용
	- 원인: 연관 엔티티를 가져오는 쿼리가 반복 발생
	- 해결: `@EntityGraph` 적용으로 쿼리 수를 1회로 줄임
	- [링크: EntityGraph 적용](https://www.notion.so/JPA-N-1-EntityGraph-222e74104e0880b3ab68c833e3260b85?source=copy_link)

---
## 🧩 패키지 구조
```
.
├── common
│   ├── exception
│   │   └── ErrorMessages.java
│   ├── response
│   │   └── ApiResponse.java
│   └── util
│       ├── CacheUtil.java
│       └── JwtUtil.java
├── config
│   ├── AppConfig.java
│   ├── CacheConfig.java
│   ├── RedisConfig.java
│   ├── SecurityConfig.java
│   └── WebConfig.java
├── domain
│   ├── auth
│   │   ├── AuthController.java
│   │   ├── dto
│   │   │   ├── AuthResponseDto.java
│   │   │   ├── LoginRequestDto.java
│   │   │   └── RegisterRequestDto.java
│   │   ├── filter
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── JwtAuthorizationFilter.java
│   │   ├── MemberRepository.java
│   │   ├── model
│   │   │   ├── CustomUserDetails.java
│   │   │   └── Member.java
│   │   ├── oauth
│   │   │   ├── OAuth2UserImpl.java
│   │   │   ├── OAuthUserInfo.java
│   │   │   └── service
│   │   │       ├── AuthService.java
│   │   │       ├── AuthServiceFactory.java
│   │   │       ├── GoogleOAuth2Service.java
│   │   │       └── NaverAuthService.java
│   │   └── service
│   │       ├── AuthenticationManagerImpl.java
│   │       ├── BasicAuthService.java
│   │       ├── RedisService.java
│   │       ├── TokenService.java
│   │       └── UserDetailsServiceImpl.java
│   ├── BaseEntity.java
│   ├── comment
│   │   ├── Comment.java
│   │   ├── CommentController.java
│   │   ├── CommentRepository.java
│   │   ├── CommentService.java
│   │   └── dto
│   │       ├── CommentRequestDto.java
│   │       └── CommentResponseDto.java
│   ├── enums
│   │   └── LoginType.java
│   └── post
│       ├── dto
│       │   ├── PostListPageDto.java
│       │   ├── PostRequestDto.java
│       │   └── PostResponseDto.java
│       ├── Post.java
│       ├── PostController.java
│       ├── PostService.java
│       └── repository
│           ├── PostQueryRepository.java
│           ├── PostQueryRepositoryImpl.java
│           └── PostRepository.java
└── PostItBackendApplication.java

```