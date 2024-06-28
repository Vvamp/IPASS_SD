import LoginService from "./login-service.js";

let service = new LoginService();

export default class Login {
  refresh() {
    service.getUser().then();
  }

  isLoggedIn() {
    return service.isLoggedIn();
    // return true;
  }

  login(username, password) {
    return service.login(username, password).then((token) => {
      if (token) {
        console.log("true");
        this.refresh();
        return true;
      } else {
        this.refresh();
        console.log("false");
        return false;
      }
    });
  }

  logout() {
    service.logout().then(this.refresh());
  }

  getRole() {
    return service.getRole();
  }
  get username() {
    return service.getUsername();
  }
}

let login = new Login();
window.login = login;
