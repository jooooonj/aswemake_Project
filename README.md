# 👩‍💻 지원자 : 이재준
#### 개발 환경
- JAVA 17
- Spring Boot 3.x.
- MySQL

#### Git
- 요구사항을 issue에 기록하고 pr 단위로 작업을 진행했습니다.

# 📌 애플리케이션의 실행 방법

#### #1 Local
1. Git clone (https://github.com/jooooonj/aswemake_Project.git)
2. application-secret.yml.default -> application-secret.yml 파일명 변경
3. application-secret.yml 환경변수 직접 기입
4. 실행

#### #2 Docker
1. Git clone (https://github.com/jooooonj/aswemake_Project.git)
2. application-secret.yml.default -> application-secret.yml 파일명 변경
3. 프로젝트 하위 경로 .env 파일 생성 후 양식에 맞게 기입
4. jar 빌드 (./gradlew clean build)
5. 도커 실행 (docker compose up -d)

---

#### 📌 애플리케이션 환경 변수 (application-secret.yml)

${DATABASE_HOST} : 데이터베이스 호스트 주소

${DATABASE_PORT} : 데이터베이스 포트 번호

${DATABASE_NAME} : 데이터베이스 이름

${DATABASE_USERNAME} : 데이터베이스 사용자 이름

${DATABASE_PASSWORD} : 데이트베이스 비밀번호

${JWT_SECRET_KEY} : JWT 토큰 서명에 사용되는 비밀 키

${MART_ADMIN_EMAIL} : 관리자 계정 (이메일 형식 @포함)

${MART_ADMIN_PASSWORD} : 관리자 계정 비밀번호 (8자리 이상)

<br></br>
#### 📌 .env 파일 양식
MYSQL_CONTAINER_NAME={사용자지정 - MYSQL 컨테이너 이름}

MYSQL_DATABASE={사용자지정 - MYSQL 데이터베이스 이름}

MYSQL_ROOT_PASSWORD={사용자지정 - MYSQL root계정 비밀번호}

JWT_SECRET_KEY={사용자지정 - JWT 토큰 서명에 사용되는 비밀 키}

MART_ADMIN_EMAIL={사용자지정 - 마트계정 이메일 (이메일 형식 @ 필수)}

MART_ADMIN_PASSWORD={사용자지정 - 마트계정 비밀번호(8자리 이상)}

---

# 📌 DB 테이블 구조

### 회원 (member)

- id (PK)
- email
- password
- roleType
  - MART, MEMBER
- created_date
- modified_date


### 상품 (product)

- id (PK)
- product_name
- price
- product_snapshot_id (FK)
  - 일대일 관계 : 상품 스냅샷 (product_snapshot)과 연결
- created_date
- modified_date


### 상품 스냅샷 (product_snapshot)

- id (PK)
- productId
- product_name
- price
- fromDate
- toDate


### 주문 (orders)

- id (PK)
- member_id (FK)
  - 다대일 관계 : 회원(member)과 연결
- coupon_id (FK)
  - 일대일 관계 : 쿠폰(discount_coupon)과 연결
- total_price
- delivery_fee
- created_date
- modified_date


### 주문품목 (order_item)

- id (PK)
- order_id (FK)
  - 다대일 관계 : 주문(orders)과 연결
- product_id (FK)
  - 다대일 관계 : 상품(productSnapshot)과 연결
- coupon_id (FK)
  - 일대일 관계 : 쿠폰(discount_coupon)과 연결
- quantity
- order_item_price


### 할인 쿠폰 (discount_coupon) [추상 클래스]

- coupon_id (PK)
- coupon_type
  - FIXED, PERCENTAGE
- coupon_apply_scope
  - ORDER_ALL, SPECIFIC_PRODUCT
- coupon_code (UUID)
- created_date
- modified_date


### 고정 할인 쿠폰 (fixed_discount_coupon)

- coupon_id (PK,FK)
- fixed_discount_amount


### 비율 할인 쿠폰 (percentage_discount_coupon)

- coupon_id (PK,FK)
- discount_percentage
  <br></br>

---

# 📌 API 명세

## 회원(member) API
- 초기 데이터 (2개)
  - 환경변수에 기입한 관리자 계정 (마트권한)
  - 이메일 : member1@naver.com, 비밀번호 : 12345678 (회원권한)

### 회원 가입 API
- 엔드포인트: POST /api/v1/member/join
- Request Body: 사용자 정보를 담은 요청 데이터

  - email (문자열): 사용자의 이메일 주소 (반드시 '@' 포함)
  - password (문자열): 사용자의 비밀번호 (최소 8자리 이상)
- Response: 생성된 회원의 ID

#### 요청
- Method: POST
- URL: http://localhost:8080/api/v1/member/join
- Headers: Content-Type: application/json
- Body:{
  "email": "member@naver.com",
  "password": "123456789"
  }

#### 응답
- Status: 201 Created
- Body : 생성된 회원의 ID

#### 설명
- 이메일과 비밀번호로 계정을 생성합니다.
- 관리자 계정은 초기에 자동 생성됩니다.
- 관리자 계정과 같은 이메일은 사용이 불가능합니다.

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/bacf823c-6ef7-4b69-9d63-7781bf8c2a66)

<br></br>

### 로그인 API
- 엔드포인트: POST /api/v1/member/login
- Request Body: 사용자 정보를 담은 요청 데이터
  - email (문자열): 가입된 사용자의 이메일 주소 (반드시 '@' 포함)
  - password (문자열): 가입된 사용자의 비밀번호 (최소 8자리 이상)
- Response: JWT 토큰

#### 요청
- Method: POST
- URL: http://localhost:8080/api/v1/member/login
- Headers:
  - Content-Type: application/json
- Body:{
  "email": "member@naver.com",
  "password": "123456789"
  }

#### 응답
- Status: 200 OK
- Body: 로그인 성공 시 발급된 JWT 토큰

#### 설명
- 가입한 이메일과 비밀번호로 로그인을 요청합니다.
- 로그인 성공시 jwt 토큰을 발급받습니다.

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/eaf41fee-300d-4020-911c-1c918213f0c2)
<br></br>

