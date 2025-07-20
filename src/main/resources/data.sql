-- Default roles
insert into roles (name) values
                             ('ROLE_USER'),
                                ('ROLE_TECHNICIAN'),
                                ('ROLE_ADMIN');

select * from roles;

update roles set name = 'ROLE_USER' where name = 'USER';
update roles set name = 'ROLE_TECHNICIAN' where name = 'TECHNICIAN';
update roles set name = 'ROLE_ADMIN' where name = 'ADMIN';

select * from users;

select * from users_roles;


select * from password_reset_codes;
