var request = require('request');
var express = require('express');
var fs = require('fs');
var request2 = require('sync-request');
var compression = require('compression')


var Pusher = require('pusher');

var pusher = new Pusher({
  appId: '243686',
  key: '3c9aa0e2bcc0ff7d6926',
  secret: 'e02daee1fb68c39072c4',
  encrypted: true
});

var app = express();

//__ CORS __
app.use(function (req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
    res.setHeader('Access-Control-Allow-Credentials', true);
    next();
});

app.use(compression());

var server = require('http').Server(app);
var bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({
    extended: true
}));

server.listen(8083, function () {
    console.log('ServerGuardias running on 8083 port');
});

// __ Express Server __
app.use('/', express.static(__dirname + '/www'));






var log_file = fs.createWriteStream(__dirname + '/debug.log', { flags: 'w' });
var log_stdout = process.stdout;






app.post('/service/sendToPhone', function (req, res) {
    console.log("Servicio consumido POST");
    console.log("/service/sendToPhone");
    console.log(req.body.data);

    pusher.trigger('enviar', 'enviar-event', req.body.data);
    res.send(req.body.data);
});



