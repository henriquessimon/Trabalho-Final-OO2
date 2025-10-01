export class GameCard {
    constructor(game) {
        this.game = game;

        this.template = `
            <article class="cardGameContainer" data-game-id=${this.game.id}>
                <div class="imgContainer">
                    <img src="${this.game.coverImage}" />
                </div>
                <div class="infoMainContainer">
                    <div class="infoContainer">
                        <h6 class="gameName">${this.game.name}</h6>
                        <ul class="platCardGameList">
                            ${this.platformsList()}
                        </ul>
                    </div>
                </div>
                <div class="buttomContainer">
                    <button class="addCartButton">Comprar R$${this.game.price}</button>
                </div>
                <img src="http://localhost:8080/static/trash.png" class="trashImg" width="30" />
                <img src="http://localhost:8080/static/update.png" class="updateImg" width="30" />
            </article>
        `;
    }

    platformsList() {
        const platforms = this.game.platformsID || [];
        return platforms.map(p => `<li class="platCardGameListItem">${p.name}</li>`).join('');
    }

    render() {
        // Criar um elemento jQuery com o template
        const $gameCard = $(this.template);
        
        // Buscar botão pelo seletor da classe (id não é legal repetir em vários cards)
        const $addCartButton = $gameCard.find('.addCartButton');
        const $trashImg = $gameCard.find('.trashImg')
        const $updateImg = $gameCard.find('.updateImg')

        // Adicionar o evento usando jQuery
        $addCartButton.on('click', () => {
            const gameName = this.game.name;
            const gamePrice = Number(this.game.price);

            this.addCart({ name: gameName, price: gamePrice });

            setTimeout(() => {
                window.location.href = 'http://localhost:8080/comprar.html';
            }, 300);
        });

        

        $trashImg.on('click', () => {
            const gameId = this.game.id;
            const self = this;
            console.log("Trash clicado, gameId:", gameId); // DEBUG

            $('#deleteModal').fadeIn();

            $('#confirmDeleteBtn').off('click').on('click', () => {
                console.log("Confirmar delete, gameId:", gameId); // DEBUG
                console.log("Self:", self); // DEBUG

                $gameCard.remove();
                self.deleteGame(gameId);
                $('#deleteModal').fadeOut();
            });

            $('#cancelDeleteBtn').off('click').on('click', () => {
                $('#deleteModal').fadeOut();
            });
        });


        //VALIDA SE SELECTS DO UPDATE EXISTEM
        function fillSelectOptions($select, options) {
            $select.empty(); // limpa opções antigas
            options.forEach(opt => {
                $select.append(new Option(opt.name, opt.id, false, false));
            });
            $select.trigger('change'); // necessário para Select2 atualizar
        }

        // Carregue em algum lugar da sua aplicação assim:
        let allGenres = [];
        let allPlatforms = [];

        // Exemplo de carga via AJAX ao iniciar a página
        $.get("http://localhost:8080/genre/getAll", function(data) {
            allGenres = data;
        });

        $.get("http://localhost:8080/platform/getAll", function(data) {
            allPlatforms = data;
        });


        //UPDATE
        $updateImg.on('click', () => {
            const game = this.game;
            const $modal = $('#updateModal');

            // Inicializa Select2
            const $genreSelect = $('select[name="gameGenre"]');
            const $platformSelect = $('select[name="gamePlatform"]');

            $genreSelect.select2({ placeholder: 'Selecione o Gênero', width: '100%' });
            $platformSelect.select2({ placeholder: 'Selecione a plataforma', width: '100%' });

            // Preenche as opções completas no select
            fillSelectOptions($genreSelect, allGenres);
            fillSelectOptions($platformSelect, allPlatforms);

            // Preenche os valores do jogo
            $('input[name="gameName"]').val(game.name);
            $('input[name="gameTrailer"]').val(game.trailer);
            $('input[name="gamePrice"]').val(game.price);
            $('textarea[name="gameDesc"]').val(game.description);
            $('input[name="gameImgUrl"]').val(game.coverImage);

            $genreSelect.val(game.genresID.map(g => g.id.toString())).trigger('change');
            $platformSelect.val(game.platformsID.map(p => p.id.toString())).trigger('change');

            $modal.data('game-id', game.id).fadeIn();

            // Botões de ação
            $('#confirmUpdateBtn').off('click').on('click', (e) => {
                e.preventDefault();
                const self = this;

                const gameId = $modal.data('game-id');

                const updatedGame = {
                    name: $('input[name="gameName"]').val(),
                    price: parseFloat($('input[name="gamePrice"]').val()),
                    trailer: $('input[name="gameTrailer"]').val(),
                    description: $('textarea[name="gameDesc"]').val(),
                    coverImage: $('input[name="gameImgUrl"]').val(),
                    dev: { id: Number($('select[name="gameDev"]').val()) },
                    genresID: $genreSelect.val().map(id => ({ id: Number(id) })),
                    platformsID: $platformSelect.val().map(id => ({ id: Number(id) })),
                };

                self.updateGame(gameId, updatedGame);
                $modal.fadeOut();
            });

            $('#cancelUpdateBtn').off('click').on('click', () => {
                $modal.fadeOut();
            });
        });

        return $gameCard.get(0); // Retorna o elemento DOM nativo para inserir onde quiser
    }


    //ADD GAME NO CARRINHO
    addCart(game) {
        // Usar ajax do jQuery para POST
        $.ajax({
            url: "http://localhost:8080/purchase/add",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(game),
            success: function(response) {
                console.log('Resposta bruta do backend:', response);
                // Se response já vier em JSON, não precisa do parse
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error("Erro ao adicionar Game no carrinho: ", textStatus, errorThrown);
            }
        });
    }

    //DELETE GAME
    deleteGame(id) {
        $.ajax({
            url: `http://localhost:8080/game/delete/${encodeURIComponent(id)}`,
            type: 'DELETE',
            success: function() {
                alert("Jogo deletado com sucesso");
            },
            error: function(err) {
                console.log("Erro ao deletar jogo", err);
                alert("Erro ao deletar jogo");
            }
        })
    }

    //UPDATE GAME
    updateGame(gameId, game) {
        $.ajax({
            url: `http://localhost:8080/game/update/${encodeURIComponent(gameId)}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(game),
            success: function() {
                alert("Jogo Atualizado com sucesso");
                location.reload();
            },
            error: function(err) {
                console.log("Erro ao atualizado jogo", err);
                alert("Erro ao atualizado jogo");
            }
        })
    }
}