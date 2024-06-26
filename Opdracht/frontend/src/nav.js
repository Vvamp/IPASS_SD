import Login from "./login.js";
import { createModal } from "./modal.js";
const service = new Login();

const navigationRight = document.querySelector("#nav-right");
const navigationLeft = document.querySelector("#nav-left");

function reloadNav() {
  let loginButton = document.querySelector("#login-button");
  let logoutbutton = document.querySelector("#logout-button");
  if (service.isLoggedIn()) {
    loginButton.style.display = "none";
    logoutbutton.style.display = "block";
  } else {
    loginButton.style.display = "block";
    logoutbutton.style.display = "none";
  }
}

navigationRight
  .querySelector("#logout-button")
  .addEventListener("click", (e) => {
    e.preventDefault();
    service.logout();
    reloadNav();
  });

navigationRight
  .querySelector("#login-button")
  .addEventListener("click", (e) => {
    e.preventDefault();
    const modal = createModal("login-modal");
    modal.querySelector("#login-form").addEventListener("reset", (b) => {
      reloadNav();
    });
  });

setInterval(reloadNav, 500);
