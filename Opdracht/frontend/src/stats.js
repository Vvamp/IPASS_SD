import StatsService from "./stats-service.js";
import DrukteService from "./drukte-service.js";
const service = new StatsService();
const ds = new DrukteService();
function refresh() {
  const statsPanel = document.querySelector("#statistics");
  const cspeed = statsPanel.querySelector(
    '[data-stats-item="currentspeed"'
  ).lastElementChild;
  const avgspeed = statsPanel.querySelector(
    '[data-stats-item="avgspeed"'
  ).lastElementChild;
  const crossings = statsPanel.querySelector(
    '[data-stats-item="crossings"'
  ).lastElementChild;
  const lastupdate = statsPanel.querySelector(
    '[data-stats-item="lastupdate"'
  ).lastElementChild;
  service.getStats().then((stats) => {
    cspeed.textContent = stats.CurrentSpeed.toFixed(2) + " kts";
    avgspeed.textContent = stats.AverageSpeed.toFixed(2) + " kts";
    crossings.textContent = stats.CrossingCount;
    lastupdate.textContent = new Date(stats.LastUpdate).toLocaleTimeString();
  });
  const drukteItem = statsPanel.querySelector(
    '[data-stats-item="drukte"'
  ).lastElementChild;

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
