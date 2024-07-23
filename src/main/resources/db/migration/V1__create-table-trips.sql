CREATE TABLE tb_trips (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    destiny VARCHAR(255) NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    confirmed BOOLEAN NOT NULL,
    owner_name VARCHAR(255) NOT NULL,
    owner_email VARCHAR(255) NOT NULL
);