package com.dejans.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class MyObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyObjectMapper () {
		this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    this.setDateFormat(new ISO8601DateFormat());
	}

}
