package easy.commons.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class EasyFile {
	public static final long MAX_FILE_SIZE = 25600;
	public static final long MAX_MEM_SIZE = 4096;

	/**
	 * Create new File
	 * 
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	@Nullable
	public static File makeFile(String path) {
		String vpath = EasyString.str(path, "");
		File file = new File(vpath);
		if (file.exists()) {
			return file;
		}
		return null;
	}

	/**
	 * Create new File
	 * 
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	@Nullable
	public static File makeFile(String path, boolean createIfNotExist) {
		String vpath = EasyString.str(path, "");
		File file = new File(vpath);
		if (file.exists()) {
			return file;
		}
		if (createIfNotExist) {
			try {
				if (file.createNewFile()) {
					return file;
				}
			} catch (Exception e) {
				EasyConsole.display(e);
			}
		}
		return null;
	}

	/**
	 * Create new File
	 * 
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	public static boolean appendToFile(String filePath, String... content) {
		File file = makeFile(filePath, true);
		if (file == null) {
			EasyConsole.display("Can not write to file: " + filePath);
			return false;
		}
		BufferedWriter bw = null;
		FileWriter fw = null;
		boolean res = false;
		try {
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			for (String str : content) {
				bw.write(str);
			}
			res = true;
		} catch (Exception e) {
			EasyConsole.display(e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
		}
		return res;
	}

	/**
	 * Create new File
	 * 
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	public static boolean writeToFile(String filePath, String... content) {
		File file = makeFile(filePath, true);
		if (file == null) {
			EasyConsole.display("Can not write to file: " + filePath);
			return false;
		}
		BufferedWriter bw = null;
		FileWriter fw = null;
		boolean res = false;
		try {
			fw = new FileWriter(file.getAbsoluteFile(), false);
			bw = new BufferedWriter(fw);
			for (String str : content) {
				bw.write(str);
			}
			res = true;
		} catch (IOException e) {
			EasyConsole.display(e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
		}
		return res;
	}

	/**
	 * Create new Folder
	 * 
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	@Nullable
	public static File mkdir(String path) {
		String vpath = EasyString.str(path, "");
		File file = new File(vpath);
		if (file.exists() && file.isDirectory()) {
			return file;
		}
		return null;
	}

	/**
	 * Create new File
	 * 
	 * @param path
	 * @param createIfNotExist
	 * @return
	 */
	@Nullable
	public static File mkdir(String path, boolean createIfNotExist) {
		String vpath = EasyString.str(path, "");
		File file = new File(vpath);
		if (file.exists() && file.isDirectory()) {
			return file;
		}
		if (createIfNotExist) {
			if (file.mkdirs()) {
				return file;
			}
		}
		return null;
	}

	/**
	 * Get check sum of the file
	 * 
	 * @param file
	 *            as String
	 * @return
	 */
	public static String getCheckSum(String filePath) {
		return getCheckSum(new File(filePath));
	}

	/**
	 * Get check sum of the file
	 * 
	 * @param file
	 *            as File
	 * @return
	 */
	@NotNull
	public static String getCheckSum(File file) {
		FileInputStream fis = null;
		String md5 = "";
		try {
			fis = new FileInputStream(file);
			md5 = DigestUtils.md5Hex(fis);
		} catch (Exception e) {
			EasyConsole.display(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
		}
		return md5;
	}

	/**
	 * 
	 * @param file
	 * @return String as Extension exclude dot (.)
	 */
	@NotNull
	public static String getFileExtension(String file) {
		return getFileExtension(new File(file));
	}

	/**
	 * 
	 * @param file
	 * @return String as Extension exclude dot (.)
	 */
	@NotNull
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

	/**
	 * Edit properties files
	 * 
	 * @param src
	 * @param value[]
	 */
	public static boolean editPropertiesFile(String src, String... values) {
		return editPropertiesFile(new File(src), values);
	}

	/**
	 * Edit properties files
	 * 
	 * @param sourceFile
	 * @param value[]
	 */
	public static boolean editPropertiesFile(File sourceFile, String... values) {
		FileWriter fw = null;
		InputStream is = null;
		boolean res = false;
		try {
			// Check file exist
			String src = sourceFile.getAbsolutePath();
			if (!sourceFile.exists()) {
				EasyConsole.display("File not found:  " + src);
				return false;
			}
			// Read all properties
			is = getInputStream(src);
			if (is != null) {
				PropertiesConfiguration propFile = new PropertiesConfiguration();
				PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();
				layout.load(propFile, new InputStreamReader(is));
				propFile.setLayout(layout);
				// Edit file by values string array
				String key = "", val = "", action = "";
				StringBuilder comment = new StringBuilder();
				String strComment = "";
				for (String line : values) {
					if (line.trim().length() < 3 || line.startsWith("#")) {
						if (comment.length() > 0) {
							comment.append(EasyConstant.NEW_LINE);
						}
						comment.append(line);
						continue;
					}
					action = String.valueOf(line.charAt(0));
					if (line.indexOf("=") > 0) {
						key = line.substring(1, line.indexOf("="));
					} else {
						key = line.substring(1);
					}
					if (line.indexOf("=") > 1 && line.length() > line.indexOf("=") + 1) {
						val = line.substring(line.indexOf("=") + 1);
					} else {
						val = "";
					}
					strComment = comment.toString();
					comment.setLength(0);
					switch (action) {
					case "+":
					case "*":
						if (propFile.containsKey(key)) {
							propFile.setProperty(key, val);
							if (strComment.trim().length() > 0) {
								propFile.getLayout().setComment(key, strComment);
							}
						} else {
							propFile.addProperty(key, val);
							if (strComment.trim().length() > 0) {
								propFile.getLayout().setComment(key, strComment);
							}
						}
						break;
					case "-":
						if (propFile.containsKey(key)) {
							propFile.clearProperty(key);
						}
						break;
					default:
						break;
					}
				}
				fw = new FileWriter(src, false);
				propFile.getLayout().save(propFile, fw);
				res = true;
			}
		} catch (Exception e) {
			EasyConsole.display(e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
		}
		return res;
	}

	/**
	 * Gets a stream for a resource
	 * 
	 * @param fileName
	 *            the resource file name
	 * @return the InputStream
	 */
	public static InputStream getInputStream(String fileName) {
		InputStream result = null;
		try {
			if (result == null) {
				// This check causes a problem where files are contained
				// in a jar in the default package. The file will not load.
				// if ( new File( fileName ).getParent() != null )
				if (fileName.indexOf("http") == 0) {
					try {
						URL url = new URL(fileName);
						result = url.openStream();
					} catch (Exception e) {
						EasyConsole.display("Cannot get input stream with " + fileName, e);
						result = null;
					}
				}
			}
			if (result == null) {
				result = new FileInputStream(fileName);
			}
		} catch (Exception ex1) {
			result = null;
			EasyConsole.display("getInputStream() Failed.", ex1);
		}
		return result;
	}

	/**
	 * Get list of all files recursively by iterating through sub directories
	 * 
	 * @param listFiles
	 * @param inputDirectory
	 * @return
	 * @throws IOException
	 */
	public static List<File> listFiles(List<File> listFiles, File inputDirectory) {
		File[] allFiles = inputDirectory.listFiles();
		if (allFiles != null) {
			for (File file : allFiles) {
				if (file.isDirectory()) {
					listFiles(listFiles, file);
				} else {
					listFiles.add(file);
				}
			}
		}
		return listFiles;
	}

	/**
	 * Create ZIP package from directory
	 * 
	 * @param inputDir
	 * @param outputZipFile
	 * @return
	 */
	public static File zipDir(String inputDir, String outputZipFile) {
		File inputFolder = mkdir(EasyString.str(inputDir, ""));
		if (inputFolder == null) {
			EasyConsole.display("File not found: " + inputDir);
			return null;
		}
		String outputpath = EasyString.str(outputZipFile, "").trim();
		File outputFile = new File(outputpath);
		if (outputpath.isEmpty()) {
			EasyConsole.display("ZIP name is invalid: " + outputZipFile);
			return null;
		}
		List<File> listFiles = new ArrayList<File>();
		listFiles = listFiles(listFiles, inputFolder);
		if (listFiles.size() == 0) {
			EasyConsole.display("Empty folder: " + inputDir);
			return null;
		}
		FileInputStream inputStream = null;
		String filePath = "", zipFilePath = "";
		ZipEntry zipEntry = null;
		ZipOutputStream zipOutputStream = null;
		int lengthFilePath = 0;
		byte[] bytes = null;
		int length = 0;
		try {
			zipOutputStream = new ZipOutputStream(new FileOutputStream(outputpath));
			int lengthDirectoryPath = inputFolder.getCanonicalPath().length();
			for (File file : listFiles) {
				filePath = file.getCanonicalPath();
				lengthFilePath = file.getCanonicalPath().length();

				// Get path of files relative to input directory.
				zipFilePath = filePath.substring(lengthDirectoryPath + 1, lengthFilePath);

				zipEntry = new ZipEntry(zipFilePath);
				zipOutputStream.putNextEntry(zipEntry);

				inputStream = new FileInputStream(file);
				bytes = new byte[1024];
				while ((length = inputStream.read(bytes)) >= 0) {
					zipOutputStream.write(bytes, 0, length);
				}
				inputStream.close();
			}
		} catch (Exception e) {
			EasyConsole.display(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
			if (zipOutputStream != null) {
				try {
					zipOutputStream.closeEntry();
					zipOutputStream.close();
				} catch (IOException e) {
					EasyConsole.display(e);
				}
			}
		}
		return outputFile.exists() ? outputFile : null;
	}

	/**
	 * Convert file to hex string
	 * 
	 * @param out
	 * @param file
	 * @throws IOException
	 */
	public static String convertToHex(String filePath) {
		File file = makeFile(filePath);
		if (file == null) {
			EasyConsole.display("File not found:  " + filePath);
			return "";
		}
		InputStream is = null;
		int bytesCounter = 0;
		int value = 0;
		StringBuilder sbHex = new StringBuilder();
		StringBuilder sbText = new StringBuilder();
		StringBuilder sbResult = new StringBuilder();
		try {
			is = new FileInputStream(file);
			while ((value = is.read()) != -1) {
				// convert to hex value with "X" formatter
				sbHex.append(String.format("%02X ", value));

				// If the character is not convertible, just print a dot symbol "."
				if (!Character.isISOControl(value)) {
					sbText.append((char) value);
				} else {
					sbText.append(".");
				}

				// if 16 bytes are read, reset the counter,
				// clear the StringBuilder for formatting purpose only.
				if (bytesCounter == 15) {
					sbResult.append(sbHex);
					sbHex.setLength(0);
					sbText.setLength(0);
					bytesCounter = 0;
				} else {
					bytesCounter++;
				}
			}
		} catch (Exception e) {
			EasyConsole.display(e);
		}
		try {
			if (is != null)
				is.close();
		} catch (IOException e) {
			EasyConsole.display(e);
		}
		return sbResult.toString();
	}

	/**
	 * Get file content as list of string
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<String> getFileAsLines(String filePath) {
		List<String> res = new LinkedList<String>();
		File file = makeFile(filePath);
		if (file == null) {
			EasyConsole.display("File not found:  " + filePath);
			return res;
		}
		try {
			res = FileUtils.readLines(file, "UTF-8");
		} catch (Exception e) {
			EasyConsole.display(e);
		}
		return res;
	}

	/**
	 * Get file content as string
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileAsString(String filePath) {
		File file = makeFile(filePath);
		if (file == null) {
			EasyConsole.display("File not found:  " + filePath);
			return "";
		}
		String res = "";
		try {
			res = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			EasyConsole.display(e);
		}
		return res;
	}

	/**
	 * Load single properties file
	 * 
	 * @param filePath
	 * @param initVector
	 *            16 byte long
	 * @param key
	 *            16 byte long
	 * @return
	 */
	public static LinkedHashMap<String, String> loadPropertiesFile(String filePath, String initVector, String key) {
		LinkedHashMap<String, String> configProperties = new LinkedHashMap<String, String>();
		File propertiesFile = makeFile(filePath);
		if (!EasyConstant.EXT_PROPERTIES_FILE.equalsIgnoreCase(getFileExtension(propertiesFile))) {
			EasyConsole.display("File does not a properties file");
			return configProperties;
		}
		if (propertiesFile == null) {
			EasyConsole.display("File not found:  " + filePath);
			return configProperties;
		}
		InputStream is = null;
		Properties propFile = null;
		Enumeration<?> keysEnum = null;
		String name = "", value = "";
		try {
			EasyConsole.display("Reading configuration file: " + filePath);
			is = new FileInputStream(propertiesFile);
			if (is != null) {
				propFile = new Properties();
				propFile.load(is);
				keysEnum = propFile.keys();
				while (keysEnum.hasMoreElements()) {
					name = (String) keysEnum.nextElement();
					value = propFile.getProperty(name);
					if (value != null) {
						value = value.trim();
						if (value.startsWith("!") && value.length() > 1) {
							value = value.substring(1);
							editPropertiesFile(propertiesFile,
									"*" + name + "={AES}" + EasyString.getEncrypted(initVector, key, value));
						} else if (value.startsWith("{AES}") && value.length() > 5) {
							String encryptedText = value.substring(5);
							value = EasyString.getDecrypted(initVector, key, encryptedText);
						}
						configProperties.put(name, value);
					}
				}
			}
		} catch (Exception e1) {
			EasyConsole.display(e1);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				EasyConsole.display("Close input stream with error");
			}
		}
		return configProperties;
	}

	/**
	 * Load all properties file in directory
	 * 
	 * @param dirPath
	 * @param initVector
	 *            16 byte long
	 * @param key
	 *            16 byte long
	 * @return
	 */
	public static LinkedHashMap<String, String> loadAllPropertiesFile(String dirPath, String initVector, String key) {
		LinkedHashMap<String, String> configProperties = new LinkedHashMap<String, String>();
		File propertiesFiles = makeFile(dirPath);
		File[] allFiles = propertiesFiles.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return EasyConstant.EXT_PROPERTIES_FILE.equalsIgnoreCase(getFileExtension(pathname));
			}
		});
		InputStream is = null;
		Properties propFile = null;
		Enumeration<?> keysEnum = null;
		String name = "", value = "";
		try {
			for (File propertiesFile : allFiles) {
				EasyConsole.display("Reading configuration file: " + propertiesFile.getAbsolutePath());
				is = new FileInputStream(propertiesFile);
				if (is != null) {
					propFile = new Properties();
					propFile.load(is);
					keysEnum = propFile.keys();
					while (keysEnum.hasMoreElements()) {
						name = (String) keysEnum.nextElement();
						value = propFile.getProperty(name);
						if (value != null) {
							value = value.trim();
							if (value.startsWith("!") && value.length() > 1) {
								value = value.substring(1);
								editPropertiesFile(propertiesFile,
										"*" + name + "={AES}" + EasyString.getEncrypted(initVector, key, value));
							} else if (value.startsWith("{AES}") && value.length() > 5) {
								String encryptedText = value.substring(5);
								value = EasyString.getDecrypted(initVector, key, encryptedText);
							}
							configProperties.put(name, value);
						}
					}
					is.close();
				}
			}
		} catch (Exception e1) {
			EasyConsole.display(e1);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				EasyConsole.display("Close input stream with error");
			}
		}
		return configProperties;
	}

	/**
	 * Get list of file as string via SFTP
	 * 
	 * @param host
	 * @param usr
	 * @param passwd
	 * @param port
	 * @param srcFolder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String[] listFilesSFTP(String host, String usr, String passwd, int port, String srcFolder) {
		JSch jsch = new JSch();
		JSch.setConfig("StrictHostKeyChecking", "no");
		Session session = null;
		ChannelSftp cFTP = null;
		Channel channel = null;
		LinkedList<String> list = new LinkedList<String>();
		try {
			session = jsch.getSession(usr, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passwd);
			session.setTimeout(5000);
			session.connect();

			channel = session.openChannel(EasyConstant.SFTP);
			channel.connect();
			cFTP = (ChannelSftp) channel;
			Vector<LsEntry> vFiles = cFTP.ls(srcFolder);
			cFTP.disconnect();
			session.disconnect();
			Enumeration<LsEntry> vEnum = vFiles.elements();
			while (vEnum.hasMoreElements()) {
				String filename = vEnum.nextElement().getFilename();
				if (!filename.equals("..") && !filename.equals(".") && filename.endsWith(".zip")) {
					list.add(filename);
				}
			}
		} catch (Exception e) {
			EasyConsole.display(e);
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
			if (cFTP != null) {
				cFTP.disconnect();
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Get list of file as String via FTP
	 * 
	 * @param host
	 * @param usr
	 * @param passwd
	 * @param port
	 * @param srcFolder
	 * @return
	 */
	public static String[] listFilesFTP(String host, String usr, String passwd, int port, String srcFolder) {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setConnectTimeout(5000);
		InetAddress inetAddress;
		try {
			inetAddress = Inet4Address.getByName(host);
			ftpClient.enterLocalPassiveMode();
			ftpClient.connect(inetAddress, port);
			ftpClient.login(usr, passwd);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] ftpFiles = ftpClient.listFiles();
			LinkedList<String> list = new LinkedList<String>();
			for (FTPFile ftpFile : ftpFiles) {
				list.add(ftpFile.getName());
			}
			return list.toArray(new String[list.size()]);
		} catch (Exception e) {
			EasyConsole.display(e);
		}
		return null;
	}

	/**
	 * Download file via SFTP
	 * 
	 * @param host
	 * @param usr
	 * @param passwd
	 * @param port
	 * @param srcFolder
	 * @param dstFolder
	 * @param fileNames
	 * @return
	 */
	@Nullable
	public static synchronized File[] getFilesSFTP(String host, String usr, String passwd, int port, String srcFolder,
			String dstFolder, String... fileNames) {
		LinkedList<File> allFile = new LinkedList<File>();
		File dst = new File(dstFolder);
		if (!dst.exists() || !dst.isDirectory()) {
			EasyConsole.display("Destination folder does not exists");
			return null;
		}
		String dstFilePath = null;
		String srcFilePath = null;
		JSch jsch = new JSch();
		JSch.setConfig("StrictHostKeyChecking", "no");
		Session session = null;
		ChannelSftp cFTP = null;
		Channel channel = null;
		try {
			session = jsch.getSession(usr, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passwd);
			session.setTimeout(15000);
			session.connect();
			channel = session.openChannel(EasyConstant.SFTP);
			channel.connect();
			cFTP = (ChannelSftp) channel;
			for (String fileName : fileNames) {
				dstFilePath = dstFolder + File.separator + fileName;
				srcFilePath = srcFolder + File.separator + fileName;
				cFTP.get(srcFilePath, dstFilePath);
				allFile.add(new File(dstFilePath));
			}
		} catch (Exception e) {
			EasyConsole.display(e);
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
			if (cFTP != null) {
				cFTP.disconnect();
			}
		}
		return allFile.toArray(new File[allFile.size()]);
	}

	/**
	 * Download file via FTP
	 * 
	 * @param host
	 * @param usr
	 * @param passwd
	 * @param port
	 * @param srcFolder
	 * @param dstFolder
	 * @param fileNames
	 * @return
	 */
	@Nullable
	public static synchronized File[] getFilesFTP(String host, String usr, String passwd, int port, String srcFolder,
			String dstFolder, String... fileNames) {
		LinkedList<File> allFile = new LinkedList<File>();
		File dst = new File(dstFolder);
		if (!dst.exists() || !dst.isDirectory()) {
			EasyConsole.display("Destination folder does not exists");
			return null;
		}
		String dstFilePath = null;
		String srcFilePath = null;
		FTPClient ftpClient = new FTPClient();
		ftpClient.setConnectTimeout(5000);
		InetAddress inetAddress;
		try {
			inetAddress = Inet4Address.getByName(host);
			ftpClient.connect(inetAddress, port);
			ftpClient.login(usr, passwd);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			for (String fileName : fileNames) {
				dstFilePath = dstFolder + File.separator + fileName;
				srcFilePath = srcFolder + File.separator + fileName;
				File target = new File(dstFilePath);
				try (FileOutputStream fileOutputStream = new FileOutputStream(target)) {
					ftpClient.retrieveFile(srcFilePath, fileOutputStream);
					allFile.add(target);
				}
			}
		} catch (Exception e) {
			EasyConsole.display(e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				EasyConsole.display(e);
			}
		}
		return allFile.toArray(new File[allFile.size()]);
	}

	/**
	 * Upload file via SFTP
	 * 
	 * @param host
	 * @param usr
	 * @param passwd
	 * @param port
	 * @param srcFolder
	 * @param dstFolder
	 */
	public static synchronized void putFilesViaSFTP(String host, String usr, String passwd, int port, String srcFolder,
			String dstFolder) {
		File src = new File(srcFolder);
		if (!src.exists() || !src.isDirectory()) {
			EasyConsole.display("Source folder does not exists");
			return;
		}
		JSch jsch = new JSch();
		JSch.setConfig("StrictHostKeyChecking", "no");
		Session session = null;
		ChannelSftp cFTP = null;
		Channel channel = null;
		try {
			session = jsch.getSession(usr, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passwd);
			session.connect();
			channel = session.openChannel(EasyConstant.SFTP);
			channel.connect();
			cFTP = (ChannelSftp) channel;
			String[] fileNames = src.list();
			if (fileNames != null) {
				for (String fileName : fileNames) {
					cFTP.put(srcFolder + File.separator + fileName, dstFolder + File.separator + fileName);
				}
			}
		} catch (Exception e) {
			EasyConsole.display(e);
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
			if (cFTP != null) {
				cFTP.disconnect();
			}
		}
	}

	/**
	 * Get total files size
	 * 
	 * @param files
	 * @return
	 */
	public static long getFilesSize(String... files) {
		long total = 0L;
		File f = null;
		try {
			for (String file : files) {
				f = makeFile(file);
				if (f != null) {
					total += Files.size(f.toPath());
				}
			}
		} catch (IOException e) {
			EasyConsole.display(e);
		}
		return total;
	}

	/**
	 * Upload a files to Servlet server
	 * 
	 * @param uploadPath
	 * @param request
	 * @return
	 */
	public static boolean uploadTo(String uploadPath, HttpServletRequest request) {
		File uploadFolder = EasyFile.makeFile(uploadPath, true);
		if (uploadFolder == null) {
			EasyConsole.display("Upload folder not found");
			return false;
		}
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			EasyConsole.display("No file uploaded");
			return false;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold((int) MAX_MEM_SIZE);
		factory.setRepository(uploadFolder);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax((int) MAX_FILE_SIZE);
		try {
			List fileItems = upload.parseRequest(request);
			Iterator i = fileItems.iterator();
			String fieldName = "", fileName = "", contentType = "";
			boolean isInMemory = false;
			long sizeInBytes = 0L;
			File file = null;
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					fieldName = fi.getFieldName();
					fileName = fi.getName();
					contentType = fi.getContentType();
					isInMemory = fi.isInMemory();
					sizeInBytes = fi.getSize();
					if (fileName.lastIndexOf("\\") >= 0) {
						file = new File(uploadPath + fileName.substring(fileName.lastIndexOf("\\")));
					} else {
						file = new File(uploadPath + fileName.substring(fileName.lastIndexOf("\\") + 1));
					}
					fi.write(file);
					EasyConsole.display("Uploaded Filename: " + fileName);
				}
			}
		} catch (Exception ex) {
			EasyConsole.display(ex);
		}
		return true;
	}
}