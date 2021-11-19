package redis.clients.jedis.providers;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.CommandArguments;
import redis.clients.jedis.Connection;
import redis.clients.jedis.ConnectionFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.util.Pool;

public class PooledJedisConnectionProvider implements JedisConnectionProvider {

  private final Pool<Connection> pool;

  public PooledJedisConnectionProvider(HostAndPort hostAndPort) {
    this(new ConnectionFactory(hostAndPort));
  }

  public PooledJedisConnectionProvider(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
    this(new ConnectionFactory(hostAndPort, clientConfig));
  }

  public PooledJedisConnectionProvider(ConnectionFactory factory) {
    this(factory, new GenericObjectPoolConfig<>());
    factory.setPool(pool);
  }

  public PooledJedisConnectionProvider(PooledObjectFactory<Connection> factory) {
    this(factory, new GenericObjectPoolConfig<>());
  }

  public PooledJedisConnectionProvider(PooledObjectFactory<Connection> factory, GenericObjectPoolConfig<Connection> poolConfig) {
    this(new Pool<>(factory, poolConfig));
  }

  public PooledJedisConnectionProvider(Pool<Connection> pool) {
    this.pool = pool;
  }

  @Override
  public void close() {
    pool.close();
  }

  @Override
  public Connection getConnection() {
    return pool.getResource();
  }

  @Override
  public Connection getConnection(CommandArguments args) {
    return pool.getResource();
  }
}
