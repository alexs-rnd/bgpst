CREATE TABLE address
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    address VARCHAR(400),
    city VARCHAR(255),
    house_number VARCHAR(255),
    house_part VARCHAR(255),
    place_name VARCHAR(255),
    short_address VARCHAR(400),
    street VARCHAR(255)
);
CREATE INDEX idx_address ON address (address);
CREATE TABLE city_service
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    personal_data_consent BOOLEAN,
    service_name VARCHAR(255),
    uuid VARCHAR(255)
);
CREATE UNIQUE INDEX uk_city_service_uuid ON city_service (uuid);
CREATE TABLE cmc_task
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    created TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    description VARCHAR(8000),
    donedate TIMESTAMP,
    duedate TIMESTAMP,
    header VARCHAR(255),
    municipalityuid VARCHAR(255),
    last_updated TIMESTAMP NOT NULL,
    uuid VARCHAR(255)
);
CREATE UNIQUE INDEX uk_cmc_task_uuid ON cmc_task (uuid);
CREATE TABLE cmc_task_history
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    comments VARCHAR(500),
    reaction_date TIMESTAMP NOT NULL,
    initiatedby VARCHAR(255),
    state VARCHAR(255),
    uuid VARCHAR(255),
    cmctask_id BIGINT NOT NULL,
    CONSTRAINT fk_cmctaskhistory_cmctask FOREIGN KEY (cmctask_id) REFERENCES cmc_task (id)
);
CREATE UNIQUE INDEX uk_cmc_task_history_uuid ON cmc_task_history (uuid);
CREATE TABLE coordinate
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    coordinates VARCHAR(255),
    latitude NUMERIC(10,8),
    longitude NUMERIC(11,8)
);

CREATE TABLE employee
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    available BOOLEAN,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    last_updated TIMESTAMP,
    middlename VARCHAR(100),
    post VARCHAR(255),
    uuid VARCHAR(255),
    city_service_id BIGINT,
    CONSTRAINT fk_employee_city_service FOREIGN KEY (city_service_id) REFERENCES city_service (id)
);
CREATE UNIQUE INDEX uk_employee_uuid ON employee (uuid);
CREATE TABLE employee_email
(
    employee_id BIGINT NOT NULL,
    email VARCHAR(100),
    CONSTRAINT fkdauhx4kjy4jj5jx3g8wrawh84 FOREIGN KEY (employee_id) REFERENCES employee (id)
);
CREATE TABLE employee_phone
(
    employee_id BIGINT NOT NULL,
    phone VARCHAR(20),
    CONSTRAINT fkpst0if4uj0g3yrbpb2y17hu9b FOREIGN KEY (employee_id) REFERENCES employee (id)
);
CREATE TABLE equipment_type
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    name VARCHAR(255),
    uuid VARCHAR(255)
);
CREATE INDEX idx_equipment_type_name_uuid ON equipment_type (name, uuid);
CREATE TABLE equipment
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    available BOOLEAN,
    cargo_weight NUMERIC(19,2),
    last_updated TIMESTAMP,
    license VARCHAR(255),
    mark VARCHAR(255),
    max_distance NUMERIC(19,2),
    passengers INTEGER,
    status VARCHAR(255),
    uuid VARCHAR(255),
    city_service_id BIGINT,
    type_id BIGINT,
    CONSTRAINT fk_equipment_city_service FOREIGN KEY (city_service_id) REFERENCES city_service (id),
    CONSTRAINT fk_equipment_type FOREIGN KEY (type_id) REFERENCES equipment_type (id)
);
CREATE UNIQUE INDEX uk_equipment_uuid ON equipment (uuid);
CREATE TABLE equipment_properties
(
    equipment_id BIGINT NOT NULL,
    value VARCHAR(255),
    key VARCHAR(255) NOT NULL,
    CONSTRAINT equipment_properties_pkey PRIMARY KEY (equipment_id, key),
    CONSTRAINT fk1pbu44h96thcm3u20od4f8043 FOREIGN KEY (equipment_id) REFERENCES equipment (id)
);

