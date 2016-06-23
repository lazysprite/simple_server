package server.core.hotswap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class SourceLoader {

	/**
	 * 读取路径下的.class文件
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws SourceFileTypeException 
	 */
	public static byte[] loadSourceFile(String path) throws IOException, SourceFileTypeException {
		if (!path.endsWith(".class")) {
			throw new SourceFileTypeException("You need Input the .class file path!!! Not the '" + path + "'");
		}
		File file = new File(path);
		if (!file.isFile() || !file.exists()) {
			throw new FileNotFoundException("Path: " + path);
		}
		
		InputStream in = new FileInputStream(file);
		int length = in.available();
		byte[] buf = new byte[length];
		in.read(buf, 0, buf.length);
		in.close();
		
		return buf;
	}
	
}
