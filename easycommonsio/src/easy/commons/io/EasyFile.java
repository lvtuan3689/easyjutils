package easy.commons.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import com.sun.istack.internal.Nullable;

public class EasyFile {
	/**
	 * Create new File
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	@Nullable
	public static File makeFile(String path) {
		String vpath = EasyString.str(path);
		File file = new File(vpath);
		if(file.exists()) {
			return file;
		}
		return null;
	}
	/**
	 * Create new File
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	@Nullable
	public static File makeFile(String path,boolean createIfNotExist) {
		String vpath = EasyString.str(path);
		File file = new File(vpath);
		if(file.exists()) {
			return file;
		}
		if(createIfNotExist) {
			Charset charset = Charset.forName("UTF-8");
			String s = "";
			try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset)) {
			    writer.write(s, 0, s.length());
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			    return null;
			}
			return file;
		}
		return null;
	}
	/**
	 * Create new Folder
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	@Nullable
	public static File mkdir(String path) {
		String vpath = EasyString.str(path);
		File file = new File(vpath);
		if(file.exists() && file.isDirectory()) {
			return file;
		}
		return null;
	}
	/**
	 * Create new File
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	@Nullable
	public static File mkdir(String path,boolean createIfNotExist) {
		String vpath = EasyString.str(path);
		File file = new File(vpath);
		if(file.exists() && file.isDirectory()) {
			return file;
		}
		if(createIfNotExist) {
			if(file.mkdirs()) {
				return file;
			}
		}
		return null;
	}
}
