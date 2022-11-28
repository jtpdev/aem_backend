CREATE TABLE desafio_db.produto (
	id INT auto_increment NOT NULL,
	nome varchar(100) NOT NULL,
	categoria varchar(100) NULL,
	preco DOUBLE NULL,
	CONSTRAINT produto_PK PRIMARY KEY (id)
);

CREATE TABLE desafio_db.cliente (
	id INT auto_increment NOT NULL,
	nome varchar(100) NOT NULL,
	CONSTRAINT cliente_PK PRIMARY KEY (id)
);

CREATE TABLE desafio_db.nota_fiscal (
	id INT auto_increment NOT NULL,
	numero INT NOT NULL,
	valor DOUBLE NOT NULL,
	cliente_id INT NOT NULL,
	CONSTRAINT nota_fiscal_PK PRIMARY KEY (id),
	CONSTRAINT cliente_FK FOREIGN KEY (cliente_id) REFERENCES desafio_db.cliente(id)
);

CREATE TABLE desafio_db.nf_produto (
	nota_fiscal_id INT NOT NULL,
	produto_id INT NOT NULL,
	CONSTRAINT produto_FK FOREIGN KEY (produto_id) REFERENCES desafio_db.produto(id),
	CONSTRAINT nota_fiscal_FK FOREIGN KEY (nota_fiscal_id) REFERENCES desafio_db.nota_fiscal(id)
)
