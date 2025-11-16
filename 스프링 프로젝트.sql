select * from tblplace;

select * from tbltouristspot;

create sequence seqReviewScrap;

select * from tblreviewboard;


- 1. 현재 테이블의 POST_STATUS 컬럼을 삭제합니다.
-- (ERD의 REVIEW_BOARD_REPORT_STATUS와 역할이 중복됩니다)
ALTER TABLE tblReviewBoard DROP COLUMN POST_STATUS;


-- 2. ERD 기준으로 빠진 4개의 컬럼을 모두 추가합니다.
ALTER TABLE tblReviewBoard ADD (
    review_board_scrap_count NUMBER DEFAULT 0,
    review_board_report_status CHAR(1 BYTE) DEFAULT 'A',
    review_board_report_count NUMBER DEFAULT 0,
    review_board_update DATE
);


commit;

select * from tblPlaceLocation;

select * from tblplace;

DELETE FROM tblPlaceLocation WHERE place_location_id BETWEEN 3 AND 23;

-- 1. (자식) tblRestaurant에서 1~50번 삭제
DELETE FROM tblRestaurant WHERE place_id BETWEEN 1 AND 50;
select * from tblRestaurant;
-- 2. (자식) tblTouristSpot에서 1~50번 삭제
DELETE FROM tblTouristSpot WHERE place_id BETWEEN 1 AND 50;
select * from tblTouristspot;
-- 3. (부모) tblPlace에서 14~50번 삭제
DELETE FROM tblPlace WHERE place_id BETWEEN 14 AND 50;

select * from tblEvent;
DELETE FROM tblevent WHERE event_id BETWEEN 1 AND 50;
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (3, '제주');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (4, '강릉');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (5, '대구');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (6, '전주');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (7, '인천');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (8, '여수');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (9, '울산');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (10, '수원');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (11, '경주');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (12, '대전');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (13, '춘천');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (14, '광주');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (15, '세종');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (16, '경기');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (17, '강원');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (18, '충북');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (19, '충남');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (20, '경북');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (21, '경남');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (22, '전북');
INSERT INTO tblPlaceLocation (place_location_id, place_location_name) VALUES (23, '전남');

commit;