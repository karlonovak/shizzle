# Shizzle #

Shizzle is a simple, opinionated, high-performance REST library using Jackson for JSON serialization and Netty for HTTP transport.

```java
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
```