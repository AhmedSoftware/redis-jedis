package redis.clients.jedis;

import java.io.Closeable;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.exceptions.JedisConnectionException;

public abstract class JedisClusterConnectionHandler implements Closeable {
  protected final JedisClusterInfoCache cache;

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, String password) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, password, null);
  }

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, String password, String clientName) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, null, password, clientName);
  }

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, String user, String password, String clientName) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, 0, user, password, clientName);
  }

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, String clientName) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, infiniteSoTimeout, user, password, clientName, false, null, null, null, null);
  }

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
      final JedisSocketConfig socketConfig, String user, String password, String clientName) {
    this(nodes, poolConfig, socketConfig, 0, user, password, clientName);
  }

  @Deprecated
  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, String password, String clientName,
      boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
      HostnameVerifier hostnameVerifier, JedisClusterHostAndPortMap portMap) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, null, password, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier, portMap);
  }

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, String password, String clientName,
      boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
      HostnameVerifier hostnameVerifier, HostAndPortMapper portMap) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, null, password, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier, portMap);
  }

  @Deprecated
  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, String user, String password, String clientName,
      boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
      HostnameVerifier hostnameVerifier, JedisClusterHostAndPortMap portMap) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, 0, user, password, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier, portMap);
  }

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, String user, String password, String clientName,
      boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
      HostnameVerifier hostnameVerifier, HostAndPortMapper portMap) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, 0, user, password, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier, portMap);
  }

  @Deprecated
  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, String clientName,
      boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
      HostnameVerifier hostnameVerifier, JedisClusterHostAndPortMap portMap) {
    this(nodes,
        DefaultJedisSocketConfig.builder().withConnectionTimeout(connectionTimeout).withSoTimeout(soTimeout)
            .withSsl(ssl).withSslSocketFactory(sslSocketFactory).withSslParameters(sslParameters)
            .withHostnameVerifier(hostnameVerifier).build(),
        poolConfig,
        DefaultJedisSocketConfig.builder().withConnectionTimeout(connectionTimeout).withSoTimeout(soTimeout)
            .withSsl(ssl).withSslSocketFactory(sslSocketFactory).withSslParameters(sslParameters)
            .withHostnameVerifier(hostnameVerifier).withHostAndPortMapper(portMap).build(),
        infiniteSoTimeout, user, password, clientName);
  }

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
      int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, String clientName,
      boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
      HostnameVerifier hostnameVerifier, HostAndPortMapper portMap) {
    this(nodes, poolConfig,
        DefaultJedisSocketConfig.builder().withConnectionTimeout(connectionTimeout).withSoTimeout(soTimeout)
            .withSsl(ssl).withSslSocketFactory(sslSocketFactory).withSslParameters(sslParameters)
            .withHostnameVerifier(hostnameVerifier).withHostAndPortMapper(portMap).build(),
        infiniteSoTimeout, user, password, clientName);
  }

  @Deprecated
  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, final JedisSocketConfig seedNodesSocketConfig,
      final GenericObjectPoolConfig poolConfig, final JedisSocketConfig clusterNodesSocketConfig,
      int infiniteSoTimeout, String user, String password, String clientName) {
    this.cache = new JedisClusterInfoCache(poolConfig, clusterNodesSocketConfig, infiniteSoTimeout, user, password, clientName);
    initializeSlotsCache(nodes, seedNodesSocketConfig, infiniteSoTimeout, user, password, clientName);
  }

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
      final JedisSocketConfig socketConfig, int infiniteSoTimeout, String user, String password, String clientName) {
    this.cache = new JedisClusterInfoCache(poolConfig, socketConfig, infiniteSoTimeout, user, password, clientName);
    initializeSlotsCache(nodes, socketConfig, infiniteSoTimeout, user, password, clientName);
  }

  abstract Jedis getConnection();

  abstract Jedis getConnectionFromSlot(int slot);

  public Jedis getConnectionFromNode(HostAndPort node) {
    return cache.setupNodeIfNotExist(node).getResource();
  }
  
  public Map<String, JedisPool> getNodes() {
    return cache.getNodes();
  }

  private void initializeSlotsCache(Set<HostAndPort> startNodes, final JedisSocketConfig socketConfig,
      int infiniteSoTimeout, String user, String password, String clientName) {

    for (HostAndPort hostAndPort : startNodes) {
      try (Jedis jedis = new Jedis(hostAndPort, socketConfig, infiniteSoTimeout)) { 
        if (user != null) {
          jedis.auth(user, password);
        } else if (password != null) {
          jedis.auth(password);
        }
        if (clientName != null) {
          jedis.clientSetname(clientName);
        }
        cache.discoverClusterNodesAndSlots(jedis);
        return;
      } catch (JedisConnectionException e) {
        // try next nodes
      }
    }
  }

  public void renewSlotCache() {
    cache.renewClusterSlots(null);
  }

  public void renewSlotCache(Jedis jedis) {
    cache.renewClusterSlots(jedis);
  }

  @Override
  public void close() {
    cache.reset();
  }
}
