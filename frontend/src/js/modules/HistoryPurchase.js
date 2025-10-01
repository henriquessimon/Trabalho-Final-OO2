export class HistoryPurchase {
    constructor() {
        this.template = `
            <div class="historyList">
                <div class="listContainer" id="listHistory"></div>
            </div>
        `;
    }

    render() {
        const historyPurchase = document.createElement('div');
        historyPurchase.innerHTML = this.template;

        const listContainer = historyPurchase.querySelector('#listHistory');

        listContainer.addEventListener('click', (event) => {
            if (event.target.classList.contains('devolvButton')) {
                const purchaseId = event.target.dataset.purchaseId;

                // Atualiza o botão do modal com o purchaseId
                const confirmButton = document.getElementById('devolvButtonModal');
                confirmButton.setAttribute('data-purchase-id', purchaseId);

                // Atualiza o texto do modal
                document.getElementById('meuModal').querySelector('p').textContent =
                    `Deseja devolver o pedido #${purchaseId}?`;

                // Abre o modal
                const modal = document.getElementById('meuModal');
                modal.style.display = 'block';

                // Busca os jogos do pedido via AJAX
                $.ajax({
                    url: `http://localhost:8080/purchase/${purchaseId}/games`,
                    method: 'GET',
                    success: (games) => {
                        const select = document.getElementById('GameDevolution');
                        // Limpa e adiciona opção "Todos"
                        select.innerHTML = '<option value="all">Todos</option>';

                        games.forEach(game => {
                            const option = document.createElement('option');
                            option.value = game.id;
                            option.textContent = game.name;
                            select.appendChild(option);
                        });
                    },
                    error: () => {
                        alert('Erro ao carregar os jogos do pedido.');
                    }
                });
            }
        });




        $.ajax({
            url: 'http://localhost:8080/purchase/history',
            method: 'GET',
            success: (data) => {
                listContainer.innerHTML = '';

                if (!data || data.length === 0) {
                    listContainer.innerHTML = '<p>Nenhum pedido encontrado.</p>';
                    return;
                }

                data.forEach(purchase => {
                    const gamesHTML = purchase.games.map(game => `
                        <div class="game">
                            <img src="${game.coverImage}" width="100" />
                            <p><strong>${game.name}</strong></p>
                            <p>R$ ${game.price.toFixed(2)}</p>
                        </div>
                    `).join('');



                    const purchaseDiv = document.createElement('div');
                    purchaseDiv.className = 'purchase';
                    purchaseDiv.innerHTML = `
                        <h3>Pedido #${purchase.purchaseId}</h3>
                        <p>Data: ${new Date(purchase.purchaseDate).toLocaleDateString()}</p>
                        <div class="games">${gamesHTML}</div>
                        <hr>
                        <div class="footerPurchase">
                            <p>Total: R$ ${purchase.totalValue.toFixed(2)}</p>
                            <button class="devolvButton" data-purchase-id="${purchase.purchaseId}">Devolver Pedido</button>
                        </div>
                    `;

                    listContainer.appendChild(purchaseDiv);
                });
            },
            error: (err) => {
                console.error('Erro ao buscar histórico:', err);
                listContainer.innerHTML = '<p>Erro ao carregar histórico.</p>';
            }
        });

        return historyPurchase.firstElementChild;
    }
}