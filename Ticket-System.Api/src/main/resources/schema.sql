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

create table foods
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
    on foods (category_id);

create table location
(
    latitude  varchar(255) null,
    longitude varchar(255) null,
    id        int auto_increment
        primary key,
    name      varchar(255) not null,
    zipcode   varchar(50)  null
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
    id int auto_increment
        primary key
);

create table orders
(
    id          int auto_increment
        primary key,
    create_date date         null,
    note        varchar(255) null
);

create table orders_detail
(
    id        int auto_increment
        primary key,
    food_id   int   not null,
    orders_id int   not null,
    amount    float null,
    constraint orders_detail_ibfk_1
        foreign key (food_id) references foods (id),
    constraint orders_detail_ibfk_2
        foreign key (orders_id) references orders (id)
);

create index food_id
    on orders_detail (food_id);

create index orders_id
    on orders_detail (orders_id);

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

create table principle_groups
(
    id   int auto_increment
        primary key,
    code varchar(254) null,
    name varchar(254) null
);

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

create table seat_type
(
    id   int auto_increment
        primary key,
    name varchar(255) null
);

create table showtimes
(
    creation_date timestamp null,
    end_date      timestamp null,
    start_date    timestamp null,
    id            int auto_increment
        primary key,
    dayshowtimes  date      null
);

create table theater
(
    latitude    varchar(255) null,
    longitude   varchar(255) null,
    id          int auto_increment
        primary key,
    code        varchar(255) not null,
    name        varchar(255) not null,
    location_id int          not null,
    constraint theater_ibfk_1
        foreign key (location_id) references location (id)
);

create table room
(
    id              int auto_increment
        primary key,
    code            varchar(255) null,
    name            varchar(255) null,
    theater_id      int          not null,
    horizontal_size int          null,
    vertical_size   int          null,
    constraint room_ibfk_1
        foreign key (theater_id) references theater (id)
);

create table showtimes_detail
(
    time_end     varchar(255) null,
    showtimes_id int          not null,
    movie_id     int          not null,
    location_id  int          not null,
    theater_id   int          not null,
    room_id      int          not null,
    time_start   varchar(100) null,
    id           int auto_increment
        primary key,
    constraint showtimes_id
        unique (showtimes_id, room_id, theater_id, location_id, movie_id, time_start),
    constraint showtimes_detail_ibfk_1
        foreign key (showtimes_id) references showtimes (id),
    constraint showtimes_detail_ibfk_2
        foreign key (movie_id) references movie (id),
    constraint showtimes_detail_ibfk_3
        foreign key (room_id) references room (id),
    constraint showtimes_detail_ibfk_4
        foreign key (theater_id) references theater (id),
    constraint showtimes_detail_ibfk_5
        foreign key (location_id) references location (id)
);

create table tier
(
    count_seat int          null,
    id         int auto_increment
        primary key,
    code       varchar(255) null,
    name       varchar(255) null,
    room_id    int          not null,
    constraint name
        unique (name, room_id),
    constraint tier_ibfk_1
        foreign key (room_id) references room (id)
);

create table seat
(
    price        float        null,
    id           int auto_increment
        primary key,
    seat_type_id int          null,
    tier_id      int          not null,
    name         varchar(100) null,
    constraint seat_ibfk_1
        foreign key (seat_type_id) references seat_type (id),
    constraint seat_ibfk_2
        foreign key (tier_id) references tier (id)
);

create index seat
    on seat (tier_id);

create table seat_room
(
    user                int                                                        null,
    seat_id             int                                                        null,
    showtimes_detail_id int                                                        not null,
    status              enum ('Available', 'Pending', 'Solid') default 'Available' null,
    id                  int auto_increment
        primary key,
    constraint showtimes_id
        unique (seat_id, showtimes_detail_id),
    constraint seat_room_ibfk_4
        foreign key (seat_id) references seat (id),
    constraint seat_room_showtimes_detail_id_fk
        foreign key (showtimes_detail_id) references showtimes_detail (id)
);

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
    first_name      varchar(255) null,
    last_name       varchar(255) null,
    full_name       varchar(500) null,
    email           varchar(255) null,
    time_zone       varchar(100) null,
    is_login_social tinyint(1)   null
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

create table ticket
(
    id                  int auto_increment
        primary key,
    user_id             int   not null,
    ticket_date         date  null,
    showtimes_detail_id int   not null,
    total_amount        float not null,
    tax                 float not null,
    orders_id           int   null,
    constraint ticket_ibfk_1
        foreign key (user_id) references user_info (id),
    constraint ticket_ibfk_3
        foreign key (orders_id) references orders (id),
    constraint ticket_showtimes_detail_id_fk
        foreign key (showtimes_detail_id) references showtimes_detail (id)
);

create index orders_id
    on ticket (orders_id);

create index user_id
    on ticket (user_id);

create table ticket_seat
(
    ticket_id    int not null,
    seat_room_id int not null,
    primary key (ticket_id, seat_room_id),
    constraint FKgi0g5nsmkdaoyc25unogm3rry
        foreign key (ticket_id) references ticket (id),
    constraint ticket_seat_ibfk_2
        foreign key (seat_room_id) references seat_room (id)
);

create index seat_room_id
    on ticket_seat (seat_room_id);

create table user_account
(
    user_info_id             int          not null,
    email_confirmation_token varchar(255) null,
    password_reminder_expire datetime(6)  null,
    password_reminder_token  varchar(255) null,
    user_name                varchar(255) null,
    userInfoId               int          not null
        primary key,
    userName                 varchar(255) null,
    email                    varchar(255) null,
    password                 varchar(255) null,
    passwordReminderToken    varchar(255) null,
    passwordReminderExpire   timestamp    null,
    emailConfirmationToken   varchar(255) null,
    user_account_status_id   int          null,
    constraint FK_user_account_user_account_status
        foreign key (user_account_status_id) references user_account_status (id),
    constraint FK_user_account_user_info
        foreign key (userInfoId) references user_info (id)
);

create table user_groups
(
    user_id  int not null,
    group_id int not null,
    primary key (user_id, group_id),
    constraint user_groups_ibfk_1
        foreign key (user_id) references user_info (id),
    constraint user_groups_ibfk_2
        foreign key (group_id) references principle_groups (id)
);

create index group_id
    on user_groups (group_id);

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
SELECT status FROM booksystem.seat_room WHERE id = idRoom;
commit;
END;

