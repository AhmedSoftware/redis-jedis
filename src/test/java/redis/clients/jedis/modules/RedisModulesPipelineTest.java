package redis.clients.jedis.modules;

import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.json.JsonSetParams;
import redis.clients.jedis.json.Path;
import redis.clients.jedis.search.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static redis.clients.jedis.search.RediSearchUtil.toStringMap;

public class RedisModulesPipelineTest extends RedisModuleCommandsTestBase {
    @BeforeClass
    public static void prepare() {
        RedisModuleCommandsTestBase.prepare();
    }

    @Test
    public void search() {
        Jedis jedis = new Jedis(hnp, DefaultJedisClientConfig.builder().timeoutMillis(500).build());
        jedis.flushAll();

        Schema sc = new Schema().addTextField("title", 1.0).addTextField("body", 1.0);

        Map<String, Object> fields = new HashMap<>();
        fields.put("title", "hello world");
        fields.put("body", "lorem ipsum");

        Pipeline p = jedis.pipelined();
        Response<String> string = p.ftCreate("testindex", IndexOptions.defaultOptions(), sc);
        for (int i = 0; i < 100; i++) {
            p.hset(String.format("doc%d", i), toStringMap(fields));
        }
        Response<SearchResult> searchResultResponse = p.ftSearch("testindex", new Query("hello world").limit(0, 5).setWithScores());
        Response<Long> delResponse = p.del("doc0");
        Response<SearchResult> searchResultResponse2 = p.ftSearch("testindex", new Query("hello world"));
        Response<String> dropIndexResponse = p.ftDropIndex("testindex");
        p.sync();


        assertEquals("OK", string.get());
        assertEquals(100, searchResultResponse.get().getTotalResults());
        assertEquals(5, searchResultResponse.get().getDocuments().size());
        for (Document d : searchResultResponse.get().getDocuments()) {
            assertTrue(d.getId().startsWith("doc"));
            assertTrue(d.getScore() < 100);
        }
        assertEquals(Long.valueOf(1), delResponse.get());
        assertEquals(99, searchResultResponse2.get().getTotalResults());
        assertEquals("OK", dropIndexResponse.get());
    }

    @Test
    public void json() {
        Jedis jedis = new Jedis(hnp, DefaultJedisClientConfig.builder().timeoutMillis(500).build());
        jedis.flushAll();

        Map<String, String> hm1 = new HashMap<>();
        hm1.put("hello", "world");
        hm1.put("oh", "snap");

        Map<String, Object> hm2 = new HashMap<>();
        hm2.put("array", new String[] {"a","b","c"});
        hm2.put("boolean", true);
        hm2.put("number", 3);

        Pipeline p = jedis.pipelined();
        Response<String> string1 = p.jsonSet("foo", Path.ROOT_PATH, hm1);
        Response<Object> object = p.jsonGet("foo");
        Response<Long> strLenPath = p.jsonStrLen("foo", new Path("hello"));
        Response<Long> strAppPath = p.jsonStrAppend("foo", new Path("hello"), "!");
        Response<Long> delPah = p.jsonDel("foo", new Path("hello"));
        Response<Long> delKey = p.jsonDel("foo");
        Response<Set<String>> keys = p.keys("*");
        Response<String> string2 = p.jsonSet("foo", Path.ROOT_PATH, hm2, new JsonSetParams().nx());
        Response<Object> pop = p.jsonArrPop("foo", new Path("array"));
        Response<Long> append = p.jsonArrAppend("foo", new Path("array"), "c", "d");
        Response<Long> index = p.jsonArrIndex("foo", new Path("array"), "c");
        Response<Long> insert = p.jsonArrInsert("foo", new Path("array"), 0,"x");
        Response<Long> arrLen = p.jsonArrLen("foo", new Path("array"));
        Response<Long> trim = p.jsonArrTrim("foo", new Path("array"), 1, 4);
        Response<String> toggle = p.jsonToggle("foo", new Path("boolean"));
        Response<Class<?>> type = p.jsonType("foo", new Path("boolean"));
        Response<Class<?>> keyType = p.jsonType("foo");
        Response<String> string3 = p.jsonSet("foo", Path.ROOT_PATH, "newStr");
        Response<Long> strLen = p.jsonStrLen("foo");
        Response<Long> strApp = p.jsonStrAppend("foo", "?");

        p.sync();

        assertEquals("OK", string1.get());
        assertEquals(hm1, object.get());
        assertEquals(Long.valueOf(5), strLenPath.get());
        assertEquals(Long.valueOf(6), strAppPath.get());
        assertEquals(Long.valueOf(1), delPah.get());
        assertEquals(Long.valueOf(1), delKey.get());
        assertEquals(0, keys.get().size());
        assertEquals("OK", string2.get());
        assertEquals("c", pop.get());
        assertEquals(Long.valueOf(4), append.get());
        assertEquals(Long.valueOf(2), index.get());
        assertEquals(Long.valueOf(5), insert.get());
        assertEquals(Long.valueOf(5), arrLen.get());
        assertEquals(Long.valueOf(4), trim.get());
        assertEquals("false", toggle.get());
        assertEquals(boolean.class, type.get());
        assertEquals(Object.class, keyType.get());
        assertEquals("OK", string3.get());
        assertEquals(Long.valueOf(6), strLen.get());
        assertEquals(Long.valueOf(7), strApp.get());
    }
}
