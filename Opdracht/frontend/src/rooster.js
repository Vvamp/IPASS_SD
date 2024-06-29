import RoosterService from "./rooster-service.js";
import RoosterDayItem from "./rooster-item.js";
import { createModal } from "./modal.js";

const service = new RoosterService();
Date.prototype.getWeek = function (dowOffset) {
  /*getWeek() was developed by Nick Baicoianu at MeanFreePath: http://www.meanfreepath.com */

  dowOffset = typeof dowOffset == "number" ? dowOffset : 0; //default dowOffset to zero
  var newYear = new Date(this.getFullYear(), 0, 1);
  var day = newYear.getDay() - dowOffset; //the day of week the year begins on
  day = day >= 0 ? day : day + 7;
  var daynum =
    Math.floor(
      (this.getTime() -
        newYear.getTime() -
        (this.getTimezoneOffset() - newYear.getTimezoneOffset()) * 60000) /
        86400000
    ) + 1;
  var weeknum;
  //if the year starts before the middle of a week
  if (day < 4) {
    weeknum = Math.floor((daynum + day - 1) / 7) + 1;
    if (weeknum > 52) {
      nYear = new Date(this.getFullYear() + 1, 0, 1);
      nday = nYear.getDay() - dowOffset;
      nday = nday >= 0 ? nday : nday + 7;
      /*if the next year starts before the middle of
                  the week, it is week #1 of that year*/
      weeknum = nday < 4 ? 1 : 53;
    }
  } else {
    weeknum = Math.floor((daynum + day - 1) / 7);
  }
  return weeknum;
};

var weekNr = new Date().getWeek();

var roosterType = document.querySelector(".schedule-data").dataset.roostertype;
let roosterAll = true;
if (roosterType == "personal") {
  roosterAll = false;
}
export default class Rooster {
  loadSchedule(getAll) {
    document.querySelectorAll(".schedule-data-day").forEach((e) => e.remove());
    service.getRooster(weekNr, getAll).then((rooster) => {
      if (rooster != "fail") {
        document
          .querySelectorAll(".no-schedule-text")
          .forEach((e) => e.remove());

        if (rooster.length == 0) {
          const h = document.createElement("h1");
          h.className = "no-schedule-text";
          if (roosterAll) {
            h.textContent = "Nog geen diensten ingepland deze week.";
          } else {
            h.textContent = "Geen diensten deze week 🎉";
          }
          h.style.textAlign = "center";
          h.style.color = "white";
          document.querySelector(".schedule-data").appendChild(h);
        } else {
          rooster.forEach((day) => {
            const dt = new Date(day.Day);
            new RoosterDayItem(day.Tasks);
          });
        }
      } else {
        document.querySelector(".content-wrapper").innerHTML =
          "<h1>You are not allowed to access this page</h1>";
      }
    });
  }
}

const rooster = new Rooster();
rooster.loadSchedule(roosterAll);

const scheduleWeekInput = document.querySelector("#schedule-week");
function updateWeek(weekNumber) {
  if (weekNumber == -1) {
    weekNumber = scheduleWeekInput.value;
  }
  scheduleWeekInput.value = weekNumber;
  weekNr = weekNumber;
}

updateWeek(weekNr);
scheduleWeekInput.addEventListener("change", () => {
  updateWeek(-1);
  rooster.loadSchedule(roosterAll);
});

const addRoosterButton = document.querySelector("#add-schedule");
if (addRoosterButton) {
  addRoosterButton.addEventListener("click", () => {
    createModal("addrooster-modal");
  });
}
