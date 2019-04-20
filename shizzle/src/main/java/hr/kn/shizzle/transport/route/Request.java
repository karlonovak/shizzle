package hr.kn.shizzle.transport.route;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Request {

	private ObjectMapper objectMapper = new ObjectMapper();

	private Map<String, String> pathParams;
	private String body;

	public <T> T getBody(Class<T> valueType) {
		try {
			return objectMapper.readValue(body, valueType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String param(String param) {
		return pathParams.get(param);
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getPathParams() {
		return pathParams;
	}

	public void setPathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}
}
