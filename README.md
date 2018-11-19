# !!EXPIRED PROJECT!!palmapps
~~palmapps - PalmPay, PalmPos Frontend and Backend~~

## 백 할 일 정리
~~* 쿠폰 발급 API~~
* 

## 프론트 할 일 정리
~~* 메뉴 이미지 저장 및 받아오는 기능~~

### Dev Log
#### 2018-11-07
~~* 프론트엔드부분은 거의 완성단계~~
~~* Beacon Detecting과 서버와 연결을 제외하면 90% 완성~~
~~* 현재 MenuRecyclerView와 OrderlistRecyclerView 만 연결하면 100퍼 완성~~
~~* 이동시 버그있는지 Resource를 너무 많이 잡아먹진 않는지 계속 확인해야함~~

#### 2018-11-10
~~* Frontend 95% 완성~~
~~* DB 90%~~
~~* Backend - Frontend 연동 - 아이디 중복확인, 회원가입, 로그인 error처리까지 ~~

#### 2018-11-11
~~* Backend - Frontend 연동 - 제휴점 리스트, 메뉴판까지 구현~~
~~진짜 얼마 안남았다<br>~~
~~1. 비콘 연동, 비콘 정보 조회 db<br>~~
~~2. 진동벨 기능<br>~~
~~3. 주문 내역 전송 기능<br>~~
~~4. palmpos<br>~~

#### 2018-11-11 19:51
~~* Backend - Frontend 연동 - 주문 내역 전송 구현 완료~~

#### 2018-11-12
~~* Backend - Frontend 연동 - 주문 내역(현재주문/과거주문) 받아오기 구현 중, front는 잘 되었으나 서버쪽에서 404 에러가 뜸, 자야될 시간이라 자야될 것 ~~

#### 2018-11-19
* 비콘 연동까지 완료했고, 비콘은 완벽한 수단이라는 것을 깨닳았으나. 치명적인 문제가 하나 존재한다. 그것은 바로 **비콘을 이용하기 위해 사용자는 스마트폰의 블루투스 기능과 위치 기능을 켜놓아야 한다**. 이걸 해결하기 위해 백그라운드 서비스로 돌리려고 해도 문제는 안드로이드 운영체제를 제공하는 구글과 각 스마트폰 제조사들이다. 이들은 최적화되지 않은 OS의 배터리 문제를 해결하기 위해 **TaskKiller**를 다들 가지고 백그라운드 태스크를 관리한다. 이를 극복하기 위해 immortal 태스크로 돌릴 수 있는 방법을 찾아 구현했으나 기업이 되었을 경우 기업 윤리에 매우 어긋나는 일 인 것 같다. 한 기업이 제공하는 어플이 사용자들을 속이고 좀비 어플리케이션을 기반으로 서비스를 제공한다 라고 하면 누가 쓰겠는가. 미래에 배터리 문제에서 자유로워지고 비콘보다 더 정확한 실내 측위가 가능해지는 날을 기약해야겠다.
