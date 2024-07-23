CREATE TABLE tb_activities (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    occurs_at TIMESTAMP NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES tb_trips(id) ON DELETE CASCADE
);