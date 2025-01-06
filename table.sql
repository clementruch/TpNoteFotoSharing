create table user
(
    id         bigint auto_increment
        primary key,
    username   varchar(255)                                                               not null,
    email      varchar(255)                                                               not null,
    password   varchar(255)                                                               not null,
    role       enum ('USER', 'ADMIN', 'MODERATOR', 'VISITOR') default 'USER'              null,
    created_at timestamp                                      default current_timestamp() null,
    updated_at timestamp                                      default current_timestamp() null on update current_timestamp(),
    constraint email
        unique (email),
    constraint username
        unique (username)
);

create table album
(
    id          bigint auto_increment
        primary key,
    name        varchar(255)                                           not null,
    description text                                                   null,
    visibility  enum ('PRIVATE', 'PUBLIC') default 'PRIVATE'           null,
    owner_id    bigint                                                 not null,
    created_at  timestamp                  default current_timestamp() null,
    updated_at  timestamp                  default current_timestamp() null on update current_timestamp(),
    constraint album_ibfk_1
        foreign key (owner_id) references user (id)
            on delete cascade
);

create index idx_album_owner
    on album (owner_id);

create table contact
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)                              not null,
    status     enum ('ACCEPTED', 'DECLINED', 'PENDING') null,
    contact_id bigint                                   not null,
    user_id    bigint                                   not null,
    constraint FKe07k4jcfdophemi6j1lt84b61
        foreign key (user_id) references user (id),
    constraint FKqgj7ka9h4nt6snuo4tpj7bh52
        foreign key (contact_id) references user (id)
);

create table photo
(
    id          bigint auto_increment
        primary key,
    title       varchar(255)                                           not null,
    description varchar(500)                                           null,
    url         varchar(255)                                           not null,
    visibility  enum ('PRIVATE', 'PUBLIC') default 'PRIVATE'           null,
    owner_id    bigint                                                 not null,
    created_at  timestamp                  default current_timestamp() null,
    updated_at  timestamp                  default current_timestamp() null on update current_timestamp(),
    constraint photo_ibfk_1
        foreign key (owner_id) references user (id)
            on delete cascade
);

create table album_photo
(
    album_id bigint not null,
    photo_id bigint not null,
    primary key (album_id, photo_id),
    constraint album_photo_ibfk_1
        foreign key (album_id) references album (id)
            on delete cascade,
    constraint album_photo_ibfk_2
        foreign key (photo_id) references photo (id)
            on delete cascade
);

create index photo_id
    on album_photo (photo_id);

create table commentaire
(
    id         bigint auto_increment
        primary key,
    text       text                                  not null,
    photo_id   bigint                                not null,
    author_id  bigint                                not null,
    created_at timestamp default current_timestamp() null,
    updated_at timestamp default current_timestamp() null on update current_timestamp(),
    constraint commentaire_ibfk_1
        foreign key (photo_id) references photo (id)
            on delete cascade,
    constraint commentaire_ibfk_2
        foreign key (author_id) references user (id)
            on delete cascade
);

create index author_id
    on commentaire (author_id);

create index photo_id
    on commentaire (photo_id);

create table partage
(
    id               bigint auto_increment
        primary key,
    photo_id         bigint                                                     not null,
    user_id          bigint                                                     not null,
    permission_level enum ('VIEW', 'EDIT', 'ADMIN') default 'VIEW'              null,
    created_at       timestamp                      default current_timestamp() null,
    constraint partage_ibfk_1
        foreign key (photo_id) references photo (id)
            on delete cascade,
    constraint partage_ibfk_2
        foreign key (user_id) references user (id)
            on delete cascade
);

create index idx_partage_user
    on partage (user_id);

create index photo_id
    on partage (photo_id);

create index idx_photo_owner
    on photo (owner_id);

create table users
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)                                    not null,
    email      varchar(255)                                   not null,
    password   varchar(255)                                   not null,
    role       enum ('ADMIN', 'MODERATOR', 'USER', 'VISITOR') not null,
    username   varchar(255)                                   not null,
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UKr43af9ap4edm43mmtq01oddj6
        unique (username)
);

create table utilisateur
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)                                    not null,
    email      varchar(255)                                   not null,
    password   varchar(255)                                   not null,
    role       enum ('ADMIN', 'MODERATOR', 'USER', 'VISITOR') not null,
    username   varchar(255)                                   not null,
    constraint UKkq7nt5wyq9v9lpcpgxag2f24a
        unique (username),
    constraint UKrma38wvnqfaf66vvmi57c71lo
        unique (email)
);

