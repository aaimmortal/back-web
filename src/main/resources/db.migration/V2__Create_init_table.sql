create table `usr`
(
    `id`       bigint primary key auto_increment,
    `login`    varchar(32),
    `password` varchar(256)
);
create table `roles`
(
    `id`   bigint primary key auto_increment,
    `name` varchar(32)
);
create table `usr_roles`
(
    `user_id`  bigint,
    `roles_id` bigint,
    foreign key (user_id) references usr (id),
    foreign key (roles_id) references roles (id)
);

