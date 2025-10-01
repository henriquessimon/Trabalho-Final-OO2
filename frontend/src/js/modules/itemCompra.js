export class ItemCompra {
    constructor(game) {
        this.game = game;

        this.template = `
            <div class="itemList">
                <div class="gameCompraItem" id="gameCompraName">
                    <h5>${game.name}</h5>
                </div>
                <div class="gameCompraItem">
                    <span>Remover do Carrinho</span>
                </div>
                <div class="gameCompraItem" id="gameValueCompra">
                    <h5>R$${game.price}</h5>
                </div>
            </div>
        `;
    }

    render() {
        const itemCompra = document.createElement('div');
        itemCompra.innerHTML = this.template;

        const removerBtn = itemCompra.querySelector('span');
        removerBtn.style.cursor = 'pointer';
        removerBtn.addEventListener('click', async () => {
            try {
                const response = await fetch(`/purchase/remove?gameId=${this.game.id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    itemCompra.remove(); // remove do DOM
                    alert('Jogo removido do carrinho.');
                } else {
                    alert('Erro ao remover jogo.');
                }
            } catch (error) {
                console.error(error);
                alert('Erro de rede.');
            }
        });

        return itemCompra.firstElementChild;
    }

}