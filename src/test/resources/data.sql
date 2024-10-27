INSERT INTO clearing_cost (id, card_issuing_country, clearing_cost_value, created_date, modified_date, created_by, modified_by)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'US', 20.0, '2024-01-01T00:00:00', '2024-02-01T00:00:00', '123e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174000'),
    ('223e4567-e89b-12d3-a456-426614174001', 'GR', 15.0, '2024-01-02T00:00:00', NULL, '223e4567-e89b-12d3-a456-426614174001', NULL),
    ('323e4567-e89b-12d3-a456-426614174002', 'FR', 10.0, '2024-01-03T00:00:00', '2024-02-03T00:00:00', '323e4567-e89b-12d3-a456-426614174002', '323e4567-e89b-12d3-a456-426614174002'),
    ('8ca06904-c20a-4412-baac-79e68b4a66d3', 'OT', 5.0, '2024-01-03T00:00:00', NULL, '8ca06904-c20a-4412-baac-79e68b4a66d3', NULL);

INSERT INTO users (id, username, password, role) VALUES
    ('c4bb9cf4-96ab-4c78-98f1-1d4fdbc5e917', 'demo', '$2y$10$eUEhpT2955IHC.a9xeXdcu0S22LBbGbNrM5j2eGzWh431kvSl4WTq', 'ROLE_ADMIN'),
    ('436bc93b-56ed-4b5c-b89d-cbc36b35b8c1', 'demo2', '$2y$10$eUEhpT2955IHC.a9xeXdcu0S22LBbGbNrM5j2eGzWh431kvSl4WTq', 'ROLE_USER');

INSERT INTO revinfo (REV, REVTSTMP)
VALUES
    (100, 1729707416889);
