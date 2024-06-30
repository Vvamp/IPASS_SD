import Map from "ol/Map.js";
import OSM from "ol/source/OSM.js";
import TileLayer from "ol/layer/Tile.js";
import View from "ol/View.js";
import Feature from "ol/Feature.js";
import Point from "ol/geom/Point.js";
import Style from "ol/style/Style";
import Icon from "ol/style/Icon.js";
import VectorLayer from "ol/layer/Vector.js";
import VectorSource from "ol/source/Vector.js";
import { fromLonLat } from "ol/proj";

const CenterCoords = fromLonLat([5.496378, 51.979966]);

const ship = new Feature({
  geometry: new Point(CenterCoords),
  name: "Geldersweert",
});

ship.setStyle(
  new Style({
    image: new Icon({
      src: "/ship.png",
      scale: 0.11,
      anchor: [0.6, 0.5],
      anchorXUnits: "fraction",
      anchorYUnits: "fraction",
    }),
  })
);

const source = new VectorSource({
  features: [ship],
});

const vectorLayer = new VectorLayer({
  source: source,
});

const map = new Map({
  layers: [
    new TileLayer({
      source: new OSM(),
    }),
    vectorLayer,
  ],
  target: "map",
  view: new View({
    center: CenterCoords,
    zoom: 17,
  }),
  // interactions: [],
  // controls: [],
});

function updateShipLocation(location) {
  try {
    var newShipCoords = fromLonLat([location[1], location[0]]);
    ship.getGeometry().setCoordinates(newShipCoords);
    // map.getView().setCenter(newShipCoords);
    map.updateSize();
  } catch (err) {
    console.warn("No ship data (yet) available");
  }
}

function resetLocation() {
  updateShipLocation([51.979527, 5.496295]);
}

function loadShipLocation() {
  let url = "/api/ais?limit=1";
  fetch(url)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      }

      console.err("[LoadShipLocation] Response not ok");
    })
    .then(function (result) {
      let res = result[0];
      updateShipLocation([
        res.Message.PositionReport.Latitude,
        res.Message.PositionReport.Longitude,
      ]);
    })
    .catch((err) => {
      //   throw err;
      console.error("Failed to update ship location: " + err);
    });
}

function init() {
  resetLocation();
  setInterval(loadShipLocation, 5000);
}
if (window.addEventListener) {
  // W3C standard
  window.addEventListener("load", init, false);
}

window.onresize = () => {
  setTimeout(() => {
    map.updateSize();
  }, 500);
};
