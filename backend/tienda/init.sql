INSERT INTO productos (nombre, precio, precio_final, descripcion, material, consideraciones, imagen_url) VALUES
('Camiseta Básica', 19.99, 19.99, 'Camiseta de algodón 100% suave y cómoda', 'Algodón', 'Lavar a 30 grados', 'https://example.com/images/camiseta_basica.jpg'),
('Pantalón Vaquero', 39.99, 39.99, 'Pantalón vaquero de corte clásico', 'Denim', 'Lavar a 40 grados', 'https://example.com/images/pantalon_vaquero.jpg'),
('Sudadera', 29.99, 29.99, 'Sudadera de algodón 100% suave y cómoda', 'Algodón', 'Lavar a 30 grados', 'https://example.com/images/sudadera.jpg');

INSERT INTO promociones (descripcion, descuento) VALUES
('Obtén un 20% de descuento en toda la tienda durante el verano', 20.00),
('Disfruta de un 15% de descuento en productos seleccionados', 15.00);

INSERT INTO carritos (total) VALUES (0.00);
INSERT INTO carritos (total) VALUES (0.00);

INSERT INTO usuarios (nombre, apellidos, username, email, password, carrito_id) VALUES
('Juan', 'García López', 'juanillo', 'juan@gmail.com', '1234', 1),
('Jose', 'Redondo Martínez', 'churumbel', 'churumbel@gmail.com', '1234', 2);