package pacote.mestrado.dominios;

/**
 * Enum destinado ao controle de que situacao um agente se encontra
 * 
 * @author Shulander
 *
 */
public enum TipoEtapaNegociacao {
    BUSCA_GESTOR,
    NEGOCIACAO_PASSIVO,
    NEGOCIACAO_ATIVO,
    EXECUCAO_ATIVIDADE,
    AGUARDANDO_ATIVIDADE_COMPATIVEL;
}
