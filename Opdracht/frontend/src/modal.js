import Login from "./login.js";

const ls = new Login();
let isModalActive = false;
function dismissModal(event, id) {
  event.preventDefault();
  document.querySelectorAll('[data-modalid="' + id + '"]').forEach((el) => {
    el.remove();
  });
  isModalActive = false;
}

export function createModal(id) {
  if (isModalActive) return;
  // Create base modal
  const modalItemTemplate = document.querySelector("#modal-template");
  const modalItem = modalItemTemplate.content.cloneNode(true);
  const modalElement = modalItem.querySelector(".modal");

  modalItem.id = id;
  modalElement.dataset.modalid = id;
  modalElement.id = id;
  let modalClose = modalItem.querySelector(".modal-close");

  // Create backdrop
  let bdElement = createBackdrop();
  bdElement.dataset.modalid = id;

  // Close modal and backdrop when clicking x
  modalClose.addEventListener("click", (e) => dismissModal(e, id));
  bdElement.addEventListener("click", (e) => dismissModal(e, id));

  // Add content to modal froma  template
  const templateId = "#" + id + "-template";
  const modalContentTemplate = modalItem.querySelector(templateId);
  const modalContent = modalContentTemplate.content.cloneNode(true);
  modalItem.querySelector(".modal-content").appendChild(modalContent);

  document.body.appendChild(modalItem);
  isModalActive = true;
  if (id === "login-modal") {
    const loginForm = document.querySelector("#login-form");
    const submitButton = loginForm.querySelector("button[type='submit']");
    const errorElement = loginForm.querySelector(".login-form-indicator");
    submitButton.addEventListener("click", (e) => {
      e.preventDefault();
      const formData = new FormData(loginForm);
      const user = formData.get("username");
      const password = formData.get("password");

      if (ls.login(user, password)) {
        loginForm.reset();
        dismissModal(e, id);
        console.log("Logged in");
      } else {
        errorElement.textContent = "Wrong username and/or password";
        errorElement.style.display = "block";
      }
    });
  }
  return modalElement;
}

function createBackdrop() {
  const backdropItemTemplate = document.querySelector("#backdrop-template");
  const backdropItem = backdropItemTemplate.content.cloneNode(true);
  const bdElement = backdropItem.querySelector(".backdrop");
  document.body.appendChild(backdropItem);

  return bdElement;
}
