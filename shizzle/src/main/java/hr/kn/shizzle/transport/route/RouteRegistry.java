package hr.kn.shizzle.transport.route;

import java.util.ArrayList;
import java.util.List;

import io.netty.handler.codec.http.HttpMethod;

public class RouteRegistry {

	private List<Route> routes = new ArrayList<>();

	{
		this.GET("/favicon.ico", request -> Response.ok());
	}

	public List<Route> routes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public RouteRegistry POST(String uri, RouteHandler route) {
		addRoute(uri, HttpMethod.POST, route);
		return this;
	}

	public RouteRegistry GET(String uri, RouteHandler route) {
		addRoute(uri, HttpMethod.GET, route);
		return this;
	}

	private void addRoute(String uri, HttpMethod method, RouteHandler handler) {
		Route route = new Route(method, uri, handler);
		routes.add(route);
	}

}
