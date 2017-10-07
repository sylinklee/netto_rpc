package com.netto.core.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Constants {

	public static String PROTOCOL_REQUEST_DELIMITER = "\r\n";
	public static String SERVICE_HEADER = "service";
	public static String METHOD_HEADER = "method";
	public static String ARGSLEN_HEADER = "argsLen";

	public static ByteBuf[] delimiterAsByteBufArray() {
		return new ByteBuf[] { Unpooled.wrappedBuffer(new byte[] { '\r', '\n' }),
				Unpooled.wrappedBuffer(new byte[] { '\n' }),

		};
	}

}
