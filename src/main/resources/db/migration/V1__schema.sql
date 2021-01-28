create table roles (id binary(16) not null, created_at datetime, updated_at datetime, name varchar(60), primary key (id)) engine=InnoDB;
create table user_roles (user_id binary(16) not null, role_id binary(16) not null, primary key (user_id, role_id)) engine=InnoDB;
create table users (id binary(16) not null, created_at datetime, updated_at datetime, email varchar(255), full_name varchar(255), password varchar(255), user_name varchar(255), primary key (id)) engine=InnoDB;
alter table roles add constraint UK_nb4h0p6txrmfc0xbrd1kglp9t unique (name);
alter table users add constraint UKr43af9ap4edm43mmtq01oddj6 unique (user_name);
alter table users add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table user_roles add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles (id);
alter table user_roles add constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users (id);
