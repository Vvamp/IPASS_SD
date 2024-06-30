import StatsService from "./stats-service.js";
import DrukteService from "./drukte-service.js";
import Crossings from "./overtochten.js";

const cs = new Crossings();
const service = new StatsService();
const ds = new DrukteService();
let lastStatus = "";
function refresh() {
  const statsPanel = document.querySelector("#statistics");
  const cspeed = statsPanel.querySelector(
    '[data-stats-item="currentspeed"]'
  ).lastElementChild;
  const avgspeed = statsPanel.querySelector(
    '[data-stats-item="avgspeed"]'
  ).lastElementChild;
  const crossings = statsPanel.querySelector(
    '[data-stats-item="crossings"]'
  ).lastElementChild;
  const lastupdate = statsPanel.querySelector(
    '[data-stats-item="lastupdate"]'
  ).lastElementChild;
  const drukteItem = statsPanel.querySelector(
    '[data-stats-item="drukte"]'
  ).lastElementChild;

  const etaItem = statsPanel.querySelector(
    '[data-stats-item="eta"]'
  ).lastElementChild;
  const statusItem = statsPanel.querySelector(
    '[data-stats-item="status"]'
  ).lastElementChild;
  const vertrekItem = statsPanel.querySelector(
    '[data-stats-item="vertrek"]'
  ).lastElementChild;

  const openItem = statsPanel.querySelector(
    '[data-stats-item="isopen"]'
  ).lastElementChild;

  service.getIsOpen().then((result) => {
    if (result.isOpen) {
      openItem.textContent = "Ja (Sluit om " + result.closingTime + ")";
    } else {
      openItem.textContent = "Nee (Opent om " + result.openingTime + ")";
    }
  });

  service.getStats().then((stats) => {
    cspeed.textContent = stats.CurrentSpeed.toFixed(2) + " kts";
    avgspeed.textContent = stats.AverageSpeed.toFixed(2) + " kts";
    crossings.textContent = stats.CrossingCount;
    let date = new Date(0);
    date.setMilliseconds(stats.LastUpdate / 1000);
    date.setHours(date.getHours() + 1);
    lastupdate.textContent = date.toLocaleTimeString();
  });

  service.getStatus().then((result) => {
    let status = "";
    if (result.Arrival == null) {
      let direction = result.Departure.Location == "INGEN" ? "ELST" : "INGEN";
      status = "Onderweg (" + direction + ")";
    } else {
      status = "Aangemeerd (" + result.Arrival.Location + ")";
    }
    if (status.toLowerCase() != lastStatus) {
      cs.loadOvertochten();
    }
    statusItem.textContent = status.toLowerCase();
    lastStatus = status.toLowerCase();
    if (result.Arrival == null) {
      let date = new Date(0);
      date.setSeconds(result.Departure.epochSeconds);
      vertrekItem.textContent =
        (date.getHours() - 1).toString().padStart(2, "0") +
        ":" +
        date.getMinutes().toString().padStart(2, "0") +
        ":" +
        date.getSeconds().toString().padStart(2, "0");
      vertrekItem.parentElement.style.display = "";
    } else {
      vertrekItem.parentElement.style.display = "none";
    }

    if (result.Arrival == null) {
      service.getEta().then((eta) => {
        etaItem.textContent = eta.eta;
        etaItem.parentElement.style.display = "";
      });
    } else {
      etaItem.parentElement.style.display = "none";
    }
  });

  ds.getDrukte().then((drukte) => {
    switch (drukte.Severity) {
      case 1:
        drukteItem.textContent = "Rustig";
        break;
      case 2:
        drukteItem.textContent = "Gemiddeld";
        break;
      case 3:
        drukteItem.textContent = "Druk";
        break;
      case 4:
        drukteItem.textContent = "Monster drukte";
        break;
      default:
        drukteItem.textContent = "Onbekend";
        break;
    }
  });
}
cs.loadOvertochten();

setInterval(refresh, 5000);
