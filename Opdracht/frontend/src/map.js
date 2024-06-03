// Coordinates for Utrecht (Longitude, Latitude)
var defaultCenterCoords = [5.496378, 51.979966];

// Convert from normal coordinates to OpenLayers' projection
var centerOLCoords = ol.proj.fromLonLat(defaultCenterCoords);

// Create a feature for the ship
var shipFeature = new ol.Feature({
  geometry: new ol.geom.Point(0, 0),
});

// Create a style for the ship feature
shipFeature.setStyle(
  new ol.style.Style({
    image: new ol.style.Icon({
      src: "/ship.png", // URL of the ship icon
      scale: 0.1, // Adjust the scale of the icon as needed
      anchor: [0.5, 0.5], // Center the anchor point
      anchorXUnits: "fraction",
      anchorYUnits: "fraction",
    }),
  })
);

// Create a vector source and add the ship feature to it
var vectorSource = new ol.source.Vector({
  features: [shipFeature],
});

// Create a vector layer with the vector source
var vectorLayer = new ol.layer.Vector({
  source: vectorSource,
});

// Create a map
var map = new ol.Map({
  target: "map", // The ID of the div element
  layers: [
    new ol.layer.Tile({
      source: new ol.source.OSM(), // OpenStreetMap as the source
    }),
    vectorLayer,
  ],
  view: new ol.View({
    center: centerOLCoords,
    zoom: 18, // Adjust zoom level as needed
  }),
});

function updateShipLocation(location) {
  try {
    var newShipCoords = ol.proj.fromLonLat([location[1], location[0]]);
    shipFeature.getGeometry().setCoordinates(newShipCoords);
  } catch (err) {
    console.warn("No ship data (yet) available");
  }
  // Optionally, update the map view to center on the new ship location
  // map.getView().setCenter(newShipCoords);
}

function resetLocation() {
  updateShipLocation([51.98056333333333, 5.4971966666666665]);
  console.log("set starting location");
}

function loadShipLocation() {
  let url = "/api/ship";
  fetch(url)
    .then((res) => res.json())
    .then((out) =>
      updateShipLocation([
        out.Message.PositionReport.Latitude,
        out.Message.PositionReport.Longitude,
      ])
    )
    .catch((err) => {
      //   throw err;
      console.log("Failed to update ship location: " + err);
    });
}

window.onload = function () {
  resetLocation();
  setInterval(loadShipLocation, 5000);
};
