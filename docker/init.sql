DROP TABLE IF EXISTS sensors;
CREATE TABLE sensors(
                        id UUID PRIMARY KEY,
                        name VARCHAR(100) DEFAULT 'not specified',
                        ip_address VARCHAR(17) NOT NULL,
                        mac_address VARCHAR(17),
                        is_active BOOL DEFAULT false
);

DROP TABLE IF EXISTS readings;
CREATE TABLE readings(
                         id UUID PRIMARY KEY,
                         sensor_id UUID NOT NULL,
                         temperature DECIMAL(5,2),
                         humidity DECIMAL(5,2),
                         capture_time TIMESTAMP DEFAULT NOW(),
                         FOREIGN KEY (sensor_id) REFERENCES sensors(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS gg_users;
CREATE TABLE gg_users(
                         username VARCHAR(100) PRIMARY KEY,
                         password VARCHAR(255)
)
