-- 创建库
create database if not exists ruogu_like;

-- 切换库
use ruogu_like;


-- 用户表
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)  not null comment '账号',
    user_password varchar(512)  not null comment '密码',
    union_id      varchar(256)  null comment '微信开放平台id',
    mp_open_id    varchar(256)  null comment '公众号openId',
    user_name     varchar(256)  null comment '用户昵称',
    user_avatar   varchar(1024) null comment '用户头像',
    user_profile  varchar(512)  null comment '用户简介',
    user_role     varchar(256)           default 'user' not null comment '用户角色：user/admin',
    user_status   tinyint                default 0 not null comment '用户状态（0正常 1停用）',
    creator       varchar(64)   NULL     DEFAULT '' COMMENT '创建者',
    create_time   datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater       varchar(64)   NULL     DEFAULT '' COMMENT '更新者',
    update_time   datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted       bit(1)        NOT NULL DEFAULT b'0' COMMENT '是否删除',
    index idx_unionId (union_id)
) comment '用户表' collate = utf8mb4_unicode_ci;


-- 博客表
create table if not exists blog
(
    id          bigint auto_increment comment 'id' primary key,
    user_id     bigint        not null comment '用户id',
    title       varchar(512)  null comment '标题',
    cover_img   varchar(1024) null comment '封面',
    content     text          not null comment '内容',
    thumb_count int                    default 0 not null comment '点赞数',
    create_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     bit(1)        NOT NULL DEFAULT b'0' COMMENT '是否删除',
    index idx_userId (user_id)
) comment '博客表' collate = utf8mb4_unicode_ci;


-- 点赞记录表
create table if not exists thumb
(
    id          bigint auto_increment comment 'id' primary key,
    user_id     bigint                             not null comment '用户id',
    blog_id     bigint                             not null comment '博客id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    unique index idx_userId_blogId (user_id, blog_id)
) comment '点赞记录表' collate = utf8mb4_unicode_ci;
