insert into vehicle (id, created_at, updated_at) values ('d03436a4-5d16-11ec-bf63-0242ac130002',  current_timestamp,  current_timestamp);
insert into vehicle (id, created_at, updated_at) values ('15fb2fa0-6fa9-4e06-ae59-ed6d691e0896',  current_timestamp,  current_timestamp);
insert into vehicle (id, created_at, updated_at) values ('850534f0-5d7d-11ec-bf63-0242ac130002',  current_timestamp,  current_timestamp);
insert into vehicle (id, created_at, updated_at) values ('90d94d02-5d7d-11ec-bf63-0242ac130002',  current_timestamp,  current_timestamp);
insert into vehicle (id, created_at, updated_at) values ('983b502c-5d7d-11ec-bf63-0242ac130002',  current_timestamp,  current_timestamp);
insert into vehicle (id, created_at, updated_at) values ('9e9b0caa-5d7d-11ec-bf63-0242ac130002',  current_timestamp,  current_timestamp);
insert into vehicle (id, created_at, updated_at) values ('a44dcc32-5d7d-11ec-bf63-0242ac130002',  current_timestamp,  current_timestamp);



insert into tracing (id, created_at, latitude, longitude, vehicle_id) values (10001, current_timestamp, 20.0, 20.0, 'd03436a4-5d16-11ec-bf63-0242ac130002');
insert into tracing (id, created_at, latitude, longitude, vehicle_id) values (10002, current_timestamp, 20.0, 20.0, 'a44dcc32-5d7d-11ec-bf63-0242ac130002');
insert into tracing (id, created_at, latitude, longitude, vehicle_id) values (10003, current_timestamp, 20.0, 20.0, '983b502c-5d7d-11ec-bf63-0242ac130002');
insert into tracing (id, created_at, latitude, longitude, vehicle_id) values (10004, current_timestamp, 20.0, 20.0, '850534f0-5d7d-11ec-bf63-0242ac130002');
insert into tracing (id, created_at, latitude, longitude, vehicle_id) values (10005, current_timestamp, 20.0, 20.0, '15fb2fa0-6fa9-4e06-ae59-ed6d691e0896');

insert into last_tracing(id, trace_id, vehicle_id) values (10001, 10001, 'd03436a4-5d16-11ec-bf63-0242ac130002');