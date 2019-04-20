package hr.kn.shizzle.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.kn.shizzle.transport.route.CustomInboundHandler;
import hr.kn.shizzle.transport.route.RouteRegistry;
import hr.kn.shizzle.transport.ssl.SslConfig;

public class ShizzleHttpServer {

	private final Logger LOGGER = LoggerFactory.getLogger(ShizzleHttpServer.class);

	private final NioEventLoopGroup group = new NioEventLoopGroup();
	private final SslContext context;
	private Channel channel;
	private RouteRegistry routeRegistry;

	private List<CustomInboundHandler> customHandlers = new ArrayList<>();

	public ShizzleHttpServer(SslConfig sslConfig, RouteRegistry routeRegistry) {
		this.context = sslConfig.getSslContext();
		this.routeRegistry = routeRegistry;
	}

	public void start(Integer port) {
		LOGGER.info("Shizzle server starting on port " + port);
		final ShizzleHttpServer endpoint = this;
		final InetSocketAddress address = new InetSocketAddress(port);

		routeRegistry.routes()
			.stream()
			.forEach(route -> {
				LOGGER.info("Registering route: " + route.method() + " " + route.uri());
				customHandlers.add(new CustomInboundHandler(route));
			});

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				endpoint.destroy();
			}
		});

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group).channel(NioServerSocketChannel.class)
				.childHandler(new ShizzleChannelInitializer(context, 1, customHandlers));
		ChannelFuture future = bootstrap.bind(address);
		LOGGER.info("Shizzle server successful bind");
		LOGGER.info("Ready to shizzle@localhost:" + port);
		future.syncUninterruptibly();
		channel = future.channel();
		future.channel().closeFuture().syncUninterruptibly();
	}

	public void destroy() {
		if (channel != null) {
			channel.close();
		}
		group.shutdownGracefully();
	}

}
