package com.apollo.main.controller;

import com.apollo.main.dto.response.DetalhePedidoRelatorioResponse;
import com.apollo.main.dto.response.RelatorioCompraPeriodoResponse;
import com.apollo.main.dto.response.RelatorioPorPessoaResponse;
import com.apollo.main.dto.response.RelatorioVendaPeriodoResponse;
import com.apollo.main.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios")
public class ReportController {

    private final ReportService reportService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    @GetMapping("/vendas/periodo")
    @Operation(summary = "Relatório de vendas por período", 
               description = "Retorna o total de vendas, lucro e ticket médio em um período específico")
    public ResponseEntity<RelatorioVendaPeriodoResponse> getRelatorioVendasPorPeriodo(
            @Parameter(description = "Data de início (formato: dd/MM/yyyy)", example = "01/01/2025")
            @RequestParam String dataInicio,
            @Parameter(description = "Data de fim (formato: dd/MM/yyyy)", example = "31/01/2025")
            @RequestParam String dataFim) {
        
        LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
        LocalDateTime fim = parseDate(dataFim).atTime(23, 59, 59);
        
        RelatorioVendaPeriodoResponse relatorio = reportService.getRelatorioVendasPorPeriodo(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/compras/periodo")
    @Operation(summary = "Relatório de compras por período", 
               description = "Retorna o total de compras e ticket médio em um período específico")
    public ResponseEntity<RelatorioCompraPeriodoResponse> getRelatorioComprasPorPeriodo(
            @Parameter(description = "Data de início (formato: dd/MM/yyyy)", example = "01/01/2025")
            @RequestParam String dataInicio,
            @Parameter(description = "Data de fim (formato: dd/MM/yyyy)", example = "31/01/2025")
            @RequestParam String dataFim) {
        
        LocalDateTime inicio = parseDate(dataInicio).atStartOfDay();
        LocalDateTime fim = parseDate(dataFim).atTime(23, 59, 59);
        
        RelatorioCompraPeriodoResponse relatorio = reportService.getRelatorioComprasPorPeriodo(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/clientes")
    @Operation(summary = "Relatório consolidado por cliente", 
               description = "Retorna estatísticas de compras de todos os clientes, com filtro opcional de período")
    public ResponseEntity<List<RelatorioPorPessoaResponse>> getRelatorioPorCliente(
            @Parameter(description = "Data de início (formato: dd/MM/yyyy) - Opcional", example = "01/01/2025")
            @RequestParam(required = false) String dataInicio,
            @Parameter(description = "Data de fim (formato: dd/MM/yyyy) - Opcional", example = "31/01/2025")
            @RequestParam(required = false) String dataFim) {
        
        LocalDateTime inicio = dataInicio != null ? parseDate(dataInicio).atStartOfDay() : null;
        LocalDateTime fim = dataFim != null ? parseDate(dataFim).atTime(23, 59, 59) : null;
        
        List<RelatorioPorPessoaResponse> relatorio = reportService.getRelatorioPorCliente(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/clientes/{idCliente}/detalhes")
    @Operation(summary = "Detalhamento de pedidos por cliente", 
               description = "Retorna todos os pedidos de um cliente específico, com filtro opcional de período")
    public ResponseEntity<List<DetalhePedidoRelatorioResponse>> getDetalhesPorCliente(
            @Parameter(description = "ID do cliente")
            @PathVariable Long idCliente,
            @Parameter(description = "Data de início (formato: dd/MM/yyyy) - Opcional", example = "01/01/2025")
            @RequestParam(required = false) String dataInicio,
            @Parameter(description = "Data de fim (formato: dd/MM/yyyy) - Opcional", example = "31/01/2025")
            @RequestParam(required = false) String dataFim) {
        
        LocalDateTime inicio = dataInicio != null ? parseDate(dataInicio).atStartOfDay() : null;
        LocalDateTime fim = dataFim != null ? parseDate(dataFim).atTime(23, 59, 59) : null;
        
        List<DetalhePedidoRelatorioResponse> detalhes = reportService.getDetalhesPorCliente(idCliente, inicio, fim);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/fornecedores")
    @Operation(summary = "Relatório consolidado por fornecedor", 
               description = "Retorna estatísticas de compras de todos os fornecedores, com filtro opcional de período")
    public ResponseEntity<List<RelatorioPorPessoaResponse>> getRelatorioPorFornecedor(
            @Parameter(description = "Data de início (formato: dd/MM/yyyy) - Opcional", example = "01/01/2025")
            @RequestParam(required = false) String dataInicio,
            @Parameter(description = "Data de fim (formato: dd/MM/yyyy) - Opcional", example = "31/01/2025")
            @RequestParam(required = false) String dataFim) {
        
        LocalDateTime inicio = dataInicio != null ? parseDate(dataInicio).atStartOfDay() : null;
        LocalDateTime fim = dataFim != null ? parseDate(dataFim).atTime(23, 59, 59) : null;
        
        List<RelatorioPorPessoaResponse> relatorio = reportService.getRelatorioPorFornecedor(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/fornecedores/{idFornecedor}/detalhes")
    @Operation(summary = "Detalhamento de pedidos por fornecedor", 
               description = "Retorna todos os pedidos de um fornecedor específico, com filtro opcional de período")
    public ResponseEntity<List<DetalhePedidoRelatorioResponse>> getDetalhesPorFornecedor(
            @Parameter(description = "ID do fornecedor")
            @PathVariable Long idFornecedor,
            @Parameter(description = "Data de início (formato: dd/MM/yyyy) - Opcional", example = "01/01/2025")
            @RequestParam(required = false) String dataInicio,
            @Parameter(description = "Data de fim (formato: dd/MM/yyyy) - Opcional", example = "31/01/2025")
            @RequestParam(required = false) String dataFim) {
        
        LocalDateTime inicio = dataInicio != null ? parseDate(dataInicio).atStartOfDay() : null;
        LocalDateTime fim = dataFim != null ? parseDate(dataFim).atTime(23, 59, 59) : null;
        
        List<DetalhePedidoRelatorioResponse> detalhes = reportService.getDetalhesPorFornecedor(idFornecedor, inicio, fim);
        return ResponseEntity.ok(detalhes);
    }
}
