package redis.clients.jedis.bloom;

public interface RedisBloomCommands extends BloomFilterCommands, CuckooFilterCommands,
    CountMinSketchCommands, TopKFilterCommands, TDIGESTCommands {

}
