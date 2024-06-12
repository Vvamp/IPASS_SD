  var defaultCenterCoords = [5.496378, 51.979966];
  var centerOLCoords = ol.proj.fromLonLat(defaultCenterCoords);

  var shipFeature = new ol.Feature({
    geometry: new ol.geom.Point(0, 0),
  });

  shipFeature.setStyle(
    new ol.style.Style({
      image: new ol.style.Icon({
        src: "/ship.png",
        scale: 0.11,
        anchor: [0.6, 0.5],
        anchorXUnits: "fraction",
        anchorYUnits: "fraction",
      }),
    })
  );

  var vectorSource = new ol.source.Vector({
    features: [shipFeature],
  });

  var vectorLayer = new ol.layer.Vector({
    source: vectorSource,
  });

  var map = new ol.Map({
    target: "map",
    layers: [
      new ol.layer.Tile({
        source: new ol.source.OSM(),
      }),
      vectorLayer,
    ],
    view: new ol.View({
      center: centerOLCoords,
      zoom: 17,
    }),
  });

function updateShipLocation(location) {
  try {
    var newShipCoords = ol.proj.fromLonLat([location[1], location[0]]);
    shipFeature.getGeometry().setCoordinates(newShipCoords);
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
    }
    )
    .catch((err) => {
      //   throw err;
      console.log("Failed to update ship location: " + err);
    });
}


function init() {
  resetLocation();
  setInterval(loadShipLocation, 5000);
}
if (window.addEventListener) // W3C standard
{
  window.addEventListener('load', init, false);
}

