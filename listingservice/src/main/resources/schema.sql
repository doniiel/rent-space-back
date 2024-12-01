CREATE TABLE listings (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          title VARCHAR(255) NOT NULL,
                          description TEXT,
                          price_per_night DOUBLE,
                          location VARCHAR(255),
                          user_id BIGINT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE photos (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        listing_id BIGINT,
                        url VARCHAR(512),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);
