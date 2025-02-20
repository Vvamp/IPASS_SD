// var t = document.getElementById("table");
import { Grid } from "gridjs";
import "gridjs/dist/theme/mermaid.css";

var currentTotal = 0;
var rendered = false;
var previousTS = -1;

function formatDuration(seconds){
    if(seconds === "Onbekend") return seconds;
    let hours = Math.floor(seconds / 3600);
    let minutes = Math.floor((seconds % 3600) / 60);
    let secs = seconds % 60;

    let parts = [];
    // if (hours > 0) parts.push(`${hours} uur`);
    // if (minutes > 0) parts.push(`${minutes} minuut${minutes !== 1 ? 'ten' : ''}`);
    // if (secs > 0 || parts.length === 0) parts.push(`${secs} seconde${secs !== 1 ? 'n' : ''}`);
    parts.push(hours.toString().padStart(2, "0")+":");
    parts.push(minutes.toString().padStart(2, "0")+":");
    parts.push(secs.toString().padStart(2, "0"));

    return parts.join("").replace("minuutten", "minuten");
}

function loadData(ais, totaldata){
    currentTotal = totaldata;
    var departureDate = new Date(0);
    departureDate.setUTCSeconds(ais.Departure.epochSeconds);
    var departureDateText = departureDate.getHours().toString().padStart(2, "0") +
    ":" +
    departureDate.getMinutes().toString().padStart(2, "0") +
    ":" +
    departureDate.getSeconds().toString().padStart(2, "0");
    
    var landTime = ais.Departure.epochSeconds - previousTS;
    if(previousTS == -1){
        landTime = "Onbekend";
    }
    if(ais.Arrival == null){
        return [departureDateText, ais.Departure.Location.toLowerCase(), "tbd", "tbd", formatDuration(landTime), "tbd"]
    }


    var arrivalDate = new Date(0);
    arrivalDate.setUTCSeconds(ais.Arrival.epochSeconds);
    var arrivalDateText = arrivalDate.getHours().toString().padStart(2, "0") +
        ":" +
        arrivalDate.getMinutes().toString().padStart(2, "0") +
        ":" +
        arrivalDate.getSeconds().toString().padStart(2, "0");
    previousTS = ais.Arrival.epochSeconds;


    var sailTime = ais.Arrival.epochSeconds - ais.Departure.epochSeconds;
    return [departureDateText, ais.Departure.Location.toLowerCase(), arrivalDateText, ais.Arrival.Location.toLowerCase(), formatDuration(landTime) , formatDuration(sailTime)]
    
}

const grid = new Grid({
    columns: ['Vertrektijd', 'Vertrekoever', 'Aankomsttijd', 'Aankomstoever', 'Tijd aan oever', 'Vaartijd'],
    sort: true,
    search: true,
    resizable: true,
    pagination: {
        limit: 15, // todo: make the pagination interact with server(setver limit with skip and limit)
        // server: {
        //     url: (prev, page, limit) => `${prev}?limit=${limit}&page=${page}`
        // }
    },
    server: {
        url: '/api/crossings/today', 
        then: data => data.results.slice().reverse().map(ais => loadData(ais, data.total)).reverse(),
        // total: data => data.total
    },
    language: {
        noRecordsFound: "Geen overtochten",
        pagination: {
            previous: "Vorige",
            next: "Volgende",
            results: "resultaten",
            of: "van",
            to: "t/m",
            showing: "Toon"
        }
    }
});
function updateGrid() {
    var t = document.getElementById("table");

    if(rendered == false){
        rendered = true;
        grid.render(t)
    }else{
        grid.updateConfig({
            server: {
                url: '/api/crossings/today', 
                then: data => data.results.slice().reverse().map(ais => loadData(ais)).reverse(),
            }
        }).forceRender();
    }   
}

// Initial load
updateGrid();

function httpGetAsync()
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200){
            var response = xmlHttp.responseText;
            if(response != currentTotal){
                updateGrid();
            }
        }
        
    }
    xmlHttp.open("GET", "http://localhost:8080/api/crossings/today/count", true); // true for asynchronous 
    xmlHttp.send(null);
}

// Refresh the grid every minute
setInterval(httpGetAsync, 20000);
