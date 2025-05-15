CREATE TABLE currency (
    id SERIAL PRIMARY KEY,
    code VARCHAR(3) UNIQUE NOT NULL
);

CREATE TABLE exchange_rate (
    id BIGSERIAL PRIMARY KEY,
    currency_code VARCHAR(3) NOT NULL,
    rate NUMERIC NOT NULL
) ;

-- Indexes
CREATE INDEX idx_currency_code ON exchange_rate(currency_code);
