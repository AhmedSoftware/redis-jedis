package redis.clients.jedis;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import redis.clients.jedis.util.SafeEncoder;

/**
 * The class to manage the client-side caching. User can provide any of implementation of this class to the client
 * object; e.g. {@link redis.clients.jedis.util.CaffeineCSC CaffeineCSC} or
 * {@link redis.clients.jedis.util.GuavaCSC GuavaCSC} or a custom implementation of their own.
 */
public abstract class ClientSideCache {

  private final Map<ByteBuffer, Set<Long>> keyToCommandHashes;

  protected ClientSideCache() {
    this.keyToCommandHashes = new ConcurrentHashMap<>();
  }

  public abstract void invalidateAll();

  protected abstract void invalidateAll(Iterable<Long> hashes);

  final void invalidate(List list) {
    if (list == null) {
      invalidateAll();
      return;
    }

    list.forEach(this::invalidateKeyAndRespectiveCommandHashes);
  }

  private void invalidateKeyAndRespectiveCommandHashes(Object key) {
    if (!(key instanceof byte[])) {
      throw new AssertionError("" + key.getClass().getSimpleName() + " is not supported. Value: " + String.valueOf(key));
    }

    final ByteBuffer mapKey = makeKeyForKeyToCommandHashes((byte[]) key);

    Set<Long> hashes = keyToCommandHashes.get(mapKey);
    if (hashes != null) {
      invalidateAll(hashes);
      keyToCommandHashes.remove(mapKey);
    }
  }

  protected abstract void put(long hash, Object value);

  protected abstract Object get(long hash);

  final <T> T getValue(Function<CommandObject<T>, T> loader, CommandObject<T> command, String... keys) {

    final long hash = getHash(command);

    T value = (T) get(hash);
    if (value != null) {
      return value;
    }

    value = loader.apply(command);
    if (value != null) {
      put(hash, value);
      for (String key : keys) {
        ByteBuffer mapKey = makeKeyForKeyToCommandHashes(key);
        if (keyToCommandHashes.containsKey(mapKey)) {
          keyToCommandHashes.get(mapKey).add(hash);
        } else {
          Set<Long> set = new HashSet<>();
          set.add(hash);
          keyToCommandHashes.put(mapKey, set);
        }
      }
    }

    return value;
  }

  protected abstract long getHash(CommandObject command);

  private ByteBuffer makeKeyForKeyToCommandHashes(String key) {
    return makeKeyForKeyToCommandHashes(SafeEncoder.encode(key));
  }

  private static ByteBuffer makeKeyForKeyToCommandHashes(byte[] b) {
    return ByteBuffer.wrap(b);
  }
}
