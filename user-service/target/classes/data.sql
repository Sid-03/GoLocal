-- Seed initial user data if the table is empty.
-- WARNING: Passwords MUST be pre-encoded using BCryptPasswordEncoder.
--          The plain text 'password123' WILL NOT WORK for login.
--          Generate the hash using SecurityConfig's passwordEncoder() or an online tool.
-- Example Hash for 'password123': $2a$10$3g.g.L6v8XpZ6/.v1g.8/.s/0y.cEa0kS8.c5o.cEa0kS8.c5o.cE
--          (REPLACE BELOW HASH WITH YOUR ACTUAL GENERATED HASH)

INSERT INTO users (username, email, password, roles) VALUES
('testuser', 'test@golocal.com', '$2a$12$rAE0V.avcNJ2mpa6b0ppsufGb7ahmFtnzg/RIJ.3PpNCBUzCjBjVK', 'ROLE_USER')
ON CONFLICT (username) DO NOTHING; -- Avoid errors if user already exists

INSERT INTO users (username, email, password, roles) VALUES
('adminuser', 'admin@golocal.com', '$2a$12$rAE0V.avcNJ2mpa6b0ppsufGb7ahmFtnzg/RIJ.3PpNCBUzCjBjVK', 'ROLE_ADMIN,ROLE_USER')
ON CONFLICT (username) DO NOTHING;


INSERT INTO users (username, email, password, roles) VALUES
('sid', 'admin@golocal.com', '$2a$12$rAE0V.avcNJ2mpa6b0ppsufGb7ahmFtnzg/RIJ.3PpNCBUzCjBjVK', 'ROLE_USER')
ON CONFLICT (username) DO NOTHING;
-- Add more users as needed