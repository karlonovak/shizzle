package hr.kn.shizzle.transport;

import java.util.List;

import hr.kn.shizzle.transport.route.CustomInboundHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.HttpMethod;

public interface HandlerStrategy {

	public ChannelInboundHandler find(List<CustomInboundHandler> handlers, String uri, HttpMethod method);

}
