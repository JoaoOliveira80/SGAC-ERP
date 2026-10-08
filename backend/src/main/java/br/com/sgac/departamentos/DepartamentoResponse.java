package br.com.sgac.departamentos;

public record DepartamentoResponse(Long id, String codigo, String nome, boolean ativo) {
    public static DepartamentoResponse from(Departamento departamento) {
        return new DepartamentoResponse(
            departamento.getId(),
            departamento.getCodigo(),
            departamento.getNome(),
            departamento.isAtivo()
        );
    }
}
