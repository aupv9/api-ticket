create table category
(
    id          int auto_increment
        primary key,
    name        varchar(255) null,
    description varchar(500) null,
    create_date timestamp    null,
    start_date  timestamp    null,
    end_date    timestamp    null,
    type        varchar(100) null
);

create table concessions
(
    id          int auto_increment
        primary key,
    name        varchar(255) null,
    create_date timestamp    null,
    start_date  timestamp    null,
    end_date    timestamp    null,
    price       float        null,
    category_id int          not null,
    constraint foods_ibfk_1
        foreign key (category_id) references category (id)
);

create index category_id
    on concessions (category_id);

create table location
(
    id        int auto_increment
        primary key,
    name      varchar(255) not null,
    zipcode   varchar(50)  null,
    latitude  varchar(255) null,
    longitude varchar(255) null
);

create table media
(
    id            int auto_increment
        primary key,
    creation_date date         null,
    start_date    date         null,
    end_date      date         null,
    description   varchar(254) null,
    name          varchar(254) null,
    media_type    int          null,
    path          varchar(254) null
);

create table movie
(
    id        int auto_increment
        primary key,
    name      varchar(255) null,
    thumbnail varchar(255) null,
    image     varchar(255) null,
    active    int          null
);

create table movie_media
(
    movie_id int not null
        primary key,
    media_id int null,
    constraint movie_media_ibfk_1
        foreign key (media_id) references media (id),
    constraint movie_media_ibfk_2
        foreign key (movie_id) references movie (id)
);

create index media_id
    on movie_media (media_id);

create table payment_method
(
    id   int auto_increment
        primary key,
    code varchar(255) null,
    name varchar(255) null
);

create table payment
(
    id                int auto_increment
        primary key,
    payment_method_id int                                      not null,
    amount            float                                    null,
    status            enum ('Complete', 'Pending', 'Verified') null,
    transaction_id    varchar(255)                             null,
    createtion_date   date                                     null,
    start_date        date                                     null,
    end_date          date                                     null,
    note              varchar(254)                             null,
    use_for           enum ('Ticket', 'Gift')                  null,
    part_id           int                                      null,
    constraint payment_ibfk_1
        foreign key (payment_method_id) references payment_method (id)
);

create index FK_payment_paymentmethod
    on payment (payment_method_id);

create table promotion
(
    id                int auto_increment
        primary key,
    creation_date     date                                                     null,
    start_date        date                                                     null,
    end_date          date                                                     null,
    display_name      varchar(254)                                             null,
    description       varchar(254)                                             null,
    promotion_type    enum ('Normal', 'Event', 'Anniversary') default 'Normal' null,
    enabled           decimal(1)                                               null,
    begin_usable      date                                                     null,
    priority          int                                                      null,
    anon_profile      decimal(1)                                               null,
    allow_multiple    decimal(1)                                               null,
    uses              int                                                      null,
    time_until_expire int                                                      null
);

create table discount_promo
(
    promotion_id int        not null
        primary key,
    percent      double     null,
    one_use      decimal(1) null,
    constraint discount_promo_ibfk_1
        foreign key (promotion_id) references promotion (id)
);

create table promo_code
(
    promotion_id int          not null,
    code         varchar(254) not null,
    primary key (promotion_id, code),
    constraint promo_code_ibfk_1
        foreign key (promotion_id) references promotion (id)
);

create table promo_media
(
    promotion_id int not null
        primary key,
    media_id     int not null,
    constraint promo_media_ibfk_1
        foreign key (promotion_id) references promotion (id),
    constraint promo_media_ibfk_2
        foreign key (media_id) references media (id)
);

create index media_id
    on promo_media (media_id);

create table role
(
    id   int auto_increment
        primary key,
    code varchar(254) null,
    name varchar(254) null
);

create table showtimes
(
    creation_date timestamp null,
    end_date      datetime  null,
    start_date    timestamp null,
    id            int auto_increment
        primary key
);

create table theater
(
    id          int auto_increment
        primary key,
    code        varchar(255) not null,
    name        varchar(255) not null,
    location_id int          not null,
    latitude    varchar(255) null,
    longitude   varchar(255) null,
    thumbnail   varchar(255) null,
    image       varchar(255) null,
    address     varchar(255) null,
    constraint theater_ibfk_1
        foreign key (location_id) references location (id)
);

create table room
(
    id         int auto_increment
        primary key,
    code       varchar(255) null,
    name       varchar(255) null,
    theater_id int          not null,
    type       varchar(255) null,
    constraint room_ibfk_1
        foreign key (theater_id) references theater (id)
);

create table seat
(
    price     float        null,
    id        int auto_increment
        primary key,
    seat_type varchar(100) null,
    room_id   int          not null,
    tier      varchar(25)  null,
    numbers   int          null,
    constraint seat_room_id_tier_numbers_uindex
        unique (room_id, tier, numbers),
    constraint seat_room_id_fk
        foreign key (room_id) references room (id)
);

create index seat_ibfk_1
    on seat (seat_type);

