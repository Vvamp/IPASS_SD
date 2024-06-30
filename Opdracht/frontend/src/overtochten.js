import OvertochtenService from "./overtochten-service.js";
let os = new OvertochtenService();

function loadOvertochten() {
  const timelinewrapper = document.querySelector(".timeline-node-wrapper");
  os.getOvertochten().then((overtochten) => {
    if (overtochten == "fail") {
      console.error("Failed to load overtochten");
      return;
    }

    overtochten.reverse(); // from old to new
    overtochten.forEach((overtocht, index) => {
      const isLast = index === overtochten.length - 1;
      const isFirst = index === 0;

      let nodeDivA = null;
      let edgeDiv = null;
      nodeDivA = document.createElement("div");
      nodeDivA.classList.add("timeline-node");
      let ptime = document.createElement("p");
      let date = new Date(0);
      date.setSeconds(overtocht.Departure.epochSeconds);
      ptime.innerHTML =
        date.getHours().toString().padStart(2, "0") +
        ":" +
        date.getMinutes().toString().padStart(2, "0");
      nodeDivA.appendChild(ptime);
      let ptext = document.createElement("p");
      ptext.innerHTML = overtocht.Departure.Location.toLowerCase();
      nodeDivA.appendChild(ptext);

      edgeDiv = document.createElement("div");
      edgeDiv.classList.add("timeline-edge");

      let nodeDivB = null;
      if (isFirst) {
        nodeDivA.classList.add("start");
      } else {
        nodeDivA.classList.add("departure");
      }

      nodeDivB = document.createElement("div");
      nodeDivB.classList.add("timeline-node");
      ptime = document.createElement("p");
      ptext = document.createElement("p");
      if (overtocht.Arrival == null) {
        if (overtocht.Departure.Location == "INGEN") {
          ptext.innerHTML = "ELST".toLowerCase();
        } else {
          ptext.innerHTML = "INGEN".toLowerCase();
        }
        date = new Date(0);
        date.setSeconds(overtocht.Departure.epochSeconds + 150);
        ptime.innerHTML =
          date.getHours().toString().padStart(2, "0") +
          ":" +
          date.getMinutes().toString().padStart(2, "0");
      } else {
        ptext.innerHTML = overtocht.Arrival.Location.toLowerCase();
        date = new Date(0);
        date.setSeconds(overtocht.Arrival.epochSeconds);
        ptime.innerHTML =
          date.getHours().toString().padStart(2, "0") +
          ":" +
          date.getMinutes().toString().padStart(2, "0");
      }
      nodeDivB.appendChild(ptime);
      nodeDivB.appendChild(ptext);
      if (overtocht.Arrival == null) {
        nodeDivB.classList.add("end");
        edgeDiv.classList.add("underway");
      } else {
        nodeDivB.classList.add("arrival");
        edgeDiv.classList.add("travel");
      }

      timelinewrapper.appendChild(nodeDivA);
      timelinewrapper.appendChild(edgeDiv);
      timelinewrapper.appendChild(nodeDivB);
      if (!isLast) {
        let edgeDivB = document.createElement("div");
        edgeDivB.classList.add("timeline-edge");
        edgeDivB.classList.add("stay");
        let edgeP = document.createElement("p");
        edgeP.innerHTML = overtocht.Arrival.Location.toLowerCase();
        edgeDivB.appendChild(edgeP);
        timelinewrapper.appendChild(edgeDivB);
      }
    });

    console.log(overtochten);
  });
}

loadOvertochten();
