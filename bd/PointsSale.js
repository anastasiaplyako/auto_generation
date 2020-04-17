let faker = require('faker');
const connection = require('../ConnectDb');
let staff = require('./Staff');
const config = require('../config/tsconfig.json');

let N = 0; //quantity of generation
let buysN = 0;
let staffN = 0;
let arrProductId = [];
let arrPointId = [];
let arrBuyId = [];

function randomInteger(min, max) {
    let rand = min - 0.5 + Math.random() * (max - min + 1);
    return Math.round(rand);
}

function insertPoints(N, buysN, staffN) {
    for (let i = 0; i < N; i++) {
        let address = faker.address.streetAddress();
        const text = 'INSERT INTO points_sale (address) VALUES($1) ';
        const values = [address];
        connection.client.query(text, values)
            .then(res => {
                insertBuys(buysN);
                for (let j = 0; j < staffN; j++) {
                    staff.insertInfStaff(buysN);
                }
            })
            .catch(e => console.error(e.stack))
    }
}

function insertBuys(buysN) {
    // inf buys and buys
    for (let i = 0; i < buysN; i++) {
        let date = faker.date.past();
        const queryInsertBuys = 'INSERT INTO buys (date) VALUES($1) RETURNING buy_id';
        const values = [date];
        connection.client.query(queryInsertBuys, values)
            .then(res => {selectBuyId();}).catch(e => console.error(e.stack));
    }
}

function selectBuyId(){
    const text1 = 'select buy_id from buys';
    connection.client.query(text1)
        .then(res => {
            for (let j = 0; j < res.rowCount; j++) {
                arrBuyId.push(res.rows[j].buy_id);
            }
            selectProductId();
        })
}

function selectProductId(){
    const text1 = 'select product_id from products';
    connection.client.query(text1)
        .then(res => {
            for (let j = 0; j < res.rowCount; j++) {
                arrProductId.push(res.rows[j].product_id);
            }
            selectPointId();
        })
}

function selectPointId(){
    const querySelectPoint = 'select point_id from points_sale';
    connection.client.query(querySelectPoint)
        .then(res => {
            for (let j = 0; j < res.rowCount; j++) {
                arrPointId.push(res.rows[j].point_id);
            }
            insertInfBuys();
        })
        .catch(e => console.error(e.stack));
}

function insertInfBuys(){
    let buyId = arrBuyId[(randomInteger(1, arrBuyId.length - 1))];
    let pointId = arrPointId[(randomInteger(1, arrPointId.length - 1))];
    let productId = arrProductId[randomInteger(1, arrProductId.length - 1)];
    let quantity = randomInteger(config.randomQuantityStart, config.randomQuantityEnd);
    let price = randomInteger(config.randomPriceStart, config.randomPriceEnd);
    const queryInsertInfBuys =
        'INSERT INTO inf_buys (buy_id, product_id, quantity, price, point_id) VALUES($1, $2, $3, $4, $5)';
    const values = [buyId, productId, quantity, price, pointId];
    connection.client.query(queryInsertInfBuys, values)
        .then(res => {}).catch(e => console.error(e.stack))
}

insertPoints();

module.exports = {
    insertPoints: insertPoints
};