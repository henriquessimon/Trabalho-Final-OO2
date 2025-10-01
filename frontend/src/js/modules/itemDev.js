export class ItemDev {
    constructor(dev) {
        this.dev = dev;
        this.template = `
            <div class="ItemContent" id="${this.dev.id}">
                <div>
                    <h4 class="nameGenPlat">${this.dev.name}</h4>
                </div>
                <div> 
                    <button class="excludeButton">Excluir</button>
                </div>
                <div> 
                    <button class="alterButton">Alterar</button>
                </div>
            </div>
        `;
    }

    render() {
        const container = document.createElement('div');
        container.innerHTML = this.template;

        const element = container.firstElementChild;
        const btnExclude = element.querySelector('.excludeButton');
        const btnAlter = element.querySelector('.alterButton');

        btnExclude.addEventListener('click', async () => {
            const confirmDelete = confirm(
                `Deseja realmente excluir a desenvolvedora "${this.dev.name}"?\n` +
                `(Todos os jogos associados serão excluídos!)`
            );
            if (!confirmDelete) return;

            try {
                const response = await fetch(`http://localhost:8080/dev/${this.dev.id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    element.remove();
                    alert('Desenvolvedora excluída com sucesso!');
                } else {
                    alert('Erro ao excluir a desenvolvedora.');
                }
            } catch (error) {
                alert('Erro na requisição de exclusão.');
                console.error(error);
            }
        });

        // Botão de alteração
        btnAlter.addEventListener('click', () => {
            const modal = document.getElementById('editModal');
            document.getElementById('entityId').value = this.dev.id;
            document.getElementById('entityName').value = this.dev.name;
            document.getElementById('entityType').textContent = 'Desenvolvedora';
            document.getElementById('editForm').dataset.entity = 'dev';
            modal.style.display = 'flex';
        });

        return element;
    }
}