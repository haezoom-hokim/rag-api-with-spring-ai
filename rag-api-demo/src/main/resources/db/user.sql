CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100)        NOT NULL,
    age        INTEGER CHECK (age >= 0 AND age <= 150),
    address    TEXT,
    email      VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- 이메일 인덱스 생성 (검색 성능 향상)
CREATE INDEX idx_users_email ON users (email);

-- 초기 데이터 삽입
INSERT INTO users (name, age, address, email)
VALUES ('김철수', 28, '서울특별시 강남구 테헤란로 123', 'kim.chulsoo@example.com'),
       ('이영희', 32, '부산광역시 해운대구 해운대로 456', 'lee.younghee@example.com'),
       ('박민수', 25, '대구광역시 중구 동성로 789', 'park.minsu@example.com'),
       ('정수연', 29, '인천광역시 남동구 구월로 321', 'jung.suyeon@example.com'),
       ('최동혁', 35, '대전광역시 유성구 대학로 654', 'choi.donghyuk@example.com'),
       ('한지원', 26, '광주광역시 서구 상무중앙로 987', 'han.jiwon@example.com'),
       ('오성민', 31, '울산광역시 남구 삼산로 147', 'oh.seongmin@example.com'),
       ('임서현', 27, '경기도 수원시 영통구 광교로 258', 'lim.seohyun@example.com'),
       ('강태호', 33, '강원도 춘천시 중앙로 369', 'kang.taeho@example.com'),
       ('윤미래', 30, '충청북도 청주시 상당구 상당로 741', 'yoon.mirae@example.com');