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
  const statusItem = document.querySelector('[data-stats-item="status"]');

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
    if (status.active && status.Arrival != null) {
      let direction = result.Departure.Location == "Ingen" ? "Elst" : "Ingen";
      status = "Onderweg (" + direction + ")";
    } else {
      status = "Aangemeerd (" + result.Arrival.Location + ")";
    }
    statusItem.lastElementChild.textContent = status;
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
