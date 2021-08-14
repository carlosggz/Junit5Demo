CREATE TABLE joke
(
    id            BIGINT       NOT NULL,
    category      VARCHAR(255) NOT NULL,
    question      VARCHAR(255) NOT NULL,
    answer        VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_joke PRIMARY KEY (id)
);

CREATE INDEX "DateOrder" ON joke (creation_date);