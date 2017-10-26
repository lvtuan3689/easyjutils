package easy.commons.io;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.eps.EPSCanvasProvider;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;
import org.w3c.dom.DocumentFragment;

public class EasyBarcode {
	public enum OutputType {
		SVG, EPS, BMP, AWT
	};

	private DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
	/**
	 * static Singleton instance.
	 */
	private static volatile EasyBarcode instance;

	/**
	 * Private constructor for singleton.
	 */
	private EasyBarcode() {
	}

	/**
	 * Return a singleton instance of EasyBarcode.
	 */
	public static EasyBarcode getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (EasyBarcode.class) {
				if (instance == null) {
					instance = new EasyBarcode();
				}
			}
		}
		return instance;
	}

	public boolean buildFromXML(String xmlConfigFile, String value, OutputType type, String outputPath,
			Graphics2D g2d) {
		File config = EasyFile.makeFile(xmlConfigFile);
		String msg = EasyString.str(value, "");
		if (config == null) {
			EasyConsole.display("File not found: " + xmlConfigFile);
			return false;
		}
		File output = new File(outputPath);
		try {
			Configuration cfg = builder.buildFromFile(config);
			BarcodeGenerator gen = BarcodeUtil.getInstance().createBarcodeGenerator(cfg);
			switch (type) {
			case BMP:
				OutputStream out = new java.io.FileOutputStream(output);
				BitmapCanvasProvider provider = new BitmapCanvasProvider(out, "image/x-png", 700,
						BufferedImage.TYPE_BYTE_GRAY, true, 0);
				gen.generateBarcode(provider, msg);
				provider.finish();
				break;
			case SVG:
				DocumentFragment doc = BarcodeUtil.getInstance().generateSVGBarcode(cfg, msg);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer trans = factory.newTransformer();
				Source src = new DOMSource(doc);
				Result res = new StreamResult(output);
				trans.transform(src, res);
				break;
			case EPS:
				OutputStream epsout = new java.io.FileOutputStream(output);
				EPSCanvasProvider epsprovider = new EPSCanvasProvider(epsout, 0);
				gen.generateBarcode(epsprovider, msg);
				epsprovider.finish();
				break;
			case AWT:
				if (g2d != null) {
					g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
							RenderingHints.VALUE_FRACTIONALMETRICS_ON);
					Java2DCanvasProvider d2provider = new Java2DCanvasProvider(g2d, 0);
					gen.generateBarcode(d2provider, msg);
					return true;
				}
			default:
				break;
			}
		} catch (Exception e) {
			EasyConsole.display(e);
		}
		return output.exists();
	}
}
