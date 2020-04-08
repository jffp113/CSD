import http from 'k6/http';


export let options = {
    //maxRedirects: 4,
    iterations: 10000000,
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