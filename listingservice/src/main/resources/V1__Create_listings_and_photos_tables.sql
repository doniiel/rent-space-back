CREATE TABLE IF NOT EXISTS listings (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        title VARCHAR(100) NOT NULL,
                                        description VARCHAR(1000) NOT NULL,
                                        price_per_night DOUBLE NOT NULL,
                                        location VARCHAR(255) NOT NULL,
                                        user_id BIGINT NOT NULL,
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        created_by VARCHAR(255) NOT NULL,
                                        updated_at TIMESTAMP,
                                        updated_by VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS photos (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      listing_id BIGINT NOT NULL,
                                      url VARCHAR(255) NOT NULL,
                                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      created_by VARCHAR(255) NOT NULL,
                                      updated_at TIMESTAMP,
                                      updated_by VARCHAR(255),
                                      FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);
