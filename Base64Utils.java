package com.aisino.bec.platform.integrated.util;

import com.aisino.ams.core.util.StringUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("restriction")
public class Base64Utils {

	/**
	 * 本地图片转换成base64字符串
	 * 
	 * @param imgFile
	 *            图片本地路径
	 * @return
	 *
	 * @author shenxiaozhong
	 * @dateTime 2018-02-23 14:40:46
	 */
	public static String ImageToBase64ByLocal(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

		InputStream in = null;
		byte[] data = null;

		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);

			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();

		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 在线图片转换成base64字符串
	 * 
	 * @param imgURL
	 *            图片线上路径
	 * @return
	 *
	 * @author shenxiaozhong
	 * @dateTime 2018-02-23 14:43:18
	 */
	public static String ImageToBase64ByOnline(String imgURL) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			// 创建URL
			URL url = new URL(imgURL);
			byte[] by = new byte[1024];
			// 创建链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			InputStream is = conn.getInputStream();
			// 将内容读取内存中
			int len = -1;
			while ((len = is.read(by)) != -1) {
				data.write(by, 0, len);
			}
			// 关闭流
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data.toByteArray());
	}

	/**
	 * inputStream转base64
	 * 
	 * @param in
	 * @return
	 */
	public static String getBase64FromInputStream(InputStream in) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;
		// 读取图片字节数组
		try {
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = in.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			data = swapStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new String(Base64.encodeBase64(data));
	}

	/**
	 * base64字符串转换成图片
	 * 
	 * @param imgStr
	 *            base64字符串
	 * @param imgFilePath
	 *            图片存放路径
	 * @return
	 *
	 * @author ZHANGJL
	 * @dateTime 2018-02-23 14:42:17
	 */
	public static boolean Base64ToImage(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片

		if (StringUtil.isEmptyString(imgStr)) // 图像数据为空
			return false;

		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}

			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * base64 转换为byte
	 * 
	 * @param base64Str
	 * @return
	 */
	public static byte[] Base64ToImage(String base64Str) {
		if (StringUtil.isEmptyString(base64Str)) // 图像数据为空
			return null;

		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(base64Str);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			return b;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Description: 将base64编码内容转换为Pdf
	 * 
	 * @param base64编码内容
	 *            shenxiaozhong Create Date: 2015年7月30日 上午9:40:23
	 */
	public static void base64StringToPdf(String base64Content, String filePath) {
		BASE64Decoder decoder = new BASE64Decoder();
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			byte[] bytes = decoder.decodeBuffer(base64Content);// base64编码内容转换为字节数组
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
			bis = new BufferedInputStream(byteInputStream);
			File file = new File(filePath);
			File path = file.getParentFile();
			if (!path.exists()) {
				path.mkdirs();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);

			byte[] buffer = new byte[1024];
			int length = bis.read(buffer);
			while (length != -1) {
				bos.write(buffer, 0, length);
				length = bis.read(buffer);
			}
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(bis, fos, bos);
		}
	}
    /**
     * Description: 将base64编码内容转换为Pdf
     *
     * @param base64编码内容
     *            shenxiaozhong Create Date: 2015年7月30日 上午9:40:23
     */
    public static void base64StringToPdf(InputStream inputStream, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(inputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bis, fos, bos);
        }
    }

	private static void closeStream(BufferedInputStream bis, FileOutputStream fos, BufferedOutputStream bos) {

		try {
			if (bis != null) {
				bis.close();
			}

			if (fos != null) {
				fos.close();
			}

			if (bos != null) {
				bos.close();
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Description: 将pdf文件转换为Base64编码
	 * 
	 * @param 要转的的pdf文件
	 * @Author shenxiaozhong Create Date: 2018年10月24日16:48:12
	 */
	public static String PDFToBase64(File file) {
		BASE64Encoder encoder = new BASE64Encoder();
		FileInputStream fin = null;
		BufferedInputStream bin = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bout = null;
		try {
			fin = new FileInputStream(file);
			bin = new BufferedInputStream(fin);
			baos = new ByteArrayOutputStream();
			bout = new BufferedOutputStream(baos);
			byte[] buffer = new byte[1024];
			int len = bin.read(buffer);
			while (len != -1) {
				bout.write(buffer, 0, len);
				len = bin.read(buffer);
			}
			// 刷新此输出流并强制写出所有缓冲的输出字节
			bout.flush();
			byte[] bytes = baos.toByteArray();
			return encoder.encodeBuffer(bytes).trim();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fin.close();
				bin.close();
				bout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static Object pdfTobase64(InputStream inputStream) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			byte[] by = new byte[1024];
			// 将内容读取内存中
			int len = -1;
			while ((len = inputStream.read(by)) != -1) {
				data.write(by, 0, len);
			}
			// 关闭流
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data.toByteArray());
	}

	public static String ByteToBase64(byte[] data) {// 将字节数组字符串进行Base64编码处理
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();

		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

}
