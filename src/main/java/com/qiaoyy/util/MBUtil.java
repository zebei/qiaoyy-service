package com.qiaoyy.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.UUID;

public class MBUtil {

	public static boolean checkParmsValid(Object... objs) {
		
		for (Object object : objs) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	public static String getUUID() {

		UUID uuid = UUID.randomUUID();
		// 得到对象产生的ID
		String a = uuid.toString();
		a = a.toUpperCase();
		// 替换 -
		a = a.replaceAll("-", "");
		return a;
	}

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static String getRequestBodyJson(InputStream inputStream) throws IOException {
		Reader input = new InputStreamReader(inputStream);
		Writer output = new StringWriter();
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toString();
	}
}
