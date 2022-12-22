
create table estado (
id bigint not null auto_increment,
nome varchar(80) NOT NULL,

primary key (id)
) engine=InnoDB charset=utf8mb4;

insert into estado (nome) select distinct nome_estado from cidade;
 
alter table cidade add column estado_id bigint not null;

UPDATE cidade c set estado_id = (select e.id from estado e where c.nome_estado = e.nome);

alter table cidade add constraint fk_cidade_estado foreign key (estado_id)
references estado (id);

alter table cidade drop column nome_estado;

ALTER TABLE CIDADE change nome_cidade nome varchar (90) not null;