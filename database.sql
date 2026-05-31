-- borramos y creamos de cero
DROP DATABASE IF EXISTS japan;
CREATE DATABASE japan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE japan;

CREATE TABLE roles (
    idrol INT AUTO_INCREMENT PRIMARY KEY,
    rol VARCHAR(50) NOT NULL
);

CREATE TABLE usuarios (
    idusuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE usuarios_roles (
    idusuario INT NOT NULL,
    idrol INT NOT NULL,
    PRIMARY KEY (idusuario, idrol),
    FOREIGN KEY (idusuario) REFERENCES usuarios(idusuario) ON DELETE CASCADE,
    FOREIGN KEY (idrol) REFERENCES roles(idrol) ON DELETE CASCADE
);

CREATE TABLE marcas (
    idmarca INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE articulos (
    idarticulo INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    texto TEXT NOT NULL,
    imagen VARCHAR(255) DEFAULT 'no-image.png',
    fecha DATE NOT NULL,
    idmarca INT,
    idusuario INT,
    FOREIGN KEY (idmarca) REFERENCES marcas(idmarca) ON DELETE SET NULL,
    FOREIGN KEY (idusuario) REFERENCES usuarios(idusuario) ON DELETE SET NULL
);

CREATE TABLE comentarios (
    idcomentario INT AUTO_INCREMENT PRIMARY KEY,
    texto TEXT NOT NULL,
    idarticulo INT NOT NULL,
    idusuario INT NOT NULL,
    FOREIGN KEY (idarticulo) REFERENCES articulos(idarticulo) ON DELETE CASCADE,
    FOREIGN KEY (idusuario) REFERENCES usuarios(idusuario) ON DELETE CASCADE
);

-- datos de prueba
INSERT INTO roles (idrol, rol) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (idrol, rol) VALUES (2, 'ROLE_USER');
INSERT INTO roles (idrol, rol) VALUES (3, 'ROLE_AUTOR');

INSERT INTO marcas (idmarca, nombre) VALUES (1, 'Honda');
INSERT INTO marcas (idmarca, nombre) VALUES (2, 'Yamaha');
INSERT INTO marcas (idmarca, nombre) VALUES (3, 'Suzuki');
INSERT INTO marcas (idmarca, nombre) VALUES (4, 'Kawasaki');

-- admin (password: japan80)
INSERT INTO usuarios (idusuario, username, password)
VALUES (1, 'admin', '$2a$10$Zr3Vxws/mRP9QXhs8GvDjuC9LzFevrO4GJkuchJxamW/glCi3JjPq');

-- usuario normal (password: user123)
INSERT INTO usuarios (idusuario, username, password)
VALUES (2, 'usuario', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVnT31B.2i');

-- autor (password: autor123)
INSERT INTO usuarios (idusuario, username, password)
VALUES (3, 'autor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVnT31B.2i');

-- asignacion de roles
INSERT INTO usuarios_roles (idusuario, idrol) VALUES (1, 1); -- admin
INSERT INTO usuarios_roles (idusuario, idrol) VALUES (2, 2); -- usuario
INSERT INTO usuarios_roles (idusuario, idrol) VALUES (3, 3); -- autor
