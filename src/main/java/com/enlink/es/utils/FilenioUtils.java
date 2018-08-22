package com.enlink.es.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO文件处理（Java7-新特性）
 * 
 * @author changgq
 *
 */
public class FilenioUtils {

	/**
	 * 复制文件
	 * 
	 * @param sourcePath
	 * @param destPath
	 * @throws Exception
	 */
	public static void copyFile(String sourcePath, String destPath) throws Exception {
		File source = new File(sourcePath);
		File dest = new File(destPath);
		if (!dest.exists()) {
			dest.createNewFile();
		}
		try (FileInputStream fis = new FileInputStream(source);
				FileOutputStream fos = new FileOutputStream(dest);
				FileChannel sourceCh = fis.getChannel();
				FileChannel destCh = fos.getChannel();) {

			MappedByteBuffer mbb = sourceCh.map(FileChannel.MapMode.READ_ONLY, 0, sourceCh.size());
			destCh.write(mbb);
		} catch (IOException exce) {
			throw exce;
		}
	}

	/**
	 * 利用NIO将内容输出到文件中
	 * 
	 * @param filePath
	 * @param content
	 */
	public static void writeFile(String filePath, String content) {
		try (FileOutputStream fos = new FileOutputStream(filePath);
				// 获取一个通道
				FileChannel fc = fos.getChannel();) {

			// 第二步 定义缓冲区
			ByteBuffer buffer = ByteBuffer.wrap(content.getBytes());
			// 将内容写到缓冲区
			fos.flush();
			fc.write(buffer);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
