create table cidade (
id bigint not null auto_increment,
nome_cidade varchar(80) not null,
nome_estado varchar(80) NOT NULL,
primary key(id)
) engine=InnoDB charset=utf8mb4;