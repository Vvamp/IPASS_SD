export default class RoosterService {
  getRooster(weekNr) {
    let fetchoptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization:
          "Bearer " +
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCYWFzIiwiZXhwIjoxNzE5NTkwNTQwLCJyb2xlIjoiYm9zcyIsImlhdCI6MTcxOTU4ODc0MH0.d2pTpJcn8rpjf4jYi7gTCQLS3OQh8zFftKW1KZ6rrl0",
      },
    };
    return Promise.resolve(
      fetch("/api/schedules?weeknr=" + weekNr, fetchoptions)
        .then(function (response) {
          if (response.ok) {
            console.log("Response ok:");
            console.log(response);
            // console.log(response.json());
            return response.json();
          } else {
            console.log("response not ok:");
            console.log(response);
          }
        })
        .catch(function (error) {
          console.error(error);
          return false;
        })
    );
  }
}
