export class createDevForm {
    constructor() {
        this.template = `
            <form id="devForm">
                <div class="fieldRegister">
                    <label>Nome da Desenvolvedora</label>
                    <input type="text" placeholder="Nome..." name="devName" class="inputText" required/>
                </div>
                <button type="submit" class="buttonRegister">Cadastrar dev</button>
            </form>
        `;
    }

    render() {
        const $form = $(this.template);

        $form.on('submit', function(event) {
            event.preventDefault();

            const devName = $form.find('[name="devName"]').val().trim();
            console.log(devName);

            const devData = {
                name: devName
            };

            $.ajax({
                url: `http://localhost:8080/dev/getOne/${encodeURIComponent(devName)}`,
                method: "GET",
                success: function(data) {
                    alert("Dev já existe!");
                },
                error: function(xhr) {
                    if (xhr.status === 404) {
                        // Dev não encontrado, pode cadastrar
                        $.ajax({
                            url: "http://localhost:8080/dev",
                            method: "POST",
                            contentType: "application/json",
                            data: JSON.stringify(devData),
                            success: function() {
                                alert("Dev cadastrado com sucesso!");
                                $form[0].reset();
                            },
                            error: function(xhr) {
                                console.error("Erro ao cadastrar dev:", xhr.responseText);
                                alert("Erro ao cadastrar dev.");
                            }
                        });
                    } else {
                        console.error("Erro ao verificar dev:", xhr.status, xhr.responseText);
                        alert("Erro ao verificar dev.");
                    }
                }
            });
        });

        return $form.get(0);
    }
}
