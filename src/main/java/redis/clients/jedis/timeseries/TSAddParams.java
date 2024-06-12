package redis.clients.jedis.timeseries;

import static redis.clients.jedis.Protocol.toByteArray;
import static redis.clients.jedis.timeseries.TimeSeriesProtocol.TimeSeriesKeyword.*;

import java.util.LinkedHashMap;
import java.util.Map;
import redis.clients.jedis.CommandArguments;
import redis.clients.jedis.params.IParams;

/**
 * Represents optional arguments of TS.ADD command.
 */
public class TSAddParams implements IParams {

  private Long retentionPeriod;
  private EncodingFormat encoding;
  private Long chunkSize;
  private DuplicatePolicy duplicatePolicy;
  private DuplicatePolicy onDuplicate;
  private Map<String, String> labels;

  public TSAddParams() {
  }

  public static TSAddParams addParams() {
    return new TSAddParams();
  }

  public TSAddParams retention(long retentionPeriod) {
    this.retentionPeriod = retentionPeriod;
    return this;
  }

  public TSAddParams encoding(EncodingFormat encoding) {
    this.encoding = encoding;
    return this;
  }

  public TSAddParams chunkSize(long chunkSize) {
    this.chunkSize = chunkSize;
    return this;
  }

  public TSAddParams duplicatePolicy(DuplicatePolicy duplicatePolicy) {
    this.duplicatePolicy = duplicatePolicy;
    return this;
  }

  public TSAddParams onDuplicate(DuplicatePolicy onDuplicate) {
    this.onDuplicate = onDuplicate;
    return this;
  }

  /**
   * Set label-value pairs
   *
   * @param labels label-value pairs
   * @return the object itself
   */
  public TSAddParams labels(Map<String, String> labels) {
    this.labels = labels;
    return this;
  }

  /**
   * Add label-value pair. Multiple pairs can be added through chaining.
   */
  public TSAddParams label(String label, String value) {
    if (this.labels == null) {
      this.labels = new LinkedHashMap<>();
    }
    this.labels.put(label, value);
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {

    if (retentionPeriod != null) {
      args.add(RETENTION).add(toByteArray(retentionPeriod));
    }

    if (encoding != null) {
      args.add(ENCODING).add(encoding);
    }

    if (chunkSize != null) {
      args.add(CHUNK_SIZE).add(toByteArray(chunkSize));
    }

    if (duplicatePolicy != null) {
      args.add(DUPLICATE_POLICY).add(duplicatePolicy);
    }

    if (duplicatePolicy != null) {
      args.add(DUPLICATE_POLICY).add(duplicatePolicy);
    }

    if (onDuplicate != null) {
      args.add(ON_DUPLICATE).add(onDuplicate);
    }

    if (labels != null) {
      args.add(LABELS);
      labels.entrySet().forEach((entry) -> args.add(entry.getKey()).add(entry.getValue()));
    }
  }
}
