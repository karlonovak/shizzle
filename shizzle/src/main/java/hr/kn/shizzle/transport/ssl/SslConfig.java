package hr.kn.shizzle.transport.ssl;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class SslConfig {

	private SslContext sslContext;

	private SslConfig() {
	}

	public static SslConfig disabled() {
		SslConfig config = new SslConfig();
		config.sslContext = null;
		return config;
	}

	public static SslConfig selfSigned() {
		SslConfig config = new SslConfig();
		try {
			SelfSignedCertificate cert = new SelfSignedCertificate();
			config.sslContext = SslContextBuilder.forServer(cert.certificate(), cert.privateKey())
					.sslProvider(SslProvider.JDK).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	public SslContext getSslContext() {
		return sslContext;
	}

	public void setSslContext(SslContext sslContext) {
		this.sslContext = sslContext;
	}

}
