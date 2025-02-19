class SiteFooter extends HTMLElement {
    connectedCallback() {
      this.innerHTML = `
        <footer>
          <p>
            &copy; 2024-${new Date().getFullYear()} Vincent van Setten
          </p>
        </footer>
      `;
    }
  }
  customElements.define("site-footer", SiteFooter);


// const eventSource = new EventSource("/api/updates");

// eventSource.onmessage = (event) => {
//   console.log("New data:", event.data);
//   document.getElementById("updates").textContent = "Last update: " + event.data;
// };

// eventSource.onerror = () => {
//   console.error("SSE connection lost.");
//   eventSource.close();
// };
