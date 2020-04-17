let faker = require('faker');
const connection = require('../ConnectDb');
const config = require('../config/tsconfig.json');

let N = 0;
let costHistoryN = 0;
let recipesN = 0;

connection.client.connect(function (err) {
    if (err) {
        return console.error("Ошибка: " + err.message);
    } else {
        console.log("Подключение к серверу MySQL успешно установлено");
    }
});

let productType= ['булочка', 'пончик', 'кекс', 'печенье', 'пирожок', 'конфеты'];
let productDescription = ['белки', 'жиры', 'углеводы'];
let arrRawProductId = [];
let arrUnitId = [];
let arrProductId = [];

function randomInteger(min, max) {
    let rand = min - 0.5 + Math.random() * (max - min + 1);
    return Math.round(rand);
}

function insertProducts(N, costHistoryN, recipesN){
    for (let i = 0; i < N; i++) {
        let productName = faker.commerce.productName();
        let type = productType[randomInteger(0,5)];
        let description = '';
        for (let j = 0; j < productDescription.length; j++) {
            description += productDescription[j] + ' ' +
                randomInteger(config.randomDescrStart,config.randomDescrEnd).toString() + ' ';
        }
        const queryInsertProducts = 'INSERT INTO products (product_name, description, type) VALUES($1, $2, $3) ';
        const values = [productName,description, type];
        connection.client.query(queryInsertProducts, values)
            .then(res => {}).catch(e => console.error(e.stack));
        selectProductId(costHistoryN, recipesN);
    }
}

function selectProductId(costHistoryN, recipesN){
    const text1 = 'select product_id from products';
    connection.client.query(text1)
        .then(res => {
            for (let j = 0; j < res.rowCount; j++) {
                arrProductId.push(res.rows[j].product_id);
            }
            insertCostHistory(costHistoryN);
            selectRawProductId(recipesN);
        })
}

function insertCostHistory(costHistoryN){
    for (let i = 0; i < costHistoryN; i++) {
        let costPrice = randomInteger(config.randomPriceStart, config.randomPriceEnd);
        let sellingPrice = costPrice + config.priceDifference;
        let dateFrom = faker.date.past();
        let dateTo = faker.date.future();
        let productId = arrProductId[randomInteger(1, arrProductId.length)];
        const queryInsertCostHistory =
            'INSERT INTO cost_history (product_id, cost_price, selling_price, date_from, date_to) VALUES($1, $2, $3, $4, $5)';
        const values = [productId,costPrice, sellingPrice, dateFrom, dateTo];
        connection.client.query(queryInsertCostHistory, values).then(res => {}).catch(e => console.error(e.stack))
    }
}

function selectRawProductId(recipesN){
    for (let i = 0; i < recipesN; i++) {
        const text1 =  'select raw_product_id from raw_products';
        connection.client.query(text1)
            .then(res => {
                for (let j = 0; j < res.rowCount; j++) {
                    arrRawProductId.push(res.rows[j].raw_product_id);
                }
                selectUnitId();
            })
            .catch(e => console.error(e.stack))
    }
}

function selectUnitId(){
    const text1 = 'select id_unit from units';
    connection.client.query(text1)
        .then(res => {
            for (let j = 0; j < res.rowCount; j++) {
                arrUnitId.push(res.rows[j].id_unit);
            }
            insertRecipes();
        })
}

function insertRecipes(){
    let rawProductId = arrRawProductId[randomInteger(1, arrRawProductId.length - 1)];
    let productId = arrProductId[randomInteger(1, arrProductId.length - 1)];
    let quantity = randomInteger(1,config.randomQuantityProduct);
    let unitId = arrUnitId[randomInteger(1, arrUnitId.length - 1)];
    const queryInsertRecipes = 'INSERT INTO recipes (product_id, raw_product_id, quantity, id_unit) VALUES($1, $2, $3, $4)'
    const values = [productId,rawProductId, quantity, unitId];
    connection.client.query(queryInsertRecipes, values).then(res => {}).catch(e => console.error(e.stack))
}

module.exports = {
    insertProducts: insertProducts
};

