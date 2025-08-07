# MovieTicketing (콘솔 기반 영화 예매 시스템)

## 🎬 프로젝트 소개

이 프로젝트는 Java로 구현된 콘솔(CLI) 환경의 영화 예매 시스템입니다. 사용자는 영화 목록을 조회하고 원하는 영화를 예매할 수 있으며, 관리자는 영화, 상영 정보, 사용자를 관리할 수 있습니다.

## 🎯 프로젝트 목적

*   Java를 이용한 객체 지향 프로그래밍 연습
*   JDBC를 활용한 데이터베이스 연동 및 SQL 처리 능력 향상
*   DAO, Service, UI 등 역할에 따른 계층적 아키텍처 설계 및 구현
*   콘솔 기반 애플리케이션의 사용자 인터페이스(UI/UX) 설계 경험

## ✨ 주요 기능

### 👤 공통 기능
*   로그인 및 회원가입

### 🙋‍♂️ 사용자 기능
*   **영화 목록 조회**: 현재 상영 중인 모든 영화 목록을 확인합니다.
*   **영화 검색**: 제목으로 특정 영화를 검색합니다.
*   **영화 예매**:
    *   원하는 영화와 상영 시간을 선택합니다.
    *   예매 가능한 좌석을 확인하고 선택합니다.
    *   예매 내역을 최종 확인하고 티켓을 발급받습니다.
*   **내 예매 내역 확인**: 자신의 예매 내역 목록을 조회합니다.
*   **예매 취소**: 예매한 티켓을 취소합니다.

### 🛠️ 관리자 기능
*   **영화 관리**: 새로운 영화를 등록하거나 기존 영화를 삭제합니다.
*   **상영 관리**: 특정 영화의 상영 시간을 추가하거나 삭제합니다.
*   **회원 관리**: 전체 회원 목록을 조회하고 회원을 삭제합니다.

## 🛠️ 기술 스택

*   **Language**: Java 11
*   **Database**: Oracle Database
*   **Driver**: Oracle JDBC Driver (ojdbc8.jar)
*   **IDE**: Eclipse
*   **Version Control**: Git, GitHub

## ⚙️ 개발 환경 설정

이 프로젝트를 로컬 환경에서 실행하기 위한 절차입니다.

### 1. 사전 요구 사항

*   **JDK 11** 이상 설치
*   **Oracle Database** 설치 및 실행
*   **Eclipse IDE** (Java EE 버전 권장)
*   **Git**

### 2. 설정 단계

1.  **데이터베이스 설정**
    Oracle DB에 접속하여 프로젝트에서 사용할 사용자를 생성하고, `database_setup.sql` 스크립트를 실행하여 테이블과 시퀀스를 생성합니다.

2.  **프로젝트 클론**
    ```bash
    git clone https://github.com/Oseongmin614/MovieTicketing.git
    ```

3.  **Eclipse로 프로젝트 가져오기**
    *   Eclipse에서 `File` > `Import...` > `General` > `Existing Projects into Workspace`를 선택합니다.
    *   `Select root directory`에서 클론한 프로젝트 폴더를 선택하고 `Finish`를 누릅니다.

4.  **빌드 경로 설정**
    *   프로젝트 우클릭 > `Build Path` > `Configure Build Path...`로 이동합니다.
    *   `Libraries` 탭에서 `Classpath`를 선택하고 `Add JARs...`를 클릭합니다.
    *   프로젝트 내 `lib/ojdbc8.jar` 파일을 선택하여 추가합니다.

5.  **DB 연결 정보 수정**
    *   `src/util/DBConnector.java` 파일을 열어 자신의 DB 환경에 맞게 `URL`, `USER`, `PASSWORD`를 수정합니다.

## 🚀 사용법

1.  Eclipse의 `Package Explorer`에서 `src/Main.java` 파일을 우클릭합니다.
2.  `Run As` > `Java Application`을 선택하여 프로그램을 실행합니다.
3.  콘솔에 나타나는 안내에 따라 원하는 기능을 사용합니다.

## 📁 상세 프로젝트 구조

