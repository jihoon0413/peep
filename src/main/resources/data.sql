insert into school (id, education_office, school_name) values (1, '광주광역시교육청', '광주고등학교');
insert into school (id, education_office, school_name) values (2, '광주광역시교육청', '조선대학교부속중학교');
insert into school (id, education_office, school_name) values (3, '광주광역시교육청', '동신여자고등하교');
insert into school (id, education_office, school_name) values (4, '광주광역시교육청', '문산중학교');
insert into school (id, education_office, school_name) values (5, '광주광역시교육청', '살레시오고등학교');

insert into question (id, content, created_at) values (1, 'Maecenas tincidunt lacus at velit.', '2024-05-24 22:27:31');
insert into question (id, content, created_at) values (2, 'Proin eu mi. Nulla ac enim.', '2024-06-11 21:08:37');
insert into question (id, content, created_at) values (3, 'consectetuer adipiscing elit. Proin risus.', '2024-06-12 17:23:41');
insert into question (id, content, created_at) values (4, 'Curabitur in libero ut massa volutpat convallis.', '2024-01-05 07:21:51');
insert into question (id, content, created_at) values (5, 'Maecenas tristique, est et tempus semper, est quam p.', '2024-08-25 01:17:21');

insert into coin (id, my_coin, created_at, updated_at, is_deleted) values (1, 898, '2024-06-29 01:32:39', '2024-06-21 10:49:26', false);
insert into coin (id, my_coin, created_at, updated_at, is_deleted) values (2, 840, '2024-05-03 16:59:25', '2024-02-14 08:26:46', false);
insert into coin (id, my_coin, created_at, updated_at, is_deleted) values (3, 852, '2024-10-16 01:57:21', '2024-03-13 08:17:55', false);
insert into coin (id, my_coin, created_at, updated_at, is_deleted) values (4, 956, '2023-11-14 17:55:06', '2024-03-18 08:25:05', false);
insert into coin (id, my_coin, created_at, updated_at, is_deleted) values (5, 690, '2024-03-25 21:28:33', '2024-05-28 07:26:59', false);

insert into photo (id, photo_url, created_at, updated_at, is_deleted) values (1, 'http://dummyimage.com/126x100.png/dddddd/000000', '2023-12-20 15:07:38', null, false);
insert into photo (id, photo_url, created_at, updated_at, is_deleted) values (2, 'http://dummyimage.com/116x100.png/5fa2dd/ffffff', '2024-07-25 03:06:50', '2024-02-08 03:06:17', false);
insert into photo (id, photo_url, created_at, updated_at, is_deleted) values (3, 'http://dummyimage.com/156x100.png/ff4444/ffffff', '2024-02-25 23:55:53', null, false);
insert into photo (id, photo_url, created_at, updated_at, is_deleted) values (4, 'http://dummyimage.com/154x100.png/cc0000/ffffff', '2024-05-13 16:07:57', '2023-11-19 00:24:54', false);
insert into photo (id, photo_url, created_at, updated_at, is_deleted) values (5, 'http://dummyimage.com/103x100.png/ff4444/ffffff', '2024-03-28 01:20:12', '2024-06-09 19:36:46', false);

insert into hashtag (id, content, type) values (1, 'shy', 'CHARACTER');
insert into hashtag (id, content, type) values (2, 'energy', 'CHARACTER');
insert into hashtag (id, content, type) values (3, 'tennis', 'HOBBY');
insert into hashtag (id, content, type) values (4, 'lazy', 'CHARACTER');
insert into hashtag (id, content, type) values (5, 'soccer', 'HOBBY');

insert into student (id, user_id, user_password, name, school_id, coin_id, photo_id, tel, grade, my_class, created_at, updated_at, is_deleted) values (1, 'jihoon', '$2a$10$pLwVE3EJmIPkRqvVLVbRKe0RRSwbC1p2tVdZrDhv6S65y9joeOYxC', '신지훈', 1, 1, 1, '181-445-5294', 1, 1, '2024-05-18 14:19:00', '2024-06-29 16:29:12', false);
insert into student (id, user_id, user_password, name, school_id, coin_id, photo_id, tel, grade, my_class, created_at, updated_at, is_deleted) values (2, 'minji', '$2a$10$pLwVE3EJmIPkRqvVLVbRKe0RRSwbC1p2tVdZrDhv6S65y9joeOYxC', '서민지', 1, 2, 2, '397-992-0977', 1, 1, '2024-03-15 01:07:48', '2024-08-14 11:15:17', false);
insert into student (id, user_id, user_password, name, school_id, coin_id, photo_id, tel, grade, my_class, created_at, updated_at, is_deleted) values (3, 'inseo', '$2a$10$pLwVE3EJmIPkRqvVLVbRKe0RRSwbC1p2tVdZrDhv6S65y9joeOYxC', '박인서', 1, 3, 3, '698-895-8592', 1, 1, '2024-09-22 05:37:18', '2024-08-05 11:34:22', false);
insert into student (id, user_id, user_password, name, school_id, coin_id, photo_id, tel, grade, my_class, created_at, updated_at, is_deleted) values (4, 'soomin', '$2a$10$pLwVE3EJmIPkRqvVLVbRKe0RRSwbC1p2tVdZrDhv6S65y9joeOYxC', '유수민', 1, 4, 4, '853-879-3906', 1, 1, '2024-10-14 20:42:28', '2023-12-30 14:05:07', false);
insert into student (id, user_id, user_password, name, school_id, coin_id, photo_id, tel, grade, my_class, created_at, updated_at, is_deleted) values (5, 'Cameron', '$2a$10$pLwVE3EJmIPkRqvVLVbRKe0RRSwbC1p2tVdZrDhv6S65y9joeOYxC', 'Cinda', 1, 5, 5, '775-365-3814', 1, 1, '2024-10-17 12:33:43', '2024-07-15 21:09:27', false);

