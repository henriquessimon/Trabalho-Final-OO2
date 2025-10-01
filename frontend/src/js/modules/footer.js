export class Footer {
    constructor() {
        this.template = `
            <footer>
                <div class="borderDiv"><span>s</span></div>
                <div class="footerContainer">
                    <div class="footerItem">IF</div>
                    <div class="footerItem"><h6>Luis Henrique Soares Simon</h6></div>
                    <div class="footerItem">Instagram</div>
                </div>
            </footer>
        `
    }

    render() {
        const footer = document.createElement("div");
        footer.innerHTML = this.template;
        return footer.firstElementChild;
    }
}