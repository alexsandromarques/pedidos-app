package com.aom.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.aom.client.PedidoClient;
import com.aom.model.Pedido;
import com.aom.model.StatusEnum;

public class PedidoApp extends JFrame {

    private final JTextField txtProduto;
    private final JTextField txtQuantidade;
    private final JTextArea txtStatusArea;
    private final PedidoClient client;

    private final Map<UUID, String> pedidos = new LinkedHashMap<>();
    private final List<UUID> pendentes = new ArrayList<>();

    public PedidoApp() {
        setTitle("Sistema de Pedidos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        client = new PedidoClient();

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtProduto = new JTextField();
        txtQuantidade = new JTextField();
        txtStatusArea = new JTextArea();
        txtStatusArea.setEditable(false);

        JButton btnEnviar = new JButton("Enviar Pedido");
        btnEnviar.addActionListener(e -> enviarPedido());

        form.add(new JLabel("Produto:"));
        form.add(txtProduto);
        form.add(new JLabel("Quantidade:"));
        form.add(txtQuantidade);
        form.add(new JLabel());
        form.add(btnEnviar);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(txtStatusArea), BorderLayout.CENTER);

        iniciarPolling();
    }

    private void enviarPedido() {
        String produto = txtProduto.getText().trim();
        String qtdText = txtQuantidade.getText().trim();

        if (produto.isEmpty() || qtdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe produto e quantidade", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantidade;

        try {
            quantidade = Integer.parseInt(qtdText);
            if (quantidade <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pedido pedido = new Pedido(produto, quantidade);

        try {
            UUID id = client.enviar(pedido);
            pedidos.put(id, StatusEnum.ENVIADO.getNome());
            pendentes.add(id);
            atualizarTela();
            txtProduto.setText("");
            txtQuantidade.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao enviar pedido", "Falha", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarPolling() {
        Timer timer = new Timer(5000, e -> {
            if (pendentes.isEmpty()) return;

            List<UUID> finalizados = new ArrayList<>();

            for (UUID id : pendentes) {
                String status = client.status(id);
                String statusAtual = pedidos.get(id);

                if (!status.equalsIgnoreCase(statusAtual)) {
                    pedidos.put(id, status);
                    atualizarTela();
                }

                if (status.equalsIgnoreCase(StatusEnum.SUCESSO.getNome()) || status.equalsIgnoreCase(StatusEnum.FALHA.getNome())) {
                    finalizados.add(id);
                }
            }

            pendentes.removeAll(finalizados);
        });

        timer.setRepeats(true);
        timer.start();
    }

    private void atualizarTela() {
        SwingUtilities.invokeLater(() -> {
            txtStatusArea.setText("");
            pedidos.forEach((id, status) -> txtStatusArea.append(id + " - " + status + "\n"));
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PedidoApp().setVisible(true));
    }
}
