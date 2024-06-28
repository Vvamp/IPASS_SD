import StatsService from "./stats-service.js";
const service = new StatsService();
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
}

setInterval(refresh, 1000);
