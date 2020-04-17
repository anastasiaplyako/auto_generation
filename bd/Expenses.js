let faker = require('faker');
const connection = require('../ConnectDb');
const config = require('../config/tsconfig.json');

let N = 0;

function randomInteger(min, max) {
    let rand = min - 0.5 + Math.random() * (max - min + 1);
    return Math.round(rand);
}

function insertAdditionalExpenses(N){
    for (let i = 0; i < N; i++) {
        let typeArr = ['rent', 'taxes', 'electric power', 'salary'];
        let type = typeArr[randomInteger(0, typeArr.length - 1)];
        let production = randomInteger(config.startProduction,config.endProduction);
        let sale = randomInteger(config.startProduction,config.endProduction);
        let date = faker.date.past();
        const queryInsertBuys = 'INSERT INTO additional_expenses (type, production, sale, date) VALUES($1, $2, $3, $4)';
        const values = [type, production, sale,date];
        connection.client.query(queryInsertBuys, values)
            .then(res => {}).catch(e => console.error(e.stack));
    }
}

insertAdditionalExpenses();

module.exports = {
    insertAdditionalExpenses: insertAdditionalExpenses
};