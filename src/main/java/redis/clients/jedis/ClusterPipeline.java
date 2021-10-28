package redis.clients.jedis;

import redis.clients.jedis.args.*;
import redis.clients.jedis.commands.PipelineBinaryCommands;
import redis.clients.jedis.commands.PipelineCommands;
import redis.clients.jedis.commands.RedisModulePipelineCommands;
import redis.clients.jedis.json.Path;
import redis.clients.jedis.params.*;
import redis.clients.jedis.providers.JedisClusterConnectionProvider;
import redis.clients.jedis.resps.*;
import redis.clients.jedis.search.IndexOptions;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.Schema;
import redis.clients.jedis.search.SearchResult;
import redis.clients.jedis.stream.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClusterPipeline extends MultiNodePipelineBase implements PipelineCommands, PipelineBinaryCommands,
        RedisModulePipelineCommands {

  private final JedisClusterConnectionProvider provider;
  private final RedisCommandObjects commandObjects;

  public ClusterPipeline(JedisClusterConnectionProvider provider) {
    this.provider = provider;
    this.commandObjects = new RedisCommandObjects();
  }

  @Override
  protected Connection getConnection(HostAndPort nodeKey) {
    return provider.getConnection(nodeKey);
  }

  @Override
  public Response<Boolean> exists(String key) {
    return appendCommand(provider.getNode(key), commandObjects.exists(key));
  }

  @Override
  public Response<Long> exists(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.exists(keys));
  }

  @Override
  public Response<Long> persist(String key) {
    return appendCommand(provider.getNode(key), commandObjects.persist(key));
  }

  @Override
  public Response<String> type(String key) {
    return appendCommand(provider.getNode(key), commandObjects.type(key));
  }

  @Override
  public Response<byte[]> dump(String key) {
    return appendCommand(provider.getNode(key), commandObjects.dump(key));
  }

  @Override
  public Response<String> restore(String key, long ttl, byte[] serializedValue) {
    return appendCommand(provider.getNode(key), commandObjects.restore(key, ttl, serializedValue));
  }

  @Override
  public Response<String> restore(String key, long ttl, byte[] serializedValue, RestoreParams params) {
    return appendCommand(provider.getNode(key), commandObjects.restore(key, ttl, serializedValue, params));
  }

  @Override
  public Response<Long> expire(String key, long seconds) {
    return appendCommand(provider.getNode(key), commandObjects.expire(key, seconds));
  }

  @Override
  public Response<Long> pexpire(String key, long milliseconds) {
    return appendCommand(provider.getNode(key), commandObjects.pexpire(key, milliseconds));
  }

  @Override
  public Response<Long> expireAt(String key, long unixTime) {
    return appendCommand(provider.getNode(key), commandObjects.expireAt(key, unixTime));
  }

  @Override
  public Response<Long> pexpireAt(String key, long millisecondsTimestamp) {
    return appendCommand(provider.getNode(key), commandObjects.pexpireAt(key, millisecondsTimestamp));
  }

  @Override
  public Response<Long> ttl(String key) {
    return appendCommand(provider.getNode(key), commandObjects.ttl(key));
  }

  @Override
  public Response<Long> pttl(String key) {
    return appendCommand(provider.getNode(key), commandObjects.pttl(key));
  }

  @Override
  public Response<Long> touch(String key) {
    return appendCommand(provider.getNode(key), commandObjects.touch(key));
  }

  @Override
  public Response<Long> touch(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.touch(keys));
  }

  @Override
  public Response<List<String>> sort(String key) {
    return appendCommand(provider.getNode(key), commandObjects.sort(key));
  }

  @Override
  public Response<Long> sort(String key, String dstKey) {
    return appendCommand(provider.getNode(key), commandObjects.sort(key, dstKey));
  }

  @Override
  public Response<List<String>> sort(String key, SortingParams sortingParameters) {
    return appendCommand(provider.getNode(key), commandObjects.sort(key, sortingParameters));
  }

  @Override
  public Response<Long> sort(String key, SortingParams sortingParameters, String dstKey) {
    return appendCommand(provider.getNode(key), commandObjects.sort(key, sortingParameters, dstKey));
  }

  @Override
  public Response<Long> del(String key) {
    return appendCommand(provider.getNode(key), commandObjects.del(key));
  }

  @Override
  public Response<Long> del(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.del(keys));
  }

  @Override
  public Response<Long> unlink(String key) {
    return appendCommand(provider.getNode(key), commandObjects.unlink(key));
  }

  @Override
  public Response<Long> unlink(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.unlink(keys));
  }

  @Override
  public Response<Boolean> copy(String srcKey, String dstKey, boolean replace) {
    return appendCommand(provider.getNode(srcKey), commandObjects.copy(srcKey, dstKey, replace));
  }

  @Override
  public Response<String> rename(String oldKey, String newKey) {
    return appendCommand(provider.getNode(oldKey), commandObjects.rename(oldKey, newKey));
  }

  @Override
  public Response<Long> renamenx(String oldKey, String newKey) {
    return appendCommand(provider.getNode(oldKey), commandObjects.renamenx(oldKey, newKey));
  }

  @Override
  public Response<Long> memoryUsage(String key) {
    return appendCommand(provider.getNode(key), commandObjects.memoryUsage(key));
  }

  @Override
  public Response<Long> memoryUsage(String key, int samples) {
    return appendCommand(provider.getNode(key), commandObjects.memoryUsage(key, samples));
  }

  @Override
  public Response<Long> objectRefcount(String key) {
    return appendCommand(provider.getNode(key), commandObjects.objectRefcount(key));
  }

  @Override
  public Response<String> objectEncoding(String key) {
    return appendCommand(provider.getNode(key), commandObjects.objectEncoding(key));
  }

  @Override
  public Response<Long> objectIdletime(String key) {
    return appendCommand(provider.getNode(key), commandObjects.objectIdletime(key));
  }

  @Override
  public Response<Long> objectFreq(String key) {
    return appendCommand(provider.getNode(key), commandObjects.objectFreq(key));
  }

  @Override
  public Response<String> migrate(String host, int port, String key, int timeout) {
    return appendCommand(provider.getNode(key), commandObjects.migrate(host, port, key, timeout));
  }

  @Override
  public Response<String> migrate(String host, int port, int timeout, MigrateParams params, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.migrate(host, port, timeout, params, keys));
  }

  @Override
  public Response<Set<String>> keys(String pattern) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(pattern), commandObjects.keys(pattern));
  }

  @Override
  public Response<ScanResult<String>> scan(String cursor) {
    return appendCommand(provider.getNode(cursor), commandObjects.scan(cursor));
  }

  @Override
  public Response<ScanResult<String>> scan(String cursor, ScanParams params) {
    return appendCommand(provider.getNode(cursor), commandObjects.scan(cursor, params));
  }

  @Override
  public Response<ScanResult<String>> scan(String cursor, ScanParams params, String type) {
    return appendCommand(provider.getNode(cursor), commandObjects.scan(cursor, params, type));
  }

  @Override
  public Response<String> randomKey() {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.randomKey());
  }

  @Override
  public Response<String> get(String key) {
    return appendCommand(provider.getNode(key), commandObjects.get(key));
  }

  @Override
  public Response<String> getDel(String key) {
    return appendCommand(provider.getNode(key), commandObjects.getDel(key));
  }

  @Override
  public Response<String> getEx(String key, GetExParams params) {
    return appendCommand(provider.getNode(key), commandObjects.getEx(key, params));
  }

  @Override
  public Response<Boolean> setbit(String key, long offset, boolean value) {
    return appendCommand(provider.getNode(key), commandObjects.setbit(key, offset, value));
  }

  @Override
  public Response<Boolean> getbit(String key, long offset) {
    return appendCommand(provider.getNode(key), commandObjects.getbit(key, offset));
  }

  @Override
  public Response<Long> setrange(String key, long offset, String value) {
    return appendCommand(provider.getNode(key), commandObjects.setrange(key, offset, value));
  }

  @Override
  public Response<String> getrange(String key, long startOffset, long endOffset) {
    return appendCommand(provider.getNode(key), commandObjects.getrange(key, startOffset, endOffset));
  }

  @Override
  public Response<String> getSet(String key, String value) {
    return appendCommand(provider.getNode(key), commandObjects.getSet(key, value));
  }

  @Override
  public Response<Long> setnx(String key, String value) {
    return appendCommand(provider.getNode(key), commandObjects.setnx(key, value));
  }

  @Override
  public Response<String> setex(String key, long seconds, String value) {
    return appendCommand(provider.getNode(key), commandObjects.setex(key, seconds, value));
  }

  @Override
  public Response<String> psetex(String key, long milliseconds, String value) {
    return appendCommand(provider.getNode(key), commandObjects.psetex(key, milliseconds, value));
  }

  @Override
  public Response<List<String>> mget(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.mget(keys));
  }

  @Override
  public Response<String> mset(String... keysvalues) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Response<Long> msetnx(String... keysvalues) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Response<Long> incr(String key) {
    return appendCommand(provider.getNode(key), commandObjects.incr(key));
  }

  @Override
  public Response<Long> incrBy(String key, long increment) {
    return appendCommand(provider.getNode(key), commandObjects.incrBy(key, increment));
  }

  @Override
  public Response<Double> incrByFloat(String key, double increment) {
    return appendCommand(provider.getNode(key), commandObjects.incrByFloat(key, increment));
  }

  @Override
  public Response<Long> decr(String key) {
    return appendCommand(provider.getNode(key), commandObjects.decr(key));
  }

  @Override
  public Response<Long> decrBy(String key, long decrement) {
    return appendCommand(provider.getNode(key), commandObjects.decrBy(key, decrement));
  }

  @Override
  public Response<Long> append(String key, String value) {
    return appendCommand(provider.getNode(key), commandObjects.append(key, value));
  }

  @Override
  public Response<String> substr(String key, int start, int end) {
    return appendCommand(provider.getNode(key), commandObjects.substr(key, start, end));
  }

  @Override
  public Response<Long> strlen(String key) {
    return appendCommand(provider.getNode(key), commandObjects.strlen(key));
  }

  @Override
  public Response<Long> bitcount(String key) {
    return appendCommand(provider.getNode(key), commandObjects.bitcount(key));
  }

  @Override
  public Response<Long> bitcount(String key, long start, long end) {
    return appendCommand(provider.getNode(key), commandObjects.bitcount(key, start, end));
  }

  @Override
  public Response<Long> bitpos(String key, boolean value) {
    return appendCommand(provider.getNode(key), commandObjects.bitpos(key, value));
  }

  @Override
  public Response<Long> bitpos(String key, boolean value, BitPosParams params) {
    return appendCommand(provider.getNode(key), commandObjects.bitpos(key, value, params));
  }

  @Override
  public Response<List<Long>> bitfield(String key, String... arguments) {
    return appendCommand(provider.getNode(key), commandObjects.bitfield(key, arguments));
  }

  @Override
  public Response<List<Long>> bitfieldReadonly(String key, String... arguments) {
    return appendCommand(provider.getNode(key), commandObjects.bitfieldReadonly(key, arguments));
  }

  @Override
  public Response<Long> bitop(BitOP op, String destKey, String... srcKeys) {
    return appendCommand(provider.getNode(srcKeys[0]), commandObjects.bitop(op, destKey, srcKeys));
  }

  @Override
  public Response<LCSMatchResult> strAlgoLCSKeys(String keyA, String keyB, StrAlgoLCSParams params) {
    return appendCommand(provider.getNode(keyA), commandObjects.strAlgoLCSKeys(keyA, keyB, params));
  }

  @Override
  public Response<String> set(String key, String value) {
    return appendCommand(provider.getNode(key), commandObjects.set(key, value));
  }

  @Override
  public Response<String> set(String key, String value, SetParams params) {
    return appendCommand(provider.getNode(key), commandObjects.set(key, value, params));
  }

  @Override
  public Response<Long> rpush(String key, String... string) {
    return appendCommand(provider.getNode(key), commandObjects.rpush(key, string));
  }

  @Override
  public Response<Long> lpush(String key, String... string) {
    return appendCommand(provider.getNode(key), commandObjects.lpush(key, string));
  }

  @Override
  public Response<Long> llen(String key) {
    return appendCommand(provider.getNode(key), commandObjects.llen(key));
  }

  @Override
  public Response<List<String>> lrange(String key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.lrange(key, start, stop));
  }

  @Override
  public Response<String> ltrim(String key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.ltrim(key, start, stop));
  }

  @Override
  public Response<String> lindex(String key, long index) {
    return appendCommand(provider.getNode(key), commandObjects.lindex(key, index));
  }

  @Override
  public Response<String> lset(String key, long index, String value) {
    return appendCommand(provider.getNode(key), commandObjects.lset(key, index, value));
  }

  @Override
  public Response<Long> lrem(String key, long count, String value) {
    return appendCommand(provider.getNode(key), commandObjects.lrem(key, count, value));
  }

  @Override
  public Response<String> lpop(String key) {
    return appendCommand(provider.getNode(key), commandObjects.lpop(key));
  }

  @Override
  public Response<List<String>> lpop(String key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.lpop(key, count));
  }

  @Override
  public Response<Long> lpos(String key, String element) {
    return appendCommand(provider.getNode(key), commandObjects.lpos(key, element));
  }

  @Override
  public Response<Long> lpos(String key, String element, LPosParams params) {
    return appendCommand(provider.getNode(key), commandObjects.lpos(key, element, params));
  }

  @Override
  public Response<List<Long>> lpos(String key, String element, LPosParams params, long count) {
    return appendCommand(provider.getNode(key), commandObjects.lpos(key, element, params, count));
  }

  @Override
  public Response<String> rpop(String key) {
    return appendCommand(provider.getNode(key), commandObjects.rpop(key));
  }

  @Override
  public Response<List<String>> rpop(String key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.rpop(key, count));
  }

  @Override
  public Response<Long> linsert(String key, ListPosition where, String pivot, String value) {
    return appendCommand(provider.getNode(key), commandObjects.linsert(key, where, pivot, value));
  }

  @Override
  public Response<Long> lpushx(String key, String... string) {
    return appendCommand(provider.getNode(key), commandObjects.lpushx(key, string));
  }

  @Override
  public Response<Long> rpushx(String key, String... string) {
    return appendCommand(provider.getNode(key), commandObjects.rpushx(key, string));
  }

  @Override
  public Response<List<String>> blpop(int timeout, String key) {
    return appendCommand(provider.getNode(key), commandObjects.blpop(timeout, key));
  }

  @Override
  public Response<KeyedListElement> blpop(double timeout, String key) {
    return appendCommand(provider.getNode(key), commandObjects.blpop(timeout, key));
  }

  @Override
  public Response<List<String>> brpop(int timeout, String key) {
    return appendCommand(provider.getNode(key), commandObjects.brpop(timeout, key));
  }

  @Override
  public Response<KeyedListElement> brpop(double timeout, String key) {
    return appendCommand(provider.getNode(key), commandObjects.brpop(timeout, key));
  }

  @Override
  public Response<List<String>> blpop(int timeout, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.blpop(timeout, keys));
  }

  @Override
  public Response<KeyedListElement> blpop(double timeout, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.blpop(timeout, keys));
  }

  @Override
  public Response<List<String>> brpop(int timeout, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.brpop(timeout, keys));
  }

  @Override
  public Response<KeyedListElement> brpop(double timeout, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.brpop(timeout, keys));
  }

  @Override
  public Response<String> rpoplpush(String srcKey, String dstKey) {
    return appendCommand(provider.getNode(srcKey), commandObjects.rpoplpush(srcKey, dstKey));
  }

  @Override
  public Response<String> brpoplpush(String source, String destination, int timeout) {
    return appendCommand(provider.getNode(source), commandObjects.brpoplpush(source, destination, timeout));
  }

  @Override
  public Response<String> lmove(String srcKey, String dstKey, ListDirection from, ListDirection to) {
    return appendCommand(provider.getNode(srcKey), commandObjects.lmove(srcKey, dstKey, from, to));
  }

  @Override
  public Response<String> blmove(String srcKey, String dstKey, ListDirection from, ListDirection to, double timeout) {
    return appendCommand(provider.getNode(srcKey), commandObjects.blmove(srcKey, dstKey, from, to, timeout));
  }

  @Override
  public Response<Long> hset(String key, String field, String value) {
    return appendCommand(provider.getNode(key), commandObjects.hset(key, field, value));
  }

  @Override
  public Response<Long> hset(String key, Map<String, String> hash) {
    return appendCommand(provider.getNode(key), commandObjects.hset(key, hash));
  }

  @Override
  public Response<String> hget(String key, String field) {
    return appendCommand(provider.getNode(key), commandObjects.hget(key, field));
  }

  @Override
  public Response<Long> hsetnx(String key, String field, String value) {
    return appendCommand(provider.getNode(key), commandObjects.hsetnx(key, field, value));
  }

  @Override
  public Response<String> hmset(String key, Map<String, String> hash) {
    return appendCommand(provider.getNode(key), commandObjects.hmset(key, hash));
  }

  @Override
  public Response<List<String>> hmget(String key, String... fields) {
    return appendCommand(provider.getNode(key), commandObjects.hmget(key, fields));
  }

  @Override
  public Response<Long> hincrBy(String key, String field, long value) {
    return appendCommand(provider.getNode(key), commandObjects.hincrBy(key, field, value));
  }

  @Override
  public Response<Double> hincrByFloat(String key, String field, double value) {
    return appendCommand(provider.getNode(key), commandObjects.hincrByFloat(key, field, value));
  }

  @Override
  public Response<Boolean> hexists(String key, String field) {
    return appendCommand(provider.getNode(key), commandObjects.hexists(key, field));
  }

  @Override
  public Response<Long> hdel(String key, String... field) {
    return appendCommand(provider.getNode(key), commandObjects.hdel(key, field));
  }

  @Override
  public Response<Long> hlen(String key) {
    return appendCommand(provider.getNode(key), commandObjects.hlen(key));
  }

  @Override
  public Response<Set<String>> hkeys(String key) {
    return appendCommand(provider.getNode(key), commandObjects.hkeys(key));
  }

  @Override
  public Response<List<String>> hvals(String key) {
    return appendCommand(provider.getNode(key), commandObjects.hvals(key));
  }

  @Override
  public Response<Map<String, String>> hgetAll(String key) {
    return appendCommand(provider.getNode(key), commandObjects.hgetAll(key));
  }

  @Override
  public Response<String> hrandfield(String key) {
    return appendCommand(provider.getNode(key), commandObjects.hrandfield(key));
  }

  @Override
  public Response<List<String>> hrandfield(String key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.hrandfield(key, count));
  }

  @Override
  public Response<Map<String, String>> hrandfieldWithValues(String key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.hrandfieldWithValues(key, count));
  }

  @Override
  public Response<ScanResult<Map.Entry<String, String>>> hscan(String key, String cursor, ScanParams params) {
    return appendCommand(provider.getNode(key), commandObjects.hscan(key, cursor, params));
  }

  @Override
  public Response<Long> hstrlen(String key, String field) {
    return appendCommand(provider.getNode(key), commandObjects.hstrlen(key, field));
  }

  @Override
  public Response<Long> sadd(String key, String... member) {
    return appendCommand(provider.getNode(key), commandObjects.sadd(key, member));
  }

  @Override
  public Response<Set<String>> smembers(String key) {
    return appendCommand(provider.getNode(key), commandObjects.smembers(key));
  }

  @Override
  public Response<Long> srem(String key, String... member) {
    return appendCommand(provider.getNode(key), commandObjects.srem(key, member));
  }

  @Override
  public Response<String> spop(String key) {
    return appendCommand(provider.getNode(key), commandObjects.spop(key));
  }

  @Override
  public Response<Set<String>> spop(String key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.spop(key, count));
  }

  @Override
  public Response<Long> scard(String key) {
    return appendCommand(provider.getNode(key), commandObjects.scard(key));
  }

  @Override
  public Response<Boolean> sismember(String key, String member) {
    return appendCommand(provider.getNode(key), commandObjects.sismember(key, member));
  }

  @Override
  public Response<List<Boolean>> smismember(String key, String... members) {
    return appendCommand(provider.getNode(key), commandObjects.smismember(key, members));
  }

  @Override
  public Response<String> srandmember(String key) {
    return appendCommand(provider.getNode(key), commandObjects.srandmember(key));
  }

  @Override
  public Response<List<String>> srandmember(String key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.srandmember(key, count));
  }

  @Override
  public Response<ScanResult<String>> sscan(String key, String cursor, ScanParams params) {
    return appendCommand(provider.getNode(key), commandObjects.sscan(key, cursor, params));
  }

  @Override
  public Response<Set<String>> sdiff(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sdiff(keys));
  }

  @Override
  public Response<Long> sdiffstore(String dstKey, String... keys) {
    return appendCommand(provider.getNode(dstKey), commandObjects.sdiffstore(dstKey, keys));
  }

  @Override
  public Response<Set<String>> sinter(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sinter(keys));
  }

  @Override
  public Response<Long> sinterstore(String dstKey, String... keys) {
    return appendCommand(provider.getNode(dstKey), commandObjects.sinterstore(dstKey, keys));
  }

  @Override
  public Response<Set<String>> sunion(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sunion(keys));
  }

  @Override
  public Response<Long> sunionstore(String dstKey, String... keys) {
    return appendCommand(provider.getNode(dstKey), commandObjects.sunionstore(dstKey, keys));
  }

  @Override
  public Response<Long> smove(String srcKey, String dstKey, String member) {
    return appendCommand(provider.getNode(srcKey), commandObjects.smove(srcKey, dstKey, member));
  }

  @Override
  public Response<Long> zadd(String key, double score, String member) {
    return appendCommand(provider.getNode(key), commandObjects.zadd(key, score, member));
  }

  @Override
  public Response<Long> zadd(String key, double score, String member, ZAddParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zadd(key, score, member, params));
  }

  @Override
  public Response<Long> zadd(String key, Map<String, Double> scoreMembers) {
    return appendCommand(provider.getNode(key), commandObjects.zadd(key, scoreMembers));
  }

  @Override
  public Response<Long> zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zadd(key, scoreMembers, params));
  }

  @Override
  public Response<Double> zaddIncr(String key, double score, String member, ZAddParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zaddIncr(key, score, member, params));
  }

  @Override
  public Response<Long> zrem(String key, String... members) {
    return appendCommand(provider.getNode(key), commandObjects.zrem(key, members));
  }

  @Override
  public Response<Double> zincrby(String key, double increment, String member) {
    return appendCommand(provider.getNode(key), commandObjects.zincrby(key, increment, member));
  }

  @Override
  public Response<Double> zincrby(String key, double increment, String member, ZIncrByParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zincrby(key, increment, member, params));
  }

  @Override
  public Response<Long> zrank(String key, String member) {
    return appendCommand(provider.getNode(key), commandObjects.zrank(key, member));
  }

  @Override
  public Response<Long> zrevrank(String key, String member) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrank(key, member));
  }

  @Override
  public Response<Set<String>> zrange(String key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zrange(key, start, stop));
  }

  @Override
  public Response<Set<String>> zrevrange(String key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrange(key, start, stop));
  }

  @Override
  public Response<Set<Tuple>> zrangeWithScores(String key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeWithScores(key, start, stop));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeWithScores(String key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeWithScores(key, start, stop));
  }

  @Override
  public Response<String> zrandmember(String key) {
    return appendCommand(provider.getNode(key), commandObjects.zrandmember(key));
  }

  @Override
  public Response<Set<String>> zrandmember(String key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.zrandmember(key, count));
  }

  @Override
  public Response<Set<Tuple>> zrandmemberWithScores(String key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.zrandmemberWithScores(key, count));
  }

  @Override
  public Response<Long> zcard(String key) {
    return appendCommand(provider.getNode(key), commandObjects.zcard(key));
  }

  @Override
  public Response<Double> zscore(String key, String member) {
    return appendCommand(provider.getNode(key), commandObjects.zscore(key, member));
  }

  @Override
  public Response<List<Double>> zmscore(String key, String... members) {    
    return appendCommand(provider.getNode(key), commandObjects.zmscore(key, members));
  }

  @Override
  public Response<Tuple> zpopmax(String key) {
    return appendCommand(provider.getNode(key), commandObjects.zpopmax(key));
  }

  @Override
  public Response<Set<Tuple>> zpopmax(String key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zpopmax(key, count));
  }

  @Override
  public Response<Tuple> zpopmin(String key) {
    return appendCommand(provider.getNode(key), commandObjects.zpopmin(key));
  }

  @Override
  public Response<Set<Tuple>> zpopmin(String key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zpopmin(key, count));
  }

  @Override
  public Response<Long> zcount(String key, double min, double max) {
    return appendCommand(provider.getNode(key), commandObjects.zcount(key, min, max));
  }

  @Override
  public Response<Long> zcount(String key, String min, String max) {
    return appendCommand(provider.getNode(key), commandObjects.zcount(key, min, max));
  }

  @Override
  public Response<Set<String>> zrangeByScore(String key, double min, double max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScore(key, min, max));
  }

  @Override
  public Response<Set<String>> zrangeByScore(String key, String min, String max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScore(key, min, max));
  }

  @Override
  public Response<Set<String>> zrevrangeByScore(String key, double max, double min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScore(key, max, min));

  }

  @Override
  public Response<Set<String>> zrangeByScore(String key, double min, double max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScore(key, min, max, offset, count));
  }

  @Override
  public Response<Set<String>> zrevrangeByScore(String key, String max, String min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScore(key, max, min));
  }

  @Override
  public Response<Set<String>> zrangeByScore(String key, String min, String max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScore(key, min, max, offset, count));
  }

  @Override
  public Response<Set<String>> zrevrangeByScore(String key, double max, double min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScore(key, max, min, offset, count));
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScoreWithScores(key, min, max));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScoreWithScores(key, max, min));
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
  }

  @Override
  public Response<Set<String>> zrevrangeByScore(String key, String max, String min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScore(key, max, min, offset, count));
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScoreWithScores(key, min, max));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScoreWithScores(key, max, min));
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
  }

  @Override
  public Response<Long> zremrangeByRank(String key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zremrangeByRank(key, start, stop));
  }

  @Override
  public Response<Long> zremrangeByScore(String key, double min, double max) {
    return appendCommand(provider.getNode(key), commandObjects.zremrangeByScore(key, min, max));
  }

  @Override
  public Response<Long> zremrangeByScore(String key, String min, String max) {
    return appendCommand(provider.getNode(key), commandObjects.zremrangeByScore(key, min, max));
  }

  @Override
  public Response<Long> zlexcount(String key, String min, String max) {
    return appendCommand(provider.getNode(key), commandObjects.zlexcount(key, min, max));
  }

  @Override
  public Response<Set<String>> zrangeByLex(String key, String min, String max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByLex(key, min, max));
  }

  @Override
  public Response<Set<String>> zrangeByLex(String key, String min, String max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByLex(key, min, max, offset, count));
  }

  @Override
  public Response<Set<String>> zrevrangeByLex(String key, String max, String min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByLex(key, max, min));
  }

  @Override
  public Response<Set<String>> zrevrangeByLex(String key, String max, String min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByLex(key, max, min, offset, count));
  }

  @Override
  public Response<Long> zremrangeByLex(String key, String min, String max) {
    return appendCommand(provider.getNode(key), commandObjects.zremrangeByLex(key, min, max));
  }

  @Override
  public Response<ScanResult<Tuple>> zscan(String key, String cursor, ScanParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zscan(key, cursor, params));
  }

  @Override
  public Response<KeyedZSetElement> bzpopmax(double timeout, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.bzpopmax(timeout, keys));
  }

  @Override
  public Response<KeyedZSetElement> bzpopmin(double timeout, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.bzpopmin(timeout, keys));
  }

  @Override
  public Response<Set<String>> zdiff(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zdiff(keys));
  }

  @Override
  public Response<Set<Tuple>> zdiffWithScores(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zdiffWithScores(keys));
  }

  @Override
  public Response<Long> zdiffStore(String dstKey, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zdiffStore(dstKey, keys));
  }

  @Override
  public Response<Long> zinterstore(String dstKey, String... sets) {
    return appendCommand(provider.getNode(dstKey), commandObjects.zinterstore(dstKey, sets));
  }

  @Override
  public Response<Long> zinterstore(String dstKey, ZParams params, String... sets) {
    return appendCommand(provider.getNode(dstKey), commandObjects.zinterstore(dstKey, params, sets));
  }

  @Override
  public Response<Set<String>> zinter(ZParams params, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zinter(params, keys));
  }

  @Override
  public Response<Set<Tuple>> zinterWithScores(ZParams params, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zinterWithScores(params, keys));
  }

  @Override
  public Response<Set<String>> zunion(ZParams params, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zunion(params, keys));
  }

  @Override
  public Response<Set<Tuple>> zunionWithScores(ZParams params, String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zunionWithScores(params, keys));
  }

  @Override
  public Response<Long> zunionstore(String dstKey, String... sets) {
    return appendCommand(provider.getNode(dstKey), commandObjects.zunionstore(dstKey, sets));
  }

  @Override
  public Response<Long> zunionstore(String dstKey, ZParams params, String... sets) {
    return appendCommand(provider.getNode(dstKey), commandObjects.zunionstore(dstKey, params, sets));
  }

  @Override
  public Response<Long> geoadd(String key, double longitude, double latitude, String member) {
    return appendCommand(provider.getNode(key), commandObjects.geoadd(key, longitude, latitude, member));
  }

  @Override
  public Response<Long> geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
    return appendCommand(provider.getNode(key), commandObjects.geoadd(key, memberCoordinateMap));
  }

  @Override
  public Response<Long> geoadd(String key, GeoAddParams params, Map<String, GeoCoordinate> memberCoordinateMap) {
    return appendCommand(provider.getNode(key), commandObjects.geoadd(key, params, memberCoordinateMap));
  }

  @Override
  public Response<Double> geodist(String key, String member1, String member2) {
    return appendCommand(provider.getNode(key), commandObjects.geodist(key, member1, member2));
  }

  @Override
  public Response<Double> geodist(String key, String member1, String member2, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.geodist(key, member1, member2, unit));
  }

  @Override
  public Response<List<String>> geohash(String key, String... members) {
    return appendCommand(provider.getNode(key), commandObjects.geohash(key, members));
  }

  @Override
  public Response<List<GeoCoordinate>> geopos(String key, String... members) {
    return appendCommand(provider.getNode(key), commandObjects.geopos(key, members));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.georadius(key, longitude, latitude, radius, unit));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
    return appendCommand(provider.getNode(key), commandObjects.georadius(key, longitude, latitude, radius, unit, param));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit, param));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMember(key, member, radius, unit));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMemberReadonly(key, member, radius, unit));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMember(key, member, radius, unit, param));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
  }

  @Override
  public Response<Long> georadiusStore(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam));
  }

  @Override
  public Response<Long> georadiusByMemberStore(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam));
  }

  @Override
  public Response<Long> pfadd(String key, String... elements) {
    return appendCommand(provider.getNode(key), commandObjects.pfadd(key, elements));
  }

  @Override
  public Response<String> pfmerge(String destkey, String... sourcekeys) {
    return appendCommand(provider.getNode(destkey), commandObjects.pfmerge(destkey, sourcekeys));
  }

  @Override
  public Response<Long> pfcount(String key) {
    return appendCommand(provider.getNode(key), commandObjects.pfcount(key));
  }

  @Override
  public Response<Long> pfcount(String... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.pfcount(keys));
  }

  @Override
  public Response<StreamEntryID> xadd(String key, StreamEntryID id, Map<String, String> hash) {
    return appendCommand(provider.getNode(key), commandObjects.xadd(key, id, hash));
  }

  @Override
  public Response<StreamEntryID> xadd_v2(String key, XAddParams params, Map<String, String> hash) {
    return appendCommand(provider.getNode(key), commandObjects.xadd(key, params, hash));
  }

  @Override
  public Response<Long> xlen(String key) {
    return appendCommand(provider.getNode(key), commandObjects.xlen(key));
  }

  @Override
  public Response<List<StreamEntry>> xrange(String key, StreamEntryID start, StreamEntryID end) {
    return appendCommand(provider.getNode(key), commandObjects.xrange(key, start, end));
  }

  @Override
  public Response<List<StreamEntry>> xrange(String key, StreamEntryID start, StreamEntryID end, int count) {
    return appendCommand(provider.getNode(key), commandObjects.xrange(key, start, end, count));
  }

  @Override
  public Response<List<StreamEntry>> xrevrange(String key, StreamEntryID end, StreamEntryID start) {
    return appendCommand(provider.getNode(key), commandObjects.xrevrange(key, start, end));
  }

  @Override
  public Response<List<StreamEntry>> xrevrange(String key, StreamEntryID end, StreamEntryID start, int count) {
    return appendCommand(provider.getNode(key), commandObjects.xrevrange(key, start, end, count));
  }

  @Override
  public Response<Long> xack(String key, String group, StreamEntryID... ids) {
    return appendCommand(provider.getNode(key), commandObjects.xack(key, group, ids));
  }

  @Override
  public Response<String> xgroupCreate(String key, String groupname, StreamEntryID id, boolean makeStream) {
    return appendCommand(provider.getNode(key), commandObjects.xgroupCreate(key, groupname, id, makeStream));
  }

  @Override
  public Response<String> xgroupSetID(String key, String groupname, StreamEntryID id) {
    return appendCommand(provider.getNode(key), commandObjects.xgroupSetID(key, groupname, id));
  }

  @Override
  public Response<Long> xgroupDestroy(String key, String groupname) {
    return appendCommand(provider.getNode(key), commandObjects.xgroupDestroy(key, groupname));
  }

  @Override
  public Response<Long> xgroupDelConsumer(String key, String groupname, String consumername) {
    return appendCommand(provider.getNode(key), commandObjects.xgroupDelConsumer(key, groupname, consumername));
  }

  @Override
  public Response<StreamPendingSummary> xpending(String key, String groupname) {
    return appendCommand(provider.getNode(key), commandObjects.xpending(key, groupname));
  }

  @Override
  public Response<List<StreamPendingEntry>> xpending(String key, String groupname, StreamEntryID start, StreamEntryID end, int count, String consumername) {
    return appendCommand(provider.getNode(key), commandObjects.xpending(key, groupname, start, end, count, consumername));
  }

  @Override
  public Response<List<StreamPendingEntry>> xpending(String key, String groupname, XPendingParams params) {
    return appendCommand(provider.getNode(key), commandObjects.xpending(key, groupname, params));
  }

  @Override
  public Response<Long> xdel(String key, StreamEntryID... ids) {
    return appendCommand(provider.getNode(key), commandObjects.xdel(key, ids));
  }

  @Override
  public Response<Long> xtrim(String key, long maxLen, boolean approximate) {
    return appendCommand(provider.getNode(key), commandObjects.xtrim(key, maxLen, approximate));
  }

  @Override
  public Response<Long> xtrim(String key, XTrimParams params) {
    return appendCommand(provider.getNode(key), commandObjects.xtrim(key, params));
  }

  @Override
  public Response<List<StreamEntry>> xclaim(String key, String group, String consumername, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
    return appendCommand(provider.getNode(key), commandObjects.xclaim(key, group, consumername, minIdleTime, params, ids));
  }

  @Override
  public Response<List<StreamEntryID>> xclaimJustId(String key, String group, String consumername, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
    return appendCommand(provider.getNode(key), commandObjects.xclaimJustId(key, group, consumername, minIdleTime, params, ids));
  }

  @Override
  public Response<Map.Entry<StreamEntryID, List<StreamEntry>>> xautoclaim(String key, String group, String consumername, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
    return appendCommand(provider.getNode(key), commandObjects.xautoclaim(key, group, consumername, minIdleTime, start, params));
  }

  @Override
  public Response<Map.Entry<StreamEntryID, List<StreamEntryID>>> xautoclaimJustId(String key, String group, String consumername, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
    return appendCommand(provider.getNode(key), commandObjects.xautoclaimJustId(key, group, consumername, minIdleTime, start, params));
  }

  @Override
  public Response<StreamInfo> xinfoStream(String key) {
    return appendCommand(provider.getNode(key), commandObjects.xinfoStream(key));
  }

  @Override
  public Response<List<StreamGroupInfo>> xinfoGroup(String key) {
    return appendCommand(provider.getNode(key), commandObjects.xinfoGroup(key));
  }

  @Override
  public Response<List<StreamConsumersInfo>> xinfoConsumers(String key, String group) {
    return appendCommand(provider.getNode(key), commandObjects.xinfoConsumers(key, group));
  }

  @Override
  public Response<List<Map.Entry<String, List<StreamEntry>>>> xread(XReadParams xReadParams, Map<String, StreamEntryID> streams) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.xread(xReadParams, streams));
  }

  @Override
  public Response<List<Map.Entry<String, List<StreamEntry>>>> xreadGroup(String groupname, String consumer, XReadGroupParams xReadGroupParams, Map<String, StreamEntryID> streams) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.xreadGroup(groupname, consumer, xReadGroupParams, streams));
  }

  @Override
  public Response<Object> eval(String script) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.eval(script));
  }

  @Override
  public Response<Object> eval(String script, int keyCount, String... params) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.eval(script, keyCount, params));
  }

  @Override
  public Response<Object> eval(String script, List<String> keys, List<String> args) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.eval(script, keys, args));
  }

  @Override
  public Response<Object> evalsha(String sha1) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.evalsha(sha1));
  }

  @Override
  public Response<Object> evalsha(String sha1, int keyCount, String... params) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.evalsha(sha1, keyCount, params));
  }

  @Override
  public Response<Object> evalsha(String sha1, List<String> keys, List<String> args) {
    return appendCommand(provider.getNode(keys.get(0)), commandObjects.evalsha(sha1, keys, args));
  }

  @Override
  public Response<Long> waitReplicas(String sampleKey, int replicas, long timeout) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.waitReplicas(sampleKey, replicas, timeout));
  }

  @Override
  public Response<Object> eval(String script, String sampleKey) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.eval(script, sampleKey));
  }

  @Override
  public Response<Object> evalsha(String sha1, String sampleKey) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.evalsha(sha1, sampleKey));
  }

  @Override
  public Response<Boolean> scriptExists(String sha1, String sampleKey) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.scriptExists(sha1, sampleKey));
  }

  @Override
  public Response<List<Boolean>> scriptExists(String sampleKey, String... sha1) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.scriptExists(sampleKey, sha1));
  }

  @Override
  public Response<String> scriptLoad(String script, String sampleKey) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.scriptLoad(script, sampleKey));
  }

  @Override
  public Response<String> scriptFlush(String sampleKey) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.scriptFlush(sampleKey));
  }

  @Override
  public Response<String> scriptFlush(String sampleKey, FlushMode flushMode) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.scriptFlush(sampleKey, flushMode));
  }

  @Override
  public Response<String> scriptKill(String sampleKey) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.scriptKill(sampleKey));
  }

  @Override
  public Response<Long> publish(String channel, String message) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.publish(channel, message));
  }

  @Override
  public Response<LCSMatchResult> strAlgoLCSStrings(String strA, String strB, StrAlgoLCSParams params) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.strAlgoLCSStrings(strA, strB, params));
  }

  @Override
  public Response<Long> geoadd(byte[] key, double longitude, double latitude, byte[] member) {
    return appendCommand(provider.getNode(key), commandObjects.geoadd(key, longitude, latitude, member));
  }

  @Override
  public Response<Long> geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
    return appendCommand(provider.getNode(key), commandObjects.geoadd(key, memberCoordinateMap));
  }

  @Override
  public Response<Long> geoadd(byte[] key, GeoAddParams params, Map<byte[], GeoCoordinate> memberCoordinateMap) {
    return appendCommand(provider.getNode(key), commandObjects.geoadd(key, params, memberCoordinateMap));
  }

  @Override
  public Response<Double> geodist(byte[] key, byte[] member1, byte[] member2) {
    return appendCommand(provider.getNode(key), commandObjects.geodist(key, member1, member2));
  }

  @Override
  public Response<Double> geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.geodist(key, member1, member2, unit));
  }

  @Override
  public Response<List<byte[]>> geohash(byte[] key, byte[]... members) {
    return appendCommand(provider.getNode(key), commandObjects.geohash(key, members));
  }

  @Override
  public Response<List<GeoCoordinate>> geopos(byte[] key, byte[]... members) {
    return appendCommand(provider.getNode(key), commandObjects.geopos(key, members));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.georadius(key, longitude, latitude, radius, unit));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
    return appendCommand(provider.getNode(key), commandObjects.georadius(key, longitude, latitude, radius, unit, param));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit, param));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMember(key, member, radius, unit));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMemberReadonly(key, member, radius, unit));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMember(key, member, radius, unit, param));
  }

  @Override
  public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
  }

  @Override
  public Response<Long> georadiusStore(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam));
  }

  @Override
  public Response<Long> georadiusByMemberStore(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
    return appendCommand(provider.getNode(key), commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam));
  }

  @Override
  public Response<Long> hset(byte[] key, byte[] field, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.hset(key, field, value));
  }

  @Override
  public Response<Long> hset(byte[] key, Map<byte[], byte[]> hash) {
    return appendCommand(provider.getNode(key), commandObjects.hset(key, hash));
  }

  @Override
  public Response<byte[]> hget(byte[] key, byte[] field) {
    return appendCommand(provider.getNode(key), commandObjects.hget(key, field));
  }

  @Override
  public Response<Long> hsetnx(byte[] key, byte[] field, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.hsetnx(key, field, value));
  }

  @Override
  public Response<String> hmset(byte[] key, Map<byte[], byte[]> hash) {
    return appendCommand(provider.getNode(key), commandObjects.hmset(key, hash));
  }

  @Override
  public Response<List<byte[]>> hmget(byte[] key, byte[]... fields) {
    return appendCommand(provider.getNode(key), commandObjects.hmget(key, fields));
  }

  @Override
  public Response<Long> hincrBy(byte[] key, byte[] field, long value) {
    return appendCommand(provider.getNode(key), commandObjects.hincrBy(key, field, value));
  }

  @Override
  public Response<Double> hincrByFloat(byte[] key, byte[] field, double value) {
    return appendCommand(provider.getNode(key), commandObjects.hincrByFloat(key, field, value));
  }

  @Override
  public Response<Boolean> hexists(byte[] key, byte[] field) {
    return appendCommand(provider.getNode(key), commandObjects.hexists(key, field));
  }

  @Override
  public Response<Long> hdel(byte[] key, byte[]... field) {
    return appendCommand(provider.getNode(key), commandObjects.hdel(key, field));
  }

  @Override
  public Response<Long> hlen(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.hlen(key));
  }

  @Override
  public Response<Set<byte[]>> hkeys(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.hkeys(key));
  }

  @Override
  public Response<List<byte[]>> hvals(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.hvals(key));
  }

  @Override
  public Response<Map<byte[], byte[]>> hgetAll(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.hgetAll(key));
  }

  @Override
  public Response<byte[]> hrandfield(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.hrandfield(key));
  }

  @Override
  public Response<List<byte[]>> hrandfield(byte[] key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.hrandfield(key, count));
  }

  @Override
  public Response<Map<byte[], byte[]>> hrandfieldWithValues(byte[] key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.hrandfieldWithValues(key, count));
  }

  @Override
  public Response<ScanResult<Map.Entry<byte[], byte[]>>> hscan(byte[] key, byte[] cursor, ScanParams params) {
    return appendCommand(provider.getNode(key), commandObjects.hscan(key, cursor, params));
  }

  @Override
  public Response<Long> hstrlen(byte[] key, byte[] field) {
    return appendCommand(provider.getNode(key), commandObjects.hstrlen(key, field));
  }

  @Override
  public Response<Long> pfadd(byte[] key, byte[]... elements) {
    return appendCommand(provider.getNode(key), commandObjects.pfadd(key, elements));
  }

  @Override
  public Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
    return appendCommand(provider.getNode(destkey), commandObjects.pfmerge(destkey, sourcekeys));
  }

  @Override
  public Response<Long> pfcount(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.pfcount(key));
  }

  @Override
  public Response<Long> pfcount(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.pfcount(keys));
  }

  @Override
  public Response<Boolean> exists(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.exists(key));
  }

  @Override
  public Response<Long> exists(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.exists(keys));
  }

  @Override
  public Response<Long> persist(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.persist(key));
  }

  @Override
  public Response<String> type(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.type(key));
  }

  @Override
  public Response<byte[]> dump(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.dump(key));
  }

  @Override
  public Response<String> restore(byte[] key, long ttl, byte[] serializedValue) {
    return appendCommand(provider.getNode(key), commandObjects.restore(key, ttl, serializedValue));
  }

  @Override
  public Response<String> restore(byte[] key, long ttl, byte[] serializedValue, RestoreParams params) {
    return appendCommand(provider.getNode(key), commandObjects.restore(key, ttl, serializedValue, params));
  }

  @Override
  public Response<Long> expire(byte[] key, long seconds) {
    return appendCommand(provider.getNode(key), commandObjects.expire(key, seconds));
  }

  @Override
  public Response<Long> pexpire(byte[] key, long milliseconds) {
    return appendCommand(provider.getNode(key), commandObjects.pexpire(key, milliseconds));
  }

  @Override
  public Response<Long> expireAt(byte[] key, long unixTime) {
    return appendCommand(provider.getNode(key), commandObjects.expireAt(key, unixTime));
  }

  @Override
  public Response<Long> pexpireAt(byte[] key, long millisecondsTimestamp) {
    return appendCommand(provider.getNode(key), commandObjects.pexpireAt(key, millisecondsTimestamp));
  }

  @Override
  public Response<Long> ttl(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.ttl(key));
  }

  @Override
  public Response<Long> pttl(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.pttl(key));
  }

  @Override
  public Response<Long> touch(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.touch(key));
  }

  @Override
  public Response<Long> touch(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.touch(keys));
  }

  @Override
  public Response<List<byte[]>> sort(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.sort(key));
  }

  @Override
  public Response<List<byte[]>> sort(byte[] key, SortingParams sortingParameters) {
    return appendCommand(provider.getNode(key), commandObjects.sort(key, sortingParameters));
  }

  @Override
  public Response<Long> del(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.del(key));
  }

  @Override
  public Response<Long> del(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.del(keys));
  }

  @Override
  public Response<Long> unlink(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.unlink(key));
  }

  @Override
  public Response<Long> unlink(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.unlink(keys));
  }

  @Override
  public Response<Boolean> copy(byte[] srcKey, byte[] dstKey, boolean replace) {
    return appendCommand(provider.getNode(srcKey), commandObjects.copy(srcKey, dstKey, replace));
  }

  @Override
  public Response<String> rename(byte[] oldkey, byte[] newkey) {
    return appendCommand(provider.getNode(oldkey), commandObjects.rename(oldkey, newkey));
  }

  @Override
  public Response<Long> renamenx(byte[] oldkey, byte[] newkey) {
    return appendCommand(provider.getNode(oldkey), commandObjects.renamenx(oldkey, newkey));
  }

  @Override
  public Response<Long> sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
    return appendCommand(provider.getNode(key), commandObjects.sort(key, sortingParameters, dstkey));
  }

  @Override
  public Response<Long> sort(byte[] key, byte[] dstkey) {
    return appendCommand(provider.getNode(key), commandObjects.sort(key, dstkey));
  }

  @Override
  public Response<Long> memoryUsage(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.memoryUsage(key));
  }

  @Override
  public Response<Long> memoryUsage(byte[] key, int samples) {
    return appendCommand(provider.getNode(key), commandObjects.memoryUsage(key, samples));
  }

  @Override
  public Response<Long> objectRefcount(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.objectRefcount(key));
  }

  @Override
  public Response<byte[]> objectEncoding(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.objectEncoding(key));
  }

  @Override
  public Response<Long> objectIdletime(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.objectIdletime(key));
  }

  @Override
  public Response<Long> objectFreq(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.objectFreq(key));
  }

  @Override
  public Response<String> migrate(String host, int port, byte[] key, int timeout) {
    return appendCommand(provider.getNode(key), commandObjects.migrate(host, port, key, timeout));
  }

  @Override
  public Response<String> migrate(String host, int port, int timeout, MigrateParams params, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.migrate(host, port, timeout, params, keys));
  }

  @Override
  public Response<Set<byte[]>> keys(byte[] pattern) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.keys(pattern));
  }

  @Override
  public Response<ScanResult<byte[]>> scan(byte[] cursor) {
    return appendCommand(provider.getNode(cursor), commandObjects.scan(cursor));
  }

  @Override
  public Response<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params) {
    return appendCommand(provider.getNode(cursor), commandObjects.scan(cursor, params));
  }

  @Override
  public Response<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params, byte[] type) {
    return appendCommand(provider.getNode(cursor), commandObjects.scan(cursor, params, type));
  }

  @Override
  public Response<byte[]> randomBinaryKey() {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.randomBinaryKey());
  }

  @Override
  public Response<Long> rpush(byte[] key, byte[]... args) {
    return appendCommand(provider.getNode(key), commandObjects.rpush(key, args));
  }

  @Override
  public Response<Long> lpush(byte[] key, byte[]... args) {
    return appendCommand(provider.getNode(key), commandObjects.lpush(key, args));
  }

  @Override
  public Response<Long> llen(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.llen(key));
  }

  @Override
  public Response<List<byte[]>> lrange(byte[] key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.lrange(key, start, stop));
  }

  @Override
  public Response<String> ltrim(byte[] key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.ltrim(key, start, stop));
  }

  @Override
  public Response<byte[]> lindex(byte[] key, long index) {
    return appendCommand(provider.getNode(key), commandObjects.lindex(key, index));
  }

  @Override
  public Response<String> lset(byte[] key, long index, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.lset(key, index, value));
  }

  @Override
  public Response<Long> lrem(byte[] key, long count, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.lrem(key, count, value));
  }

  @Override
  public Response<byte[]> lpop(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.lpop(key));
  }

  @Override
  public Response<List<byte[]>> lpop(byte[] key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.lpop(key, count));
  }

  @Override
  public Response<Long> lpos(byte[] key, byte[] element) {
    return appendCommand(provider.getNode(key), commandObjects.lpos(key, element));
  }

  @Override
  public Response<Long> lpos(byte[] key, byte[] element, LPosParams params) {
    return appendCommand(provider.getNode(key), commandObjects.lpos(key, element, params));
  }

  @Override
  public Response<List<Long>> lpos(byte[] key, byte[] element, LPosParams params, long count) {
    return appendCommand(provider.getNode(key), commandObjects.lpos(key, element, params, count));
  }

  @Override
  public Response<byte[]> rpop(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.rpop(key));
  }

  @Override
  public Response<List<byte[]>> rpop(byte[] key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.rpop(key, count));
  }

  @Override
  public Response<Long> linsert(byte[] key, ListPosition where, byte[] pivot, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.linsert(key, where, pivot, value));
  }

  @Override
  public Response<Long> lpushx(byte[] key, byte[]... arg) {
    return appendCommand(provider.getNode(key), commandObjects.lpushx(key, arg));
  }

  @Override
  public Response<Long> rpushx(byte[] key, byte[]... arg) {
    return appendCommand(provider.getNode(key), commandObjects.rpushx(key, arg));
  }

  @Override
  public Response<List<byte[]>> blpop(int timeout, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.blpop(timeout, keys));
  }

  @Override
  public Response<List<byte[]>> blpop(double timeout, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.blpop(timeout, keys));
  }

  @Override
  public Response<List<byte[]>> brpop(int timeout, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.brpop(timeout, keys));
  }

  @Override
  public Response<List<byte[]>> brpop(double timeout, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.brpop(timeout, keys));
  }

  @Override
  public Response<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
    return appendCommand(provider.getNode(srckey), commandObjects.rpoplpush(srckey, dstkey));
  }

  @Override
  public Response<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
    return appendCommand(provider.getNode(source), commandObjects.brpoplpush(source, destination, timeout));
  }

  @Override
  public Response<byte[]> lmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to) {
    return appendCommand(provider.getNode(srcKey), commandObjects.lmove(srcKey, dstKey, from, to));
  }

  @Override
  public Response<byte[]> blmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to, double timeout) {
    return appendCommand(provider.getNode(srcKey), commandObjects.blmove(srcKey, dstKey, from, to, timeout));
  }

  @Override
  public Response<Long> publish(byte[] channel, byte[] message) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.publish(channel, message));
  }

  @Override
  public Response<LCSMatchResult> strAlgoLCSStrings(byte[] strA, byte[] strB, StrAlgoLCSParams params) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.strAlgoLCSStrings(strA, strB, params));
  }

  @Override
  public Response<Long> waitReplicas(byte[] sampleKey, int replicas, long timeout) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.waitReplicas(sampleKey, replicas, timeout));
  }

  @Override
  public Response<Object> eval(byte[] script, byte[] sampleKey) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.eval(script, sampleKey));
  }

  @Override
  public Response<Object> evalsha(byte[] sha1, byte[] sampleKey) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.evalsha(sha1, sampleKey));
  }

  @Override
  public Response<Boolean> scriptExists(byte[] sha1, byte[] sampleKey) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.scriptExists(sha1, sampleKey));
  }

  @Override
  public Response<List<Boolean>> scriptExists(byte[] sampleKey, byte[]... sha1s) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.scriptExists(sampleKey, sha1s));
  }

  @Override
  public Response<byte[]> scriptLoad(byte[] script, byte[] sampleKey) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.scriptLoad(script, sampleKey));
  }

  @Override
  public Response<String> scriptFlush(byte[] sampleKey) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.scriptFlush(sampleKey));
  }

  @Override
  public Response<String> scriptFlush(byte[] sampleKey, FlushMode flushMode) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.scriptFlush(sampleKey, flushMode));
  }

  @Override
  public Response<String> scriptKill(byte[] sampleKey) {
    return appendCommand(provider.getNode(sampleKey), commandObjects.scriptKill(sampleKey));
  }

  @Override
  public Response<Object> eval(byte[] script) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.eval(script));
  }

  @Override
  public Response<Object> eval(byte[] script, int keyCount, byte[]... params) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.eval(script, keyCount, params));
  }

  @Override
  public Response<Object> eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
    return appendCommand(provider.getNode(keys.get(0)), commandObjects.eval(script, keys, args));
  }

  @Override
  public Response<Object> evalsha(byte[] sha1) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.evalsha(sha1));
  }

  @Override
  public Response<Object> evalsha(byte[] sha1, int keyCount, byte[]... params) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.evalsha(sha1, keyCount, params));
  }

  @Override
  public Response<Object> evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
    return appendCommand(provider.getNode(keys.get(0)), commandObjects.evalsha(sha1, keys, args));
  }

  @Override
  public Response<Long> sadd(byte[] key, byte[]... member) {
    return appendCommand(provider.getNode(key), commandObjects.sadd(key, member));
  }

  @Override
  public Response<Set<byte[]>> smembers(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.smembers(key));
  }

  @Override
  public Response<Long> srem(byte[] key, byte[]... member) {
    return appendCommand(provider.getNode(key), commandObjects.srem(key, member));
  }

  @Override
  public Response<byte[]> spop(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.spop(key));
  }

  @Override
  public Response<Set<byte[]>> spop(byte[] key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.spop(key, count));
  }

  @Override
  public Response<Long> scard(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.scard(key));
  }

  @Override
  public Response<Boolean> sismember(byte[] key, byte[] member) {
    return appendCommand(provider.getNode(key), commandObjects.sismember(key, member));
  }

  @Override
  public Response<List<Boolean>> smismember(byte[] key, byte[]... members) {
    return appendCommand(provider.getNode(key), commandObjects.smismember(key, members));
  }

  @Override
  public Response<byte[]> srandmember(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.srandmember(key));
  }

  @Override
  public Response<List<byte[]>> srandmember(byte[] key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.srandmember(key, count));
  }

  @Override
  public Response<ScanResult<byte[]>> sscan(byte[] key, byte[] cursor, ScanParams params) {
    return appendCommand(provider.getNode(key), commandObjects.sscan(key, cursor, params));
  }

  @Override
  public Response<Set<byte[]>> sdiff(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sdiff(keys));
  }

  @Override
  public Response<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sdiffstore(dstkey, keys));
  }

  @Override
  public Response<Set<byte[]>> sinter(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sinter(keys));
  }

  @Override
  public Response<Long> sinterstore(byte[] dstkey, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sinterstore(dstkey, keys));
  }

  @Override
  public Response<Set<byte[]>> sunion(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sunion(keys));
  }

  @Override
  public Response<Long> sunionstore(byte[] dstkey, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.sunionstore(dstkey, keys));
  }

  @Override
  public Response<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
    return appendCommand(provider.getNode(srckey), commandObjects.smove(srckey, dstkey, member));
  }

  @Override
  public Response<Long> zadd(byte[] key, double score, byte[] member) {
    return appendCommand(provider.getNode(key), commandObjects.zadd(key, score, member));
  }

  @Override
  public Response<Long> zadd(byte[] key, double score, byte[] member, ZAddParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zadd(key, score, member, params));
  }

  @Override
  public Response<Long> zadd(byte[] key, Map<byte[], Double> scoreMembers) {
    return appendCommand(provider.getNode(key), commandObjects.zadd(key, scoreMembers));
  }

  @Override
  public Response<Long> zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zadd(key, scoreMembers, params));
  }

  @Override
  public Response<Double> zaddIncr(byte[] key, double score, byte[] member, ZAddParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zaddIncr(key, score, member, params));
  }

  @Override
  public Response<Long> zrem(byte[] key, byte[]... members) {
    return appendCommand(provider.getNode(key), commandObjects.zrem(key, members));
  }

  @Override
  public Response<Double> zincrby(byte[] key, double increment, byte[] member) {
    return appendCommand(provider.getNode(key), commandObjects.zincrby(key, increment, member));
  }

  @Override
  public Response<Double> zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zincrby(key, increment, member, params));
  }

  @Override
  public Response<Long> zrank(byte[] key, byte[] member) {
    return appendCommand(provider.getNode(key), commandObjects.zrank(key, member));
  }

  @Override
  public Response<Long> zrevrank(byte[] key, byte[] member) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrank(key, member));
  }

  @Override
  public Response<Set<byte[]>> zrange(byte[] key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zrange(key, start, stop));
  }

  @Override
  public Response<Set<byte[]>> zrevrange(byte[] key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrange(key, start, stop));
  }

  @Override
  public Response<Set<Tuple>> zrangeWithScores(byte[] key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeWithScores(key, start, stop));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeWithScores(byte[] key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeWithScores(key, start, stop));
  }

  @Override
  public Response<byte[]> zrandmember(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.zrandmember(key));
  }

  @Override
  public Response<Set<byte[]>> zrandmember(byte[] key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.zrandmember(key, count));
  }

  @Override
  public Response<Set<Tuple>> zrandmemberWithScores(byte[] key, long count) {
    return appendCommand(provider.getNode(key), commandObjects.zrandmemberWithScores(key, count));
  }

  @Override
  public Response<Long> zcard(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.zcard(key));
  }

  @Override
  public Response<Double> zscore(byte[] key, byte[] member) {
    return appendCommand(provider.getNode(key), commandObjects.zscore(key, member));
  }

  @Override
  public Response<List<Double>> zmscore(byte[] key, byte[]... members) {
    return appendCommand(provider.getNode(key), commandObjects.zmscore(key, members));
  }

  @Override
  public Response<Tuple> zpopmax(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.zpopmax(key));
  }

  @Override
  public Response<Set<Tuple>> zpopmax(byte[] key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zpopmax(key, count));
  }

  @Override
  public Response<Tuple> zpopmin(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.zpopmin(key));
  }

  @Override
  public Response<Set<Tuple>> zpopmin(byte[] key, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zpopmin(key, count));
  }

  @Override
  public Response<Long> zcount(byte[] key, double min, double max) {
    return appendCommand(provider.getNode(key), commandObjects.zcount(key, min, max));
  }

  @Override
  public Response<Long> zcount(byte[] key, byte[] min, byte[] max) {
    return appendCommand(provider.getNode(key), commandObjects.zcount(key, min, max));
  }

  @Override
  public Response<Set<byte[]>> zrangeByScore(byte[] key, double min, double max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScore(key, min, max));
  }

  @Override
  public Response<Set<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScore(key, min, max));
  }

  @Override
  public Response<Set<byte[]>> zrevrangeByScore(byte[] key, double max, double min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScore(key, min, max));
  }

  @Override
  public Response<Set<byte[]>> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScore(key, min, max, offset, count));
  }

  @Override
  public Response<Set<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScore(key, min, max));
  }

  @Override
  public Response<Set<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScore(key, min, max, offset, count));
  }

  @Override
  public Response<Set<byte[]>> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScore(key, min, max, offset, count));
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScoreWithScores(key, min, max));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScoreWithScores(key, min, max));
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
  }

  @Override
  public Response<Set<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScore(key, min, max, offset, count));
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScoreWithScores(key, min, max));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScoreWithScores(key, min, max));
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScoreWithScores(key, min, max, offset, count));
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByScoreWithScores(key, min, max, offset, count));
  }

  @Override
  public Response<Long> zremrangeByRank(byte[] key, long start, long stop) {
    return appendCommand(provider.getNode(key), commandObjects.zremrangeByRank(key, start, stop));
  }

  @Override
  public Response<Long> zremrangeByScore(byte[] key, double min, double max) {
    return appendCommand(provider.getNode(key), commandObjects.zremrangeByScore(key, min, max));
  }

  @Override
  public Response<Long> zremrangeByScore(byte[] key, byte[] min, byte[] max) {
    return appendCommand(provider.getNode(key), commandObjects.zremrangeByScore(key, min, max));
  }

  @Override
  public Response<Long> zlexcount(byte[] key, byte[] min, byte[] max) {
    return appendCommand(provider.getNode(key), commandObjects.zlexcount(key, min, max));
  }

  @Override
  public Response<Set<byte[]>> zrangeByLex(byte[] key, byte[] min, byte[] max) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByLex(key, min, max));
  }

  @Override
  public Response<Set<byte[]>> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrangeByLex(key, min, max, offset, count));
  }

  @Override
  public Response<Set<byte[]>> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByLex(key, min, max));
  }

  @Override
  public Response<Set<byte[]>> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
    return appendCommand(provider.getNode(key), commandObjects.zrevrangeByLex(key, min, max, offset, count));
  }

  @Override
  public Response<Long> zremrangeByLex(byte[] key, byte[] min, byte[] max) {
    return appendCommand(provider.getNode(key), commandObjects.zremrangeByLex(key, min, max));
  }

  @Override
  public Response<ScanResult<Tuple>> zscan(byte[] key, byte[] cursor, ScanParams params) {
    return appendCommand(provider.getNode(key), commandObjects.zscan(key, cursor, params));
  }

  @Override
  public Response<List<byte[]>> bzpopmax(double timeout, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.bzpopmax(timeout, keys));
  }

  @Override
  public Response<List<byte[]>> bzpopmin(double timeout, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.bzpopmin(timeout, keys));
  }

  @Override
  public Response<Set<byte[]>> zdiff(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zdiff(keys));
  }

  @Override
  public Response<Set<Tuple>> zdiffWithScores(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zdiffWithScores(keys));
  }

  @Override
  public Response<Long> zdiffStore(byte[] dstkey, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zdiffStore(dstkey, keys));
  }

  @Override
  public Response<Set<byte[]>> zinter(ZParams params, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zinter(params, keys));
  }

  @Override
  public Response<Set<Tuple>> zinterWithScores(ZParams params, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zinterWithScores(params, keys));
  }

  @Override
  public Response<Long> zinterstore(byte[] dstkey, byte[]... sets) {
    return appendCommand(provider.getNode(dstkey), commandObjects.zinterstore(dstkey, sets));
  }

  @Override
  public Response<Long> zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
    return appendCommand(provider.getNode(dstkey), commandObjects.zinterstore(dstkey, params, sets));
  }

  @Override
  public Response<Set<byte[]>> zunion(ZParams params, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zunion(params, keys));
  }

  @Override
  public Response<Set<Tuple>> zunionWithScores(ZParams params, byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.zunionWithScores(params, keys));
  }

  @Override
  public Response<Long> zunionstore(byte[] dstkey, byte[]... sets) {
    return appendCommand(provider.getNode(dstkey), commandObjects.zunionstore(dstkey, sets));
  }

  @Override
  public Response<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
    return appendCommand(provider.getNode(dstkey), commandObjects.zunionstore(dstkey, params, sets));
  }

  @Override
  public Response<byte[]> xadd(byte[] key, XAddParams params, Map<byte[], byte[]> hash) {
    return appendCommand(provider.getNode(key), commandObjects.xadd(key, params, hash));
  }

  @Override
  public Response<Long> xlen(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.xlen(key));
  }

  @Override
  public Response<List<byte[]>> xrange(byte[] key, byte[] start, byte[] end) {
    return appendCommand(provider.getNode(key), commandObjects.xrange(key, start, end));
  }

  @Override
  public Response<List<byte[]>> xrange(byte[] key, byte[] start, byte[] end, int count) {
    return appendCommand(provider.getNode(key), commandObjects.xrange(key, start, end, count));
  }

  @Override
  public Response<List<byte[]>> xrevrange(byte[] key, byte[] end, byte[] start) {
    return appendCommand(provider.getNode(key), commandObjects.xrevrange(key, end, start));
  }

  @Override
  public Response<List<byte[]>> xrevrange(byte[] key, byte[] end, byte[] start, int count) {
    return appendCommand(provider.getNode(key), commandObjects.xrevrange(key, end, start, count));
  }

  @Override
  public Response<Long> xack(byte[] key, byte[] group, byte[]... ids) {
    return appendCommand(provider.getNode(key), commandObjects.xack(key, group, ids));
  }

  @Override
  public Response<String> xgroupCreate(byte[] key, byte[] groupname, byte[] id, boolean makeStream) {
    return appendCommand(provider.getNode(key), commandObjects.xgroupCreate(key, groupname, id, makeStream));
  }

  @Override
  public Response<String> xgroupSetID(byte[] key, byte[] groupname, byte[] id) {
    return appendCommand(provider.getNode(key), commandObjects.xgroupSetID(key, groupname, id));
  }

  @Override
  public Response<Long> xgroupDestroy(byte[] key, byte[] groupname) {
    return appendCommand(provider.getNode(key), commandObjects.xgroupDestroy(key, groupname));
  }

  @Override
  public Response<Long> xgroupDelConsumer(byte[] key, byte[] groupname, byte[] consumerName) {
    return appendCommand(provider.getNode(key), commandObjects.xgroupDelConsumer(key, groupname, consumerName));
  }

  @Override
  public Response<Long> xdel(byte[] key, byte[]... ids) {
    return appendCommand(provider.getNode(key), commandObjects.xdel(key, ids));
  }

  @Override
  public Response<Long> xtrim(byte[] key, long maxLen, boolean approximateLength) {
    return appendCommand(provider.getNode(key), commandObjects.xtrim(key, maxLen, approximateLength));
  }

  @Override
  public Response<Long> xtrim(byte[] key, XTrimParams params) {
    return appendCommand(provider.getNode(key), commandObjects.xtrim(key, params));
  }

  @Override
  public Response<Object> xpending(byte[] key, byte[] groupname) {
    return appendCommand(provider.getNode(key), commandObjects.xpending(key, groupname));
  }

  @Override
  public Response<List<Object>> xpending(byte[] key, byte[] groupname, byte[] start, byte[] end, int count, byte[] consumername) {
    return appendCommand(provider.getNode(key), commandObjects.xpending(key, groupname, start, end, count, consumername));
  }

  @Override
  public Response<List<Object>> xpending(byte[] key, byte[] groupname, XPendingParams params) {
    return appendCommand(provider.getNode(key), commandObjects.xpending(key, groupname, params));
  }

  @Override
  public Response<List<byte[]>> xclaim(byte[] key, byte[] group, byte[] consumername, long minIdleTime, XClaimParams params, byte[]... ids) {
    return appendCommand(provider.getNode(key), commandObjects.xclaim(key, group, consumername, minIdleTime, params, ids));
  }

  @Override
  public Response<List<byte[]>> xclaimJustId(byte[] key, byte[] group, byte[] consumername, long minIdleTime, XClaimParams params, byte[]... ids) {
    return appendCommand(provider.getNode(key), commandObjects.xclaimJustId(key, group, consumername, minIdleTime, params, ids));
  }

  @Override
  public Response<List<Object>> xautoclaim(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
    return appendCommand(provider.getNode(key), commandObjects.xautoclaim(key, groupName, consumerName, minIdleTime, start, params));
  }

  @Override
  public Response<List<Object>> xautoclaimJustId(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
    return appendCommand(provider.getNode(key), commandObjects.xautoclaimJustId(key, groupName, consumerName, minIdleTime, start, params));
  }

  @Override
  public Response<Object> xinfoStream(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.xinfoStream(key));
  }

  @Override
  public Response<List<Object>> xinfoGroup(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.xinfoGroup(key));
  }

  @Override
  public Response<List<Object>> xinfoConsumers(byte[] key, byte[] group) {
    return appendCommand(provider.getNode(key), commandObjects.xinfoConsumers(key, group));
  }

  @Override
  public Response<List<byte[]>> xread(XReadParams xReadParams, Map.Entry<byte[], byte[]>... streams) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.xread(xReadParams, streams));
  }

  @Override
  public Response<List<byte[]>> xreadGroup(byte[] groupname, byte[] consumer, XReadGroupParams xReadGroupParams, Map.Entry<byte[], byte[]>... streams) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.xreadGroup(groupname, consumer, xReadGroupParams, streams));
  }

  @Override
  public Response<String> set(byte[] key, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.set(key, value));
  }

  @Override
  public Response<String> set(byte[] key, byte[] value, SetParams params) {
    return appendCommand(provider.getNode(key), commandObjects.set(key, value, params));
  }

  @Override
  public Response<byte[]> get(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.get(key));
  }

  @Override
  public Response<byte[]> getDel(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.getDel(key));
  }

  @Override
  public Response<byte[]> getEx(byte[] key, GetExParams params) {
    return appendCommand(provider.getNode(key), commandObjects.getEx(key, params));
  }

  @Override
  public Response<Boolean> setbit(byte[] key, long offset, boolean value) {
    return appendCommand(provider.getNode(key), commandObjects.setbit(key, offset, value));
  }

  @Override
  public Response<Boolean> getbit(byte[] key, long offset) {
    return appendCommand(provider.getNode(key), commandObjects.getbit(key, offset));
  }

  @Override
  public Response<Long> setrange(byte[] key, long offset, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.setrange(key, offset, value));
  }

  @Override
  public Response<byte[]> getrange(byte[] key, long startOffset, long endOffset) {
    return appendCommand(provider.getNode(key), commandObjects.getrange(key, startOffset, endOffset));
  }

  @Override
  public Response<byte[]> getSet(byte[] key, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.getSet(key, value));
  }

  @Override
  public Response<Long> setnx(byte[] key, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.setnx(key, value));
  }

  @Override
  public Response<String> setex(byte[] key, long seconds, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.setex(key, seconds, value));
  }

  @Override
  public Response<String> psetex(byte[] key, long milliseconds, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.psetex(key, milliseconds, value));
  }

  @Override
  public Response<List<byte[]>> mget(byte[]... keys) {
    return appendCommand(provider.getNode(keys[0]), commandObjects.mget(keys));
  }

  @Override
  public Response<String> mset(byte[]... keysvalues) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.mset(keysvalues));
  }

  @Override
  public Response<Long> msetnx(byte[]... keysvalues) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.msetnx(keysvalues));
  }

  @Override
  public Response<Long> incr(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.incr(key));
  }

  @Override
  public Response<Long> incrBy(byte[] key, long increment) {
    return appendCommand(provider.getNode(key), commandObjects.incrBy(key, increment));
  }

  @Override
  public Response<Double> incrByFloat(byte[] key, double increment) {
    return appendCommand(provider.getNode(key), commandObjects.incrByFloat(key, increment));
  }

  @Override
  public Response<Long> decr(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.decr(key));
  }

  @Override
  public Response<Long> decrBy(byte[] key, long decrement) {
    return appendCommand(provider.getNode(key), commandObjects.decrBy(key, decrement));
  }

  @Override
  public Response<Long> append(byte[] key, byte[] value) {
    return appendCommand(provider.getNode(key), commandObjects.append(key, value));
  }

  @Override
  public Response<byte[]> substr(byte[] key, int start, int end) {
    return appendCommand(provider.getNode(key), commandObjects.substr(key, start, end));
  }

  @Override
  public Response<Long> strlen(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.strlen(key));
  }

  @Override
  public Response<Long> bitcount(byte[] key) {
    return appendCommand(provider.getNode(key), commandObjects.bitcount(key));
  }

  @Override
  public Response<Long> bitcount(byte[] key, long start, long end) {
    return appendCommand(provider.getNode(key), commandObjects.bitcount(key, start, end));
  }

  @Override
  public Response<Long> bitpos(byte[] key, boolean value) {
    return appendCommand(provider.getNode(key), commandObjects.bitpos(key, value));
  }

  @Override
  public Response<Long> bitpos(byte[] key, boolean value, BitPosParams params) {
    return appendCommand(provider.getNode(key), commandObjects.bitpos(key, value, params));
  }

  @Override
  public Response<List<Long>> bitfield(byte[] key, byte[]... arguments) {
    return appendCommand(provider.getNode(key), commandObjects.bitfield(key, arguments));
  }

  @Override
  public Response<List<Long>> bitfieldReadonly(byte[] key, byte[]... arguments) {
    return appendCommand(provider.getNode(key), commandObjects.bitfieldReadonly(key, arguments));
  }

  @Override
  public Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
    return appendCommand(provider.getNode(srcKeys[0]), commandObjects.bitop(op, destKey, srcKeys));
  }

  @Override
  public Response<LCSMatchResult> strAlgoLCSKeys(byte[] keyA, byte[] keyB, StrAlgoLCSParams params) {
    throw new UnsupportedOperationException("Not supported yet.");
    // return appendCommand(provider.getNode(key), commandObjects.strAlgoLCSStrings(keyA, keyB, params));
  }

  @Override
  public Response<String> jsonSet(String key, Object object) {
    return appendCommand(provider.getNode(key), commandObjects.jsonSet(key, object));
  }

  @Override
  public Response<String> jsonSet(String key, Path path, Object object) {
    return appendCommand(provider.getNode(key), commandObjects.jsonSet(key, path, object));
  }

  @Override
  public <T> Response<T> jsonGet(String key, Class<T> clazz) {
    return appendCommand(provider.getNode(key), commandObjects.jsonGet(key, clazz));
  }

  @Override
  public <T> Response<T> jsonGet(String key, Class<T> clazz, Path... paths) {
    return appendCommand(provider.getNode(key), commandObjects.jsonGet(key, clazz, paths));
  }

  @Override
  public Response<Long> jsonDel(String key) {
    return appendCommand(provider.getNode(key), commandObjects.jsonDel(key));
  }

  @Override
  public Response<Long> jsonDel(String key, Path path) {
    return appendCommand(provider.getNode(key), commandObjects.jsonDel(key, path));
  }

  @Override
  public Response<String> ftCreate(String indexName, IndexOptions indexOptions, Schema schema) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.ftCreate(indexName, indexOptions, schema));
  }

  @Override
  public Response<SearchResult> ftSearch(String indexName, Query query) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.ftCreate(indexName, query));
  }

  @Override
  public Response<SearchResult> ftSearch(byte[] indexName, Query query) {
    throw new UnsupportedOperationException("Not supported yet.");
    //return appendCommand(provider.getNode(key), commandObjects.ftCreate(indexName, query));
  }
}
