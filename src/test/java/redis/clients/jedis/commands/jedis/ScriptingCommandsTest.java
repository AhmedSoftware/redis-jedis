package redis.clients.jedis.commands.jedis;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.FlushMode;
import redis.clients.jedis.args.RestorePolicy;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisNoScriptException;
import redis.clients.jedis.params.FunctionLoadParams;
import redis.clients.jedis.resps.LibraryInfo;
import redis.clients.jedis.util.ClientKillerUtil;
import redis.clients.jedis.util.SafeEncoder;

public class ScriptingCommandsTest extends JedisCommandsTestBase {

  final byte[] bfoo = { 0x01, 0x02, 0x03, 0x04 };
  final byte[] bfoo1 = { 0x01, 0x02, 0x03, 0x04, 0x0A };
  final byte[] bfoo2 = { 0x01, 0x02, 0x03, 0x04, 0x0B };
  final byte[] bfoo3 = { 0x01, 0x02, 0x03, 0x04, 0x0C };
  final byte[] bbar = { 0x05, 0x06, 0x07, 0x08 };
  final byte[] bbar1 = { 0x05, 0x06, 0x07, 0x08, 0x0A };
  final byte[] bbar2 = { 0x05, 0x06, 0x07, 0x08, 0x0B };
  final byte[] bbar3 = { 0x05, 0x06, 0x07, 0x08, 0x0C };
  final byte[] bfoobar = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };

  @SuppressWarnings("unchecked")
  @Test
  public void evalMultiBulk() {
    String script = "return {KEYS[1],KEYS[2],ARGV[1],ARGV[2],ARGV[3]}";
    List<String> keys = new ArrayList<String>();
    keys.add("key1");
    keys.add("key2");

    List<String> args = new ArrayList<String>();
    args.add("first");
    args.add("second");
    args.add("third");

    List<String> response = (List<String>) jedis.eval(script, keys, args);

    assertEquals(5, response.size());
    assertEquals("key1", response.get(0));
    assertEquals("key2", response.get(1));
    assertEquals("first", response.get(2));
    assertEquals("second", response.get(3));
    assertEquals("third", response.get(4));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void evalMultiBulkWithBinaryJedis() {
    String script = "return {KEYS[1],KEYS[2],ARGV[1],ARGV[2],ARGV[3]}";
    List<byte[]> keys = new ArrayList<byte[]>();
    keys.add("key1".getBytes());
    keys.add("key2".getBytes());

    List<byte[]> args = new ArrayList<byte[]>();
    args.add("first".getBytes());
    args.add("second".getBytes());
    args.add("third".getBytes());

    List<byte[]> responses = (List<byte[]>) jedis.eval(script.getBytes(), keys, args);
    assertEquals(5, responses.size());
    assertEquals("key1", new String(responses.get(0)));
    assertEquals("key2", new String(responses.get(1)));
    assertEquals("first", new String(responses.get(2)));
    assertEquals("second", new String(responses.get(3)));
    assertEquals("third", new String(responses.get(4)));
  }

  @Test
  public void evalBulk() {
    String script = "return KEYS[1]";
    List<String> keys = new ArrayList<String>();
    keys.add("key1");

    List<String> args = new ArrayList<String>();
    args.add("first");

    String response = (String) jedis.eval(script, keys, args);

    assertEquals("key1", response);
  }

  @Test
  public void evalInt() {
    String script = "return 2";
    List<String> keys = new ArrayList<String>();
    keys.add("key1");

    Long response = (Long) jedis.eval(script, keys, new ArrayList<String>());

    assertEquals(new Long(2), response);
  }

  @Test
  public void evalNestedLists() {
    String script = "return { {KEYS[1]} , {2} }";
    List<?> results = (List<?>) jedis.eval(script, 1, "key1");

    assertThat((List<String>) results.get(0), listWithItem("key1"));
    assertThat((List<Long>) results.get(1), listWithItem(2L));
  }

  @Test
  public void evalNoArgs() {
    String script = "return KEYS[1]";
    List<String> keys = new ArrayList<String>();
    keys.add("key1");
    String response = (String) jedis.eval(script, keys, new ArrayList<String>());

    assertEquals("key1", response);
  }

  @Test
  public void evalReadonly() {
    String script = "return KEYS[1]";
    List<String> keys = new ArrayList<String>();
    keys.add("key1");

    List<String> args = new ArrayList<String>();
    args.add("first");

    String response = (String) jedis.evalReadonly(script, keys, args);

    assertEquals("key1", response);
  }

  @Test
  public void evalsha() {
    jedis.set("foo", "bar");
    jedis.eval("return redis.call('get','foo')");
    String result = (String) jedis.evalsha("6b1bf486c81ceb7edf3c093f4c48582e38c0e791");

    assertEquals("bar", result);
  }

  @Test
  public void evalshaReadonly() {
    jedis.set("foo", "bar");
    jedis.eval("return redis.call('get','foo')");
    String result = (String) jedis.evalshaReadonly("6b1bf486c81ceb7edf3c093f4c48582e38c0e791",
            Collections.emptyList(), Collections.emptyList());

    assertEquals("bar", result);
  }

  @Test
  public void evalshaBinary() {
    jedis.set(SafeEncoder.encode("foo"), SafeEncoder.encode("bar"));
    jedis.eval(SafeEncoder.encode("return redis.call('get','foo')"));
    byte[] result = (byte[]) jedis.evalsha(SafeEncoder
        .encode("6b1bf486c81ceb7edf3c093f4c48582e38c0e791"));

    assertArrayEquals(SafeEncoder.encode("bar"), result);
  }

  @Test
  public void evalshaReadonlyBinary() {
    jedis.set(SafeEncoder.encode("foo"), SafeEncoder.encode("bar"));
    jedis.eval(SafeEncoder.encode("return redis.call('get','foo')"));
    byte[] result = (byte[]) jedis.evalshaReadonly(SafeEncoder.encode("6b1bf486c81ceb7edf3c093f4c48582e38c0e791"),
            Collections.emptyList(), Collections.emptyList());

    assertArrayEquals(SafeEncoder.encode("bar"), result);
  }

  @Test(expected = JedisNoScriptException.class)
  public void evalshaShaNotFound() {
    jedis.evalsha("ffffffffffffffffffffffffffffffffffffffff");
  }

  @Test
  public void scriptFlush() {
    jedis.set("foo", "bar");
    jedis.eval("return redis.call('get','foo')");
    jedis.scriptFlush();
    assertFalse(jedis.scriptExists("6b1bf486c81ceb7edf3c093f4c48582e38c0e791"));
  }

  @Test
  public void scriptFlushMode() {
    jedis.set("foo", "bar");
    jedis.eval("return redis.call('get','foo')");
    String sha1 = "6b1bf486c81ceb7edf3c093f4c48582e38c0e791";
    assertTrue(jedis.scriptExists(sha1));
    jedis.scriptFlush(FlushMode.SYNC);
    assertFalse(jedis.scriptExists(sha1));
  }

  @Test
  public void scriptExists() {
    jedis.scriptLoad("return redis.call('get','foo')");
    List<Boolean> exists = jedis.scriptExists("ffffffffffffffffffffffffffffffffffffffff",
      "6b1bf486c81ceb7edf3c093f4c48582e38c0e791");
    assertFalse(exists.get(0));
    assertTrue(exists.get(1));
  }

  @Test
  public void scriptExistsBinary() {
    jedis.scriptLoad(SafeEncoder.encode("return redis.call('get','foo')"));
    List<Boolean> exists = jedis.scriptExists(
      SafeEncoder.encode("ffffffffffffffffffffffffffffffffffffffff"),
      SafeEncoder.encode("6b1bf486c81ceb7edf3c093f4c48582e38c0e791"));
    assertFalse(exists.get(0));
    assertTrue(exists.get(1));
  }

  @Test
  public void scriptLoad() {
    jedis.scriptLoad("return redis.call('get','foo')");
    assertTrue(jedis.scriptExists("6b1bf486c81ceb7edf3c093f4c48582e38c0e791"));
  }

  @Test
  public void scriptLoadBinary() {
    jedis.scriptLoad(SafeEncoder.encode("return redis.call('get','foo')"));
    Boolean exists = jedis
        .scriptExists(SafeEncoder.encode("6b1bf486c81ceb7edf3c093f4c48582e38c0e791"));
    assertTrue(exists);
  }

  @Test
  public void scriptKill() {
    try {
      jedis.scriptKill();
    } catch (JedisDataException e) {
      assertTrue(e.getMessage().contains("No scripts in execution right now."));
    }
  }

  @Test
  public void scriptEvalReturnNullValues() {
    jedis.del("key1");
    jedis.del("key2");

    String script = "return {redis.call('hget',KEYS[1],ARGV[1]),redis.call('hget',KEYS[2],ARGV[2])}";
    List<String> results = (List<String>) jedis.eval(script, 2, "key1", "key2", "1", "2");
    assertEquals(2, results.size());
    assertNull(results.get(0));
    assertNull(results.get(1));
  }

  @Test
  public void scriptEvalShaReturnNullValues() {
    jedis.del("key1");
    jedis.del("key2");

    String script = "return {redis.call('hget',KEYS[1],ARGV[1]),redis.call('hget',KEYS[2],ARGV[2])}";
    String sha = jedis.scriptLoad(script);
    List<String> results = (List<String>) jedis.evalsha(sha, 2, "key1", "key2", "1", "2");
    assertEquals(2, results.size());
    assertNull(results.get(0));
    assertNull(results.get(1));
  }

  @Test
  public void scriptEvalShaReturnValues() {
    jedis.hset("foo", "foo1", "bar1");
    jedis.hset("foobar", "foo2", "bar2");

    String script = "return {redis.call('hget',KEYS[1],ARGV[1]),redis.call('hget',KEYS[2],ARGV[2])}";
    String sha = jedis.scriptLoad(script);
    List<String> results = (List<String>) jedis.evalsha(sha, Arrays.asList("foo", "foobar"), Arrays.asList("foo1", "foo2"));
    assertEquals(2, results.size());
    assertEquals("bar1", results.get(0));
    assertEquals("bar2", results.get(1));
  }

  @Test
  public void scriptEvalShaReturnValuesBinary() {
    jedis.hset(bfoo, bfoo1, bbar1);
    jedis.hset(bfoobar, bfoo2, bbar2);

    byte[] script = "return {redis.call('hget',KEYS[1],ARGV[1]),redis.call('hget',KEYS[2],ARGV[2])}".getBytes();
    byte[] sha = jedis.scriptLoad(script);
    List<byte[]> results = (List<byte[]>) jedis.evalsha(sha, Arrays.asList(bfoo, bfoobar), Arrays.asList(bfoo1, bfoo2));
    assertEquals(2, results.size());
    assertArrayEquals(bbar1, results.get(0));
    assertArrayEquals(bbar2, results.get(1));
  }

  @Test
  public void scriptExistsWithBrokenConnection() {
    Jedis deadClient = createJedis();

    deadClient.clientSetname("DEAD");

    ClientKillerUtil.killClient(deadClient, "DEAD");

    // sure, script doesn't exist, but it's just for checking connection
    try {
      deadClient.scriptExists("abcdefg");
    } catch (JedisConnectionException e) {
      // ignore it
    }

    assertEquals(true, deadClient.isBroken());

    deadClient.close();
  }

  @Test
  public void functionLoadAndDelete() {
    jedis.functionFlush();
    String engine = "Lua";
    String library = "mylib";
    String function = "redis.register_function('myfunc', function(keys, args) return args[1] end)";

    assertEquals("OK", jedis.functionLoad(engine, library, function));
    assertEquals("OK", jedis.functionLoad(engine, library, new FunctionLoadParams().replace(), function));
//    assertEquals("OK", jedis.functionLoad(engine, library, new FunctionLoadParams().libraryDescription(""), function));

    jedis.functionDelete(library);

    // Binary
    assertEquals("OK", jedis.functionLoad(engine.getBytes(), library.getBytes(), function.getBytes()));
    assertEquals("OK", jedis.functionLoad(engine.getBytes(), library.getBytes(), new FunctionLoadParams().replace(), function.getBytes()));

    jedis.functionDelete(library.getBytes());
  }

  @Test
  public void functionFlush() {
    jedis.functionFlush();
    String engine = "Lua";
    String library = "mylib";
    String function = "redis.register_function('myfunc', function(keys, args) return args[1] end)";

    assertEquals("OK", jedis.functionLoad(engine, library, function));
    jedis.functionFlush();
    assertEquals("OK", jedis.functionLoad(engine, library, function));
    jedis.functionFlush(FlushMode.ASYNC);
    assertEquals("OK", jedis.functionLoad(engine, library, function));
    jedis.functionFlush(FlushMode.SYNC);
  }

  @Test
  public void functionList() {
    jedis.functionFlush();
    String engine = "LUA";
    String library = "mylib";
    String function = "redis.register_function('myfunc', function(keys, args) return args[1] end)";
    jedis.functionLoad(engine, library, function);

    LibraryInfo response = jedis.functionList().get(0);
    assertEquals(library, response.getName());
    assertEquals(engine, response.getEngine());
    assertNull(response.getDescription());
    assertEquals(1, response.getFunctions().length);

    // check function info
    LibraryInfo.FunctionInfo func = response.getFunctions()[0];
    assertEquals("name", func.getName());
    assertEquals("myfunc", func.getDescription());
    assertNull(func.getFlags());

    // check WITHCODE
    response = jedis.functionListWithCode().get(0);
    assertEquals(function, response.getCode());

    // check with LIBRARYNAME
    response = jedis.functionList(library).get(0);
    assertEquals(library, response.getName());

    // check with code and with LIBRARYNAME
    response = jedis.functionListWithCode(library).get(0);
    assertEquals(library, response.getName());
    assertEquals(function, response.getCode());

    // Binary
    response = jedis.functionList(library.getBytes()).get(0);
    assertEquals(library, response.getName());

    response = jedis.functionListWithCode(library.getBytes()).get(0);
    assertEquals(library, response.getName());
  }

  @Test
  public void functionDumpRestore() {
    jedis.functionFlush();
    String engine = "Lua";
    String library = "mylib";
    String function = "redis.register_function('myfunc', function(keys, args) return args[1] end)";

    jedis.functionLoad(engine, library, function);
    byte[] payload = jedis.functionDump();
    jedis.functionFlush();
    assertEquals("OK", jedis.functionRestore(payload));
    jedis.functionFlush();
    assertEquals("OK", jedis.functionRestore(payload, RestorePolicy.FLUSH));
    jedis.functionFlush();
    assertEquals("OK", jedis.functionRestore(payload, RestorePolicy.APPEND));
    jedis.functionFlush();
    assertEquals("OK", jedis.functionRestore(payload, RestorePolicy.REPLACE));
    jedis.functionFlush();
  }

  @Test
  public void fcall() {
    jedis.functionFlush();
    String engine = "Lua";
    String library = "mylib";
    String function = "redis.register_function('myfunc', function(keys, args) return args[1] end)";

    jedis.functionLoad(engine, library, function);
    List<String> args = new ArrayList<>();
    args.add("hello");
    assertNotNull(jedis.fcall("myfunc", new ArrayList<>(), args));
//    assertNotNull(jedis.fcallReadonly("myfunc", new ArrayList<>(), args));

    List<byte[]> bargs = new ArrayList<>();
    bargs.add("hello".getBytes());
    assertNotNull(jedis.fcall("myfunc".getBytes(), new ArrayList<>(), bargs));
//    assertNotNull(jedis.fcallReadonly("myfunc".getBytes(), new ArrayList<>(), bargs));
  }

  private <T> Matcher<Iterable<? super T>> listWithItem(T expected) {
    return CoreMatchers.<T> hasItem(equalTo(expected));
  }
}
