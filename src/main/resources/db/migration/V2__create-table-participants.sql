CREATE TABLE tb_participants (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    confirmed BOOLEAN NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES tb_trips(id) ON DELETE CASCADE
);