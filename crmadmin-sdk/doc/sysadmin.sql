/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/7/17 ����һ 15:48:03                       */
/*==============================================================*/


drop table if exists data_auth;

drop table if exists menu;

drop table if exists resource;

drop table if exists role;

drop table if exists role_menu;

drop table if exists role_permission;

drop table if exists user;

drop table if exists user_data_auth;

drop table if exists user_role;

/*==============================================================*/
/* Table: data_auth                                             */
/*==============================================================*/
create table data_auth
(
   id                   bigint(20) not null,
   type                 varchar(20) not null comment '����Ȩ�޷���##����ɾ����ҵ��ϵͳȷ��',
   data_id              varchar(50) not null comment '����Ȩ��ID##ҵ����',
   name                 varchar(50) not null comment '����',
   parent_data_id       bigint(20) not null default 0 comment '��������ID##�����������������Ȩ��',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=34768 DEFAULT CHARSET=utf8;

alter table data_auth comment '����Ȩ��';

/*==============================================================*/
/* Table: menu                                                  */
/*==============================================================*/
create table menu
(
   id                   bigint(20) not null auto_increment comment '����',
   parent_id            bigint(20) not null comment '�����˵�id',
   sort                 integer(11) not null comment '�����',
   is_root              integer(11) not null comment '�Ƿ�Ϊ���ڵ�',
   menu_url             varchar(255) comment '�˵�url',
   name                 varchar(255) not null comment '����',
   description          varchar(255) not null comment '����',
   target               integer(4) comment '�����ӵ�λ��##{data:[{value:0, text:"��ǰ����"},{value:1, text:"�¿�����"}]}',
   created              timestamp not null default CURRENT_TIMESTAMP comment '����ʱ��',
   modified             timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '�޸�ʱ��',
   yn                   integer(4) not null comment '��Ч��##������Ч�ԣ������߼�ɾ��##{data:[{value:0, text:"��Ч"},{value:1, text:"��Ч"}]}',
   type                 integer(4) not null default 0 comment '����##{data:[{value:0, text:"Ŀ¼"},{value:1, text:"����"}]}',
   icon_cls             varchar(40) comment '�˵�ͼ��',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=345 DEFAULT CHARSET=utf8;

alter table menu comment '�˵�';

/*==============================================================*/
/* Table: resource                                              */
/*==============================================================*/
create table resource
(
   id                   bigint(20) not null auto_increment comment '����',
   resource_name        varchar(255) not null comment '����',
   description          varchar(255) comment '����',
   menu_id              bigint(20) not null comment '���������menu��',
   code                 varchar(255) comment '����##ԭresource URL',
   status               integer(4) not null comment '״̬##{data:[{value:1, text:"����"},{value:0, text:"ͣ��"}]}',
   created              timestamp not null default CURRENT_TIMESTAMP comment '����ʱ��',
   modified             timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '�޸�ʱ��',
   yn                   integer(4) not null comment '��Ч��##������Ч�ԣ������߼�ɾ��##{data:[{value:0, text:"��Ч"},{value:1, text:"��Ч"}]}',
   primary key (id),
   key FK_RESOURCE_NAVBAR (menu_id)
)
ENGINE=InnoDB AUTO_INCREMENT=11623 DEFAULT CHARSET=utf8;

alter table resource comment '��Դ';

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   bigint(20) not null auto_increment comment '����',
   role_name            varchar(255) not null comment '��ɫ��',
   description          varchar(255) not null comment '��ɫ����',
   created              timestamp not null default CURRENT_TIMESTAMP comment '����ʱ��',
   modified             timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '�޸�ʱ��',
   yn                   integer(4) not null comment '��Ч��##������Ч�ԣ������߼�ɾ��##{data:[{value:0, text:"��Ч"},{value:1, text:"��Ч"}]}',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;

alter table role comment '��ɫ';

/*==============================================================*/
/* Table: role_menu                                             */
/*==============================================================*/
create table role_menu
(
   id                   bigint(20) not null comment '����',
   role_id              bigint(20) not null comment '��ɫID##���',
   menu_id              bigint(20) not null comment '�˵�ID##���',
   primary key (id),
   key FK_ROLEMENU_ROLE (role_id),
   key FK_MENUROLE_MENU (menu_id)
)
ENGINE=InnoDB AUTO_INCREMENT=39236 DEFAULT CHARSET=utf8;

alter table role_menu comment '��ɫ�˵���ϵ';

/*==============================================================*/
/* Table: role_permission                                       */
/*==============================================================*/
create table role_permission
(
   id                   bigint(20) not null comment '����',
   role_id              bigint(20) not null comment '��ɫID##���',
   resource_id          bigint(20) not null comment '��ԴID##���',
   primary key (id),
   key FK_ROLEPERMISSION_ROLE (role_id),
   key FK_ROLEPERMISSION_RESOURCE (resource_id)
)
ENGINE=InnoDB AUTO_INCREMENT=1136 DEFAULT CHARSET=utf8;

alter table role_permission comment '��ɫ��Դ��ϵ';

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   bigint(20) not null auto_increment comment '����',
   user_name            varchar(50) not null comment '�û���',
   password             varchar(128) not null comment '����',
   last_login_ip        varchar(20) comment '����¼ip',
   last_login_time      timestamp comment '����¼ʱ��',
   created              timestamp not null default CURRENT_TIMESTAMP comment '����ʱ��',
   modified             timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '�޸�ʱ��',
   status               integer(4) not null default 1 comment '״̬##״̬##{data:[{value:1,text:"����"},{value:0,text:"ͣ��"}]}',
   yn                   integer(4) not null comment '��Ч��##������Ч�ԣ������߼�ɾ��##{data:[{value:0, text:"��Ч"},{value:1, text:"��Ч"}]}',
   real_name            varchar(64) not null default 'system_default' comment '��ʵ����',
   serial_number        varchar(128) not null default '000' comment '�û����',
   fixed_line_telephone varchar(24) not null comment '�̶��绰',
   cellphone            varchar(24) not null comment '�ֻ�����',
   email                varchar(64) not null comment '����',
   valid_time_begin     timestamp default CURRENT_TIMESTAMP comment '��Чʱ�俪ʼ��',
   valid_time_end       timestamp comment '��Чʱ�������',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=299 DEFAULT CHARSET=utf8;

alter table user comment '�û�';

/*==============================================================*/
/* Table: user_data_auth                                        */
/*==============================================================*/
create table user_data_auth
(
   id                   bigint(20) not null,
   data_auth_id         bigint(20) not null comment '����Ȩ�ޱ�id',
   user_id              bigint(20) not null comment '�û�id',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=34768 DEFAULT CHARSET=utf8;

alter table user_data_auth comment '����Ȩ��';

/*==============================================================*/
/* Table: user_role                                             */
/*==============================================================*/
create table user_role
(
   id                   bigint(20) not null comment '����',
   user_id              bigint(20) not null comment '�û�id##���',
   role_id              bigint(20) not null comment '��ɫid##���',
   primary key (id),
   key FK_USERROLE_USER (user_id),
   key FK_USERROLE_ROLE (role_id)
)
ENGINE=InnoDB AUTO_INCREMENT=3277 DEFAULT CHARSET=utf8;

alter table user_role comment '�û���ɫ��ϵ';

