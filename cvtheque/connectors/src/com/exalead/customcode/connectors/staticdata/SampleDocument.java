package com.exalead.customcode.connectors.staticdata;

import com.exalead.config.bean.IsMandatory;
import com.exalead.config.bean.PropertyDescription;
import com.exalead.config.bean.PropertyLabel;

/**
 * Document configuration
 * It contains pushed information as the uri, stamp, metas and parts
 */
public class SampleDocument {
	private String uri;
	private String stamp;
	private SampleMeta[] metas;
	private SamplePart[] parts;

	/**
	 * Meta configuration
	 */
	public static class SampleMeta {
		private String name;
		private String value;

		public String getName() {
			return name;
		}
		@IsMandatory(true)
        @PropertyLabel("Name")
        @PropertyDescription("Meta's name")
		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}
		@IsMandatory(true)
        @PropertyLabel("Value")
        @PropertyDescription("Meta's value")
		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * Part configuration
	 */
	public static class SamplePart {
		private String value;

		public String getValue() {
			return value;
		}
		@IsMandatory(true)
        @PropertyLabel("Value")
        @PropertyDescription("Part's value")
		public void setValue(String value) {
			this.value = value;
		}
	}

	public String getUri() {
		return uri;
	}
	@IsMandatory(true)
    @PropertyLabel("URI")
    @PropertyDescription("Document's URI")
	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getStamp() {
		return stamp;
	}
	@IsMandatory(true)
    @PropertyLabel("Stamp")
    @PropertyDescription("Document's Stamp")
	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public SampleMeta[] getMetas() {
		return metas;
	}
	@IsMandatory(true)
    @PropertyLabel("Metas")
    @PropertyDescription("Document's Metas")
	public void setMetas(SampleMeta[] metas) {
		this.metas = metas;
	}

	public SamplePart[] getParts() {
		return parts;
	}
	@IsMandatory(true)
    @PropertyLabel("Parts")
    @PropertyDescription("Document's Parts")
	public void setparts(SamplePart[] parts) {
		this.parts = parts;
	}
}
