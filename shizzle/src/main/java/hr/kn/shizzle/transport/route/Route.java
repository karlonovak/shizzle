package hr.kn.shizzle.transport.route;

import io.netty.handler.codec.http.HttpMethod;

public class Route {

	private HttpMethod method;
	private String uri;
	private RouteHandler handler;

	public Route(HttpMethod method, String uri, RouteHandler handler) {
		this.method = method;
		this.uri = uri;
		this.handler = handler;
	}

	public HttpMethod method() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String uri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public RouteHandler handler() {
		return handler;
	}

	public void setHandler(RouteHandler handler) {
		this.handler = handler;
	}

}
