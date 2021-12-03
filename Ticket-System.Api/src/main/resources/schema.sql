create table category
(
    id          int auto_increment
        primary key,
    name        varchar(255)                                 null,
    description varchar(500)                                 null,
    image       varchar(255)                                 null,
    type        enum ('combos', 'foods', 'others', 'drinks') null,
    thumbnail   varchar(255)                                 null
);

create table concession
(
    id          int auto_increment
        primary key,
    name        varchar(255) null,
    price       double       null,
    category_id int          null,
    image       varchar(255) null,
    thumbnail   varchar(255) null,
    constraint concession_category_id_fk
        foreign key (category_id) references category (id)
);

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

create table offer
(
    name               varchar(254) charset utf8                not null,
    type               enum ('Flat', 'Percentage')              null,
    method             enum ('Voucher', 'Coupon')               null,
    start_date         datetime                                 null,
    end_date           datetime                                 null,
    creation_date      datetime                                 null,
    creationBy         int                                      null,
    updatedBy          int                                      null,
    updated_date       datetime                                 null,
    message            varchar(500) charset utf8                null,
    max_total_usage    int                                      null,
    discount_amount    double                                   null,
    max_usage_per_user int                                      null,
    rule               longtext                                 null,
    max_discount       double                                   null,
    id                 int auto_increment
        primary key,
    anon_profile       bit                                      null,
    allow_multiple     bit                                      null,
    percentage         double                                   null,
    status             enum ('new', 'active', 'expire', 'used') null,
    constraint offer_name_uindex
        unique (name)
);

create table offer_detail
(
    offer_id int          not null,
    code     varchar(254) not null,
    primary key (offer_id, code)
);

create table offer_movie
(
    offer_id int not null,
    movie_id int not null,
    primary key (offer_id, movie_id)
);

create index offer_movie_movie_id_fk
    on offer_movie (movie_id);

create table payment_method
(
    id   int auto_increment
        primary key,
    code varchar(255) null,
    name varchar(255) null
);

create table payment
(
    note              varchar(254)                             null,
    use_for           enum ('Ticket', 'Gift', 'MemberCash')    null,
    part_id           int                                      null,
    creation          int                                      null,
    id                int auto_increment
        primary key,
    payment_method_id int                                      not null,
    amount            float                                    null,
    status            enum ('Complete', 'Pending', 'Verified') null,
    transaction_id    varchar(255)                             null,
    created_date      datetime                                 null,
    updatedAt         datetime                                 null,
    updatedBy         int                                      null,
    user_id           int                                      null,
    constraint payment_ibfk_1
        foreign key (payment_method_id) references payment_method (id)
);

create index FK_payment_paymentmethod
    on payment (payment_method_id);

create index payment_created_date_index
    on payment (created_date);

create index payment_creation_index
    on payment (creation);

create index payment_status_index
    on payment (status);

create index payment_use_for_index
    on payment (use_for);

create index payment_user_info_id_fk
    on payment (user_id);

create table privilege
(
    id   int auto_increment
        primary key,
    name varchar(255) null
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
    enabled           bit                                                      null,
    begin_usable      date                                                     null,
    priority          int                                                      null,
    anon_profile      bit                                                      null,
    allow_multiple    bit                                                      null,
    time_until_expire int                                                      null,
    end_usable        date                                                     null,
    global            bit                                                      null
);

