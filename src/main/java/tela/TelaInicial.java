package tela;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eduFila.InteligenciaFila;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JToggleButton tglRemovePessoas;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				TelaInicial frame = new TelaInicial();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public TelaInicial() {
		setResizable(false);
		setTitle("FilaEdu - Monitoramento de Filas");
		setIconImage(Toolkit.getDefaultToolkit().getImage(TelaInicial.class.getResource("/img/icon.jpg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 361);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(TelaInicial.class.getResource("/img/filaedu.png")));
		lblNewLabel.setBounds(97, 25, 315, 117);
		contentPane.add(lblNewLabel);

		JButton btnNewButton = new JButton("Monitorar");
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setFocusPainted(false);
		btnNewButton.setFont(new Font("Eras Bold ITC", Font.PLAIN, 24));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(20, 41, 10));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InteligenciaFila pc = new InteligenciaFila();
				pc.processarVideo("C:\\opencv\\yolov7-tiny.weights", "C:\\opencv\\yolov7-tiny.cfg", "C:\\opencv\\teste.mp4",
						tglRemovePessoas.isSelected());
			}
		});
		btnNewButton.setBounds(97, 153, 306, 87);
		contentPane.add(btnNewButton);

		tglRemovePessoas = new JToggleButton("Detectar Tudo / OFF");
		tglRemovePessoas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tglRemovePessoas.setFocusPainted(false);
		tglRemovePessoas.setFont(new Font("Eras Bold ITC", Font.BOLD, 12));
		tglRemovePessoas.setForeground(Color.WHITE);
		tglRemovePessoas.setBackground(new Color(158, 10, 10));
		tglRemovePessoas.setBounds(148, 262, 201, 31);
		contentPane.add(tglRemovePessoas);

		tglRemovePessoas.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tglRemovePessoas.isSelected()) {
					tglRemovePessoas.setBackground(Color.green);
					tglRemovePessoas.setText("Detectar Tudo / ON");
				} else {
					tglRemovePessoas.setBackground(new Color(158, 10, 10));
					tglRemovePessoas.setText("Detectar Tudo / OFF");
				}
			}
		});
	}
}