package cn.sbx.deeper.moblie.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	private ZipFile         zipFile; 
    private ZipOutputStream zipOut;     
    private  int      		bufSize;   
    private byte[]          buf; 
    private int             readedBytes; 
    public ZipUtil(){ 
        this(512); 
    } 

    public ZipUtil(int bufSize){ 
        this.bufSize = bufSize; 
        this.buf = new byte[this.bufSize]; 
    } 
     
	public void doZip(String srcFile, String destFile) {
		File zipDir;
		String dirName;

		zipDir = new File(srcFile);
		dirName = zipDir.getName();
		try {
			this.zipOut = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(destFile)));
			zipOut.setComment("comment");
//			zipOut.setEncoding("GBK");
			zipOut.setMethod(ZipOutputStream.DEFLATED); 
			zipOut.setLevel(Deflater.BEST_COMPRESSION); 
			handleDir(zipDir, this.zipOut,dirName);
			this.zipOut.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	private void handleDir(File dir, ZipOutputStream zipOut,String dirName) throws IOException {
		System.out.println("����Ŀ¼��"+dir.getName());
		FileInputStream fileIn;
		File[] files;

		files = dir.listFiles();

		if (files.length == 0) {
			System.out.println("ѹ���ġ�Name:"+dirName);
			this.zipOut.putNextEntry(new ZipEntry(dirName));
			this.zipOut.closeEntry();
		} else {
			for (File fileName : files) {
				if (fileName.isDirectory()) {
					handleDir(fileName, this.zipOut,dirName+File.separator+fileName.getName()+File.separator);
				} else {
					System.out.println("ѹ���ġ�Name:"+dirName + File.separator+fileName.getName());
					fileIn = new FileInputStream(fileName);
					this.zipOut.putNextEntry(new ZipEntry(dirName + File.separator+fileName.getName()));
					while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
						this.zipOut.write(this.buf, 0, this.readedBytes);
					}
					this.zipOut.closeEntry();
				}
			}
		}
	}

	public void unZip(String unZipfile, String destFile) {
//		FileOutputStream fileOut;
//		File file;
//		InputStream inputStream;
//		try {
//			this.zipFile = new ZipFile(unZipfile);
//			for (@SuppressWarnings("rawtypes")
//			Enumeration entries = this.zipFile.getEntries(); entries
//					.hasMoreElements();) {
//				ZipEntry entry = (ZipEntry) entries.nextElement();
//				file = new File(destFile+File.separator+entry.getName());
//
//				if (entry.isDirectory()) {
//					file.mkdirs();
//				} else {
//					File parent = file.getParentFile();
//					if (!parent.exists()) {
//						parent.mkdirs();
//					}
//					inputStream = zipFile.getInputStream(entry);
//					fileOut = new FileOutputStream(file);
//					while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
//						fileOut.write(this.buf, 0, this.readedBytes);
//					}
//					fileOut.close();
//
//					inputStream.close();
//				}
//			}
//			this.zipFile.close();
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
	}
	public void setBufSize(int bufSize) {
		this.bufSize = bufSize;
	}
}
