export default class OvertochtenService {
  getOvertochten() {
    let fetchoptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + window.localStorage.getItem("loginToken"),
      },
    };

    let url = "/api/crossings?limit=4";

    return Promise.resolve(
      fetch(url, fetchoptions)
        .then(function (response) {
          if (response.ok) {
            return response.json();
          } else {
            return "fail";
          }
        })
        .catch(function (error) {
          console.error(error);
          return "fail";
        })
    );
  }
}
