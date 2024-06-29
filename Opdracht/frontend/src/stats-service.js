export default class StatsService {
  getStats() {
    let fetchoptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    return Promise.resolve(
      fetch("/api/stats", fetchoptions)
        .then(function (response) {
          if (response.ok) return response.json();
        })
        .catch(function (error) {
          console.error(error);
          return false;
        })
    );
  }

  getEta() {
    let fetchoptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    return Promise.resolve(
      fetch("/api/crossings/eta", fetchoptions)
        .then(function (response) {
          if (response.ok) return response.json();
        })
        .catch(function (error) {
          console.error(error);
          return false;
        })
    );
  }

  getStatus() {
    let fetchoptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    return Promise.resolve(
      fetch("/api/crossings/latest", fetchoptions)
        .then(function (response) {
          if (response.ok) return response.json();
        })
        .catch(function (error) {
          console.error(error);
          return false;
        })
    );
  }
}
