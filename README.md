# opensourceTeamproject
팀_ 공교로운

## 1. 목표와 기능

### 1.1 목표
- 오늘 탈 막차가 떠나기 전, 언제 떠나는지, 떠나기까지 얼마나 남았는지를 어플을 통해 사용자에게 안내하는 서비스를 제공

### 1.2 기능
- 정문 정류소의 버스 시간 안내
- 사용자에게 버스 출발 예정 시간을 안내
- 사용자게에 버스 출발하기까지 남은 시간을 안내
- 원하는 버스 노선 즐겨찾기 기능
- 버스 노선 알림 기능

## 2. 개발 환경 및 배포 URL
### 2.1 개발 환경
- Android Studio
- GitHub Projects
- 서비스 배포 환경
    - 플레이스토어 (예정)

### 2.2 배포 URL
- https://dlqovop.github.io/ (임시)

## 3. 개발 일정
![image](https://github.com/dLqovop/opensourceTeamproject/assets/126761271/f0735254-1184-46f5-abf2-6f71017b3cdf)

## 4. 역할 분담
- 팀장 : 윤ㅇㅇ ( UI 디자인 및 구현, splash 화면 구현, 버그 픽스 )
- 팀원
    - 문지현 ( 버스 출발 시간, 출발하기까지 남은 시간 실시간 계산 및 정렬, 즐겨찾기 페이지 구현, 버스 노선 API 연동 및 확장 view로 출력, SQLite 즐겨찾기 DB 구현, 사용자 정의 알림기능 구현 )
    - 신ㅇㅇ

## 5. UI / BM
### 5.1 PROTOTYPE
![image](https://github.com/dLqovop/opensourceTeamproject/assets/126761271/ad30302f-7ffe-4cf9-8b90-1a184c45e17d)

### 5.2
- 전체 페이지 구조
![image](https://github.com/dLqovop/opensourceTeamproject/assets/126761271/38f5a690-62d6-4b45-9e1e-ae2aa39ca59b)

### 
![image](https://github.com/dLqovop/opensourceTeamproject/assets/126761271/bf68dbbf-57f8-40c4-8ab4-a5bae8c86924)
- 로딩 페이지

![image](https://github.com/dLqovop/opensourceTeamproject/assets/126761271/05456fa2-b183-43bd-a72c-082de53c19f7)
- 버스 출발 시간, 버스 출발하기까지 남은시간을 화면에 출력합니다.
- 실시간으로 버스 출발하기까지 남은 시간을 계산하고 이를 기준으로 오름차순 정렬하여 화면에 갱신합니다.
- 선택한 버스의 노선을 확장 view로 출력합니다.
- 별모양 아이콘을 이용해 희망하는 버스를 즐겨찾기 할 수 있습니다.

![image](https://github.com/dLqovop/opensourceTeamproject/assets/126761271/3c633fe6-1c3a-4244-a885-0ee2705810a7)
- SQLite를 이용해 사용자가 저장한 버스를 즐겨찾기 페이지 화면에서 출력합니다.
- 선택한 버스의 노선을 확장 view로 출력합니다.

![image](https://github.com/dLqovop/opensourceTeamproject/assets/126761271/79819265-253a-4caa-b724-d914df26d1ad)
- 사용자 임의로 알림을 설정할 수 있습니다.

## 6. 확장 계획
- 제주도 전체 버스 노선으로 확장
- 어플 최적화
- 노선 번호 검색
- 장소기반 출발지, 목적지 선택
- 정류장 정보 호출 구현
- 환승 정보 구현

## 개발하면서 느낀점
- 개발은 언제나 시간이 부족합니다. 개발 기간이 짧아 계획을 착실히 세웠다고 생각했으나 개발이 내 마음대로 되지 않았습니다. 효율적으로 코드를 짜야겠다는 마음에 볼륨이 작은 기획이더라도 개발 경험을 늘려 기획하고 개발하는 과정을 몸에 익혀야 겠다는 생각이 들었습니다.
