package com.rs.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;

import com.thoughtworks.xstream.XStream;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * 
 */

public final class NpcXMLHandler {

	private NpcXMLHandler() {
	}

	private static XStream xmlHandler;

	static {
		xmlHandler = new XStream();
		xmlHandler.alias("NPC", Entry.class);

	}

	public static void toXML(String file, Object object) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			xmlHandler.toXML(object, out);
			out.flush();
		} finally {
			out.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXML(String file) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			return (T) xmlHandler.fromXML(in);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return null;
	}

	public static void setXmlHandler(XStream xstream) {
		xmlHandler = xstream;
		xmlHandler.alias("NPC", Entry.class);
	}

}