CREATE TABLE evacuation_point
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    address VARCHAR(255),
    municipalityuid VARCHAR(255),
    name VARCHAR(255),
    uuid VARCHAR(255)
);
CREATE UNIQUE INDEX uk_evacuation_point_uuid ON evacuation_point (uuid);
CREATE TABLE fiasaddressobject
(
    aoid VARCHAR(36) PRIMARY KEY NOT NULL,
    aoguid VARCHAR(36) NOT NULL,
    aolevel INTEGER NOT NULL,
    areaname VARCHAR(120),
    autoname VARCHAR(120),
    cityname VARCHAR(120),
    ctarname VARCHAR(120),
    extrname VARCHAR(120),
    formalname VARCHAR(120) NOT NULL,
    fullname VARCHAR(255),
    offname VARCHAR(120),
    parentguid VARCHAR(36),
    placename VARCHAR(120),
    postalcode VARCHAR(6),
    priority INTEGER NOT NULL,
    regionname VARCHAR(120),
    sextname VARCHAR(120),
    shortname VARCHAR(10),
    streetname VARCHAR(120)
);
CREATE TABLE hibernate_sequences
(
    sequence_name VARCHAR(255) PRIMARY KEY NOT NULL,
    next_val BIGINT
);
CREATE TABLE location
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    address_id BIGINT,
    coordinate_id BIGINT,
    CONSTRAINT fk9mfl18vf4crjthypn1phosbov FOREIGN KEY (address_id) REFERENCES address (id),
    CONSTRAINT fk4lulm2sw3w7mls9jldl2spthu FOREIGN KEY (coordinate_id) REFERENCES coordinate (id)
);
CREATE TABLE municipality
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    name VARCHAR(255),
    portalmunicipality BOOLEAN NOT NULL,
    uuid VARCHAR(255)
);
CREATE UNIQUE INDEX uk_municipality_uuid ON municipality (uuid);
CREATE TABLE incident_type
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    label VARCHAR(255),
    uuid VARCHAR(255)
);
CREATE INDEX idx_inctype_name_uuid ON incident_type (label, uuid);
CREATE TABLE incident
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    additional VARCHAR(255),
    danger BOOLEAN NOT NULL,
    description VARCHAR(8000),
    finished TIMESTAMP,
    incident_status VARCHAR(255),
    planed_processing_time TIMESTAMP,
    registration_time TIMESTAMP,
    is_emergency BOOLEAN,
    uuid VARCHAR(255),
    incident_time TIMESTAMP,
    location_id BIGINT,
    municipality_id BIGINT NOT NULL,
    type_id BIGINT,
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES location (id),
    CONSTRAINT fk_incident_municipality FOREIGN KEY (municipality_id) REFERENCES municipality (id),
    CONSTRAINT fk_incident_type FOREIGN KEY (type_id) REFERENCES incident_type (id)
);
CREATE UNIQUE INDEX uk_incident_uuid ON incident (uuid);
CREATE TABLE incident_attachment
(
    incident_id BIGINT NOT NULL,
    attachment VARCHAR(255),
    CONSTRAINT fkg69208l9bksucatsssge0n4k FOREIGN KEY (incident_id) REFERENCES incident (id)
);
CREATE TABLE incident_evacuation_points
(
    incident_id BIGINT NOT NULL,
    evacuation_point_id BIGINT NOT NULL,
    CONSTRAINT incident_evacuation_points_pkey PRIMARY KEY (incident_id, evacuation_point_id),
    CONSTRAINT fk_incident_incidentevacuationpoints FOREIGN KEY (incident_id) REFERENCES incident (id),
    CONSTRAINT fk_incidentevacuationpoints_incident FOREIGN KEY (evacuation_point_id) REFERENCES evacuation_point (id)
);
CREATE TABLE incident_history
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    comments VARCHAR(500),
    reaction_date TIMESTAMP NOT NULL,
    state INTEGER,
    uuid VARCHAR(255),
    incident_id BIGINT NOT NULL,
    CONSTRAINT fk_incidenthistory_incident FOREIGN KEY (incident_id) REFERENCES incident (id)
);
CREATE UNIQUE INDEX uk_incident_history_uuid ON incident_history (uuid);
CREATE TABLE incoming_message_type
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    label VARCHAR(255),
    uuid VARCHAR(255)
);
CREATE INDEX idx_messagetype_name_uuid ON incoming_message_type (label, uuid);

