let faker = require('faker');
const connection = require('../ConnectDb');
const config = require('../config/tsconfig.json');

connection.client.connect(function (err) {
    if (err) {
        return console.error("Ошибка: " + err.message);
    } else {
        console.log("Подключение к серверу MySQL успешно установлено");
    }
});

let N = 1; //quantity of generation
let numbers = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
let positionArr = ['Менеджер по продажам ', 'Продавец', 'Директор по продажам',
    'Консультант ', 'house keepeng', 'пекарь', 'программист'];
let arrPointId = [];
let arrEmployeeId = [];

function randomInteger(min, max) {
    let rand = min - 0.5 + Math.random() * (max - min + 1);
    return Math.round(rand);
}

function insertInfStaff(N){
    for (let i = 0; i < N; i++) {
        let nameEmployee = faker.name.findName();
        let passportDataEmployee = '';
        //generation passport data
        for (let i = 0; i < config.passportData; i++) {
            let index = randomInteger(0, 9);
            passportDataEmployee += numbers[index].toString();
        }
        const text = 'INSERT INTO inf_staff (full_name, passport_data) VALUES($1, $2)';
        const values = [nameEmployee, passportDataEmployee];
        connection.client.query(text, values)
            .then(res => {selectEmployeeId();}).catch(e => console.error(e.stack))
    }
}

function selectEmployeeId(){
    let text = 'SELECT employee_id from inf_staff';
    connection.client.query(text)
        .then(res => {
            for (let i = 0; i < res.rowCount; i++) {
                arrEmployeeId.push(res.rows[i].employee_id);
            }
            selectPointId();
        })
        .catch(e => console.error(e.stack));
}

function selectPointId() {
    let text = 'SELECT point_id from points_sale';
    connection.client.query(text)
        .then(res => {
           for (let i = 0; i < res.rowCount; i++) {
                arrPointId.push(res.rows[i].point_id);
            }
            insertHistoryStaff();
        })
        .catch(e => console.error(e.stack));
}

function insertHistoryStaff(){
    let employeeId = arrEmployeeId[randomInteger(1, arrEmployeeId.length - 1)];
    let position = positionArr[randomInteger(0, positionArr.length - 1)];
    let dateFrom = faker.date.past();
    let dateTo = faker.date.future();
    let salary = randomInteger(config.salaryStart, config.salaryEnd);
    let pointId = arrPointId[randomInteger(1, arrPointId.length - 1)];
    const text = 'INSERT INTO history_staff (employee_id, position, date_to, date_from, point_id, salary) VALUES($1, $2, $3, $4, $5, $6)';
    const values = [employeeId, position, dateTo, dateFrom, pointId, salary];
    connection.client.query(text, values)
        .then(res => {}).catch(e => console.error(e.stack))
}
module.exports = {
    insertInfStaff: insertInfStaff
};

insertInfStaff();