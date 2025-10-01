export class HistoryDevolve {
    constructor() {
        this.template = `
            <div class="itemDevolucao">
            </div>
        `;
    }

    render() {
        const wrapper = document.createElement('div');
        wrapper.innerHTML = this.template;

        const itemDevolucao = wrapper.querySelector('.itemDevolucao');

        $.ajax({
            url: "http://localhost:8080/gameReturn/getAll",
            method: "GET",
            success: function (gamesReturn) {
                gamesReturn.forEach(game => {
                    const listDevolucao = document.createElement('ul');
                    listDevolucao.classList.add('listDevolucao');

                    const NameLi = document.createElement('li');
                    const ReturnsLi = document.createElement('li');
                    const SoldQTDLi = document.createElement('li');
                    const taxaDevolucaoLi = document.createElement('li');

                    const returns = parseFloat(game.returns) || 0;
                    const sold = parseFloat(game.soldQuantity) || 1; // Evita divisão por zero

                    const taxaDevolucao = (returns / sold) * 100;

                    NameLi.textContent = `${game.name}`;
                    ReturnsLi.textContent = `Devoluções: ${game.returns}`;
                    SoldQTDLi.textContent = `Vendidos: ${game.soldQuantity}`;
                    taxaDevolucaoLi.textContent = `Taxa de devolução: ${taxaDevolucao}%`;

                    NameLi.classList.add('itemListGameReturn');
                    ReturnsLi.classList.add('itemListGameReturn');
                    SoldQTDLi.classList.add('itemListGameReturn');
                    taxaDevolucaoLi.classList.add('itemListGameReturn');

                    listDevolucao.appendChild(NameLi);
                    listDevolucao.appendChild(ReturnsLi);
                    listDevolucao.appendChild(SoldQTDLi);
                    listDevolucao.appendChild(taxaDevolucaoLi);

                    itemDevolucao.appendChild(listDevolucao);
                });
            }
        });

        return wrapper.firstElementChild;
    }
}