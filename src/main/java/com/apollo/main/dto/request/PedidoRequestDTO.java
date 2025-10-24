package com.apollo.main.dto.request;

import com.apollo.main.model.PedidoProduto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.apollo.main.model.TipoPedido;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoRequestDTO {

    @NotNull(message = "Tipo do pedido é obrigatório")
    private TipoPedido tipo;

    @Future(message = "A data de vencimento deve ser futura")
    private LocalDateTime vencimento;

    @NotNull(message = "Forma de pagamento é obrigatória")
    @NotBlank(message = "Forma de pagamento é obrigatória")
    private String formaPagamento;

    @NotNull(message = "ID do cliente/fornecedor é obrigatório")
    private Long idPessoa;

    @NotNull(message = "ID do funcionário é obrigatório")
    private Long idFuncionario;

    @NotNull(message = "Lista de itens é obrigatória")
    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<PedidoProdutoRequestDTO> itens;
}