insert into block (id, blocker_id, blocked_id, created_at, updated_at) values (1, 1, 5, '2023-05-22 20:54:03','2024-05-22 20:54:03');

insert into follow (id, follower_id, following_id, created_at, is_deleted) values (1, 1, 3, '2024-05-22 20:54:03', false);
insert into follow (id, follower_id, following_id, created_at, is_deleted) values (2, 1, 2, '2024-01-20 15:04:37', false);
insert into follow (id, follower_id, following_id, created_at, is_deleted) values (3, 2, 1, '2024-04-21 06:41:32', false);
insert into follow (id, follower_id, following_id, created_at, is_deleted) values (4, 3, 1, '2023-11-30 00:34:41', false);
insert into follow (id, follower_id, following_id, created_at, is_deleted) values (5, 5, 2, '2024-03-04 13:42:45', false);

insert into community (id, school_id, grade, my_class, created_at, updated_at, is_deleted) values (1, 1, 1, 1, '2024-01-12 10:31:14', '2024-10-22 11:11:30', true);
insert into community (id, school_id, grade, my_class, created_at, updated_at, is_deleted) values (2, 1, 1, 2, '2024-10-01 14:17:55', '2024-04-01 04:35:25', false);
insert into community (id, school_id, grade, my_class, created_at, updated_at, is_deleted) values (3, 1, 1, 3, '2024-08-01 14:57:54', '2024-01-01 19:10:22', false);
insert into community (id, school_id, grade, my_class, created_at, updated_at, is_deleted) values (4, 1, 2, 1, '2024-10-19 18:53:26', '2024-01-21 13:56:56', true);
insert into community (id, school_id, grade, my_class, created_at, updated_at, is_deleted) values (5, 2, 2, 2, '2024-06-25 16:40:10', '2024-09-01 23:12:35', true);

insert into student_community (id, student_id, community_id, created_at, updated_at, is_deleted) values (1, 1, 1, '2023-11-26 21:33:55', '2023-11-27 22:46:14', true);
insert into student_community (id, student_id, community_id, created_at, updated_at, is_deleted) values (2, 2, 1, '2024-08-24 17:33:48', '2024-10-26 10:23:15', false);
insert into student_community (id, student_id, community_id, created_at, updated_at, is_deleted) values (3, 3, 1, '2024-01-02 18:39:41', '2023-11-16 05:55:36', false);
insert into student_community (id, student_id, community_id, created_at, updated_at, is_deleted) values (4, 4, 1, '2024-03-30 07:03:24', '2024-01-22 04:23:01', false);
insert into student_community (id, student_id, community_id, created_at, updated_at, is_deleted) values (5, 5, 1, '2024-03-26 01:58:49', '2024-04-21 19:53:05', true);

insert into student_hashtag (id, student_id, hashtag_id, created_at, is_deleted) values (1, 1, 5, '2024-05-17 12:34:10', false);
insert into student_hashtag (id, student_id, hashtag_id, created_at, is_deleted) values (2, 1, 4, '2023-12-16 21:29:58', false);
insert into student_hashtag (id, student_id, hashtag_id, created_at, is_deleted) values (3, 1, 1, '2024-10-07 19:09:39', false);
insert into student_hashtag (id, student_id, hashtag_id, created_at, is_deleted) values (4, 2, 3, '2024-04-24 07:14:50', false);
insert into student_hashtag (id, student_id, hashtag_id, created_at, is_deleted) values (5, 2, 5, '2024-06-28 18:18:43', false);

insert into community_question (id, community_id, question_id, created_at, updated_at) values (1, 1, 3, '2024-09-27 07:23:38', null);
insert into community_question (id, community_id, question_id, created_at, updated_at) values (2, 1, 2, '2024-03-26 20:42:29', null);
insert into community_question (id, community_id, question_id, created_at, updated_at) values (3, 1, 5, '2024-03-19 12:24:56', null);
insert into community_question (id, community_id, question_id, created_at, updated_at) values (4, 1, 1, '2024-02-24 02:04:07', null);
insert into community_question (id, community_id, question_id, created_at, updated_at) values (5, 2, 1, '2024-10-29 05:23:46', null);

insert into student_community_question (id, writer_id, chosen_id, community_question_id, created_at, updated_at) values (1, 1, 2, 5, '2024-05-26 04:11:25', '2023-11-13 16:14:24');
insert into student_community_question (id, writer_id, chosen_id, community_question_id, created_at, updated_at) values (2, 1, 3, 3, '2024-04-02 03:05:29', '2023-11-21 18:43:28');
insert into student_community_question (id, writer_id, chosen_id, community_question_id, created_at, updated_at) values (3, 2, 1, 4, '2024-09-18 14:58:27', '2024-04-27 11:47:41');
insert into student_community_question (id, writer_id, chosen_id, community_question_id, created_at, updated_at) values (4, 3, 1, 1, '2024-07-22 17:31:20', '2024-03-17 16:45:46');
insert into student_community_question (id, writer_id, chosen_id, community_question_id, created_at, updated_at) values (5, 4, 1, 4, '2023-11-30 21:44:12', '2024-06-18 02:21:16');