create table `wfm`
(
    `id`      bigint primary key auto_increment,
    `agentid` varchar(80),
    `date`    datetime,
    `action`  varchar(80)
);