-- Tables creation -------------------------------------------------------------

create database if not exists Tessiland;
use Tessiland;

create table if not exists TAG (
    name varchar(32) not null,
    constraint tag_pk primary key (name)
);

create table if not exists MATERIAL (
    code int not null auto_increment,
    description varchar(64) not null,
    constraint material_pk primary key (code)
);

create table if not exists PRODUCT (
    code int not null auto_increment,
    name varchar(64) not null,
    description varchar(5000),
    constraint product_pk primary key (code)
);

create table if not exists COMPOSITION (
    product_code int not null,
    material_code int not null,
    percent float(1) not null,
    constraint composition_pk primary key (product_code, material_code)
);

create table if not exists TAGGED (
    product_code int not null,
    tag_name varchar(32) not null,
    constraint tagged_pk primary key (tag_name, product_code)
);

-- Constraints -----------------------------------------------------------------

alter table COMPOSITION add constraint composition_references_material
foreign key (material_code)
references MATERIAL (code);

alter table COMPOSITION add constraint composition_references_product
foreign key (product_code)
references PRODUCT (code);

alter table TAGGED add constraint tagged_references_tag
foreign key (tag_name)
references TAG (name);

alter table TAGGED add constraint tagged_references_product
foreign key (product_code)
references PRODUCT (code);
