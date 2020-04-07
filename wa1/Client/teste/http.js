import http from 'k6/http';


export let options = {
    tlsAuth: [
      {
            domains: ["localhost"],
            cert: open("./pem.pem"),
            key: open("./server.pem")
        }
    ],
    //maxRedirects: 4,
    iterations: 1000,
    //vus: 100
};

export default function(idUser) {
    var url = 'http://localhost:8444/money/create';

    var params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    //for (var id = 1; id <= 100; id++) {
        var payload = JSON.stringify({
            to: 'teste' ,
            amount: 200,
        });
        http.post(url, payload, params);
    //}
}