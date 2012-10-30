package de.uni.due.paluno.casestudy.ui;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractDTO {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
		try {
			return (objectMapper.writeValueAsString(this));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return (e.getClass() + " : " + e.getMessage());
		}
	}
}
