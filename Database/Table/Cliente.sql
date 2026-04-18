CREATE TABLE public.cliente (
	cd_cliente int8 NOT NULL,
	cpf varchar(255)  NULL,
	nome varchar(255) NULL,
	dt_nasc timestamp(6) NULL,
	email varchar(255) NULL,
	endereco varchar(255) NULL,
	telefone varchar(255) NULL,
	
	CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente)
);