# Shizzle #

Shizzle is an __example of__ a simple, opinionated REST library using Jackson for JSON serialization and Netty for HTTP transport. Not intended for public purpose since limited in functionalities.

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
