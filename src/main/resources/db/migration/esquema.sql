CREATE TABLE categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    slug VARCHAR(40) NOT NULL
);

CREATE TABLE producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    marca VARCHAR(60) NOT NULL,
    modelo VARCHAR(80),
    descripcion VARCHAR(300),
    precio INT NOT NULL,
    stock INT NOT NULL,
    imagen VARCHAR(500),
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    run VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'CLIENTE'
);

CREATE TABLE boleta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(20) NOT NULL UNIQUE,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total INT NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_boleta_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE TABLE detalle_boleta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario INT NOT NULL,
    boleta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    CONSTRAINT fk_detalle_boleta FOREIGN KEY (boleta_id) REFERENCES boleta (id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto (id)
);
