import Login from "./login.js";
import { createModal } from "./modal.js";
const service = new Login();

const navigationRight = document.querySelector("#nav-right");
const navigationLeft = document.querySelector("#nav-left");

const loginButton = document.querySelector("#login-button");
const logoutbutton = document.querySelector("#logout-button");
const roosterButton = document.querySelector("#rooster-button");
const drukteButton = document.querySelector("#drukte-button");
function reloadNav() {
  if (service.isLoggedIn()) {
    loginButton.classList.add("hidden");
    logoutbutton.classList.remove("hidden");
    roosterButton.classList.remove("hidden");
    drukteButton.classList.remove("hidden");
    roosterButton.setAttribute("href", service.getRole() + "/rooster/");
    drukteButton.setAttribute("href", service.getRole() + "/drukte/");
  } else {
    loginButton.classList.remove("hidden");
    logoutbutton.classList.add("hidden");
    roosterButton.classList.add("hidden");
    roosterButton.removeAttribute("href");
    drukteButton.classList.add("hidden");
  }
}

logoutbutton.addEventListener("click", (e) => {
  e.preventDefault();
  service.logout();
  reloadNav();
});

loginButton.addEventListener("click", (e) => {
  e.preventDefault();
  const modal = createModal("login-modal");
  modal.querySelector("#login-form").addEventListener("reset", (b) => {
    reloadNav();
  });
});

setInterval(reloadNav, 500);
