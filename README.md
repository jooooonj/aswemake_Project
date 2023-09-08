# 👩‍💻 지원자 : 이재준

# 📌 애플리케이션의 실행 방법

1. Git clone (https://github.com/jooooonj/aswemake_Project.git)
2. application-secret.yml.default -> application-secret.yml 파일명 변경
3. application-secret.yml 환경변수 직접 기입
4. 실행

<br></br>

### 환경변수
${DATABASE_HOST} : 데이터베이스 호스트 주소

${DATABASE_PORT} : 데이터베이스 포트 번호

${DATABASE_NAME} : 데이터베이스 이름

${DATABASE_USERNAME} : 데이터베이스 사용자 이름

${DATABASE_PASSWORD} : 데이트베이스 비밀번호

${JWT_SECRET_KEY} : JWT 토큰 서명에 사용되는 비밀 키

${MART_ADMIN_EMAIL} : 관리자 계정 (이메일 형식 @포함)

${MART_ADMIN_PASSWORD} : 관리자 계정 비밀번호 (8자리 이상)

<br></br>

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

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/eaf41fee-300d-4020-911c-1c918213f0c2)
<br></br>

---

## 상품(product) API

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

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/78d9eae6-b95b-4882-b86c-e445bf7029d6)
<br></br>

## 주문에 대한 필요 결제 금액 조회 API
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

![image](https://github.com/jooooonj/aswemake_Project/assets/110995932/a425493c-66d7-401f-8c1b-800df066a751)


<br></br>