CREATE TABLE incoming_message
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    consultation_text VARCHAR(4000),
    description VARCHAR(8000),
    eliminating_time TIMESTAMP,
    incident_time TIMESTAMP,
    reaction_completed_time TIMESTAMP,
    registration_number BIGINT,
    service_uid VARCHAR(255),
    report_time TIMESTAMP,
    user_uid VARCHAR(255),
    uuid VARCHAR(255),
    location_id BIGINT,
    municipality_id BIGINT NOT NULL,
    type_id BIGINT,
    CONSTRAINT fk_incomingmsg_location FOREIGN KEY (location_id) REFERENCES location (id),
    CONSTRAINT fk_incoming_message_municipality FOREIGN KEY (municipality_id) REFERENCES municipality (id),
    CONSTRAINT fk_incomingmessage_type FOREIGN KEY (type_id) REFERENCES incoming_message_type (id)
);
CREATE UNIQUE INDEX uk_incoming_message_uuid ON incoming_message (uuid);
CREATE TABLE incoming_message_history
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    comments VARCHAR(500),
    reaction_date TIMESTAMP NOT NULL,
    state VARCHAR(255),
    uuid VARCHAR(255),
    message_id BIGINT NOT NULL,
    CONSTRAINT fk_incomingmessagehistory_incomingmessage FOREIGN KEY (message_id) REFERENCES incoming_message (id)
);
CREATE UNIQUE INDEX uk_incoming_msg_history_uuid ON incoming_message_history (uuid);
CREATE TABLE incoming_message_task_history
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    comments VARCHAR(500),
    reaction_date TIMESTAMP NOT NULL,
    uuid VARCHAR(255),
    city_service_id BIGINT,
    CONSTRAINT fk_messagetaskhistory_city_service FOREIGN KEY (city_service_id) REFERENCES city_service (id)
);
CREATE UNIQUE INDEX uk_incoming_msg_task_history_uuid ON incoming_message_task_history (uuid);

CREATE TABLE incomingmessage_attachment
(
    message_id BIGINT NOT NULL,
    attachment VARCHAR(255),
    CONSTRAINT fkn2255xccuo7usr7plt24hq0rw FOREIGN KEY (message_id) REFERENCES incoming_message (id)
);
CREATE TABLE incomingmessagehistory_attachment
(
    incomingmessagehistory_id BIGINT NOT NULL,
    attachment VARCHAR(255),
    CONSTRAINT fkm263co8tc2lrjs5urvmnar67 FOREIGN KEY (incomingmessagehistory_id) REFERENCES incoming_message_history (id)
);

CREATE TABLE message_taskhistory
(
    message_id BIGINT NOT NULL,
    taskhistory_id BIGINT NOT NULL,
    CONSTRAINT message_taskhistory_pkey PRIMARY KEY (message_id, taskhistory_id),
    CONSTRAINT fk_taskhistory_message FOREIGN KEY (message_id) REFERENCES incoming_message (id),
    CONSTRAINT fk_message_taskhistory FOREIGN KEY (taskhistory_id) REFERENCES incoming_message_task_history (id)
);
CREATE TABLE messagetaskhistory_attachment
(
    messagetaskhistory_id BIGINT NOT NULL,
    attachment VARCHAR(255),
    CONSTRAINT fkge46msgb90aem0twfk875wn3f FOREIGN KEY (messagetaskhistory_id) REFERENCES incoming_message_task_history (id)
);

