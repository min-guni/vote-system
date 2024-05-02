# vote-system

## Simple Spring Project
방을 만들어서 의결을 진행할 수 있는 투표 프로그램 백엔드.


## STACK

[![stackticon](https://firebasestorage.googleapis.com/v0/b/stackticon-81399.appspot.com/o/images%2F1714452152100?alt=media&token=3815a66d-dff5-443f-920c-717fd48b3718)](https://github.com/msdio/stackticon)



## REST API

### USER
- POST /user/  - 회원 가입 기능
- PUT  /user/  - 회원 수정 기능 (Login Token 필요)

### AUTH
- POST /auth/token - 로그인 기능

### ROOM (Login Token 필요)
- POST /room/ - room 생성 기능
- GET /room/ - 사용자가 참여하는 ROOM 조회 기능
- GET /room/{roomId} - 해당 room 의 정보 제공 기능
- GET /room/{roomId}/users - 해당 room의 참여하고 있는 User 정보 제공 기능
- PUT /room/{roomId{/user/{userId} - 해당 userId의 user을 roomId 방에 추가 기능
- DELETE /room/{roomId}/user/{userId} - 해당 userId의 user을 roomId 방에서 삭제 기능
- DELETE /room/{roomId} - 해당 room 삭제 

### VOTE (Login Token 필요)
- POST /vote/{roomId} - vote 생성
- PATCH /vote/{voteId} - 투표 업데이트
- PUT /vote/{voteId} - 투표하기
- DELETE /vote/{voteId} - 투표 삭제
- DELETE /vote/{voteId}/clear - 투표 리셋
- GET /vote/{voteId} - 투표 결과
- GET /vote/?roomId= - 해당 방 전체 투표 결과
