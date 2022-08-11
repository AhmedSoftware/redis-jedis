package redis.clients.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import redis.clients.jedis.Response;

public interface TDigestSketchPipelineCommands {

  Response<String> tdigestCreate(String key);

  Response<String> tdigestCreate(String key, int compression);

  Response<String> tdigestReset(String key);

  Response<String> tdigestMerge(String destinationKey, String sourceKey);

  Response<String> tdigestMergeStore(String destinationKey, String... sourceKeys);

  Response<String> tdigestMergeStore(int compression, String destinationKey, String... sourceKeys);

  Response<Map<String, Object>> tdigestInfo(String key);

  Response<String> tdigestAdd(String key, Map.Entry<Double, Double>... valueWeights);

  Response<Double> tdigestCDF(String key, double value);

  Response<List<Double>> tdigestQuantile(String key, double... quantiles);

  Response<Double> tdigestMin(String key);

  Response<Double> tdigestMax(String key);

  Response<Double> tdigestTrimmedMean(String key, double lowCutQuantile, double highCutQuantile);
}
