INSERT INTO gg_users
VALUES (
           'admin',
           '$2a$10$agqKO9An2MIiTejIK3cpO.tYp3lAw30s7uRiDGBSGmpISmeHo865G'
       );

INSERT INTO sensors(id, username, name, ip_address, mac_address,  is_active )
INSERT INTO locations
VALUES ('Wroclaw');

INSERT INTO locations
VALUES ('Warszawa');

INSERT INTO locations
VALUES ('Poznan');

INSERT INTO locations
VALUES ('Rzeszow');

INSERT INTO locations
VALUES ('Gdansk');

INSERT INTO locations
VALUES ('not specified');

VALUES (
        RANDOM_UUID(),
        'admin',
        'sensor 1',
        '192.168.1.2',
        '00:1A:2B:3C:4D:5E',
        true
       );

INSERT INTO sensors(id, username, name, ip_address, mac_address, is_active )
VALUES (
        RANDOM_UUID(),
        'admin',
        'sensor 2',
        '192.168.1.3',
        'A1:B2:C3:D4:E5:F6',
        true
    );

INSERT INTO sensors(id, username, name, ip_address, mac_address,  is_active )
VALUES (
           RANDOM_UUID(),
           'admin',
           'sensor 5',
           '192.168.1.5',
           'A1:B2:C3:D4:E5:10',
           false
       );

INSERT INTO sensors(id, username, ip_address, mac_address)
VALUES (
       RANDOM_UUID(),
       'admin',
       '192.168.1.4',
       '12:34:56:78:9A:BC'
       );

INSERT INTO readings
VALUES (
        RANDOM_UUID(),
        SELECT id FROM sensors WHERE sensors.name='sensor 1',
        10.23,
        40.10,
        CURRENT_TIMESTAMP()
       );

INSERT INTO readings
VALUES (
           RANDOM_UUID(),
           SELECT id FROM sensors WHERE sensors.name='sensor 1',
           10.25,
           44.10,
           CURRENT_TIMESTAMP()
       );

INSERT INTO readings
VALUES (
           RANDOM_UUID(),
           SELECT id FROM sensors WHERE sensors.name='sensor 1',
           12.25,
           47.10,
           CURRENT_TIMESTAMP()
       );

INSERT INTO readings
VALUES (
           RANDOM_UUID(),
           SELECT id FROM sensors WHERE sensors.name='sensor 2',
           25.23,
           20.10,
           CURRENT_TIMESTAMP()
       );

INSERT INTO readings
VALUES (
           RANDOM_UUID(),
           SELECT id FROM sensors WHERE sensors.name='sensor 2',
           22.10,
           34.10,
           CURRENT_TIMESTAMP()
       );

INSERT INTO readings
VALUES (
           RANDOM_UUID(),
           SELECT id FROM sensors WHERE sensors.name='sensor 2',
           20.25,
           37.10,
           CURRENT_TIMESTAMP()
       );

