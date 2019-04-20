package hr.kn.shizzle.transport.route;

@FunctionalInterface
public interface RouteHandler {
	public Response handle(Request request);
}
