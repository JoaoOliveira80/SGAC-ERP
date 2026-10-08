package br.com.sgac.fornecedores;

public record FornecedorResponse(
    Long id,
    String cnpj,
    String razaoSocial,
    String email,
    boolean ativo
) {
    public static FornecedorResponse from(Fornecedor fornecedor) {
        return new FornecedorResponse(
            fornecedor.getId(), fornecedor.getCnpj(),
            fornecedor.getRazaoSocial(), fornecedor.getEmail(), fornecedor.isAtivo()
        );
    }
}
