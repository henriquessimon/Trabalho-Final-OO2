export class ItemPlatform {
    constructor(platform) {
        this.platform = platform;
        this.template = `
            <div class="ItemContent" id="${this.platform.id}">
                <div>
                    <h4 class="nameGenPlat">${this.platform.name}</h4>
                </div>
                <div> 
                    <button class="excludeButton">Excluir</button>
                </div>
            </div>
        `;
    }

    render() {
        const container = document.createElement('div');
        container.innerHTML = this.template;

        const element = container.firstElementChild;
        const btnExclude = element.querySelector('.excludeButton');

        btnExclude.addEventListener('click', async () => {
            if (confirm(`Deseja realmente excluir a plataforma "${this.platform.name}"?`)) {
                try {
                    const response = await fetch(`http://localhost:8080/platform/${this.platform.id}`, {
                        method: 'DELETE'
                    });

                    if (response.ok) {
                        element.remove();  // Remove da tela
                        alert('Plataforma excluída com sucesso!');
                    } else {
                        alert('Erro ao excluir a plataforma.');
                    }
                } catch (error) {
                    alert('Erro na requisição de exclusão.');
                    console.error(error);
                }
            }
        });

        return element;
    }
}
