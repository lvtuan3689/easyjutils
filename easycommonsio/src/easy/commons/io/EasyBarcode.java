package easy.commons.io;

import java.io.File;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;

public class EasyBarcode {
	private DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();

	public boolean build(String xmlConfigFile) {
		File config = EasyFile.makeFile(xmlConfigFile);
		if (config == null) {
			EasyConsole.display("File not found: " + xmlConfigFile);
			return false;
		}
		try {
			Configuration cfg = builder.buildFromFile(config);
			return true;
		} catch (Exception e) {
			EasyConsole.display(e);
		}
		return false;
	}
}
