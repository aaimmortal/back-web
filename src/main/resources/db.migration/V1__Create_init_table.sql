create table `usr`
(
    `id`       bigint primary key auto_increment,
    `login`    varchar(32),
    `password` varchar(256),
    `avatar`   varchar(80)
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

create table `wfm`
(
    `id`             bigint primary key auto_increment,
    `agentid`        varchar(80),
    `date`           datetime,
    `action`         varchar(80),
    `address`        varchar(80),
    `pausedduration` int

);
create table `calldata`
(
    `id`              varchar(80),
    `calldate`        datetime,
    `source`          varchar(80),
    `destination`     varchar(80),
    `disposition`     varchar(80),
    `duration`        int,
    `audiopath`       varchar(80),
    `rating`          int,
    `language`        varchar(32),
    `connect`         datetime,
    `disconnect`      datetime,
    `waiting`         int,
    `durationConsult` int,
    `dropped`         int
);
create table `agent`
(
    `id`       bigint primary key auto_increment,
    `name`     varchar(80),
    `agentId`  varchar(80),
    `status`   varchar(80),
    `paused`   varchar(80),
    `lastCall` datetime
);
create table `agent_calldata`
(
    `id`          bigint primary key auto_increment,
    `agentid`     varchar(80),
    `calldataid`  varchar(32),
    `disposition` varchar(32),
    `calldate`    DATETIME
);
create table `address`
(
    `id`           bigint primary key auto_increment,
    `agentid`      varchar(80),
    `streetNumber` varchar(80),
    `action`       varchar(80)
);