package eduFila;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

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

public class InteligenciaFila {

	private JFrame jframe;
	private JLabel vidpanel;

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
	        jframe.setSize(800, 600);  // Defina um tamanho inicial
	        jframe.addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) {
	                redimensionarVideo();
	            }
	        });
	        jframe.setLocationRelativeTo(null);
	        jframe.setVisible(true);
	    }


	public void processarVideo(String modelWeights, String modelConfiguration, String filePath) {
		SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() throws Exception {
				System.load("C:\\opencv\\build\\java\\x64\\opencv_java480.dll");

				VideoCapture cap = new VideoCapture(filePath);

				Mat frame = new Mat();

				Net net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights);

				Size sz = new Size(288, 288);

				while (cap.read(frame)) {
					Mat blob = Dnn.blobFromImage(frame, 0.00392, sz, new Scalar(0), true, false);
					net.setInput(blob);

					List<Mat> result = new ArrayList<>();
					List<String> outBlobNames = getOutputNames(net);
					net.forward(result, outBlobNames);

					float confThreshold = 0.5f;

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

							if (confidence > confThreshold) {
								String label = NomesClassificadores.CLASS_NAMES.get((int) classIdPoint.x);
								if (label.equalsIgnoreCase("pessoa")) {
									Rect2d box = getBoundingBox(row, frame.cols(), frame.rows());
									confs.add(confidence);
									rects.add(box);
									labels.add(label);
								}
							}
						}
					}

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
						System.out.println(i);
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
		MatOfByte bytemat = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		try {
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