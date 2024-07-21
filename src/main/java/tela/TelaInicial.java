package tela;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eduFila.InteligenciaFila;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.MatteBorder;

public class TelaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JToggleButton tglRemovePessoas;
	private JComboBox comboCamera;

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
		setBounds(100, 100, 515, 454);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(TelaInicial.class.getResource("/img/filaedu.png")));
		lblNewLabel.setBounds(97, 25, 315, 117);
		contentPane.add(lblNewLabel);

		JButton botaoMonitorar = new JButton("Monitorar");
		botaoMonitorar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botaoMonitorar.setFocusPainted(false);
		botaoMonitorar.setFont(new Font("Eras Bold ITC", Font.PLAIN, 24));
		botaoMonitorar.setForeground(new Color(255, 255, 255));
		botaoMonitorar.setBackground(new Color(20, 41, 10));
		botaoMonitorar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(comboCamera.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "POR FAVOR SELECIONE UM TIPO DE CÂMERA", "ALERTA", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				InteligenciaFila pc = new InteligenciaFila();
				pc.processarVideo("files\\yolov7-tiny.weights", "files\\yolov7-tiny.cfg", "files\\teste.mp4",
						tglRemovePessoas.isSelected());
				pc.todosObjetos = tglRemovePessoas.isSelected();
				pc.cameraSelecionada = IdentificarCamera();
			}
		});
		botaoMonitorar.setBounds(97, 212, 297, 46);
		contentPane.add(botaoMonitorar);

		tglRemovePessoas = new JToggleButton("Detectar Tudo / OFF");
		tglRemovePessoas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tglRemovePessoas.setFocusPainted(false);
		tglRemovePessoas.setFont(new Font("Eras Bold ITC", Font.BOLD, 13));
		tglRemovePessoas.setForeground(Color.WHITE);
		tglRemovePessoas.setBackground(new Color(158, 10, 10));
		tglRemovePessoas.setBounds(97, 339, 297, 37);
		contentPane.add(tglRemovePessoas);
		
		JButton botaoMonitorarVideo = new JButton("Monitorar Vídeo");
		botaoMonitorarVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InteligenciaFila pc = new InteligenciaFila();
				pc.processarVideo("files\\yolov7-tiny.weights", "files\\yolov7-tiny.cfg", "C:\\opencv\\teste.mp4",
						tglRemovePessoas.isSelected());
				pc.todosObjetos = tglRemovePessoas.isSelected();
				pc.botaoIniciar.setText("Selecionar Vídeo");
			}
		});
		botaoMonitorarVideo.setFocusable(false);
		botaoMonitorarVideo.setBackground(new Color(20, 41, 10));
		botaoMonitorarVideo.setFont(new Font("Eras Bold ITC", Font.PLAIN, 24));
		botaoMonitorarVideo.setForeground(Color.WHITE);
		botaoMonitorarVideo.setBounds(97, 153, 297, 46);
		contentPane.add(botaoMonitorarVideo);
		
		comboCamera = new JComboBox();
		comboCamera.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comboCamera.setFocusable(false);
		comboCamera.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(20, 41, 10)));
		comboCamera.setForeground(new Color(20, 41, 10));
		comboCamera.setBackground(Color.WHITE);
		comboCamera.setFont(new Font("Eras Bold ITC", Font.PLAIN, 18));
		comboCamera.setModel(new DefaultComboBoxModel(new String[] {"Selecione o tipo de câmera", "Câmera Nativa", "Câmera Externa"}));
		comboCamera.setBounds(97, 270, 297, 46);
		contentPane.add(comboCamera);

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

	protected int IdentificarCamera() {
		
		int valorCamera = 0;
		
		if(comboCamera.getSelectedIndex() == 1) {
			valorCamera = 1;
		} else if(comboCamera.getSelectedIndex() == 2) {
			valorCamera = 2;
		}
		return valorCamera;
	}
}