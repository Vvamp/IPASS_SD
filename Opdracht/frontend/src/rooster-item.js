import RoosterService from "./rooster-service";
const service = new RoosterService();

function formatTime(date) {
  const hours = String(date.getUTCHours()).padStart(2, "0");
  const minutes = String(date.getUTCMinutes()).padStart(2, "0");
  return `${hours}:${minutes}`;
}
export default class RoosterDayItem {
  constructor(tasks) {
    this.tasks = tasks;
    this.render();
  }

  render() {
    const dataitem = document
      .querySelector("#schedule-data-day-template")
      .content.cloneNode(true);

    let dataelement = dataitem.querySelector(".schedule-data-day");

    // for each task in the day:
    this.tasks.forEach((task) => {
      dataitem.querySelector(
        ".schedule-data-day-date"
      ).textContent = `${new Date(task.Begin).getUTCDate()}/${
        new Date(task.Begin).getUTCMonth() + 1
      }`;
      const taskitem = dataitem
        .querySelector("#schedule-data-row-template")
        .content.cloneNode(true);
      taskitem.querySelector(".schedule-data-row-time").textContent =
        formatTime(new Date(task.Begin)) +
        " - " +
        formatTime(new Date(task.End));
      taskitem.querySelector(".schedule-data-row-role").textContent = task.Type;
      taskitem.querySelector(".schedule-data-row-name").textContent =
        task.Username;
      const taskelement = taskitem.querySelector(".schedule-data-row");
      const deleteElement = taskelement.querySelector(
        ".schedule-data-row-delete"
      );

      if (deleteElement) {
        deleteElement.dataset.uuid = task.UUID;
        deleteElement.addEventListener("click", (e) => {
          service.removeRoosterItem(task.UUID).then(() => {
            deleteElement.closest(".schedule-data-row").remove();
          });
        });
      }

      dataelement.appendChild(taskitem);
    });

    document.querySelector(".schedule-data").appendChild(dataitem);
  }
}
