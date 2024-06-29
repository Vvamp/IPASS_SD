import DrukteService from "./drukte-service.js";
const ds = new DrukteService();

function onRadioPaddingClick(e) {
  let padding = e.currentTarget;
  let radio = padding.querySelector('input[type="radio"]');
  radio.checked = true;
}

const wrapper = document.querySelector(".drukte-radio-wrapper");
wrapper.querySelectorAll("li").forEach((li) => {
  li.addEventListener("click", onRadioPaddingClick);
});

const form = document.querySelector("#drukte-form");
const submit = form.querySelector(".bluebutton");
submit.addEventListener("click", (e) => {
  e.preventDefault();
  let radio = wrapper.querySelector('input[type="radio"]:checked');
  ds.setDrukte(radio.value).then((ok) => {
    if (ok) {
      //   window.location.href = "/drukte";
      alert("Success");
    } else {
      alert("Er is iets misgegaan bij het opslaan van de drukte.");
    }
  });
});
