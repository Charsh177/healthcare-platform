-- Create Schemas
CREATE SCHEMA IF NOT EXISTS auth_schema;
CREATE SCHEMA IF NOT EXISTS patient_schema;
CREATE SCHEMA IF NOT EXISTS billing_schema;
CREATE SCHEMA IF NOT EXISTS claim_schema;
CREATE SCHEMA IF NOT EXISTS file_schema;

-- Seed initial roles
INSERT INTO auth_schema.roles (role_id, role_name, description) VALUES
    ('a1b2c3d4-e5f6-4789-b0c1-d2e3f4a5b6c7', 'ADMIN', 'System administrator with full access'),
    ('b2c3d4e5-f6a7-4890-c1d2-e3f4a5b6c7d8', 'DOCTOR', 'Medical doctor'),
    ('c3d4e5f6-a7b8-4901-d2e3-f4a5b6c7d8e9', 'RECEPTIONIST', 'Hospital receptionist'),
    ('d4e5f6a7-b8c9-4012-e3f4-a5b6c7d8e9f0', 'INSURANCE_EXECUTIVE', 'Insurance claim processor'),
    ('e5f6a7b8-c9d0-4123-f4a5-b6c7d8e9f0a1', 'BILLING_STAFF', 'Billing department staff'),
    ('f6a7b8c9-d0e1-4234-a5b6-c7d8e9f0a1b2', 'SUPER_ADMIN', 'Super administrator');

-- Create initial admin user (password: admin@123)
-- Note: In production, use bcrypt hashed password
INSERT INTO auth_schema.users (user_id, username, password_hash, email, role_id, active, created_at) VALUES
    ('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', 'admin', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9EwkIK3m8p8sNRFX.z9ZRZEFSQGq', 'admin@healthcare.local', 'a1b2c3d4-e5f6-4789-b0c1-d2e3f4a5b6c7', true, NOW())
    ON CONFLICT DO NOTHING;
