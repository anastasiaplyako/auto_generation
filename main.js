const connection = require('./ConnectDb');
let staff = require('./bd/Staff');
let supply = require('./bd/Supply');
let products = require('./bd/Products');
let pointSale = require('./bd/PointsSale');
let expenses  = require('./bd/Expenses');
let bodyParser = require('body-parser');
const express = require("express");
let path = require('path');
let http = require('http');


connection.client.connect(function (err) {
    if (err) {
        return console.error("Ошибка: " + err.message);
    } else {
        console.log("Подключение к серверу MySQL успешно установлено");
    }
});

let N = 0;
let costHistoryN = 0;
let recipesN = 0;
let buysN = 0;
let staffN = 0;

let app = express();

app.use(express.static(path.join(__dirname, '../')));
let urlencoded  = bodyParser.urlencoded({extended: false});

app.post('/product', urlencoded,  function (req, res) {
    console.log('oooou');
    N = req.body.N;
    costHistoryN = req.body.costHistoryN;
    recipesN = req.body.recipesN;
    console.log('N = ' + N);
    console.log('costHistoryN = ' + costHistoryN);
    console.log('recipesN = ' + recipesN);
    console.log(__dirname);
    res.sendFile(path.join(__dirname, './html/successDownload.html'));
    products.insertProducts(N, costHistoryN, recipesN);
});

app.post('/staff', urlencoded,  function (req, res) {
    console.log('oooou');
    N = req.body.N;
    console.log('N = ' + N);
    console.log(__dirname);
    res.sendFile(path.join(__dirname, './html/successDownload.html'));
    staff.insertInfStaff(N);
});

app.post('/point', urlencoded,  function (req, res) {
    console.log('oooou');
    N = req.body.N;
    buysN = req.body.buysN;
    staffN = req.body.staffN;
    console.log('N = ' + N);
    console.log(__dirname);
    res.sendFile(path.join(__dirname, './html/successDownload.html'));
    pointSale.insertPoints(N, buysN, staffN);
});

app.post('/expenses', urlencoded,  function (req, res) {
    console.log('oooou');
    N = req.body.N;
    console.log(__dirname);
    res.sendFile(path.join(__dirname, './html/successDownload.html'));
    expenses.insertAdditionalExpenses(N);
});

app.post('/supplier', urlencoded,  function (req, res) {
    console.log('oooou');
    N = req.body.N;
    console.log(__dirname);
    res.sendFile(path.join(__dirname, './html/successDownload.html'));
    supply.insertInfSupplier(N);
});

app.listen(3000, function(){
    console.log('connection');
});

http.createServer(app).listen(app.get('port'), function(){
    console.log('Express server listening on port ' + app.get('port'));
});



