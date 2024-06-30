import DrukteService from "./drukte-service.js";
const ds = new DrukteService();

const drukteIndicator = document.querySelector("#drukte-indicator");
const drukteTs = document.querySelector("#drukte-timestamp");

function updateDrukte() {
  // let drukte = ds.getDrukte();
  ds.getDrukte().then((drukte) => {
    if (drukte.Severity == 1) {
      drukteIndicator.innerHTML = "Rustig";
      drukteIndicator.style.backgroundColor = "green";
    } else if (drukte.Severity == 2) {
      drukteIndicator.innerHTML = "Gemiddeld";
      drukteIndicator.style.backgroundColor = "orange";
    } else if (drukte.Severity == 3) {
      drukteIndicator.innerHTML = "Druk";
      drukteIndicator.style.backgroundColor = "red";
    } else if (drukte.Severity == 4) {
      drukteIndicator.innerHTML = "Monster drukte";
      drukteIndicator.style.backgroundColor = "black";
    } else {
      drukteIndicator.innerHTML = "Onbekend";
      drukteIndicator.style.backgroundColor = "grey";
    }

    drukteTs.textContent = "Laatst geupdate: " + drukte.Time;
  });
}

setInterval(updateDrukte, 1000);
