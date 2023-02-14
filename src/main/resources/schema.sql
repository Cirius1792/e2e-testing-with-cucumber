CREATE TABLE USER_PROFILE (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) not null,
    name VARCHAR(255),
    surname VARCHAR(255),
    description VARCHAR(255)
    );
