export default class StatsService {
  getStats() {
    let fetchoptions = {
      method: "GET",
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
}
