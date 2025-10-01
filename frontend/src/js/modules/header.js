import { navFor } from "/js/modules/router.js";

export class Header {
    constructor() {
        this.template = `
            <header>
                <div class="headerContainer">
                    <div class="iconsHeader">
                        <img src="./static/bonfire.png" width="40" class="headerImg"/>
                    </div>
                    <ul class="listContainer">
                        <li class="listItem">
                            <a href="#" data-route="lista-de-jogos">Lista de Jogos</a>
                        </li>
                        <li class="listItem">
                            <a href="#" data-route="gerenciar-jogos">Gerenciar Jogos</a>
                        </li>
                        <li class="listItem">
                            <a href="#" data-route="historico-devolucoes">Histórico de Devoluções</a>
                        </li>
                        <li class="listItem">
                            <a href="#" data-route="historico-compras">Histórico de compras</a>
                        </li>
                        <li class="listItem">
                            <a href="#" data-route="GerenciarGenresPlatform">Generos e Plataformas</a>
                        </li>
                    </ul>
                    <div class="iconsHeader">
                        <img src="./static/cart.webp" width="40" class="headerImg" id="cartHeader"/>
                    </div>
                </div>
                <div class="borderDiv"><span>s</span></div>
            </header>
        `;
    }

    render() {
        const $header = $(this.template);

        // Eventos dos links com data-route
        $header.find('a').on('click', function(event) {
            event.preventDefault();
            const rota = $(this).data('route');
            navFor(rota);
        });

        // Evento do carrinho
        $header.find('#cartHeader').on('click', () => {
            window.location.href = "http://localhost:8080/comprar.html";
        });

        return $header.get(0);
    }
}