---

## 상품(product) API
- 초기 데이터 (2개)
  - 상품번호 : 1, 상품명 : 테스트 상품 1, 상품가격 : 10000
    - 현재 시점의 상품 스냅샷 (~ing)
    - 2012.12.25 ~ 2012.12.30 시점의 상품 스냅샷
  - 상품번호 : 2, 상품명 : 테스트 상품 2, 상품가격 : 20000
    - 현재 시점의 상품 스냅샷 (~ing)

### 상품 생성 (등록) API
- 엔드포인트: POST /api/v1/products
- Request Body: 게시글 생성 요청 데이터
  - productName (문자열): 상품명 (Not Blank)
  - price (숫자): 상품 가격 (min = 1000)
- Response: 상품의 정보를 담은 응답 데이터

#### 요청
- Method: POST
- URL: http://localhost:8080/api/v1/products
- Headers:
  - Content-Type: application/json
  - Authorization: Bearer {JWT_Token} - (MART 권한 필수)
- Body:{
  "productName" : "상품입니다.",
  "price" : "20000"
  }

#### 응답
- Status: 201 Created
- Body: {
  "id": 3,
  "productName": "상품입니다.",
  "price": 20000
  }

#### 설명
- 상품명과 상품 가격으로 상품을 생성할 수 있습니다.
- 마트 권한 계정으로 로그인이 필요합니다.

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/dccaac25-05e4-44d0-a13f-4975a29ca6ac)
<br></br>

### 상품 가격 수정 API
- 엔드포인트: PATCH /api/v1/products/{productId}
- Request Body: 상품 가격 수정 요청 데이터
  - price : 수정하고자 하는 상품 가격
- Response: 수정된 상품의 정보를 담은 응답 데이터

#### 요청
- Method: PATCH
- URL: http://localhost:8080/api/v1/products/{productId}
- Headers:
  - Content-Type: application/json
  - Authorization: Bearer {JWT_Token} (MART 권한 필수)
- Body:{
  "price" : "1000000"
  }


#### 응답
- Status: 200 OK
- Body: {
  "id": {productId},
  "productName": "테스트 상품 1",
  "price": 1000000
  }

#### 설명
- 상품 가격을 수정합니다.
- 마트 권한 계정으로 로그인이 필요합니다.


![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/513b1046-5b3f-4c0d-83b0-52665a50d8eb)
<br></br>

### 상품 삭제 API
- 엔드포인트: DELETE /api/v1/products/{productId}
- Response: 없음

#### 요청
- Method: DELETE
- URL: http://localhost:8080/api/v1/products/{productId}
- Headers:
  - Content-Type: application/json
  - Authorization: Bearer {JWT_Token} (MART 권한 필수)

#### 응답
- Status: 204 No Content

#### 설명
- 상품 번호로 상품을 삭제합니다.
- 마트 권한 계정으로 로그인이 필요합니다.

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/b5660fd0-3eb1-46b9-8393-39ac4eec441b)
<br></br>

### 특정 시점 상품 가격 조회 API
- 엔드포인트: GET /api/v1/products/price
  - Request Param : productId (상품 ID)
  - Request Param : timestamp (찾고자 하는 특정 시점, required=false) (Lodat
- Response: timestamp 시기에 특정 상품의 가격

#### 요청
- Method: GET
- URL: http://localhost:8080/api/v1/products/price?productId=&timestamp=
- Headers:
  - Authorization: Bearer {JWT_Token}

#### 응답
- Status: 200 OK
- Body: 특정 시점 상품 가격

#### 특이사항
- timestamp 빈 값이 들어올시 현재 가격을 조회합니다.

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/de71db67-d86b-44b5-a351-3de2c56e1aae)
<br></br>

