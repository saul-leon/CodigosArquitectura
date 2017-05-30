var app = angular.module('myApp', []);
app.controller('myCtrl', function ($scope) {




    Pusher.logToConsole = true;

    var pusher = new Pusher('3c9aa0e2bcc0ff7d6926', {
        encrypted: true
    });

    var channel = pusher.subscribe('solicitar');
    channel.bind('solicitar-event', function (data) {
        console.log(data);



        //consumo de servicio de memo
        fetch("http://10.51.145.97:8080/YakanaAuthServer/webresources/monitor", {
            method: "POST",
            headers: {
                "Content-type": "application/x-www-form-urlencoded"
            },
            body: "pa_FchInicio=" + data.pa_FchInicio + "&pa_FchFin=" + data.pa_FchFin + "&destinatario=" + data.destinatario
        }).then(function (res) {
            return res.text()
        }).then(function (data) {
            $scope.logs = []
            var logs = []
            try {
                var codigos = JSON.parse(data)
                console.log("********************");
                console.log(codigos);
                console.log("********************");

                //se envia con pusher al telefono ********************

                fetch("http://10.51.146.165:8083/service/sendToPhone", {
                    method: "POST",
                    headers: {
                        "Content-type": "application/x-www-form-urlencoded"
                    },
                    body: "data=" + data
                }).then(function (res) {
                    return res.text()
                }).then(function (data) {
                    
                    
                    try {
                    
                        console.log(" enviado al telefono");
                        console.log(data);
                        console.log("********************");

                    } catch (error) {
                        console.log(error + " Object: " + JSON.stringify(data))
                        return
                    }

                });
                //********************************************** */


            } catch (error) {
                console.log(error + " Object: " + JSON.stringify(codigos))
                return
            }

        });

        //********************** */



    });


});
