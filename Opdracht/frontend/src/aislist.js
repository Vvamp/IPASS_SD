// var t = document.getElementById("table");
import { Grid } from "gridjs";
import "gridjs/dist/theme/mermaid.css";

var x = false;
function updateGrid() {
    var t = document.getElementById("table");
    // t.innerHTML = "";
    var g = new Grid({
        columns: ['Vertrektijd', 'Vertrekoever', 'Aankomsttijd', 'Aankomstoever'],
        sort: true,
        search: true,
        resizable: true,
        pagination: {
            limit: 15, // todo: make the pagination interact with server(setver limit with skip and limit)
            server: {
                url: (prev, page, limit) => `${prev}?limit=${limit}&page=${page}`
            }
        },
        server: {
            url: '/api/crossings/today', 
            then: data => data.results.map(ais => {
                var departureDate = new Date(0);
                var arrivalDate = new Date(0);
                departureDate.setUTCSeconds(ais.Departure.epochSeconds);
                arrivalDate.setUTCSeconds(ais.Arrival.epochSeconds);
                var departureDateText = departureDate.getHours().toString().padStart(2, "0") +
                    ":" +
                    departureDate.getMinutes().toString().padStart(2, "0") +
                    ":" +
                    departureDate.getSeconds().toString().padStart(2, "0");
                var arrivalDateText = arrivalDate.getHours().toString().padStart(2, "0") +
                    ":" +
                    arrivalDate.getMinutes().toString().padStart(2, "0") +
                    ":" +
                    arrivalDate.getSeconds().toString().padStart(2, "0");

                return [departureDateText, ais.Departure.Location.toLowerCase(), arrivalDateText, ais.Arrival.Location.toLowerCase()]
            }),
            total: data => data.total
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
    
    if(x == false){
        g.render(t)
        x = true;
    }else{
        g.forceRender(t);
    }
    
    // .forceRender(document.getElementById('table'));
}

// Initial load
updateGrid();

// Refresh the grid every minute
setInterval(updateGrid, 20000);
