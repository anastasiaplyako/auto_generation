
const {Pool, Client} = require('pg');

const pool = new Pool(
    {
        host: 'localhost',
        port: 5432,
        database: 'postgres',
        user: 'postgres',
        password: '12345678',
    }
);

pool.connect(function (err) {
        if (err) {
                return console.error("Ошибка: " + err.message);
        } else {
                console.log("Подключение к серверу MySQL успешно установлено");
        }
});

/*

let express = require('express');

// создаём Express-приложение
let app = express();

// создаём маршрут для главной страницы
// http://localhost:8080/
app.get('/', function(req, res) {
        res.sendfile('./inputHTML/inputProducts.html');
        res.end();
});
*/


// запускаем сервер на порту 8080

module.exports = {
        pool: pool,
        client: new Client({
                host: 'localhost',
                port: 5432,
                database: 'postgres',
                user: 'postgres',
                password: '12345678',
        })
};