package redis.clients.jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class HostAndPorts {

  private static HashMap<String, EndpointConfig> endpointConfigs;

  private static List<HostAndPort> redisHostAndPortList = new ArrayList<>();
  private static List<HostAndPort> sentinelHostAndPortList = new ArrayList<>();
  private static List<HostAndPort> clusterHostAndPortList = new ArrayList<>();
  private static List<HostAndPort> stableClusterHostAndPortList = new ArrayList<>();

  static {
    try {
      endpointConfigs = EndpointConfig.loadFromJSON("src/test/resources/endpoints.json");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // TODO: This is a temporary solution to avoid breaking existing tests.
    //       We should update the tests to make them compatible
    //       with RE Discovery Service and OSS Cluster API.

    sentinelHostAndPortList.add(new HostAndPort("localhost", Protocol.DEFAULT_SENTINEL_PORT));
    sentinelHostAndPortList.add(new HostAndPort("localhost", Protocol.DEFAULT_SENTINEL_PORT + 1));
    sentinelHostAndPortList.add(new HostAndPort("localhost", Protocol.DEFAULT_SENTINEL_PORT + 2));
    sentinelHostAndPortList.add(new HostAndPort("localhost", Protocol.DEFAULT_SENTINEL_PORT + 3));
    sentinelHostAndPortList.add(new HostAndPort("localhost", Protocol.DEFAULT_SENTINEL_PORT + 4));

    clusterHostAndPortList.add(new HostAndPort("localhost", 7379));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7380));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7381));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7382));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7383));
    clusterHostAndPortList.add(new HostAndPort("localhost", 7384));

    stableClusterHostAndPortList.add(new HostAndPort("localhost", 7479));
    stableClusterHostAndPortList.add(new HostAndPort("localhost", 7480));
    stableClusterHostAndPortList.add(new HostAndPort("localhost", 7481));
  }

  public static EndpointConfig getRedisEndpoint(String endpointName) {
    if (!endpointConfigs.containsKey(endpointName)) {
      throw new IllegalArgumentException("Unknown Redis endpoint: " + endpointName);
    }

    return endpointConfigs.get(endpointName);
  }

  public static List<HostAndPort> getSentinelServers() {
    return sentinelHostAndPortList;
  }

  public static List<HostAndPort> getClusterServers() {
    return clusterHostAndPortList;
  }

  public static List<HostAndPort> getStableClusterServers() {
    return stableClusterHostAndPortList;
  }

  private HostAndPorts() {
    throw new InstantiationError("Must not instantiate this class");
  }
}
