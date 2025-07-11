package com.aom.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Pedido {

	private static final long serialVersionUID = 1L;

	private final UUID id;

	private String produto;

	private int quantidade;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private final LocalDateTime dataCriacao;

	public Pedido(String produto, int quantidade) {
		this.id = UUID.randomUUID();
		this.produto = produto;
		this.quantidade = quantidade;
		this.dataCriacao = LocalDateTime.now();
	}

	public UUID getId() {
		return id;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
}
