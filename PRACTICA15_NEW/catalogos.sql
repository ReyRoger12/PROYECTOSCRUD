CREATE DATABASE IF NOT EXISTS el_negrito;
USE el_negrito;

-- Tabla: Categorías
CREATE TABLE IF NOT EXISTS categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Descripcion TEXT,
    Fecha_creacion DATE,
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo'
);

-- Tabla: Artículos
CREATE TABLE IF NOT EXISTS articulos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Precio DECIMAL(10,2) NOT NULL,
    Stock INT DEFAULT 0,
    Codigo VARCHAR(50),
    Unidad VARCHAR(50),
    Disponible ENUM('Sí', 'No') DEFAULT 'Sí',
    CategoriaID INT,
    FOREIGN KEY (CategoriaID) REFERENCES categorias(id) ON DELETE SET NULL
);

-- Tabla: Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Telefono VARCHAR(20),
    Correo VARCHAR(100),
    Direccion TEXT,
    RFC VARCHAR(20),
    Fecha_registro DATE
);

-- Tabla: Empleados
CREATE TABLE IF NOT EXISTS empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Puesto VARCHAR(100),
    Salario DECIMAL(10,2),
    Direccion TEXT,
    Fecha_ingreso DATE,
    Activo ENUM('Sí', 'No') DEFAULT 'Sí'
);
