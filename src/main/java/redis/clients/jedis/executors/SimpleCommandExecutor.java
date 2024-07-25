package redis.clients.jedis.executors;

import java.util.function.Supplier;
import redis.clients.jedis.CommandObject;
import redis.clients.jedis.Connection;
import redis.clients.jedis.util.IOUtils;

public class SimpleCommandExecutor implements CommandExecutor {

  protected final Connection connection;

  public SimpleCommandExecutor(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void close() {
    IOUtils.closeQuietly(connection);
  }

  @Override
  public final <T> T executeCommand(CommandObject<T> commandObject) {
    return connection.executeCommand(commandObject);
  }

  @Override
  public <T> T executeCommand(CommandObject<T> commandObject, Supplier<Object[]> keys) {
    return this.executeCommand(commandObject);
  }
}
