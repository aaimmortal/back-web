alter table `calldata`
    add column `rating` int,
    add column `connect` datetime,
    add column `disconnect` datetime,
    add column `waiting` int,
    add column `durationConsult` int;