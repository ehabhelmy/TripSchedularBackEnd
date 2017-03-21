CREATE DATABASE TripSchedular;
USE TripSchedular;

CREATE TABLE IF NOT EXISTS users(
user_email varchar(200) primary key,
user_password text
);

CREATE TABLE IF NOT EXISTS trips(
trip_id int ,
trip_name text,
duration long,
trip_date long,
trip_status text,
avg_speed text,
trip_source text,
trip_destination text,
user_email varchar(200),
foreign KEY (user_email) REFERENCES users (user_email),
primary key(trip_id,user_email));
        
CREATE TABLE IF NOT EXISTS notes(
note_id int,
content text,
trip_id int,
user_email varchar(200),
foreign KEY (trip_id,user_email) REFERENCES trips (trip_id,user_email),
primary key(note_id,trip_id,user_email)
);


