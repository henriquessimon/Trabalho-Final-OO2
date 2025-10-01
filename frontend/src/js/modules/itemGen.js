export class ItemGenre {
    constructor(genre) {
        this.genre = genre;
        this.template = `
            <div class="ItemContent" id="${this.genre.id}">
                <div>
                    <h4 class="nameGenPlat">${this.genre.name}</h4>
                </div>
                <div> 
                    <button type="button" class="excludeButton">Excluir</button>
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

        btnExclude.addEventListener('click', async () => {
            const confirmDelete = confirm(`Deseja realmente excluir o gênero "${this.genre.name}"?`);
            if (!confirmDelete) return;

            try {
                const response = await fetch(`http://localhost:8080/genre/${this.genre.id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    element.remove(); // remove do DOM
                    alert('Gênero excluído com sucesso!');
                } else {
                    alert('Erro ao excluir o gênero.');
                }
            } catch (error) {
                alert('Erro na requisição de exclusão.');
                console.error(error);
            }
        });

        const btnAlter = element.querySelector('.alterButton');
        btnAlter.textContent = 'Alterar';

        btnAlter.addEventListener('click', () => {
            document.getElementById('entityId').value = this.genre.id;
            document.getElementById('entityName').value = this.genre.name;
            document.getElementById('entityType').textContent = 'Gênero';
            document.getElementById('editModal').style.display = 'block';
            document.getElementById('editForm').dataset.entity = 'genre';
        });



        return element;
    }
}