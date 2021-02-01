create table account (
	account_id bigint not null auto_increment,
	password varchar(255) not null,
	username varchar(255) not null unique,
	primary key (account_id)
);

create table role (
	role_id bigint not null auto_increment,
	role_name varchar(255) not null unique,
	primary key (role_id)
);

create table account_role (
	account_role_id bigint not null auto_increment,
	account_id bigint,
	role_id bigint,
	primary key (account_role_id),
	foreign key (`account_id`) references account(`account_id`),
	foreign key (`role_id`) references role(`role_id`)
);

create table role_hierarchy (
	role_hierarchy_id bigint not null auto_increment,
	role_id bigint,
	parent_id bigint,
	primary key (role_hierarchy_id),
	foreign key (`role_id`) references role(`role_id`),
	foreign key (`parent_id`) references role_hierarchy(`role_hierarchy_id`)
);

create table board (
	board_id bigint not null auto_increment,
	no bigint,
	title varchar(255),
	writer varchar(255),
	content mediumtext,
	created_date datetime,
	hit bigint default 0,
	primary key (board_id)
);

create table comment (
	comment_id bigint not null auto_increment,
	writer varchar(255),
	content varchar(255),
	order_number varchar(255),
	created_date datetime,
	board_id bigint,
	main_comment_id bigint,
	primary key (comment_id),
	foreign key (`board_id`) references board(`board_id`),
	foreign key (`main_comment_id`) references comment(`comment_id`)
);

create table image (
	image_id bigint not null auto_increment,
	name varchar(255) not null,
	board_id bigint,
	primary key (image_id),
	foreign key (`board_id`) references board(`board_id`)
);

create table chat (
	chat_id bigint not null auto_increment,
	writer varchar(255),
	text varchar(255),
	is_day_first boolean not null,
	created_date datetime,
	primary key (chat_id)
);

-- 권한 계층 구조 설정
insert into role (role_name) values ('ROLE_USER');
insert into role_hierarchy (role_id) values (1);
insert into role (role_name) values ('ROLE_MANAGER');
insert into role_hierarchy (parent_id, role_id) values (1, 2);
insert into role (role_name) values ('ROLE_ADMIN');
insert into role_hierarchy (parent_id, role_id) values (2, 3);

-- 계정 추가
-- 	(id: root, password: yellowsunn, role: ROLE_ADMIN)
insert into account (username, password) values ('root', '{bcrypt}$2y$12$t38YJDFVpqPGwYyg1aZJI.0IvM8wBHBK8Jw04qvSrpvU2N5Dlxa2.');
insert into account_role (account_id, role_id) values (1, 3);
-- 	(id: manager, password: yellowsunn, role: ROLE_MANAGER)
insert into account (username, password) values ('manager', '{bcrypt}$2y$12$nd6eBVHhFo602Cuhc0EgJ.8eLHaeBtseBU4neEqVWLpt0gtbfq7bO');
insert into account_role (account_id, role_id) values (2, 2);
-- 	(id: user, password: yellowsunn, role: ROLE_USER)
insert into account (username, password) values ('user', '{bcrypt}$2y$12$FbArnu7ngv.AoFSbC9zyTubLKd/0UymABifCKz8TifsfI0y2apIrW');
insert into account_role (account_id, role_id) values (3, 1);