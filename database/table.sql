create table FOLLOW
(
    id        bigint auto_increment
        primary key,
    follower  bigint not null,
    following bigint not null
);

create table RECORD
(
    id           bigint auto_increment
        primary key,
    name         varchar(50)  null,
    comment      varchar(500) null,
    type         varchar(50)  null,
    started_at   datetime     null,
    finished_at  datetime     null,
    paused_count int          null,
    paused_time  bigint       null,
    duration     bigint       null,
    content      json         null,
    created_at   datetime     null,
    updated_at   datetime     null,
    user_id      bigint       not null,
    task_id      bigint       not null
);

create table TASK
(
    id             bigint auto_increment
        primary key,
    name           varchar(50) null,
    type           varchar(50) null,
    status         varchar(20) null,
    state          varchar(20) null,
    continuous     int         null,
    pause_count    int         null,
    paused_time    bigint      null,
    started_at     datetime    null,
    paused_at      datetime    null,
    last_done_at   datetime    null,
    total_duration bigint      null,
    created_at     datetime    null,
    updated_at     datetime    null,
    user_id        bigint      not null
);

create table USER
(
    id         bigint auto_increment
        primary key,
    name       varchar(50)  not null,
    email      varchar(100) not null,
    comment    varchar(500) null,
    profile    json         null,
    created_at datetime     not null,
    updated_at datetime     not null,
    constraint USER_email_uindex
        unique (email)
);
