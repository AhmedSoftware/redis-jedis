package redis.clients.jedis.commands;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.args.ListDirection;
import redis.clients.jedis.args.ListPosition;
import redis.clients.jedis.params.LPosParams;
import redis.clients.jedis.resps.KeyedListElementsBinary;

public interface ListBinaryCommands {

  long rpush(byte[] key, byte[]... args);

  long lpush(byte[] key, byte[]... args);

  long llen(byte[] key);

  List<byte[]> lrange(byte[] key, long start, long stop);

  String ltrim(byte[] key, long start, long stop);

  byte[] lindex(byte[] key, long index);

  String lset(byte[] key, long index, byte[] value);

  long lrem(byte[] key, long count, byte[] value);

  byte[] lpop(byte[] key);

  List<byte[]> lpop(byte[] key, int count);

  Long lpos(byte[] key, byte[] element);

  Long lpos(byte[] key, byte[] element, LPosParams params);

  List<Long> lpos(byte[] key, byte[] element, LPosParams params, long count);

  byte[] rpop(byte[] key);

  List<byte[]> rpop(byte[] key, int count);

  long linsert(byte[] key, ListPosition where, byte[] pivot, byte[] value);

  long lpushx(byte[] key, byte[]... args);

  long rpushx(byte[] key, byte[]... args);

  List<byte[]> blpop(int timeout, byte[]... keys);

  List<byte[]> blpop(double timeout, byte[]... keys);

  List<byte[]> brpop(int timeout, byte[]... keys);

  List<byte[]> brpop(double timeout, byte[]... keys);

  byte[] rpoplpush(byte[] srckey, byte[] dstkey);

  byte[] brpoplpush(byte[] source, byte[] destination, int timeout);

  byte[] lmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to);

  byte[] blmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to, double timeout);

  /**
   * Pops one element from the first non-empty list key from the list of provided key names.
   *
   * @param from LEFT|RIGHT
   * @param keys key of list
   * @return element from the first non-empty list key from the list of provided key names
   * @see <a href="https://redis.io/commands/lmpop">LMPOP numkeys key [key ...] LEFT|RIGHT<a/>
   */
  List<Object> lmpop(ListDirection from, byte[]... keys);

  /**
   * Pops one or more elements from the first non-empty list key from the list of provided key names.
   *
   * @param from  LEFT|RIGHT
   * @param count count of pop elements
   * @param keys  key of list
   * @return elements from the first non-empty list key from the list of provided key names.
   * @see <a href="https://redis.io/commands/lmpop">LMPOP numkeys key [key ...] LEFT|RIGHT COUNT count<a/>
   */
  List<Object> lmpop(ListDirection from, int count, byte[]... keys);

}
