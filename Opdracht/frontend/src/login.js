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
        this.refresh();
        return true;
      } else {
        this.refresh();
        return false;
      }
    });
  }

  logout() {
    service.logout().then(() => {
      this.refresh();
      window.location.href = "/";
    });
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
