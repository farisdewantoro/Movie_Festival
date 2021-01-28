INSERT IGNORE INTO roles(id,name,created_at) VALUES(UUID_TO_BIN('32d0ce08-4401-4ad1-98ec-c6970d4d43ea'),'ROLE_USER','2021-01-26 03:45:16');
INSERT IGNORE INTO roles(id,name,created_at) VALUES(UUID_TO_BIN('8173760d-137c-405f-974a-840e7144f273'),'ROLE_ADMIN','2021-01-26 03:45:16');
INSERT INTO users (id, email, full_name,password,user_name,created_at)
VALUES (UUID_TO_BIN('e342193b-8179-435f-a843-73e425cbba09'),'admin@admin.com','admin','admin','admin','2021-01-26 03:45:16');
INSERT INTO users (id, email, full_name,password,user_name,created_at)
VALUES (UUID_TO_BIN('13090c4b-9d1e-40bd-aad2-981fd3d176b8'),'user0@user.com','user0','user0','user0','2021-01-26 03:45:16');