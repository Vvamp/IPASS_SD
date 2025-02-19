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