```
MovieTicketing/
├── .git/              # Git 버전 관리 폴더
├── .settings/         # Eclipse IDE 설정
├── lib/
│   └── ojdbc8.jar     # Oracle 데이터베이스 연결을 위한 JDBC 드라이버
├── src/
│   ├── Main.java      # 프로그램의 시작점(Entry Point)
│   │
│   ├── dao/           # Data Access Object: DB와 직접 통신하여 SQL을 실행
│   │   ├── BaseDAO.java
│   │   ├── MovieDAO.java
│   │   └── ... (UserDAO, ShowDAO 등)
│   │
│   ├── exception/     # 사용자 정의 예외 클래스
│   │   └── ChoiceOutOfBoundException.java
│   │
│   ├── manager/       # 비즈니스 로직을 총괄하는 관리자 클래스
│   │   ├── MenuManger.java
│   │   ├── MovieManger.java
│   │   └── ...
│   │
│   ├── service/       # UI(View)와 DAO 사이의 데이터 흐름을 제어하는 서비스
│   │   ├── LoginService.java
│   │   ├── MovieService.java
│   │   └── ...
│   │
│   ├── ui/            # 사용자와 상호작용하는 콘솔 인터페이스(View)
│   │   ├── BaseView.java
│   │   ├── MainView.java
│   │   ├── admin/     # 관리자 전용 View
│   │   └── user/      # 일반 사용자용 View
│   │
│   ├── util/          # 유틸리티 클래스
│   │   └── DBConnector.java # 데이터베이스 연결 관리
│   │
│   └── vo/            # Value Object: 계층 간 데이터 전송을 위한 객체
│       ├── MovieVO.java
│       └── ... (UserVO, ReservationVO 등)
│
├── .classpath         # Eclipse 빌드 경로 설정 파일
├── .project           # Eclipse 프로젝트 정보 파일
├── .gitignore         # Git이 추적하지 않을 파일/폴더 목록
└── database_setup.sql # DB 테이블 및 시퀀스 생성 스크립트
```

## 🗄️ 데이터베이스 구조

프로젝트에 필요한 모든 테이블, 시퀀스 및 관계는 `database_setup.sql` 파일에 정의되어 있습니다. 각 테이블의 상세한 역할과 구조는 다음과 같습니다.
<img width="815" height="827" alt="image" src="https://github.com/user-attachments/assets/89f2fd00-972e-47b8-b7c0-d3b80b617729" />

### 1. `USERS`
- **역할**: 사용자(회원)의 정보를 저장합니다.
- **컬럼**:
  - `USER_ID` (PK): 사용자 아이디 (기본 키)
  - `PASSWORD`: 비밀번호
  - `NAME`: 사용자 이름
  - `IS_ADMIN`: 관리자 여부 (0: 일반 사용자, 1: 관리자)

### 2. `MOVIES`
- **역할**: 상영할 영화의 기본 정보를 저장합니다.
- **컬럼**:
  - `MOVIE_ID` (PK): 영화 고유 번호 (기본 키)
  - `TITLE`: 영화 제목
  - `GENRE`: 장르
  - `RUNNING_TIME`: 상영 시간 (분 단위)

### 3. `SHOWS`
- **역할**: 어떤 영화(`MOVIES`)가 언제 상영되는지에 대한 상영 시간표 정보를 저장합니다.
- **컬럼**:
  - `SHOW_ID` (PK): 상영 정보 고유 번호 (기본 키)
  - `MOVIE_ID` (FK): `MOVIES` 테이블을 참조하는 외래 키
  - `SHOW_TIME`: 상영 시작 시간 (날짜와 시간 포함)

### 4. `SEATS`
- **역할**: 상영관의 모든 좌석 정보를 정의합니다. (예: A1, A2, ...)
- **컬럼**:
  - `SEAT_ID` (PK): 좌석 고유 번호 (기본 키)
  - `SEAT_NUMBER`: 좌석 번호 (예: 'A1')

### 5. `SHOW_SEATS`
- **역할**: 특정 상영(`SHOWS`)의 각 좌석(`SEATS`)이 예매되었는지 상태를 저장하는 테이블입니다. (매핑 테이블)
- **컬럼**:
  - `SHOW_SEAT_ID` (PK): 상영 좌석 고유 번호 (기본 키)
  - `SHOW_ID` (FK): `SHOWS` 테이블 참조
  - `SEAT_ID` (FK): `SEATS` 테이블 참조
  - `IS_RESERVED`: 예매 상태 (0: 예매 가능, 1: 예매 완료)

### 6. `RESERVATIONS`
- **역할**: 사용자의 최종 예매 내역을 기록합니다.
- **컬럼**:
  - `RESERVATION_ID` (PK): 예매 고유 번호 (기본 키)
  - `USER_ID` (FK): 예매한 사용자의 아이디 (`USERS` 참조)
  - `SHOW_ID` (FK): 예매한 상영 정보 (`SHOWS` 참조)
  - `SEAT_ID` (FK): 예매한 좌석 정보 (`SEATS` 참조)
  - `RESERVATION_DATE`: 예매한 날짜

## 📅 개발 기간

*   **총 개발 기간**: 2024년 4월 20일 ~ 2024년 5월 12일
*   **주요 개발 내역**:
    *   `4/20`: 디렉토리 구조 설계
    *   `4/21`: 메인 기능 실행, DB 설계 및 연결
    *   `4/23`: 로그인 DAO, VO 구현
    *   `4/25`: 영화 리스트 조회 기능 구현
    *   `4/29`: 영화 예매 기능 구현 (시간대/좌석)
    *   `4/30`: 예매 가능/불가 처리, 예매 정보 저장 및 티켓 출력
    *   `5/5`: 내 예매 내역 조회 및 예매 취소 기능 구현
    *   `5/6`: 관리자 기능 (영화 및 상영 시간 관리)
    *   `5/7`: 관리자 기능 (사용자 관리)

