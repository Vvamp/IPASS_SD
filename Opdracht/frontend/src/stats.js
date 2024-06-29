import StatsService from "./stats-service.js";
import DrukteService from "./drukte-service.js";
const service = new StatsService();
const ds = new DrukteService();
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
    lastupdate.textContent = new Date(stats.LastUpdate).toLocaleTimeString();
  });

  service.getEta().then((eta) => {
    etaItem.textContent = eta.eta;
  });

  service.getStatus().then((result) => {
    let status = "";
    if (result.Arrival == null) {
      let direction = result.Departure.Location == "Ingen" ? "Elst" : "Ingen";
      status = "Onderweg (" + direction + ")";
    } else {
      status = "Aangemeerd (" + result.Arrival.Location + ")";
    }
    statusItem.textContent = status;
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

setInterval(refresh, 1000);
