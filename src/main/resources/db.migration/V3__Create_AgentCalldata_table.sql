create table `agent_calldata`
(
    `id`          bigint primary key auto_increment,
    `agentid`     varchar(80),
    `calldataid`  varchar(32),
    `disposition` varchar(32)
);