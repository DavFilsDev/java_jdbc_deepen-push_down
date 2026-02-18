create invoice_management_db;

create user invoice_management_db_manager with password 'admin_invoice_management_db';

grant connect on database invoice_management_db to invoice_management_db_manager ;

\c invoice_management_db;

grant create on schema public to  invoice_management_db_manager;

alter default privileges in schema public
    grant select, insert, update, delete on tables to invoice_management_db_manager;

alter default privileges in schema public
      grant usage, select, update on sequences to  invoice_management_db_manager;

CREATE TYPE invoice_status AS ENUM('DRAFT', 'CONFIRMED', 'PAID');
CREATE TABLE invoice (
                         id SERIAL PRIMARY KEY,
                         customer_name VARCHAR NOT NULL,
                         status invoice_status);
CREATE TABLE invoice_line (
                              id SERIAL PRIMARY KEY,
                              invoice_id INT NOT NULL REFERENCES invoice(id),
                              label VARCHAR NOT NULL,
                              quantity INT NOT NULL,
                              unit_price NUMERIC(10,2) NOT NULL
);

INSERT INTO invoice (customer_name, status) VALUES
                                                (1, 'Alice', 'CONFIRMED'),
                                                (2, 'Bob', 'PAID'),
                                                (3, 'Charlie', 'DRAFT');
INSERT INTO invoice_line (invoice_id, label, quantity, unit_price) VALUES
                                                                       (1, 'Produit A', 2, 100),
                                                                       (1, 'Produit B', 1, 50),
                                                                       (2, 'Produit A', 5, 100),
                                                                       (2, 'Service C', 1, 200),
                                                                       (3, 'Produit B', 3, 50);