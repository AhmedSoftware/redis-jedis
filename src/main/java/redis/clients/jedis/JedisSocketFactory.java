package redis.clients.jedis;

import java.io.IOException;
import java.net.Socket;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * JedisSocketFactory: responsible for creating socket connections
 * from the within the Jedis client, the default socket factory will
 * create TCP sockets with the recommended configuration.
 *
 * You can use a custom JedisSocketFactory for many use cases, such as:
 * - a custom address resolver
 * - a unix domain socket
 * - a custom configuration for you TCP sockets
 */
public interface JedisSocketFactory {

  /**
   * @deprecated throwing IOException will not be supported in future
   * @return Socket
   * @throws IOException this will be removed in future
   * @throws JedisConnectionException
   */
  @Deprecated Socket createSocket() throws IOException, JedisConnectionException;

  @Deprecated String getDescription();

  String getHost();

  @Deprecated void setHost(String host);

  int getPort();

  @Deprecated void setPort(int port);

  int getConnectionTimeout();

  @Deprecated void setConnectionTimeout(int connectionTimeout);

  int getSoTimeout();

  @Deprecated void setSoTimeout(int soTimeout);
}
