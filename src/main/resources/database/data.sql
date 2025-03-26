INSERT INTO sensors
VALUES (
        RANDOM_UUID(),
        'sensor 1',
        '192.168.1.2',
        '00:1A:2B:3C:4D:5E',
        true
       );

INSERT INTO sensors
VALUES (
        RANDOM_UUID(),
        'sensor 2',
        '192.168.1.3',
        'A1:B2:C3:D4:E5:F6',
        true
    );

INSERT INTO sensors(id, ip_address, mac_address)
VALUES (
       RANDOM_UUID(),
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
