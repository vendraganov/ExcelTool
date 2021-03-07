INSERT INTO authorities (`authority_id`, `role`) VALUES (1, 'ADMIN');
INSERT INTO authorities (`authority_id`, `role`) VALUES (2, 'USER');
INSERT INTO users (`user_id`,`email`,`password`, `authority_id`) VALUES (1, 'admin@a1.bg', '$2a$10$UBfx4TZnp6jUwtnwGAmYteaQVKDea8ct2OdJxC4MD3jQK3jP.KVnq', 1);
INSERT INTO users (`user_id`,`email`,`password`, `authority_id`) VALUES (2, 'user@a1.bg', '$2a$10$UBfx4TZnp6jUwtnwGAmYteaQVKDea8ct2OdJxC4MD3jQK3jP.KVnq', 2);