CREATE TABLE operating_system (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    os_family TEXT,
    version TEXT,
    eol_date DATE,
    release_type TEXT
);

CREATE TABLE software (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    version TEXT,
    available_version TEXT,
    url TEXT,
    origin TEXT,
    installation_type TEXT,
    installed_at TIMESTAMP,
    last_updated TIMESTAMP
);

CREATE TABLE software_stack (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE appliance (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    os_id BIGINT,
    stack_id BIGINT,
    CONSTRAINT fk_appliance_os FOREIGN KEY (os_id) REFERENCES operating_system(id),
    CONSTRAINT fk_appliance_stack FOREIGN KEY (stack_id) REFERENCES software_stack(id)
);

CREATE TABLE pc (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    mac_address TEXT,
    base_price DECIMAL(18, 4),
    bought_at TIMESTAMP
);

CREATE TABLE component_type (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

INSERT INTO component_type (name) VALUES 
    ('CPU'), ('Mainboard'), ('RAM'), ('PSU'), ('SSD'), ('HDD'), ('Case'), ('other');

CREATE TABLE component (
    id BIGSERIAL PRIMARY KEY,
    component_type_id BIGINT,
    model TEXT,
    manufacturer TEXT,
    vendor TEXT,
    price DECIMAL(18, 4),
    bought_at TIMESTAMP,
    pc_id BIGINT,
    CONSTRAINT fk_component_type FOREIGN KEY (component_type_id) REFERENCES component_type(id),
    CONSTRAINT fk_component_pc FOREIGN KEY (pc_id) REFERENCES pc(id)
);

CREATE TABLE stack_software (
    stack_id BIGINT NOT NULL,
    software_id BIGINT NOT NULL,
    PRIMARY KEY (stack_id, software_id),
    CONSTRAINT fk_stack_software_stack FOREIGN KEY (stack_id) REFERENCES software_stack(id),
    CONSTRAINT fk_stack_software_software FOREIGN KEY (software_id) REFERENCES software(id)
);

CREATE TABLE pc_appliance (
    pc_id BIGINT NOT NULL,
    appliance_id BIGINT NOT NULL,
    PRIMARY KEY (pc_id, appliance_id),
    CONSTRAINT fk_pc_appliance_pc FOREIGN KEY (pc_id) REFERENCES pc(id),
    CONSTRAINT fk_pc_appliance_app FOREIGN KEY (appliance_id) REFERENCES appliance(id)
);

CREATE TABLE database_product (
    id BIGSERIAL PRIMARY KEY,
    vendor_name TEXT,
    name TEXT,
    latest_version TEXT,
    released_at DATE
);

CREATE TABLE database_instance (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    database_product_id BIGINT,
    stack_id BIGINT,
    installed_version TEXT,
    port INTEGER,
    installed_at TIMESTAMP,
    last_updated TIMESTAMP,
    CONSTRAINT fk_database_instance_product FOREIGN KEY (database_product_id) REFERENCES database_product(id),
    CONSTRAINT fk_database_instance_stack FOREIGN KEY (stack_id) REFERENCES software_stack(id)
);

CREATE TABLE subdatabase (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    database_instance_id BIGINT,
    created_at TIMESTAMP,
    CONSTRAINT fk_subdatabase_instance FOREIGN KEY (database_instance_id) REFERENCES database_instance(id)
);

CREATE TABLE service (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    stack_id BIGINT,
    status TEXT,
    port INTEGER,
    installed_at TIMESTAMP,
    last_updated TIMESTAMP,
    CONSTRAINT fk_service_stack FOREIGN KEY (stack_id) REFERENCES software_stack(id)
);

CREATE TABLE service_software (
    service_id BIGINT NOT NULL,
    software_id BIGINT NOT NULL,
    PRIMARY KEY (service_id, software_id),
    CONSTRAINT fk_service_software_service FOREIGN KEY (service_id) REFERENCES service(id),
    CONSTRAINT fk_service_software_software FOREIGN KEY (software_id) REFERENCES software(id)
);

CREATE TABLE service_subdatabase (
    service_id BIGINT NOT NULL,
    subdatabase_id BIGINT NOT NULL,
    PRIMARY KEY (service_id, subdatabase_id),
    CONSTRAINT fk_service_subdatabase_service FOREIGN KEY (service_id) REFERENCES service(id),
    CONSTRAINT fk_service_subdatabase_subdb FOREIGN KEY (subdatabase_id) REFERENCES subdatabase(id)
);
