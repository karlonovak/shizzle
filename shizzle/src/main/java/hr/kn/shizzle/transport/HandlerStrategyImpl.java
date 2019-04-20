package hr.kn.shizzle.transport;

import java.util.List;

import hr.kn.shizzle.transport.route.CustomInboundHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.HttpMethod;

public class HandlerStrategyImpl implements HandlerStrategy {

	@Override
	public ChannelInboundHandler find(List<CustomInboundHandler> handlers, String uri, HttpMethod method) {
		return handlers.stream()
				.filter(handler -> uriMatch(uri, handler.route().uri()) && handler.route().method().equals(method))
				.findFirst()
				.get();
	}

	private boolean uriMatch(String requestUri, String handlerUri) {
		String[] requestUriParts = requestUri.split("/");
		String[] handlerUriParts = handlerUri.split("/");

		if(requestUriParts.length == handlerUriParts.length) {
			for(int i = 0; i < requestUriParts.length; i++) {
				if(!partsMatch(requestUriParts[i], handlerUriParts[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private boolean partsMatch(String requestPart, String handlerPart) {
		if(requestPart.equals(handlerPart)) {
			return true;
		} else {
			if(handlerPart.startsWith("{") && handlerPart.endsWith("}")) {
				return true;
			}
		}
		return false;
	}

}
