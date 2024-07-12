package eduFila;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect2d;
import org.opencv.core.Point;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InteligenciaFila {

	private JFrame jframe;
	private JLabel vidpanel;
	private JLabel contagem;
	public VideoCapture cap;
	public boolean todosObjetos;

	public InteligenciaFila() {
		initializeGUI();
	}

	private void initializeGUI() {
		jframe = new JFrame("Video");
		jframe.setIconImage(Toolkit.getDefaultToolkit().getImage(InteligenciaFila.class.getResource("/img/icon.jpg")));
		jframe.setTitle("FilaEdu - Monitoramento de Filas");
		vidpanel = new VideoLabel();
		jframe.getContentPane().setLayout(new BorderLayout());
		jframe.getContentPane().add(vidpanel, BorderLayout.CENTER);

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(800, 600); // defina um tamanho inicial
		jframe.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redimensionarVideo();
			}
		});
		jframe.setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.BLACK);
		jframe.setJMenuBar(menuBar);

		JLabel lblNewLabel_3 = new JLabel(" Fila");
		lblNewLabel_3.setForeground(new Color(155, 0, 0));
		lblNewLabel_3.setFont(new Font("Eras Bold ITC", Font.BOLD, 30));
		menuBar.add(lblNewLabel_3);

		JLabel lblNewLabel = new JLabel("Edu   ");
		lblNewLabel.setFont(new Font("Eras Bold ITC", Font.PLAIN, 30));
		lblNewLabel.setForeground(new Color(0, 128, 0));
		menuBar.add(lblNewLabel);

		JButton btnNewButton = new JButton("Iniciar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IniciarGravacao();
			}
		});
		btnNewButton.setFocusPainted(false);
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(new Color(0, 128, 0));
		menuBar.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Parar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PararGravacao();
			}
		});
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setForeground(Color.WHITE);
		btnNewButton_1.setBackground(new Color(179, 0, 4));
		menuBar.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Monitorar Vídeo");
		btnNewButton_2.setForeground(Color.WHITE);
		btnNewButton_2.setBackground(Color.BLACK);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelecionarVideo();
			}
		});
		menuBar.add(btnNewButton_2);

		JLabel lblNewLabel_1 = new JLabel(
				"                                              ");
		menuBar.add(lblNewLabel_1);

		contagem = new JLabel("Quantidade de Pessoas:");
		contagem.setForeground(Color.WHITE);
		contagem.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 17));
		menuBar.add(contagem);
		jframe.setVisible(true);
	}

	protected void SelecionarVideo() {
		
		JFileChooser jfileChooser = new JFileChooser();
		
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("Apenas .mp4", "mp4");
		jfileChooser.setAcceptAllFileFilterUsed(false);
		jfileChooser.addChoosableFileFilter(filtro);
		
		int respostDoFileChooser = jfileChooser.showOpenDialog(null);
		
		if(respostDoFileChooser == JFileChooser.APPROVE_OPTION) {
			File arquivoSelecionado = jfileChooser.getSelectedFile();
			System.out.println("Caminho do arquivo selecionado: " + arquivoSelecionado.getAbsolutePath());
			
			if(cap != null) {
				cap.release();
			}
			
			cap = new VideoCapture(arquivoSelecionado + "");
			processarVideo("C:\\opencv\\yolov7-tiny.weights", "C:\\opencv\\yolov7-tiny.cfg", arquivoSelecionado + "", todosObjetos);
			
		} else {
			System.out.println("nenhum arquivo encontrado");
		}
		
	}

	protected void PararGravacao() {
		cap.release();
	}

	protected void IniciarGravacao() {
		cap = new VideoCapture(0, Videoio.CAP_DSHOW);
		processarVideo("files\\yolov7-tiny.weights", "files\\yolov7-tiny.cfg", "file\\teste.mp4", todosObjetos);
	}

	public void processarVideo(String modelWeights, String modelConfiguration, String filePath, boolean todosObjetos) {
		SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() throws Exception {
				System.load("C:\\opencv\\build\\java\\x64\\opencv_java480.dll");

				Mat frame = new Mat();

				Net net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights);

				Size sz = new Size(288, 288);

				while (cap.read(frame)) {
					Mat blob = Dnn.blobFromImage(frame, 0.00392, sz, new Scalar(0), true, false);
					net.setInput(blob);

					List<Mat> result = new ArrayList<>();
					List<String> outBlobNames = getOutputNames(net);
					net.forward(result, outBlobNames);

					float confThreshold = 0.3f;

					List<Float> confs = new ArrayList<>();
					List<Rect2d> rects = new ArrayList<>();
					List<String> labels = new ArrayList<>();

					for (Mat level : result) {
						for (int j = 0; j < level.rows(); ++j) {
							Mat row = level.row(j);
							Mat scores = row.colRange(5, level.cols());
							Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
							float confidence = (float) mm.maxVal;
							Point classIdPoint = mm.maxLoc;
							String label = NomesClassificadores.CLASS_NAMES.get((int) classIdPoint.x);

							// Adiciona objetos com confianca acima do limite, considerando a classe
							// "pessoa" apenas se `todosObjetos` for verdadeiro
							if (confidence > confThreshold
									&& (todosObjetos || (label.equals("pessoa") && !todosObjetos))) {
								Rect2d box = getBoundingBox(row, frame.cols(), frame.rows());
								confs.add(confidence);
								rects.add(box);
								labels.add(label);
								contagem.setText("Quantidade de objetos: ");
							}
						}

					}
						//conferindo se as listas nao estao vazias antes de continuar
					if (!confs.isEmpty() && !rects.isEmpty() && !labels.isEmpty()) {
						
					    MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));
					    MatOfRect2d boxes = new MatOfRect2d(rects.toArray(new Rect2d[0]));

					    MatOfInt indices = new MatOfInt();
					    Dnn.NMSBoxes(boxes, confidences, confThreshold, confThreshold, indices);

					    int[] ind = indices.toArray();
					    for (int i : ind) {
					        Rect2d box = rects.get(i);
					        String labelText = labels.get(i);
					        Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(0, 255, 0), 2);
					        Imgproc.putText(frame, labelText, new Point(box.x, box.y - 10), Imgproc.FONT_HERSHEY_SIMPLEX,
					                0.5, new Scalar(0, 255, 0), 2);
					        
					        if(todosObjetos == true) {
					        	contagem.setText("Quantidade de Objetos: " + ind.length); // mostrando a quantidade de Objetos no label
					        } else {
					        	contagem.setText("Quantidade de Pessoas: " + ind.length); // mostrando a quantidade de pessoas no label
					        }
					        
					    }
					}
					
					ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
					vidpanel.setIcon(image);
					vidpanel.repaint();
				}

				return null;
			}
		};

		worker.execute();
	}

	private static List<String> getOutputNames(Net net) {
		List<String> names = new ArrayList<>();
		List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
		List<String> layersNames = net.getLayerNames();

		outLayers.forEach((item) -> names.add(layersNames.get(item - 1)));
		return names;
	}

	private static Rect2d getBoundingBox(Mat row, double frameWidth, double frameHeight) {
		double centerX = row.get(0, 0)[0] * frameWidth;
		double centerY = row.get(0, 1)[0] * frameHeight;
		double width = row.get(0, 2)[0] * frameWidth;
		double height = row.get(0, 3)[0] * frameHeight;

		double left = centerX - width / 2;
		double top = centerY - height / 2;

		return new Rect2d(left, top, width, height);
	}

	private static BufferedImage Mat2bufferedImage(Mat image) {
	    if (image.empty()) {
	        return null;  
	    }

	    MatOfByte bytemat = new MatOfByte();
	    Imgcodecs.imencode(".jpg", image, bytemat);
	    byte[] bytes = bytemat.toArray();

	    try (InputStream in = new ByteArrayInputStream(bytes)) {
	        return ImageIO.read(in);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	private void redimensionarVideo() {
		SwingUtilities.invokeLater(() -> {
			ImageIcon imageIcon = (ImageIcon) vidpanel.getIcon();
			if (imageIcon != null) {
				Image imagem = imageIcon.getImage();
				Image novaImagem = imagem.getScaledInstance(vidpanel.getWidth(), vidpanel.getHeight(),
						Image.SCALE_SMOOTH);
				vidpanel.setIcon(new ImageIcon(novaImagem));
			}
		});
	}
	
}