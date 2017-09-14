/**
 * CREATE Script for init of DB
 */


insert into customer(id, first_name, last_name, email, password, date_created) values (1, 'custFirst01', 'custLast01', 'customer@mail.com', '1aPassword!', now());

insert into bank_account(id, account_number, account_type, balance, customer_id, date_created) values (1, 12345678, 'CURRENT_ACCOUNT', 345.12, 1, now());

insert into bank_transaction(id, transaction_type, amount, transaction_date, updated_balance, account_id) values (1, 'DEPOSIT', 145.12, now(), 345.12, 1);

