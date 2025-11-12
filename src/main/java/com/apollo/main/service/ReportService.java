package com.apollo.main.service;

import com.apollo.main.dto.response.DetalhePedidoRelatorioResponse;
import com.apollo.main.dto.response.RankingProdutoResponse;
import com.apollo.main.dto.response.RelatorioCompraPeriodoResponse;
import com.apollo.main.dto.response.RelatorioPorPessoaResponse;
import com.apollo.main.dto.response.RelatorioVendaPeriodoResponse;
import com.apollo.main.model.Pedido;
import com.apollo.main.model.PedidoProduto;
import com.apollo.main.model.TipoPedido;
import com.apollo.main.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PedidoRepository pedidoRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Gera relatório de vendas por período
     */
    public RelatorioVendaPeriodoResponse getRelatorioVendasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Pedido> pedidos = pedidoRepository.findAll().stream()
                .filter(p -> p.getTipo() == TipoPedido.VENDA)
                .filter(p -> p.getStatus() == com.apollo.main.model.StatusPedido.FATURADO)
                .filter(p -> p.getDataEmissao() != null)
                .filter(p -> !p.getDataEmissao().isBefore(dataInicio) && !p.getDataEmissao().isAfter(dataFim))
                .collect(Collectors.toList());

        BigDecimal totalVendas = pedidos.stream()
                .map(Pedido::getTotalVenda)
                .filter(val -> val != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCusto = pedidos.stream()
                .map(Pedido::getTotalCusto)
                .filter(val -> val != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lucro = totalVendas.subtract(totalCusto);
        
        BigDecimal ticketMedio = pedidos.isEmpty() 
                ? BigDecimal.ZERO 
                : totalVendas.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);

        return RelatorioVendaPeriodoResponse.builder()
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .quantidadePedidos((long) pedidos.size())
                .totalVendas(totalVendas)
                .totalCusto(totalCusto)
                .lucro(lucro)
                .ticketMedio(ticketMedio)
                .build();
    }

    /**
     * Gera relatório de compras por período
     */
    public RelatorioCompraPeriodoResponse getRelatorioComprasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Pedido> pedidos = pedidoRepository.findAll().stream()
                .filter(p -> p.getTipo() == TipoPedido.COMPRA)
                .filter(p -> p.getStatus() == com.apollo.main.model.StatusPedido.FATURADO)
                .filter(p -> p.getDataEmissao() != null)
                .filter(p -> !p.getDataEmissao().isBefore(dataInicio) && !p.getDataEmissao().isAfter(dataFim))
                .collect(Collectors.toList());

        BigDecimal totalCompras = pedidos.stream()
                .map(Pedido::getTotalCusto)
                .filter(val -> val != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ticketMedio = pedidos.isEmpty() 
                ? BigDecimal.ZERO 
                : totalCompras.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);

        return RelatorioCompraPeriodoResponse.builder()
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .quantidadePedidos((long) pedidos.size())
                .totalCompras(totalCompras)
                .ticketMedio(ticketMedio)
                .build();
    }

    /**
     * Gera relatório por cliente
     */
    public List<RelatorioPorPessoaResponse> getRelatorioPorCliente() {
        return getRelatorioPorCliente(null, null);
    }

    /**
     * Gera relatório por cliente com filtro de período
     */
    public List<RelatorioPorPessoaResponse> getRelatorioPorCliente(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Pedido> pedidosVenda = pedidoRepository.findAll().stream()
                .filter(p -> p.getTipo() == TipoPedido.VENDA)
                .filter(p -> p.getStatus() == com.apollo.main.model.StatusPedido.FATURADO)
                .filter(p -> p.getPessoa() != null)
                .filter(p -> dataInicio == null || (p.getDataEmissao() != null && !p.getDataEmissao().isBefore(dataInicio)))
                .filter(p -> dataFim == null || (p.getDataEmissao() != null && !p.getDataEmissao().isAfter(dataFim)))
                .collect(Collectors.toList());

        return pedidosVenda.stream()
                .collect(Collectors.groupingBy(p -> p.getPessoa().getIdPessoa()))
                .entrySet().stream()
                .map(entry -> {
                    Pedido primeiroPedido = entry.getValue().get(0);
                    Long quantidadePedidos = (long) entry.getValue().size();
                    
                    BigDecimal totalValor = entry.getValue().stream()
                            .map(Pedido::getTotalVenda)
                            .filter(val -> val != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal ticketMedio = quantidadePedidos > 0 
                            ? totalValor.divide(BigDecimal.valueOf(quantidadePedidos), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    String ultimoPedido = entry.getValue().stream()
                            .map(Pedido::getDataEmissao)
                            .filter(data -> data != null)
                            .max(LocalDateTime::compareTo)
                            .map(data -> data.format(formatter))
                            .orElse("N/A");

                    return RelatorioPorPessoaResponse.builder()
                            .idPessoa(primeiroPedido.getPessoa().getIdPessoa())
                            .nome(primeiroPedido.getPessoa().getNome())
                            .cpfCnpj(primeiroPedido.getPessoa().getCpfcnpj())
                            .tipo("CLIENTE")
                            .quantidadePedidos(quantidadePedidos)
                            .totalValor(totalValor)
                            .ticketMedio(ticketMedio)
                            .ultimoPedido(ultimoPedido)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Gera relatório por fornecedor
     */
    public List<RelatorioPorPessoaResponse> getRelatorioPorFornecedor() {
        return getRelatorioPorFornecedor(null, null);
    }

    /**
     * Gera relatório por fornecedor com filtro de período
     */
    public List<RelatorioPorPessoaResponse> getRelatorioPorFornecedor(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Pedido> pedidosCompra = pedidoRepository.findAll().stream()
                .filter(p -> p.getTipo() == TipoPedido.COMPRA)
                .filter(p -> p.getStatus() == com.apollo.main.model.StatusPedido.FATURADO)
                .filter(p -> p.getPessoa() != null)
                .filter(p -> dataInicio == null || (p.getDataEmissao() != null && !p.getDataEmissao().isBefore(dataInicio)))
                .filter(p -> dataFim == null || (p.getDataEmissao() != null && !p.getDataEmissao().isAfter(dataFim)))
                .collect(Collectors.toList());

        return pedidosCompra.stream()
                .collect(Collectors.groupingBy(p -> p.getPessoa().getIdPessoa()))
                .entrySet().stream()
                .map(entry -> {
                    Pedido primeiroPedido = entry.getValue().get(0);
                    Long quantidadePedidos = (long) entry.getValue().size();
                    
                    BigDecimal totalValor = entry.getValue().stream()
                            .map(Pedido::getTotalCusto)
                            .filter(val -> val != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal ticketMedio = quantidadePedidos > 0 
                            ? totalValor.divide(BigDecimal.valueOf(quantidadePedidos), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    String ultimoPedido = entry.getValue().stream()
                            .map(Pedido::getDataEmissao)
                            .filter(data -> data != null)
                            .max(LocalDateTime::compareTo)
                            .map(data -> data.format(formatter))
                            .orElse("N/A");

                    return RelatorioPorPessoaResponse.builder()
                            .idPessoa(primeiroPedido.getPessoa().getIdPessoa())
                            .nome(primeiroPedido.getPessoa().getNome())
                            .cpfCnpj(primeiroPedido.getPessoa().getCpfcnpj())
                            .tipo("FORNECEDOR")
                            .quantidadePedidos(quantidadePedidos)
                            .totalValor(totalValor)
                            .ticketMedio(ticketMedio)
                            .ultimoPedido(ultimoPedido)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Detalha pedidos por cliente específico
     */
    public List<DetalhePedidoRelatorioResponse> getDetalhesPorCliente(Long idCliente) {
        return getDetalhesPorCliente(idCliente, null, null);
    }

    /**
     * Detalha pedidos por cliente específico com filtro de período
     */
    public List<DetalhePedidoRelatorioResponse> getDetalhesPorCliente(Long idCliente, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return pedidoRepository.findAll().stream()
                .filter(p -> p.getTipo() == TipoPedido.VENDA)
                .filter(p -> p.getStatus() == com.apollo.main.model.StatusPedido.FATURADO)
                .filter(p -> p.getPessoa() != null && p.getPessoa().getIdPessoa().equals(idCliente))
                .filter(p -> dataInicio == null || (p.getDataEmissao() != null && !p.getDataEmissao().isBefore(dataInicio)))
                .filter(p -> dataFim == null || (p.getDataEmissao() != null && !p.getDataEmissao().isAfter(dataFim)))
                .map(this::mapToDetalhePedido)
                .collect(Collectors.toList());
    }

    /**
     * Detalha pedidos por fornecedor específico
     */
    public List<DetalhePedidoRelatorioResponse> getDetalhesPorFornecedor(Long idFornecedor) {
        return getDetalhesPorFornecedor(idFornecedor, null, null);
    }

    /**
     * Detalha pedidos por fornecedor específico com filtro de período
     */
    public List<DetalhePedidoRelatorioResponse> getDetalhesPorFornecedor(Long idFornecedor, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return pedidoRepository.findAll().stream()
                .filter(p -> p.getTipo() == TipoPedido.COMPRA)
                .filter(p -> p.getStatus() == com.apollo.main.model.StatusPedido.FATURADO)
                .filter(p -> p.getPessoa() != null && p.getPessoa().getIdPessoa().equals(idFornecedor))
                .filter(p -> dataInicio == null || (p.getDataEmissao() != null && !p.getDataEmissao().isBefore(dataInicio)))
                .filter(p -> dataFim == null || (p.getDataEmissao() != null && !p.getDataEmissao().isAfter(dataFim)))
                .map(this::mapToDetalhePedido)
                .collect(Collectors.toList());
    }

    private DetalhePedidoRelatorioResponse mapToDetalhePedido(Pedido pedido) {
        return DetalhePedidoRelatorioResponse.builder()
                .idPedido(pedido.getIdPedido())
                .tipo(pedido.getTipo())
                .status(pedido.getStatus())
                .dataEmissao(pedido.getDataEmissao())
                .nomePessoa(pedido.getPessoa() != null ? pedido.getPessoa().getNome() : "N/A")
                .nomeFuncionario(pedido.getFuncionario() != null ? pedido.getFuncionario().getNome() : "N/A")
                .totalCusto(pedido.getTotalCusto())
                .totalVenda(pedido.getTotalVenda())
                .formaPagamento(pedido.getFormaPagamento())
                .build();
    }

    /**
     * Gera ranking de produtos mais vendidos em um período
     */
    public List<RankingProdutoResponse> getRankingProdutosMaisVendidos(LocalDateTime dataInicio, LocalDateTime dataFim) {
        // Busca todos os pedidos de venda faturados no período
        List<Pedido> pedidosVenda = pedidoRepository.findAll().stream()
                .filter(p -> p.getTipo() == TipoPedido.VENDA)
                .filter(p -> p.getStatus() == com.apollo.main.model.StatusPedido.FATURADO)
                .filter(p -> p.getDataEmissao() != null)
                .filter(p -> !p.getDataEmissao().isBefore(dataInicio) && !p.getDataEmissao().isAfter(dataFim))
                .collect(Collectors.toList());

        // Agrupa os produtos vendidos
        Map<Long, List<PedidoProduto>> produtosVendidos = pedidosVenda.stream()
                .flatMap(pedido -> pedido.getItens().stream())
                .collect(Collectors.groupingBy(item -> item.getProduto().getId()));

        // Cria o ranking
        AtomicInteger posicao = new AtomicInteger(1);
        
        return produtosVendidos.entrySet().stream()
                .map(entry -> {
                    PedidoProduto primeiroItem = entry.getValue().get(0);
                    
                    // Soma a quantidade total vendida
                    Long quantidadeTotal = entry.getValue().stream()
                            .mapToLong(PedidoProduto::getQntd)
                            .sum();

                    // Calcula o valor total vendido
                    BigDecimal valorTotal = entry.getValue().stream()
                            .map(item -> item.getPrecoVendaUN().multiply(BigDecimal.valueOf(item.getQntd())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // Calcula o custo total
                    BigDecimal custoTotal = entry.getValue().stream()
                            .map(item -> item.getPrecoCustoUN().multiply(BigDecimal.valueOf(item.getQntd())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // Calcula o lucro total
                    BigDecimal lucroTotal = valorTotal.subtract(custoTotal);

                    // Calcula a margem de lucro (percentual)
                    BigDecimal margemLucro = valorTotal.compareTo(BigDecimal.ZERO) > 0
                            ? lucroTotal.divide(valorTotal, 4, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100))
                                    .setScale(2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    // Calcula o ticket médio (valor total / quantidade de itens vendidos)
                    BigDecimal ticketMedio = quantidadeTotal > 0
                            ? valorTotal.divide(BigDecimal.valueOf(quantidadeTotal), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    return RankingProdutoResponse.builder()
                            .idProduto(primeiroItem.getProduto().getId())
                            .nome(primeiroItem.getProduto().getNome())
                            .descricao(primeiroItem.getProduto().getDescricao())
                            .quantidadeVendida(quantidadeTotal)
                            .valorTotalVendido(valorTotal)
                            .lucroTotal(lucroTotal)
                            .margemLucro(margemLucro)
                            .ticketMedio(ticketMedio)
                            .build();
                })
                // Ordena por valor total vendido (decrescente)
                .sorted((p1, p2) -> p2.getValorTotalVendido().compareTo(p1.getValorTotalVendido()))
                // Adiciona a posição no ranking
                .peek(produto -> produto.setPosicaoRanking(posicao.getAndIncrement()))
                .collect(Collectors.toList());
    }
}
