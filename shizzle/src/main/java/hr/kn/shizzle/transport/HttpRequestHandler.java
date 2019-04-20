package hr.kn.shizzle.transport;

import java.util.List;

import hr.kn.shizzle.transport.route.CustomInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

@Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private HandlerStrategy handlerStrategy;
	private List<CustomInboundHandler> customHandlers;

	public HttpRequestHandler(List<CustomInboundHandler> customHandlers, HandlerStrategy handlerStrategy) {
		this.customHandlers = customHandlers;
		this.handlerStrategy = handlerStrategy;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		ChannelInboundHandler handler = handlerStrategy.find(customHandlers, request.uri(), request.method());
		ReferenceCountUtil.retain(request);
		handler.channelRead(ctx, request);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
