export class createGameForm {
    constructor() {
        this.template = `
            <form id="gameForm">
                <div class="fieldRegister">
                    <label>Nome</label>
                    <input type="text" placeholder="Nome..." name="gameName" class="inputText" required />
                </div>
                <div class="fieldRegister">
                    <label>Data de lançamento</label>
                    <input type="date" name="gameDate" class="inputText" required />
                </div>
                <div class="fieldRegister">
                    <label>Link do trailer</label>
                    <input type="text" placeholder="trailer..." name="gameTrailer" class="inputText" required/>
                </div>
                <div class="fieldRegister">
                    <label>Preço</label>
                    <input type="number" step="0.01" placeholder="Preço..." name="gamePrice" class="inputText" required />
                </div>
                <div class="fieldRegister">
                    <label>Descrição</label>
                    <textarea rows="4" cols="50" name="gameDesc" required>Descrição</textarea>
                </div>
                <div class="fieldRegister">
                    <label>Capa do Jogo</label>
                    <input type="text" placeholder="Imagem Link" name="gameImgUrl" class="inputText" required/>
                </div>
                <div class="fieldRegister">
                    <label>Desenvolvedora</label>
                    <select name="gameDev" class="gameSelects" required>
                        <option value=""> Selecione a desenvolvedora</option>
                    </select>
                </div>
                <div class="fieldRegister">
                    <label>Gênero</label>
                    <select name="gameGenre" class="gameSelects" multiple required>
                        <option value=""> Selecione o Gênero</option>
                    </select>
                </div>
                <div class="fieldRegister">
                    <label>Plataforma</label>
                    <select name="gamePlatform" class="gameSelects" multiple required>
                        <option value="">Selecione a plataforma</option>
                    </select>
                </div>
                <button type="submit" class="buttonRegister">Cadastrar Game</button>
            </form>
        `;
    }

    getGenres($container) {
        $.ajax({
            url: "http://localhost:8080/genre/getAll",
            method: "GET",
            success: function(genres) {
                const $genreSelect = $container.find('[name="gameGenre"]');
                genres.forEach(genre => {
                    $genreSelect.append(`<option value="${genre.id}">${genre.name}</option>`);
                });
            },
            error: function() {
                console.error("Erro ao carregar gêneros");
            }
        });
    }

    getPlatforms($container) {
        $.ajax({
            url: "http://localhost:8080/platform/getAll",
            method: "GET",
            success: function(platforms) {
                const $platformSelect = $container.find('[name="gamePlatform"]');
                platforms.forEach(platform => {
                    $platformSelect.append(`<option value="${platform.id}">${platform.name}</option>`);
                });
            },
            error: function() {
                console.error("Erro ao carregar plataformas");
            }
        });
    }

    getDev($container) {
        $.ajax({
            url: "http://localhost:8080/dev/getAll",
            method: "GET",
            success: function(devs) {
                const $devSelect = $container.find('[name="gameDev"]');
                devs.forEach(dev => {
                    $devSelect.append(`<option value="${dev.id}">${dev.name}</option>`);
                });
            },
            error: function() {
                console.error("Erro ao carregar desenvolvedores");
            }
        });
    }

    render() {
        const $container = $(this.template);
        this.getGenres($container);
        this.getPlatforms($container);
        this.getDev($container);

        const $form = $container;

        $form.on('submit', (event) => {
            event.preventDefault();

            const getSelectedIds = (name) =>
                $form.find(`[name="${name}"]`)
                    .val() // retorna array de strings
                    ?.map(id => ({ id: parseInt(id) })) || [];

            const gameData = {
                name: $form.find('[name="gameName"]').val().trim(),
                releaseYear: new Date($form.find('[name="gameDate"]').val()).getFullYear(),
                dev: { id: parseInt($form.find('[name="gameDev"]').val()) },
                trailerUrl: $form.find('[name="gameTrailer"]').val().trim(),
                price: parseFloat($form.find('[name="gamePrice"]').val()),
                coverImage: $form.find('[name="gameImgUrl"]').val().trim(),
                description: $form.find('[name="gameDesc"]').val().trim(),
                genres: getSelectedIds("gameGenre"),
                platforms: getSelectedIds("gamePlatform"),
                createdAt: new Date().toISOString().slice(0, 19) // "2025-06-20T18:55:00"
            };


            console.log(gameData.name);

            $.ajax({
                url: `http://localhost:8080/game/getOne/${encodeURIComponent(gameData.name)}`,
                method: "GET",
                success: function(data) {
                    alert("Jogo já cadastrado");
                },
                error: function(xhr) {
                    if (xhr.status === 404) {
                        // Jogo não encontrado, pode cadastrar
                        $.ajax({
                            url: "http://localhost:8080/game",
                            method: "POST",
                            contentType: "application/json",
                            data: JSON.stringify(gameData),
                            success: function() {
                                alert("Jogo cadastrado com sucesso!");
                                $form[0].reset();
                            },
                            error: function(xhr) {
                                console.error("Erro ao cadastrar jogo:", xhr.responseText);
                                alert("Erro ao cadastrar jogo.");
                            }
                        });
                    } else {
                        console.error("Erro ao buscar jogo:", xhr.status, xhr.responseText);
                        alert("Erro ao verificar jogo.");
                    }
                }
            });

        });
        // Após append dos selects (ou seja, ainda dentro do render)
        $container.find('[name="gameGenre"]').select2({
            placeholder: "Selecione o Gênero",
            allowClear: true,
            width: '100%'
        });

        $container.find('[name="gamePlatform"]').select2({
            placeholder: "Selecione a Plataforma",
            allowClear: true,
            width: '100%',
        });

        return $container.get(0);
    }
}
