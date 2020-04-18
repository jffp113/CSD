import http from 'k6/http';


export let options = {
    //maxRedirects: 4,
    iterations: 1000,
    //vus: 100
};

export default function(idUser) {
    var url = 'http://localhost:8444/money/current/jorge';


    http.get(url);
}