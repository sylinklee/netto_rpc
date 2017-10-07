package com.netto.core.message;

import io.netty.buffer.ByteBuf;

public class NettoFrame {
	public static String NETTO_HEADER_START = "NETTO:";
	public static String SIGNATURE_HEADER = "signature";
	public static int HEADER_LENGTH = 64;

	private int bodySize = 0;

	private int headerContentSize = 0;

	private ByteBuf headerContent;

	private ByteBuf body;

	public int getBodySize() {
		return bodySize;
	}

	public void setBodySize(int bodySize) {
		this.bodySize = bodySize;
	}

	public int getHeaderContentSize() {
		return headerContentSize;
	}

	public void setHeaderContentSize(int headerContentSize) {
		this.headerContentSize = headerContentSize;
	}

	public ByteBuf getHeaderContent() {
		return headerContent;
	}

	public void setHeaderContent(ByteBuf headerContent) {
		this.headerContent = headerContent;
	}

	public ByteBuf getBody() {
		return body;
	}

	public void setBody(ByteBuf body) {
		this.body = body;
	}

}
