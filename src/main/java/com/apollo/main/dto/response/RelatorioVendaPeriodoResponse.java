package com.apollo.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioVendaPeriodoResponse {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Long quantidadePedidos;
    private BigDecimal totalVendas;
    private BigDecimal totalCusto;
    private BigDecimal lucro;
    private BigDecimal ticketMedio;
}
