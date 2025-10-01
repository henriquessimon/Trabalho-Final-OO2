export class createPlatForm {
    constructor() {
        this.template = `
            <form id="platFormRegister">
                <div class="fieldRegister">
                    <label>Nome da Plataforma</label>
                    <input type="text" placeholder="Nome..." name="platName" class="inputText" required/>
                </div>
                <button type="submit" class="buttonRegister">Registrar Plataforma</button>
            </form>
        `;
    }

    render() {
        const $form = $(this.template);

        $form.on('submit', function(e) {
            e.preventDefault();

            const platName = $form.find('[name="platName"]').val().trim();
            const payload = { name: platName };

            $.ajax({
                url: `http://localhost:8080/platform/getOne/${encodeURIComponent(platName)}`,
                method: "GET",
                success: function() {
                    alert("Plataforma já existe");
                },
                error: function(xhr) {
                    if (xhr.status === 404) {
                        $.ajax({
                            url: "http://localhost:8080/platform",
                            method: "POST",
                            contentType: "application/json",
                            data: JSON.stringify(payload),
                            success: function() {
                                alert("Plataforma cadastrada com sucesso!");
                                $form[0].reset();
                            },
                            error: function(xhr) {
                                console.error("Erro ao cadastrar plataforma:", xhr.responseText);
                                alert("Erro ao cadastrar plataforma.");
                            }
                        });
                    } else {
                        alert("Erro ao verificar plataforma.");
                    }
                }
            });
        });

        return $form.get(0);
    }
}