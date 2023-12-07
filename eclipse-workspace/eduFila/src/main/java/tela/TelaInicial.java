package tela;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import eduFila.InteligenciaFila;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
				pc.processarVideo("C:\\opencv\\yolov4.weights", "C:\\opencv\\yolov4.cfg", "C:\\opencv\\ifrn.mp4");
			}
		});
		btnNewButton.setBounds(107, 153, 305, 87);
		contentPane.add(btnNewButton);

		JToggleButton tglbtnNewToggleButton = new JToggleButton("Detectar Objetos");
		tglbtnNewToggleButton.setBackground(new Color(255, 51, 0));
		tglbtnNewToggleButton.setBounds(190, 262, 146, 23);
		contentPane.add(tglbtnNewToggleButton);
	}
}