---

## 주문(order) API
- 초기 데이터 (2개)
  - 주문번호 : 1, 배송비 : 3000
    - 주문품목번호 : 1, 상품번호 : 1 (1만원), 수량 : 5
    - 주문품목번호 : 2, 상품번호 : 2 (2만원), 수량 : 2
  
  - 주문번호 : 2, 배송비 : 5000
    - 주문품목번호 : 3, 상품번호 : 1 (1만원), 수량 : 2
    - 주문품목번호 : 4, 상품번호 : 2 (2만원), 수량 : 10

## 주문 생성 API
- 엔드포인트: POST /api/v1/orders
- Request Body: 주문 생성 요청 데이터
  - orderItemRequestDtoList (json 리스트): 주문품목 요청 데이터 리스트
    - productId (숫자) : 주문 품목의 상품 고유 번호 (not null)
    - quantity (숫자) : 주문하려는 상품의 수량 (min = 1)
  - deliveryFee : 배송비 (not null)

#### 요청
- Method: POST
- URL: http://localhost:8080/api/v1/orders
- Headers:
  - Content-Type: application/json
  - Authorization: Bearer {JWT_Token}
- Body:{
  "orderItemRequestDtoList": [
  {
  "productId": 1,
  "quantity": 3
  },
  {
  "productId": 2,
  "quantity": 2
  }
  ],
  "deliveryFee": 5000
  }

#### 응답
- Status: 201 Created
- Body: 생성된 주문 번호

#### 설명
- 주문 데이터를 생성합니다.
- 로그인이 필요합니다. (권한은 상관없습니다.)

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/ab728ceb-8ba9-4fb3-9e2a-1da230aaf123)
<br></br>

## 주문에 대한 총금액 조회 API
- 엔드포인트: GET /api/v1/orders/{orderId}/price
- Response: 해당 주문 총금액

#### 요청
- Method: GET
- URL: http://localhost:8080/api/v1/orders/1/price
- Headers:
  - Authorization: Bearer {JWT_Token}

#### 응답
- Status: 200 OK
- Body: 주문 총금액

#### 설명
- 특정 주문에 대한 총금액을 조회합니다. (배송비 포함)
- 로그인이 필요합니다. (권한은 상관없습니다.)

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/78d9eae6-b95b-4882-b86c-e445bf7029d6)
<br></br>

## 주문에 대한 필요 결제 금액 조회 API
- 초기 데이터 (4개)
  - 쿠폰 번호 : 1, 적용범위 : 전체주문, 쿠폰종류 : 고정할인쿠폰, 할인금액 : 20000, 쿠폰코드 : "2만원_고정할인_전체주문"
  - 쿠폰 번호 : 2, 적용범위 : 특정상품, 쿠폰종류 : 고정할인쿠폰, 할인금액 : 20000, 쿠폰코드 : "2만원_고정할인_특정상품"
  - 쿠폰 번호 : 3, 적용범위 : 전체주문, 쿠폰종류 : 비율할인쿠폰, 할인율 : 10%, 쿠폰코드 : "10퍼센트_비율할인_전체주문"
  - 쿠폰 번호 : 4, 적용범위 : 특정상품, 쿠폰종류 : 고정할인쿠폰, 할인율 : 10%, 쿠폰코드 : "10퍼센트_비율할인_특정상품"

- 본래 쿠폰 코드는 UUID 랜덤값이지만 원활한 테스트를 위해서 직접 생성했습니다.

<br></br>

- 엔드포인트: POST /api/v1/orders/{orderId}/calculate-paymentPrice
  - request param : couponCode (할인 쿠폰 코드, required=false)
  - request param : orderItemId (할인 쿠폰 코드 적용하고자 하는 특정 주문 품목, required=false)
- Response: 주문에 대한 필요 결제 금액

#### 요청
- Method: POST
- URL: http://localhost:8080/api/v1/orders/1/calculate-paymentPrice?couponCode=2만원_고정할인_특정상품&orderItemId=1
- Headers:
  - Authorization: Bearer {JWT_Token}

#### 응답
- Status: 200 OK
- Body: 주문에 대한 필요 결제 금액

#### 설명
- 특정 주문에 대한 결제 금액을 조회합니다.
- 쿠폰 코드를 사용해 쿠폰 적용이 가능합니다.
  - 쿠폰은 주문 당 하나만 적용이 가능하다고 가정합니다.
  - 고정할인쿠폰 / 비율할인쿠폰은 쿠폰 코드에 따라 다릅니다.
  - orderItemId 값의 유/무에 따라 전체주문, 특정상품 적용범위를 결정합니다.
- 로그인이 필요합니다. (권한은 상관없습니다.)

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/a425493c-66d7-401f-8c1b-800df066a751)


<br></br>
