# ☕️ 카페만을 위한 포스 서비스, Cafe POS
|  |
| -- |
|![main_image](https://github.com/AII-the-time/POS_Android/assets/64644738/c1e655c9-de58-41cb-8d1b-b9a5dafc32f7)|
- SW마에스트로 14기 프로젝트
- <b>시중 포스의 불편한 점과 재고 관리 문제점을 개선</b>한 카페만을 위한 태블릿 포스 서비스입니다.
- <b>등록된 레시피와 발생하는 매출</b>을 바탕으로 재고관리를 진행합니다.

 ## 🔗 Tech Stack

| Category            | Details                                      |
| ------------------- | -------------------------------------------- |
| Architecture        | MVVM, Multi Module, Clean Architecture, Hilt |
| AAC                 | ViewModel, LiveData, DataBinding, Navigation |
| Concurrency         | Coroutine, Flow                              |
| Networking          | OkHttp3, Retrofit2, Moshi                    |
| CI/CD               | Github Actions                               |
| Language            | Kotlin                                       |

## 🔗 Clean Architecutre

![clean architecture](https://github.com/AII-the-time/POS_Android/assets/64644738/1d0dfbfd-6bf7-424a-aa5d-5aeb6d05b62d)
    
    🔗 클린 아키텍처
    • 책임에 따라 layer를 나눠 관심사를 분리
    • DTO / VO를 구분하여 코드 변경성에 있어 안정성을 높임
    • data layer에서만 데이터를 다룸으로써 데이터 변경사항을 한 곳으로 일원화
    • 데이터 흐름을 한 방향으로만 흐르도록 하여 데이터의 일관성 유지
    
## 🔗 Multi Module

![multi module](https://github.com/AII-the-time/POS_Android/assets/64644738/dd8fa08e-0d0c-4b1d-be7e-6a106a232df7)

    🔗 멀티 모듈
    • 코드 베이스를 느슨하게 결합된 독립적인 부분으로 구성
    • 각 모듈은 하나의 작은 기능을 나타냄
      • 작은 단위로 기능을 쪼갬 -> 시스템 설계 및 유지보수의 복잡성 감소
    • 코드의 재사용성 및 확장성 향상
    • 빌드 속도 향상

 ## 🔗 Server Driven UI

| 적용 결과                        | [Json 형태](https://separated-stick-863.notion.site/ServerDriven-UI-Json-cf513b967af7429893dc301cf9414ec6?pvs=4)  |
| ----------------------------- |----------|
| <img src="https://github.com/AII-the-time/POS_Android/assets/64644738/97b8c2c9-2762-4c02-92ce-79f0ffbd559d" width="600" height="400"/>|<img src="https://github.com/AII-the-time/POS_Android/assets/64644738/f8715d73-10e0-4557-9475-b6e277051fef" width="300" height="350"/>|

     🔗 적용 이유
    • 총 세 가지의 매출 리포트에 대해 적용 => 화면 구성을 앱 업데이트 없이 진행할 수 있도록 함
    • 리포트 내용을 서버에서 컨트롤 가능하도록 함

## 🔗 구현 화면

| 화면 분류          | 이미지                                                       |
| ----------------- | ------------------------------------------------------------ |
| 로그인 & 회원가입    | <img src="https://github.com/AII-the-time/POS_Android/assets/64644738/b167cf39-2e57-402a-8ea1-0c3b0c8ea729" width="600" height="400"/> |
| 주문 & 마일리지 적립 | <img src="https://github.com/AII-the-time/POS_Android/assets/64644738/36287d7b-dab1-481d-a276-e62296640155" width="600" height="400"/> |
| 예약 주문          | <img src="https://github.com/AII-the-time/POS_Android/assets/64644738/b5c62297-d3ed-46c2-bc0e-680b9a17531c" width="600" height="400"/> |
| 메뉴 & 레시피 등록   | <img src="https://github.com/AII-the-time/POS_Android/assets/64644738/4f9f19e5-6a8b-4051-a0d7-9bbf56122092" width="600" height="400"/> |
| 재고 등록 & 재고 관리 | <img src="https://github.com/AII-the-time/POS_Android/assets/64644738/c1f20f44-83aa-4fa1-a034-5d8d033d6558" width="600" height="400"/> |
