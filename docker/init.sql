DROP TABLE IF EXISTS locations;
CREATE TABLE locations(
                          name VARCHAR(255) PRIMARY KEY
);

DROP TABLE IF EXISTS gg_users;
CREATE TABLE gg_users(
                         username VARCHAR(100) PRIMARY KEY,
                         password VARCHAR(255)
);

DROP TABLE IF EXISTS sensors;
CREATE TABLE sensors(
                        id UUID PRIMARY KEY,
                        username VARCHAR(100) NOT NULL,
                        name VARCHAR(100) DEFAULT 'not specified',
                        ip_address VARCHAR(17) NOT NULL,
                        mac_address VARCHAR(17),
                        location VARCHAR(100) DEFAULT 'not specified',
                        is_active BOOL DEFAULT false,

                        FOREIGN KEY (location) REFERENCES locations(name) ON DELETE SET DEFAULT,
                        FOREIGN KEY (username) REFERENCES gg_users(username) ON DELETE CASCADE
);

DROP TABLE IF EXISTS readings;
CREATE TABLE readings(
                         id UUID PRIMARY KEY,
                         sensor_id UUID NOT NULL,
                         temperature FLOAT,
                         humidity FLOAT,
                         capture_time TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

                         FOREIGN KEY (sensor_id) REFERENCES sensors(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS users_locations;
CREATE TABLE users_locations (
                                 username VARCHAR(255) NOT NULL,
                                 location_name VARCHAR(255) NOT NULL,

                                 PRIMARY KEY (username, location_name),
                                 FOREIGN KEY (username) REFERENCES gg_users(username),
                                 FOREIGN KEY (location_name) REFERENCES locations(name)
);
