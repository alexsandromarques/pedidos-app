package com.aom.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class StatusPedido {
	
    private static final long serialVersionUID = 1L;

    private UUID idPedido;
    
    private StatusEnum status;
    
    private LocalDateTime dataProcessamento;
    
    private String mensagemErro;

    public StatusPedido() {
    }

    public UUID getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(UUID idPedido) {
        this.idPedido = idPedido;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getDataProcessamento() {
        return dataProcessamento;
    }

    public void setDataProcessamento(LocalDateTime dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }
}
