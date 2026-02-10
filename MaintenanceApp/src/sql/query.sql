CREATE TABLE owners (
    owner_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    maintenance INTEGER DEFAULT 0 CHECK (maintenance >= 0)
);

CREATE TABLE sites (
    site_id SERIAL PRIMARY KEY,
    length INTEGER NOT NULL,
    breadth INTEGER NOT NULL,
    persqft INTEGER NOT NULL,
    site_type VARCHAR(20) NOT NULL 
        CHECK (site_type IN ('OPEN', 'VILLA', 'APARTMENT', 'HOUSE')),
    owner_id INTEGER REFERENCES owners(owner_id) ON DELETE SET NULL
);

CREATE TABLE requests (
    request_id SERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL 
        CHECK (type IN ('TYPE_CHANGE')),
    type_change VARCHAR(20) NOT NULL
        CHECK (type_change IN ('OPEN', 'VILLA', 'APARTMENT', 'HOUSE')),
    site_id INTEGER NOT NULL REFERENCES sites(site_id) ON DELETE CASCADE
);

