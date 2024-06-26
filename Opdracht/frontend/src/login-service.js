export default class LoginService {
  isLoggedIn() {
    return (
      window.localStorage.getItem("loginToken") != null &&
      window.localStorage.getItem("loginToken")
    );
  }

  login(user, password) {
    let body = { user, password };
    let jsonBody = JSON.stringify(body);

    let fetchoptions = {
      method: "POST",
      body: jsonBody,
      headers: {
        "Content-Type": "application/json",
      },
    };
    return Promise.resolve(
      fetch("/api/user/login", fetchoptions)
        .then(function (response) {
          if (response.ok) return response.json();
          else throw "Wrong username and/or password";
        })
        .then(function (result) {
          window.localStorage.setItem("user", user);
          window.localStorage.setItem("loginToken", result.token);
          return result.token;
        })
        .catch((error) => console.error(error))
    );
  }

  getUser() {
    let token = window.localStorage.getItem("loginToken");
    if (!token) return Promise.resolve(false); // no need to check an empty/null token

    let fetchoptions = {
      method: "GET",
    };
    return Promise.resolve(
      fetch("/api/user/validate?token=" + token, fetchoptions)
        .then(function (response) {
          if (response.ok) return response.json();
        })
        .then(function (result) {
          if (result.isValid) {
            return true;
          } else {
            window.localStorage.removeItem("loginToken"); // client-side logout
            return false;
          }
        })
        .catch(function (error) {
          console.error(error);
          return false;
        })
    );
  }

  logout() {
    let token = window.localStorage.getItem("loginToken");
    if (!this.isLoggedIn() || !token) return Promise.resolve(false); // no need to logout when not logged in
    let jsonBody = JSON.stringify({ token: token });

    let fetchoptions = {
      method: "POST",
      body: jsonBody,
      headers: {
        "Content-Type": "application/json",
      },
    };
    return Promise.resolve(
      fetch("/api/user/logout", fetchoptions)
        .then(function (response) {
          if (response.ok) return response.json();
          else console.error("Error while logging out");
        })
        .then((result) => window.localStorage.removeItem("loginToken"))
        .catch((error) => console.error(error))
    );
  }
}
