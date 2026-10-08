package br.com.sgac.centroscusto;

import br.com.sgac.departamentos.Departamento;

public record CentroCustoResponse(
    Long id,
    String codigo,
    String nome,
    Long departamentoId,
    String departamentoNome,
    boolean ativo
) {
    public static CentroCustoResponse from(CentroCusto centroCusto) {
        Departamento departamento = centroCusto.getDepartamento();
        return new CentroCustoResponse(
            centroCusto.getId(), centroCusto.getCodigo(), centroCusto.getNome(),
            departamento.getId(), departamento.getNome(), centroCusto.isAtivo()
        );
    }
}
