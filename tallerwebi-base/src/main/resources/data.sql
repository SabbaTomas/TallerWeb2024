-- Crear usuario de ejemplo si no existe
INSERT INTO Usuario(id, email, password, rol, activo)
SELECT null, 'test@unlam.edu.ar', 'test', 'ADMIN', true
    WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'test@unlam.edu.ar');

-- San Justo
INSERT INTO Locker (descripcion, latitud, longitud, seleccionado, tipo, codigo_postal)
VALUES ('Locker San Justo 1', -34.6853, -58.5588, true, 'PEQUEÑO', '1754')
    ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion),
    tipo = VALUES(tipo),
                         codigo_postal = VALUES(codigo_postal);

INSERT INTO Locker (descripcion, latitud, longitud, seleccionado, tipo, codigo_postal)
VALUES ('Locker San Justo 2', -34.6860, -58.5631, true, 'MEDIANO', '1754')
    ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion),
    tipo = VALUES(tipo),
                         codigo_postal = VALUES(codigo_postal);

-- Ramos Mejía
INSERT INTO Locker (descripcion, latitud, longitud, seleccionado, tipo, codigo_postal)
VALUES ('Locker Ramos Mejía 1', -34.6485, -58.5617, true, 'GRANDE', '1704')
    ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion),
    tipo = VALUES(tipo),
                         codigo_postal = VALUES(codigo_postal);

INSERT INTO Locker (descripcion, latitud, longitud, seleccionado, tipo, codigo_postal)
VALUES ('Locker Ramos Mejía 2', -34.6440, -58.5683, true, 'PEQUEÑO', '1704')
    ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion),
    tipo = VALUES(tipo),
                         codigo_postal = VALUES(codigo_postal);
