package br.com.sgac.solicitacoes;

import br.com.sgac.centroscusto.CentroCusto;
import br.com.sgac.centroscusto.CentroCustoRepository;
import br.com.sgac.fornecedores.Fornecedor;
import br.com.sgac.fornecedores.FornecedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository solicitacoes;
    private final SolicitacaoHistoricoRepository historicos;
    private final CentroCustoRepository centros;
    private final FornecedorRepository fornecedores;

    public SolicitacaoService(SolicitacaoRepository solicitacoes,
                              SolicitacaoHistoricoRepository historicos,
                              CentroCustoRepository centros,
                              FornecedorRepository fornecedores) {
        this.solicitacoes = solicitacoes;
        this.historicos = historicos;
        this.centros = centros;
        this.fornecedores = fornecedores;
    }

    @Transactional
    public SolicitacaoResponse criar(SolicitacaoCriarRequest request) {
        CentroCusto centro = centros.findById(request.centroCustoId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro de custo nao encontrado"));
        if (!centro.isAtivo() || !centro.getDepartamento().isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Centro de custo ou departamento inativo");
        }
        Fornecedor fornecedor = fornecedores.findById(request.fornecedorId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor nao encontrado"));
        if (!fornecedor.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Fornecedor inativo");
        }
        Solicitacao solicitacao = new Solicitacao(centro, fornecedor,
            request.solicitante().trim(), request.justificativa().trim());
        for (SolicitacaoItemRequest i : request.itens()) {
            solicitacao.adicionarItem(i.descricao().trim(), i.quantidade(), i.valorUnitario());
        }
        if (solicitacao.getValorTotal().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Valor total excede o limite do banco");
        }
        solicitacoes.saveAndFlush(solicitacao);
        registrar(solicitacao, null, SolicitacaoStatus.RASCUNHO,
            solicitacao.getSolicitante(), "Solicitacao criada");
        return SolicitacaoResponse.from(solicitacao);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoResumoResponse> listar() {
        return solicitacoes.findAllByOrderByCriadaEmDescIdDesc().stream()
            .map(SolicitacaoResumoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoResponse buscar(Long id) {
        return SolicitacaoResponse.from(encontrar(id));
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoHistoricoResponse> historico(Long id) {
        encontrar(id);
        return historicos.findBySolicitacaoIdOrderByRegistradoEmAscIdAsc(id).stream()
            .map(SolicitacaoHistoricoResponse::from).toList();
    }

    @Transactional
    public SolicitacaoResponse enviar(Long id) {
        Solicitacao s = encontrar(id);
        SolicitacaoStatus anterior = s.getStatus();
        exigir(anterior == SolicitacaoStatus.RASCUNHO);
        // O cadastro foi validado na criacao. Para enviar, validamos novamente os cadastros.
        if (!s.getCentroCusto().isAtivo() || !s.getCentroCusto().getDepartamento().isAtivo()
            || !s.getFornecedor().isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Centro de custo, departamento ou fornecedor inativo");
        }
        s.enviar();
        registrar(s, anterior, s.getStatus(), s.getSolicitante(), "Enviada para aprovacao");
        return SolicitacaoResponse.from(s);
    }

    @Transactional
    public SolicitacaoResponse aprovar(Long id, SolicitacaoDecisaoRequest request) {
        Solicitacao s = encontrar(id);
        SolicitacaoStatus anterior = s.getStatus();
        exigir(anterior == SolicitacaoStatus.PENDENTE);
        s.aprovar();
        registrar(s, anterior, s.getStatus(), request.responsavel().trim(),
            limpar(request.observacao()));
        return SolicitacaoResponse.from(s);
    }

    @Transactional
    public SolicitacaoResponse rejeitar(Long id, SolicitacaoRejeicaoRequest request) {
        Solicitacao s = encontrar(id);
        SolicitacaoStatus anterior = s.getStatus();
        exigir(anterior == SolicitacaoStatus.PENDENTE);
        if (request.motivo().trim().length() < 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Motivo precisa de ao menos 10 caracteres");
        }
        s.rejeitar();
        registrar(s, anterior, s.getStatus(), request.responsavel().trim(), request.motivo().trim());
        return SolicitacaoResponse.from(s);
    }

    private Solicitacao encontrar(Long id) {
        return solicitacoes.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitacao nao encontrada"));
    }

    private void registrar(Solicitacao s, SolicitacaoStatus anterior,
                          SolicitacaoStatus novo, String responsavel, String observacao) {
        historicos.save(new SolicitacaoHistorico(s, anterior, novo, responsavel, observacao));
    }

    private void exigir(boolean permitido) {
        if (!permitido) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Transicao de status nao permitida");
        }
    }

    private String limpar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