CREATE TABLE private_incident
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    additional VARCHAR(255),
    danger BOOLEAN NOT NULL,
    description VARCHAR(8000),
    external_sensor VARCHAR(4000),
    finished TIMESTAMP,
    forecast_update TIMESTAMP,
    last_updated TIMESTAMP NOT NULL,
    planed_processing_time TIMESTAMP,
    registration_time TIMESTAMP,
    is_emergency BOOLEAN,
    uuid VARCHAR(255),
    incident_time TIMESTAMP,
    location_id BIGINT,
    municipality_id BIGINT NOT NULL,
    type_id BIGINT,
    CONSTRAINT fk_private_incident_location FOREIGN KEY (location_id) REFERENCES location (id),
    CONSTRAINT fk_private_incident_municipality FOREIGN KEY (municipality_id) REFERENCES municipality (id),
    CONSTRAINT fk_private_incident_type FOREIGN KEY (type_id) REFERENCES incident_type (id)
);
CREATE UNIQUE INDEX uk_private_incident_uuid ON private_incident (uuid);
CREATE TABLE private_incident_attachment
(
    private_incident_id BIGINT NOT NULL,
    attachment VARCHAR(255),
    CONSTRAINT fk63pbfo4amoygikuh9xk3aoph FOREIGN KEY (private_incident_id) REFERENCES private_incident (id)
);
CREATE TABLE private_incident_evacuation_points
(
    private_incident_id BIGINT NOT NULL,
    private_evacuation_point_id BIGINT NOT NULL,
    CONSTRAINT private_incident_evacuation_points_pkey PRIMARY KEY (private_incident_id, private_evacuation_point_id),
    CONSTRAINT fk_privincident_privincidentevacuationpoints FOREIGN KEY (private_incident_id) REFERENCES private_incident (id),
    CONSTRAINT fk_privincidentevacuationpoints_privincident FOREIGN KEY (private_evacuation_point_id) REFERENCES evacuation_point (id)
);
CREATE TABLE private_incident_history
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    comments VARCHAR(500),
    reaction_date TIMESTAMP NOT NULL,
    state INTEGER,
    uuid VARCHAR(255),
    incident_id BIGINT NOT NULL,
    CONSTRAINT fk_private_incidenthistory_incident FOREIGN KEY (incident_id) REFERENCES private_incident (id)
);
CREATE UNIQUE INDEX uk_private_incident_history_uuid ON private_incident_history (uuid);
CREATE TABLE cmc_report
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    entity_imported TIMESTAMP NOT NULL,
    reactiondate TIMESTAMP,
    subject VARCHAR(400),
    text TEXT,
    last_updated TIMESTAMP NOT NULL,
    uuid VARCHAR(255),
    cmctask_id BIGINT,
    incident_id BIGINT,
    CONSTRAINT fk_cmcreport_cmctask FOREIGN KEY (cmctask_id) REFERENCES cmc_task (id),
    CONSTRAINT fk_cmcreport_incident FOREIGN KEY (incident_id) REFERENCES private_incident (id)
);
CREATE UNIQUE INDEX uk_cmc_report_uuid ON cmc_report (uuid);
CREATE TABLE cmcreport_attachment
(
    cmcreport_id BIGINT NOT NULL,
    attachment VARCHAR(255),
    CONSTRAINT fkhcaihw2qchhwg73y9uvt8ejle FOREIGN KEY (cmcreport_id) REFERENCES cmc_report (id)
);