create table showtimes_detail
(
    movie_id   int  not null,
    room_id    int  not null,
    id         int auto_increment
        primary key,
    time_end   time null,
    time_start time null,
    date       date null,
    constraint showtimes_detail_movie_id_room_id_date_start_time_start_uindex
        unique (movie_id, room_id, date, time_start),
    constraint showtimes_detail_movie_id_fk
        foreign key (movie_id) references movie (id),
    constraint showtimes_detail_room_id_fk
        foreign key (room_id) references room (id)
);

create table theater_media
(
    theater_id int not null
        primary key,
    media_id   int null,
    constraint theater_media_ibfk_1
        foreign key (media_id) references media (id)
);

create index media_id
    on theater_media (media_id);

create table user_account_status
(
    id   int auto_increment
        primary key,
    code varchar(100) null,
    name varchar(200) null
);

create table user_info
(
    id              int auto_increment
        primary key,
    first_name      varchar(255)                                                                       null,
    last_name       varchar(255)                                                                       null,
    full_name       varchar(500)                                                                       null,
    email           varchar(255)                                                                       null,
    time_zone       varchar(100)                                                                       null,
    is_login_social tinyint(1)                                                                         null,
    role            enum ('ROLE_USER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN') default 'ROLE_USER' not null
);

create table gift_card
(
    gift_id         int auto_increment
        primary key,
    number          varchar(255)     not null,
    pin             varchar(255)     not null,
    user_id         int              not null,
    createtion_date date             null,
    start_date      date             null,
    end_date        date             null,
    balance         double default 0 null,
    constraint gift_card_ibfk_1
        foreign key (user_id) references user_info (id)
);

create index user_id
    on gift_card (user_id);

create table membership
(
    member_id       int auto_increment
        primary key,
    number          varchar(254)                        not null,
    pin             varchar(254)                        not null,
    user_id         int                                 not null,
    createtion_date date                                null,
    start_date      date                                null,
    end_date        date                                null,
    level           enum ('Member', 'Vip', 'Super Vip') null,
    point           double                              null,
    constraint membership_ibfk_1
        foreign key (user_id) references user_info (id)
);

create index user_id
    on membership (user_id);

create table orders
(
    id                  int auto_increment
        primary key,
    user_id             int                                          not null,
    ticket_date         date                                         null,
    showtimes_detail_id int                                          not null,
    total_amount        float                                        not null,
    tax                 float                                        not null,
    create_date         datetime                                     null,
    note                varchar(255)                                 null,
    creation            int                                          null,
    type_user           enum ('user', 'non_user') default 'non_user' null,
    constraint orders_ibfk_1
        foreign key (user_id) references user_info (id),
    constraint ticket_showtimes_detail_id_fk
        foreign key (showtimes_detail_id) references showtimes_detail (id)
);

create index user_id
    on orders (user_id);

create table orders_detail
(
    id        int auto_increment
        primary key,
    food_id   int   not null,
    orders_id int   not null,
    amount    float null,
    constraint orders_detail_ibfk_1
        foreign key (food_id) references concessions (id),
    constraint orders_detail_ibfk_2
        foreign key (orders_id) references orders (id)
);

create index food_id
    on orders_detail (food_id);

create index orders_id
    on orders_detail (orders_id);

create table orders_seat
(
    orders_id int not null,
    seat_id   int not null,
    primary key (orders_id, seat_id),
    constraint FKgi0g5nsmkdaoyc25unogm3rry
        foreign key (orders_id) references orders (id),
    constraint orders_seat_ibfk_1
        foreign key (seat_id) references seat (id)
);

create index seat_id
    on orders_seat (seat_id);

create table user_account
(
    user_account_status_id   int          null,
    user_info_id             int          not null,
    email_confirmation_token varchar(255) null,
    password_reminder_expire datetime(6)  null,
    userInfoId               int          not null
        primary key,
    userName                 varchar(255) null,
    email                    varchar(255) null,
    password                 varchar(255) null,
    password_reminder_token  varchar(255) null,
    user_name                varchar(255) null,
    active                   bit          null,
    constraint FK_user_account_user_account_status
        foreign key (user_account_status_id) references user_account_status (id),
    constraint FK_user_account_user_info
        foreign key (userInfoId) references user_info (id)
);

create table user_role
(
    user_id  int not null,
    group_id int not null,
    primary key (user_id, group_id),
    constraint user_role_ibfk_1
        foreign key (user_id) references user_info (id),
    constraint user_role_ibfk_2
        foreign key (group_id) references role (id)
);

create index group_id
    on user_role (group_id);

create
definer = root@localhost procedure clockSeatRoom(IN idSeat int, IN statusParam varchar(255), OUT idSeatRoom int)
BEGIN
START TRANSACTION;
SELECT * FROM booksystem.seat_room WHERE id = idSeat and status = 'Available' FOR UPDATE OF seat_room NOWAIT;
UPDATE booksystem.seat_room set status = statusParam where id = idSeat;
set idSeatRoom = idSeat;
commit;
END;

create
definer = root@localhost procedure findAllShowTimes()
BEGIN
SELECT * FROM booksystem.showtimes;

END;

create
definer = root@localhost procedure unClockSeatRoom(IN idRoom int)
BEGIN
SELECT status FROM booksystem.seat_room WHERE id = idRoom for update nowait;
update seat_room set status = 'Available' where id = idRoom;
commit;
END;

