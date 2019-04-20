package hr.kn.shizzle.transport.route;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class CustomInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final HttpVersion HTTP_VERSION = HttpVersion.HTTP_1_1;

	private Route route;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public CustomInboundHandler(Route route) {
		this.route = route;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		Request request = populateRequest(msg);
		FullHttpResponse response = generateResponse(request);
		ChannelFuture writeFuture = ctx.writeAndFlush(response);
		writeFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				future.channel().close();
			}
		});
	}

	private Request populateRequest(FullHttpRequest msg) {
		Request request = new Request();
		request.setBody(msg.content().toString(CharsetUtil.UTF_8));

		Map<String, String> pathParams = new HashMap<>();
		fillPathParams(pathParams, msg.uri());
		request.setPathParams(pathParams);

		return request;
	}

	private void fillPathParams(Map<String, String> pathParams, String requestUri) {
		String[] requestUriParts = requestUri.split("/");
		String[] handlerUriParts = route.uri().split("/");

		if(requestUriParts.length == handlerUriParts.length) {
			for(int i = 0; i < requestUriParts.length; i++) {
				if(handlerUriParts[i].startsWith("{") && handlerUriParts[i].endsWith("}")) {
					pathParams.put(handlerUriParts[i].substring(1, handlerUriParts[i].length() - 1), requestUriParts[i]);
				}
			}
		}
	}

	private FullHttpResponse generateResponse(Request request) throws JsonProcessingException {
		Response responseObject = route.handler().handle(request);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_VERSION, responseObject.getStatus());
        String body = objectMapper.writeValueAsString(responseObject.getBody());
        response.content().writeBytes(body.getBytes(CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
		return response;
	}

	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

	public Route route() {
		return route;
	}

}
