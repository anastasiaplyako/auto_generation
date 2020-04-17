let faker = require('faker');
const connection = require('../ConnectDb');
let staff = require('./Staff');
let main = require('../main');
const config = require('../config/tsconfig.json');


let N = 0; //quantity of generation
let typeArr = ['мука', 'сахар', 'сливки', 'масло', 'молоко', 'яйцо', 'сода'];
let arrSupplierId = [];
let arrProductId = [];

function randomInteger(min, max) {
    let rand = min - 0.5 + Math.random() * (max - min + 1);
    return Math.round(rand);
}

function insertInfSupplier(N){
    insertRawProduct();
    for (let i = 0; i < N; i++) {
        let nameSupplier = faker.company.companyName();
        let city = faker.address.city();
        let tel = faker.phone.phoneNumber();
        const queryInsertInfSupplier = 'INSERT INTO inf_suppliers (name_supplier, city, tel) VALUES($1, $2, $3) ';
        const values = [nameSupplier,city, tel];
        connection.client.query(queryInsertInfSupplier, values)
            .then(res => {selectSupplyId();})
            .catch(e => console.error(e.stack))
    }
}

function selectSupplyId(){
    const text5 = 'SELECT supplier_id FROM inf_suppliers';
    connection.client.query(text5)
        .then(res => {
            for (let i = 0; i < res.rowCount; i++) {
                arrSupplierId.push(res.rows[i].supplier_id)
            }
            selectRawProductId();
        })
        .catch(e => console.error(e.stack));
}

function selectRawProductId(){
    const text5 = 'SELECT raw_product_id FROM raw_products';
    connection.client.query(text5)
        .then(res => {
            for (let i = 0; i < res.rowCount; i++) {
                arrProductId.push(res.rows[i].raw_product_id)
            }
            insertSupply()
        })
        .catch(e => console.error(e.stack));
}

function insertSupply(){
    let rawProductId = arrProductId[randomInteger(1, arrProductId.length - 1)];//
    let supplierId = arrSupplierId[randomInteger(1, arrSupplierId.length - 1)];//
    let numberInvoice = randomInteger(1, config.randomNumberInvoice);
    let date = faker.date.past();
    let price = randomInteger(config.randomPriceEndSupply, config.randomPriceEndSupply) ;
    let quantity = randomInteger(1, config.randomQuantitySupply);
    let idUnit = randomInteger(config.idUnitStart, config.idUnitEnd);
    const text21 = 'INSERT INTO supply (raw_product_id, supplier_id, number_invoice, date, price, quantity, id_unit) VALUES($1, $2, $3, $4, $5, $6, $7) ';
    const values21 = [rawProductId,supplierId, numberInvoice, date, price, quantity, idUnit];
    connection.client.query(text21, values21)
        .then(res => {
            console.log(res)
        })
        .catch(e => console.error(e.stack))
}

function insertRawProduct(){
    let rawProductName  =  faker.commerce.productName();
    let type = typeArr[randomInteger(0,typeArr.length - 1)];
    const queryInsertRawProduct = 'INSERT INTO raw_products (raw_product_name, type) VALUES($1, $2)';
    const value = [rawProductName, type];
    connection.client.query(queryInsertRawProduct, value)
        .then(res => {})
        .catch(e => console.error(e.stack));
}

insertInfSupplier();

module.exports = {
    insertInfSupplier: insertInfSupplier
};