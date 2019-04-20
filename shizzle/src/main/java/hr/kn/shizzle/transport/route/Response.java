package hr.kn.shizzle.transport.route;

import java.util.Map;

import io.netty.handler.codec.http.HttpResponseStatus;

public class Response {

	private Map<String, String> headers;
	private HttpResponseStatus status;
	private Object body;

	public static Response ok(Object body) {
		Response response = new Response();
		response.body = body;
		response.status = HttpResponseStatus.OK;
		return response;
	}

	public static Response ok() {
		Response response = new Response();
		response.status = HttpResponseStatus.OK;
		return response;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public HttpResponseStatus getStatus() {
		return status;
	}

	public void setStatus(HttpResponseStatus status) {
		this.status = status;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

}
