function loadStats() {
    let statsWindow = document.querySelector("#statistics"); 

    // Status
    let statusRow = document.querySelector('[data-stats-item="status"');
    let statusCell = statusRow.lastElementChild;
    fetch("/api/crossings?limit=1",{method: "GET"})
    .then(function(response){
        if(response.ok){
            return response.json();
        }
    }).then(function(result){
        statusCell.textContent = result[0].active ? "Varen" : "Aangemeerd";
        //todo: based on if arrival is null: show which direction by inverting departure (departure.location == ingen ? elst : ingen)
    });
}

if (window.addEventListener) // W3C standard
{
  window.addEventListener('load', loadStats, false); // NB **not** 'onload'
} 