package redis.clients.jedis.commands;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.GeoRadiusStoreParam;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.params.XReadParams;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface MultiKeyBinaryCommands {
  Long del(byte[]... keys);

  Long unlink(byte[]... keys);

  Long exists(byte[]... keys);

  List<byte[]> blpop(int timeout, byte[]... keys);

  List<byte[]> brpop(int timeout, byte[]... keys);

  List<byte[]> blpop(byte[]... args);

  List<byte[]> brpop(byte[]... args);

  Set<byte[]> keys(byte[] pattern);

  List<byte[]> mget(byte[]... keys);

  String mset(byte[]... keysvalues);

  Long msetnx(byte[]... keysvalues);

  String rename(byte[] oldkey, byte[] newkey);

  Long renamenx(byte[] oldkey, byte[] newkey);

  byte[] rpoplpush(byte[] srckey, byte[] dstkey);

  Set<byte[]> sdiff(byte[]... keys);

  Long sdiffstore(byte[] dstkey, byte[]... keys);

  Set<byte[]> sinter(byte[]... keys);

  Long sinterstore(byte[] dstkey, byte[]... keys);

  Long smove(byte[] srckey, byte[] dstkey, byte[] member);

  Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey);

  Long sort(byte[] key, byte[] dstkey);

  Set<byte[]> sunion(byte[]... keys);

  Long sunionstore(byte[] dstkey, byte[]... keys);

  String watch(byte[]... keys);

  String unwatch();

  Long zinterstore(byte[] dstkey, byte[]... sets);

  Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets);

  Long zunionstore(byte[] dstkey, byte[]... sets);

  Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets);

  byte[] brpoplpush(byte[] source, byte[] destination, int timeout);

  Long publish(byte[] channel, byte[] message);

  void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels);

  void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns);

  byte[] randomBinaryKey();

  Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys);

  String pfmerge(byte[] destkey, byte[]... sourcekeys);

  Long pfcount(byte[]... keys);

  Long touch(byte[]... keys);

  List<byte[]> xread(int count, long block, Map<byte[], byte[]> streams);

  List<byte[]> xread(XReadParams xReadParams, Entry<byte[], byte[]>... streams);

  List<byte[]> xreadGroup(byte[] groupname, byte[] consumer, int count, long block, boolean noAck,
      Map<byte[], byte[]> streams);

  List<byte[]> xreadGroup(byte[] groupname, byte[] consumer, XReadGroupParams xReadGroupParams,
      Entry<byte[], byte[]>... streams);

  Long georadiusStore(byte[] key, double longitude, double latitude, double radius, GeoUnit unit,
      GeoRadiusParam param, GeoRadiusStoreParam storeParam);

  Long georadiusByMemberStore(byte[] key, byte[] member, double radius, GeoUnit unit,
      GeoRadiusParam param, GeoRadiusStoreParam storeParam);
}
