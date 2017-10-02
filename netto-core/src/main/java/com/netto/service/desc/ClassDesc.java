package com.netto.service.desc;

import java.util.List;

public class ClassDesc {
	private String className;
	private List<FieldDesc> fields;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<FieldDesc> getFields() {
		return fields;
	}

	public void setFields(List<FieldDesc> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append(className).append("\r\n");
		for (FieldDesc field : fields) {
			sb.append("|__").append("|__").append(field.getFieldType()).append(" ").append(field.getFieldName())
					.append("\r\n");
		}
		return sb.toString();
	}

}
