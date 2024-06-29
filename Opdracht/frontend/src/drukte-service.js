export default class DrukteService {
  setDrukte(num) {
    let fetchoptions = {
      method: "POST",
      body: JSON.stringify({ drukte: num }),
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + window.localStorage.getItem("loginToken"),
      },
    };
    return Promise.resolve(
      fetch("/api/drukte/add", fetchoptions).then((response) => {
        return response.ok;
      })
    );
  }

  getDrukte() {
    let fetchoptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + window.localStorage.getItem("loginToken"),
      },
    };
    return Promise.resolve(
      fetch("/api/drukte", fetchoptions).then((response) => {
        if (response.ok) return response.json();
        else return null;
      })
    );
  }
}