CREATE TABLE crew
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    description VARCHAR(255),
    last_updated TIMESTAMP,
    uuid VARCHAR(255),
    city_service_id BIGINT,
    equipment_id BIGINT,
    incident_id BIGINT,
    manager_id BIGINT,
    CONSTRAINT fk_crew_city_service FOREIGN KEY (city_service_id) REFERENCES city_service (id),
    CONSTRAINT fk_crew_equipment FOREIGN KEY (equipment_id) REFERENCES equipment (id),
    CONSTRAINT fk_crew_incident FOREIGN KEY (incident_id) REFERENCES private_incident (id),
    CONSTRAINT fk_crew_manager FOREIGN KEY (manager_id) REFERENCES employee (id)
);
CREATE UNIQUE INDEX uk_crew_uuid ON crew (uuid);
CREATE TABLE service_municipality
(
    service_id BIGINT NOT NULL,
    municipality VARCHAR(4000),
    CONSTRAINT fk29wp6kkkwu0oaeqmg4jn57tus FOREIGN KEY (service_id) REFERENCES city_service (id)
);
CREATE TABLE task
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    entity_imported TIMESTAMP NOT NULL,
    creator_uuid VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    description VARCHAR(8000),
    due_time TIMESTAMP,
    finished_date TIMESTAMP,
    kind VARCHAR(255) NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    optional BOOLEAN,
    registered_date TIMESTAMP,
    time_to_react INTEGER,
    uuid VARCHAR(255),
    city_service_id BIGINT,
    incident_id BIGINT,
    CONSTRAINT fk_task_city_service FOREIGN KEY (city_service_id) REFERENCES city_service (id),
    CONSTRAINT fk_task_incident FOREIGN KEY (incident_id) REFERENCES private_incident (id)
);
CREATE UNIQUE INDEX uk_task_uuid ON task (uuid);
CREATE TABLE task_history
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    comments VARCHAR(500),
    reaction_date TIMESTAMP NOT NULL,
    state VARCHAR(255),
    uuid VARCHAR(255),
    task_id BIGINT NOT NULL,
    CONSTRAINT fk_taskhistory_task FOREIGN KEY (task_id) REFERENCES task (id)
);
CREATE UNIQUE INDEX uk_task_history_uuid ON task_history (uuid);
CREATE TABLE task_report
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    report VARCHAR(4000),
    task_id BIGINT NOT NULL,
    CONSTRAINT fk_taskreport_task FOREIGN KEY (task_id) REFERENCES task (id)
);
CREATE TABLE taskreport_attachment
(
    taskreport_id BIGINT NOT NULL,
    attachment VARCHAR(255),
    CONSTRAINT fkqwg7haxbblncxb26htkqldn2e FOREIGN KEY (taskreport_id) REFERENCES task_report (id)
);

CREATE TABLE cs_report_metadata
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    label VARCHAR(255),
    uuid VARCHAR(255)
);
CREATE TABLE cs_report
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    date TIMESTAMP,
    uuid VARCHAR(255),
    metadata_id BIGINT,
    CONSTRAINT fk8jrhhbs8q26psbf8xsf7wu7wr FOREIGN KEY (metadata_id) REFERENCES cs_report_metadata (id)
);
CREATE TABLE cs_report_value_metadata
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    datatype VARCHAR(255) NOT NULL,
    label VARCHAR(255),
    uuid VARCHAR(255)
);
CREATE TABLE cs_report_value
(
    id BIGINT PRIMARY KEY NOT NULL,
    is_deleted CHAR,
    locking_key BIGINT,
    value_string VARCHAR(255),
    valuemetadata_id BIGINT NOT NULL,
    CONSTRAINT fkbl9uv9vikhy8kvi2ccs9soofq FOREIGN KEY (valuemetadata_id) REFERENCES cs_report_value_metadata (id)
);
CREATE TABLE cs_report_cs_report_value
(
    cityservicereport_id BIGINT NOT NULL,
    values_id BIGINT NOT NULL,
    CONSTRAINT fko74m0a22leqai5g4lnx2hyy9y FOREIGN KEY (cityservicereport_id) REFERENCES cs_report (id),
    CONSTRAINT fk7t1k1dlf56uqid2ynvh9orx8l FOREIGN KEY (values_id) REFERENCES cs_report_value (id)
);
CREATE UNIQUE INDEX uk_phu4agyln07ddu1dxmyam3f32 ON cs_report_cs_report_value (values_id);

CREATE TABLE cs_report_metadata_cs_report_value_metadata
(
    cityservicereportmetadata_id BIGINT NOT NULL,
    valuemetadata_id BIGINT NOT NULL,
    CONSTRAINT fke7t6p2j14ffrmx00eb0xd6u23 FOREIGN KEY (cityservicereportmetadata_id) REFERENCES cs_report_metadata (id),
    CONSTRAINT fk1tm4kdq77erpxn0quf1vgfa5k FOREIGN KEY (valuemetadata_id) REFERENCES cs_report_value_metadata (id)
);
CREATE UNIQUE INDEX uk_i2q8bpgv5ahrh0pupit38v8pn ON cs_report_metadata_cs_report_value_metadata (valuemetadata_id);
