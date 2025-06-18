CREATE TABLE MASTER_ROLE (
    role_id SERIAL PRIMARY KEY,
    role_code VARCHAR(50) UNIQUE NOT NULL,
    role_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(10) CHECK (status IN ('Active', 'Inactive')) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE MASTER_USER (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    unit_id INT,
    role_id INT,
    status VARCHAR(10) CHECK (status IN ('Active', 'Inactive')) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (role_id) REFERENCES MASTER_ROLE(role_id)
);

CREATE TABLE MASTER_MENU (
    menu_id SERIAL PRIMARY KEY,
    parent_id INT,
    menu_code VARCHAR(50) UNIQUE NOT NULL,
    menu_name VARCHAR(100) NOT NULL,
    menu_icon VARCHAR(100),
    menu_url VARCHAR(255),
    menu_order INT DEFAULT 0,
    is_active VARCHAR(10) CHECK (is_active IN ('Active', 'Inactive')) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (parent_id) REFERENCES MASTER_MENU(menu_id)
);

CREATE TABLE ROLE_MENU (
    role_menu_id SERIAL PRIMARY KEY,
    role_id INT,
    menu_id INT,
    is_active VARCHAR(10) CHECK (is_active IN ('Active', 'Inactive')) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES MASTER_ROLE(role_id),
    FOREIGN KEY (menu_id) REFERENCES MASTER_MENU(menu_id)
);

CREATE TABLE USER_SESSIONS (
    session_id SERIAL PRIMARY KEY,
    user_id INT,
    session_token VARCHAR(255) UNIQUE NOT NULL,
    login_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    logout_at TIMESTAMP,
    ip_address VARCHAR(50),
    user_agent VARCHAR(255),
    status VARCHAR(10) CHECK (status IN ('Active', 'Logout')) DEFAULT 'Active',
    FOREIGN KEY (user_id) REFERENCES MASTER_USER(user_id)
);

-- ENUM untuk status
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_enum') THEN
        CREATE TYPE status_enum AS ENUM ('Active', 'Inactive');
    END IF;
END $$;

-- TABEL MASTER_UNIT
CREATE TABLE MASTER_UNIT (
    unit_id SERIAL PRIMARY KEY,
    unit_code VARCHAR(50) UNIQUE NOT NULL,
    unit_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    parent_unit_id INT,
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    manager_id INT,
    status status_enum DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,

    FOREIGN KEY (parent_unit_id) REFERENCES MASTER_UNIT(unit_id),
    FOREIGN KEY (manager_id) REFERENCES MASTER_USER(user_id),
    FOREIGN KEY (created_by) REFERENCES MASTER_USER(user_id),
    FOREIGN KEY (updated_by) REFERENCES MASTER_USER(user_id)
);
