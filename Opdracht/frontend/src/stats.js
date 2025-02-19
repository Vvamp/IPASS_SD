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
  const maxspeed = statsPanel.querySelector(
    '[data-stats-item="maxspeed"]'
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
    if (result.open) {
      openItem.textContent = "Ja (Sluit om " + result.closingTime + ")";
    } else {
      openItem.textContent = "Nee (Opent om " + result.openingTime + ")";
    }
  });

  service.getStats().then((stats) => {
    cspeed.textContent = stats.CurrentSpeed.toFixed(2) + " kn";
    avgspeed.textContent = stats.AverageSpeed.toFixed(2) + " kn";
    maxspeed.textContent = stats.MaxSpeed.toFixed(2) + " kn";
    let date = new Date(0);
    date.setUTCMilliseconds(stats.MaxSpeedTimestamp);
    maxspeed.textContent +=
      " @ " +
      date.getHours().toString().padStart(2, "0") +
      ":" +
      date.getMinutes().toString().padStart(2, "0") +
      "";
    crossings.textContent = stats.CrossingCount;
    date = new Date(0);
    date.setUTCMilliseconds(stats.LastUpdate / 1000);
    lastupdate.textContent =
      date.getHours().toString().padStart(2, "0") +
      ":" +
      date.getMinutes().toString().padStart(2, "0") +
      ":" +
      date.getSeconds().toString().padStart(2, "0");
  });

  service.getStatus().then((result) => {
    let status = "";
    if (result.Arrival == null) {
      let direction = result.Departure.Location == "INGEN" ? "ELST" : "INGEN";
      status = "Onderweg (" + direction + ")";
      cspeed.parentElement.classList.remove("hidden");
    } else {
      status = "Aangemeerd (" + result.Arrival.Location + ")";
      cspeed.parentElement.classList.add("hidden");

    }
    if (status.toLowerCase() != lastStatus) {
      cs.loadOvertochten();
    }
    statusItem.textContent = status.toLowerCase();
    lastStatus = status.toLowerCase();
    if (result.Arrival == null) {
      let date = new Date(0);
      date.setUTCSeconds(result.Departure.epochSeconds);
      vertrekItem.textContent =
        date.getHours().toString().padStart(2, "0") +
        ":" +
        date.getMinutes().toString().padStart(2, "0") +
        ":" +
        date.getSeconds().toString().padStart(2, "0");
      vertrekItem.parentElement.classList.remove("hidden");
    } else {
      vertrekItem.parentElement.classList.add("hidden");
    }

    if (result.Arrival == null) {
      service.getEta().then((eta) => {
        let date = new Date(0);
        date.setUTCSeconds(eta.eta);
        etaItem.textContent =
          date.getHours().toString().padStart(2, "0") +
          ":" +
          date.getMinutes().toString().padStart(2, "0") +
          ":" +
          date.getSeconds().toString().padStart(2, "0");
        etaItem.parentElement.classList.remove("hidden");
      });
    } else {
      etaItem.parentElement.classList.add("hidden");
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
refresh();
setInterval(refresh, 5000);
