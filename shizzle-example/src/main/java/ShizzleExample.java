import hr.kn.shizzle.transport.ShizzleHttpServer;
import hr.kn.shizzle.transport.route.Response;
import hr.kn.shizzle.transport.route.RouteRegistry;
import hr.kn.shizzle.transport.ssl.SslConfig;

public class ShizzleExample {

	private UserService userService = new UserService();

	public static void main(String[] args) throws Exception {
		new ShizzleExample().start();
	}

	private void start() {
		SslConfig sslConfig = SslConfig.selfSigned();
		RouteRegistry routes = new RouteRegistry();

		routes
			.GET("/", req -> Response.ok("Welcome home!"))
			.GET("/author", req -> Response.ok(userService.findAllUsers()))
			.GET("/author/{id}", req -> {
				Integer id = Integer.valueOf(req.param("id"));
				return Response.ok(userService.findUser(id));
			})
			.POST("/author", req -> {
				User user = req.getBody(User.class);
				userService.addUser(user);
				return Response.ok();
			});

		ShizzleHttpServer server = new ShizzleHttpServer(sslConfig, routes);
		server.start(9999);
	}

}
