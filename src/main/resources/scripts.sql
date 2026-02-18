create invoice_management_db;

create user invoice_management_db_manager with password 'admin_invoice_management_db';

grant connect on database invoice_management_db to invoice_management_db_manager ;

\c invoice_management_db;

grant create on schema public to  invoice_management_db_manager;

alter default privileges in schema public
    grant select, insert, update, delete on tables to invoice_management_db_manager;

alter default privileges in schema public
      grant usage, select, update on sequences to  invoice_management_db_manager;