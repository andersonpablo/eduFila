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
		setBounds(100, 100, 528, 361);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(TelaInicial.class.getResource("/img/filaedu_300.png")));
		lblNewLabel.setBounds(97, 25, 315, 117);
		contentPane.add(lblNewLabel);

		JButton btnNewButton = new JButton("Monitorar");
		btnNewButton.setFont(new Font("Eras Bold ITC", Font.PLAIN, 24));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(0, 51, 0));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InteligenciaFila pc = new InteligenciaFila();
				pc.processarVideo("C:\\opencv\\yolov4.weights", "C:\\opencv\\yolov4.cfg", "C:\\opencv\\ifrn.mp4",
						tglRemovePessoas.isSelected());
			}
		});
		btnNewButton.setBounds(107, 153, 305, 87);
		contentPane.add(btnNewButton);

		tglRemovePessoas = new JToggleButton("Detectar Tudo / OFF");
		tglRemovePessoas.setBackground(new Color(255, 0, 0));
		tglRemovePessoas.setBounds(160, 263, 201, 31);
		contentPane.add(tglRemovePessoas);

		tglRemovePessoas.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tglRemovePessoas.isSelected()) {
					tglRemovePessoas.setBackground(new Color(0, 128, 0));
					tglRemovePessoas.setText("Detectar Tudo / ON");
				} else {
					tglRemovePessoas.setBackground(new Color(255, 0, 0));
					tglRemovePessoas.setText("Detectar Tudo / OFF");
				}
			}
		});
	}
}