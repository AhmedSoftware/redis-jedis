package redis.clients.jedis;

import java.io.IOException;
import java.io.Serializable;
import redis.clients.jedis.util.SafeEncoder;

public class StreamEntryID implements Comparable<StreamEntryID>, Serializable {

  private static final long serialVersionUID = 1L;

  private long time;
  private long sequence;

  public StreamEntryID() {
    this(0, 0L);
  }

  public StreamEntryID(byte[] id) {
    this(SafeEncoder.encode(id));
  }

  public StreamEntryID(String id) {
    String[] split = id.split("-");
    this.time = Long.parseLong(split[0]);
    this.sequence = Long.parseLong(split[1]);
  }

  public StreamEntryID(long time) {
    this(time, 0);
  }

  public StreamEntryID(long time, long sequence) {
    this.time = time;
    this.sequence = sequence;
  }

  @Override
  public String toString() {
    return time + "-" + sequence;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    StreamEntryID other = (StreamEntryID) obj;
    return this.time == other.time && this.sequence == other.sequence;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  @Override
  public int compareTo(StreamEntryID other) {
    int timeCompare = Long.compare(this.time, other.time);
    return timeCompare != 0 ? timeCompare : Long.compare(this.sequence, other.sequence);
  }

  public long getTime() {
    return time;
  }

  public long getSequence() {
    return sequence;
  }

  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    out.writeLong(this.time);
    out.writeLong(this.sequence);
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    this.time = in.readLong();
    this.sequence = in.readLong();
  }

  /**
   * Should be used only with XADD
   *
   * {@code XADD mystream * field1 value1}
   */
  public static final StreamEntryID NEW_ENTRY = new StreamEntryID() {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      return "*";
    }
  };

  /**
   * Should be used only with XGROUP CREATE
   *
   * {@code XGROUP CREATE mystream consumer-group-name $}
   */
  public static final StreamEntryID XGROUP_LAST_ENTRY = new StreamEntryID() {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      return "$";
    }
  };

  /**
   * @deprecated Use {@link StreamEntryID#XGROUP_LAST_ENTRY} for XREADGROUP command or
   * {@link StreamEntryID#XREAD_NEW_ENTRY} for XREAD command.
   */
  @Deprecated
  public static final StreamEntryID LAST_ENTRY = XGROUP_LAST_ENTRY;

  /**
   * Should be used only with XREAD
   *
   * {@code XREAD BLOCK 5000 COUNT 100 STREAMS mystream $}
   */
  public static final StreamEntryID XREAD_NEW_ENTRY = new StreamEntryID() {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      return "$";
    }
  };

  /**
   * Should be used only with XREADGROUP
   * <p>
   * {@code XREADGROUP $GroupName $ConsumerName BLOCK 2000 COUNT 10 STREAMS mystream >}
   */
  public static final StreamEntryID UNRECEIVED_ENTRY = new StreamEntryID() {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      return ">";
    }
  };

  /**
   * Can be used in XRANGE, XREVRANGE and XPENDING commands.
   */
  // TODO: FIRST_ID ?
  public static final StreamEntryID MINIMUM_ID = new StreamEntryID() {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      return "-";
    }
  };

  /**
   * Can be used in XRANGE, XREVRANGE and XPENDING commands.
   */
  // TODO: LAST_ID ?
  // TODO: unify with XREAD_LAST_ENTRY ??
  public static final StreamEntryID MAXIMUM_ID = new StreamEntryID() {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      return "+";
    }
  };

  /**
   * Should be used only with XREAD
   *
   * {@code XREAD STREAMS mystream +}
   */
  // TODO: unify with MAXIMUM_ID ??
  public static final StreamEntryID XREAD_LAST_ENTRY = new StreamEntryID() {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      return "+";
    }
  };
}
