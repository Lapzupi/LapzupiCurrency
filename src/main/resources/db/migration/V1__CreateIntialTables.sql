CREATE TABLE IF NOT EXISTS currency_users(
    `uuid` VARCHAR(36) NOT NULL,
    username VARCHAR(255) NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0,
    PRIMARY KEY (`uuid`)
);

CREATE TABLE IF NOT EXISTS currency_transactions(
    friendly_id VARCHAR(22) NOT NULL,
    user_uuid VARCHAR(36) NOT NULL,
    `timestamp` TIMESTAMP NOT NULL,
    amount DOUBLE NOT NULL,
    plugin VARCHAR(255) NOT NULL,
    `type` ENUM ('TAKE', 'GIVE', 'SET'),
    reason TEXT NOT NULL,
    PRIMARY KEY (friendly_id)
);