create table discount_promo
(
    promotion_id           int          not null
        primary key,
    adjuster               double       null,
    one_use                decimal(1)   null,
    calculator_description varchar(254) null,
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

create table reserved
(
    seat_id              int      null,
    user_id              int      null,
    show_time_id         int      null,
    room_id              int      null,
    time_expire_reserved datetime null
);

create table role
(
    id   int auto_increment
        primary key,
    code varchar(254) null,
    name varchar(254) null
);

create table roles_privileges
(
    role_id      int not null,
    privilege_id int not null,
    primary key (role_id, privilege_id)
);

create table service
(
    id          int auto_increment
        primary key,
    name        varchar(254) null,
    description longtext     null,
    thumbnail   text         null
);

create table theater
(
    latitude    varchar(255)     null,
    longitude   varchar(255)     null,
    address     varchar(255)     null,
    thumbnail   varchar(255)     null,
    image       varchar(255)     null,
    id          int auto_increment
        primary key,
    code        varchar(255)     not null,
    name        varchar(255)     not null,
    location_id int              not null,
    active      bit default b'0' null,
    constraint theater_ibfk_1
        foreign key (location_id) references location (id)
);

create table room
(
    id         int auto_increment
        primary key,
    code       varchar(255)     null,
    name       varchar(255)     null,
    theater_id int              not null,
    active     bit default b'0' null,
    constraint room_ibfk_1
        foreign key (theater_id) references theater (id)
);

create table room_service
(
    room_id    int not null,
    service_id int not null,
    primary key (room_id, service_id),
    constraint room_service_room_id_fk
        foreign key (room_id) references room (id),
    constraint room_service_service_id_fk
        foreign key (service_id) references service (id)
);

create table seat
(
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
    movie_id   int              not null,
    room_id    int              not null,
    id         int auto_increment
        primary key,
    time_end   time             null,
    time_start timestamp        null,
    price      double default 0 null,
    constraint showtimes_detail_movie_id_room_id_date_start_time_start_uindex
        unique (movie_id, room_id, time_start),
    constraint showtimes_detail_movie_id_fk
        foreign key (movie_id) references movie (id),
    constraint showtimes_detail_room_id_fk
        foreign key (room_id) references room (id)
);

create table orders
(
    showtimes_detail_id int                                                             not null,
    tax                 double default 10                                               null,
    note                varchar(255)                                                    null,
    updatedBy           int                                                             null,
    updatedAt           datetime                                                        null,
    expire_payment      datetime                                                        null,
    created_date        datetime                                                        null,
    status              enum ('ordered', 'cancelled', 'non_payment', 'payment', 'edit') null,
    id                  int auto_increment
        primary key,
    profile             bit                                                             null,
    user_id             int                                                             null,
    creation            int                                                             null,
    total               double                                                          null,
    constraint ticket_showtimes_detail_id_fk
        foreign key (showtimes_detail_id) references showtimes_detail (id)
);

create table offer_history
(
    order_id       int          null,
    user_id        int          null,
    id             int auto_increment
        primary key,
    offer_id       int          null,
    time_used      datetime     null,
    status         varchar(100) null,
    total_discount double       null,
    code           varchar(254) null,
    constraint offer_history_offer_id_fk
        foreign key (offer_id) references offer (id),
    constraint offer_history_orders_id_fk
        foreign key (order_id) references orders (id)
);

create index offer_history_user_info_id_fk
    on offer_history (user_id);

create index user_id
    on orders (user_id);

create table orders_detail
(
    concession_id int not null,
    orders_id     int not null,
    quantity      int null,
    primary key (concession_id, orders_id),
    constraint orders_detail_concession_id_fk
        foreign key (concession_id) references concession (id),
    constraint orders_detail_orders_id_fk
        foreign key (orders_id) references orders (id)
);

create table orders_seat
(
    orders_id int                                                          not null,
    seat_id   int                                                          not null,
    type      enum ('Adult', 'Child', 'Student', 'Senior') default 'Adult' not null,
    primary key (orders_id, seat_id),
    constraint FKgi0g5nsmkdaoyc25unogm3rry
        foreign key (orders_id) references orders (id),
    constraint orders_seat_ibfk_1
        foreign key (seat_id) references seat (id)
);

create index seat_id
    on orders_seat (seat_id);

create table promotion_showtime
(
    promotion_id int null,
    show_time_id int null,
    constraint promotion_showtime_promotion_id_fk
        foreign key (promotion_id) references promotion (id),
    constraint promotion_showtime_showtimes_detail_id_fk
        foreign key (show_time_id) references showtimes_detail (id)
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

create table time
(
    time varchar(254) not null
        primary key
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
    last_login      datetime                  null,
    id              int auto_increment
        primary key,
    first_name      varchar(255)              null,
    last_name       varchar(255)              null,
    full_name       varchar(500)              null,
    email           varchar(255)              null,
    time_zone       varchar(100)              null,
    is_login_social bit                       null,
    photo           varchar(255) default ''   null,
    current_logged  bit          default b'0' null
);

create table employee
(
    user_id    int                                                         null,
    status     enum ('New', 'Approved', 'Active', 'Blocked', 'Terminated') null,
    createdAt  datetime                                                    null,
    updatedAt  datetime                                                    null,
    id         int auto_increment
        primary key,
    startsAt   datetime                                                    null,
    endsAt     datetime                                                    null,
    notes      text                                                        null,
    createdBy  int                                                         null,
    updatedBy  int                                                         null,
    theater_id int                                                         null,
    constraint fk_employee_creator
        foreign key (createdBy) references user_info (id),
    constraint fk_employee_modifier
        foreign key (updatedBy) references user_info (id),
    constraint fk_employee_theater
        foreign key (theater_id) references theater (id),
    constraint fk_employee_user
        foreign key (user_id) references user_info (id)
);

create index idx_employee_creator
    on employee (createdBy);

create index idx_employee_modifier
    on employee (updatedBy);

create index idx_employee_theater
    on employee (theater_id);

create index idx_employee_user
    on employee (user_id);

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

create table google_account
(
    user_info_id int         not null
        primary key,
    google_id    varchar(50) not null,
    constraint google_account_google_id_uindex
        unique (google_id),
    constraint google_account_user_info_id_fk
        foreign key (user_info_id) references user_info (id)
);

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

create table theater_manager
(
    theater_id  int not null,
    employee_id int not null,
    constraint theater_manager_ibfk_1
        foreign key (theater_id) references theater (id),
    constraint theater_manager_ibfk_2
        foreign key (employee_id) references employee (id)
);

create index employee_id
    on theater_manager (employee_id);

create index theater_id
    on theater_manager (theater_id);

create table user_account
(
    user_account_status_id   int default 1 null,
    email                    varchar(255)  null,
    password                 varchar(255)  null,
    password_reminder_token  varchar(255)  null,
    user_info_id             int           not null
        primary key,
    activeDate               datetime      null,
    registeredAt             datetime      null,
    email_confirmation_token varchar(255)  null,
    password_reminder_expire datetime(6)   null,
    createdBy                int           null,
    createdDate              datetime      null,
    modifiedBy               int           null,
    modifiedDate             datetime      null,
    address                  text          null,
    state                    text          null,
    city                     text          null,
    constraint FK_user_account_user_account_status
        foreign key (user_account_status_id) references user_account_status (id),
    constraint FK_user_account_user_info
        foreign key (user_info_id) references user_info (id)
);

create index user_info_email_index
    on user_info (email);

create table user_role
(
    user_id int not null,
    role_id int not null,
    primary key (user_id, role_id),
    constraint user_role_ibfk_1
        foreign key (user_id) references user_info (id),
    constraint user_role_ibfk_2
        foreign key (role_id) references role (id)
);

create index group_id
    on user_role (role_id);

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

