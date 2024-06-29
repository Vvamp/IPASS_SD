export default class RoosterService {
  getRooster(weekNr, getAll) {
    let fetchoptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + window.localStorage.getItem("loginToken"),
      },
    };

    let url = "/api/schedules";
    if (!getAll) {
      url = "/api/schedules/" + window.localStorage.getItem("username");
    }
    return Promise.resolve(
      fetch(url + "?weeknr=" + weekNr, fetchoptions)
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
  addRoosterItem(data) {
    let fetchoptions = {
      method: "POST",
      body: JSON.stringify(data),
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + window.localStorage.getItem("loginToken"),
      },
    };
    return Promise.resolve(
      fetch("/api/schedules/add", fetchoptions).then((response) => {
        return response.ok;
      })
    );
  }

  removeRoosterItem(uuid) {
    let fetchoptions = {
      method: "DELETE",
      body: JSON.stringify({ uuid: uuid }),
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + window.localStorage.getItem("loginToken"),
      },
    };
    return Promise.resolve(
      fetch("/api/schedules/delete", fetchoptions).then((response) => {
        return response.ok;
      })
    );
  }
}
