package eduFila;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class VideoLabel extends JLabel {

	private static final long serialVersionUID = -6145957887008576078L;

	@Override
	public void setIcon(Icon icon) {
		if (icon instanceof ImageIcon) {
			ImageIcon imageIcon = (ImageIcon) icon;
			Image imagem = imageIcon.getImage();
			Image novaImagem = imagem.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			super.setIcon(new ImageIcon(novaImagem));
		} else {
			super.setIcon(icon);
		}
	}

}
