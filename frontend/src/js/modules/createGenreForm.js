export class createGenreForm {
    constructor() {
        this.template = `
            <form id="genreForm">
                <div class="fieldRegister">
                    <label>Nome do Gênero</label>
                    <input type="text" placeholder="Nome..." name="genreName" class="inputText" required/>
                </div>
                <button type="submit" class="buttonRegister">Cadastrar Gênero</button>
            </form>
        `;
    }

    render() {
        const $form = $(this.template);

        $form.on('submit', function(event) {
            event.preventDefault();

            const genreName = $form.find('[name="genreName"]').val().trim();
            console.log(genreName);

            const genreData = {
                name: genreName
            };

            $.ajax({
                url: `http://localhost:8080/genre/getOne/${encodeURIComponent(genreName)}`,
                method: "GET",
                success: function(data) {
                    alert("Gênero já existe!");
                },
                error: function(xhr) {
                    if (xhr.status === 404) {
                        $.ajax({
                            url: "http://localhost:8080/genre",
                            method: "POST",
                            contentType: "application/json",
                            data: JSON.stringify(genreData),
                            success: function() {
                                alert("Gênero cadastrado com sucesso!");
                                $form[0].reset();
                            },
                            error: function(xhr) {
                                console.error("Erro ao cadastrar gênero:", xhr.responseText);
                                alert("Erro ao cadastrar gênero.");
                            }
                        });
                    } else {
                        console.error("Erro ao verificar gênero:", xhr.status, xhr.responseText);
                        alert("Erro ao verificar gênero.");
                    }
                }
            });
        });

        return $form.get(0);
    }
}
