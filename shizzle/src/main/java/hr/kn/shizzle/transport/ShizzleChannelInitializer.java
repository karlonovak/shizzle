package hr.kn.shizzle.transport;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import java.util.List;
import javax.net.ssl.SSLEngine;

import hr.kn.shizzle.transport.route.CustomInboundHandler;

public class ShizzleChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext context;
	private final Integer maxContentLengthBytes;
	private List<CustomInboundHandler> customHandlers;

    public ShizzleChannelInitializer(SslContext context, Integer maxContentLengthMegaBytes, List<CustomInboundHandler> customHandlers) {
        this.context = context;
        this.maxContentLengthBytes = maxContentLengthMegaBytes * 1024 * 1024;
        this.customHandlers = customHandlers;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        initSsl(ch, pipeline);
        pipeline.addLast("httpRequestDecoder", new HttpRequestDecoder());
        pipeline.addLast("httpResponseEncoder", new HttpResponseEncoder());
        pipeline.addLast("httpChunkAggregator", new HttpObjectAggregator(maxContentLengthBytes));
        pipeline.addLast("httpRequestHandler", new HttpRequestHandler(customHandlers, new HandlerStrategyImpl()));
    }

	private void initSsl(SocketChannel ch, ChannelPipeline pipeline) {
		if(context != null) {
        	SSLEngine engine = context.newEngine(ch.alloc());
        	engine.setUseClientMode(false);
        	pipeline.addLast("sslHandler", new SslHandler(engine));
        }
	}

}
