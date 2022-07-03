package redis.clients.jedis.search;

import java.util.ArrayList;
import java.util.List;
import redis.clients.jedis.Builder;
import redis.clients.jedis.BuilderFactory;

/**
 * SearchResult encapsulates the returned result from a search query. It contains publicly
 * accessible fields for the total number of results, and an array of {@link Document} objects
 * containing the actual returned documents.
 */
public class SearchResult {

  private final long totalResults;
  private final List<Document> documents;

  private SearchResult(long totalResults, List<Document> documents) {
    this.totalResults = totalResults;
    this.documents = documents;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public List<Document> getDocuments() {
    return documents;
  }

  public static class SearchResultBuilder extends Builder<SearchResult> {

    private final boolean hasContent;
    private final boolean hasScores;
    private final boolean hasPayloads;
    private final boolean hasSortKeys;
    private final boolean hasExplainScore;
    private final boolean decode;

    public SearchResultBuilder(boolean hasContent, boolean hasScores, boolean hasPayloads, boolean hasSortKeys, boolean hasExplainScore, boolean decode) {
      this.hasContent = hasContent;
      this.hasScores = hasScores;
      this.hasPayloads = hasPayloads;
      this.hasSortKeys = hasSortKeys;
      this.hasExplainScore = hasExplainScore;
      this.decode = decode;
    }

    @Override
    public SearchResult build(Object data) {
      List<Object> resp = (List<Object>) data;

      int step = 1;
      int scoreOffset = 0;
      int contentOffset = 1;
      int payloadOffset = 0;
      if (hasScores) {
        step += 1;
        scoreOffset = 1;
        contentOffset += 1;
      }
      if (hasContent) {
        step += 1;
        if (hasPayloads) {
          payloadOffset = scoreOffset + 1;
          step += 1;
          contentOffset += 1;
        }
      }
      if (hasSortKeys) {
        step += 1;
        contentOffset += 1;
      }

      // the first element is always the number of results
      long totalResults = (Long) resp.get(0);
      List<Document> documents = new ArrayList<>(resp.size() - 1);

      for (int i = 1; i < resp.size(); i += step) {

        double score = hasScores ? (hasExplainScore ? BuilderFactory.DOUBLE.build(((List<Object>) resp.get(i + scoreOffset)).get(0)) : BuilderFactory.DOUBLE.build(resp.get(i + scoreOffset))) : 1.0;
        List<byte[]> explainScore = hasExplainScore ? (List<byte[]>) ((List<Object>) resp.get(i + scoreOffset)).get(1) : null;
        byte[] payload = hasPayloads ? (byte[]) resp.get(i + payloadOffset) : null;
        String sortKey = hasSortKeys ? new String((byte[]) resp.get(i + payloadOffset + 1)) : null;
        List<byte[]> fields = hasContent ? (List<byte[]>) resp.get(i + contentOffset) : null;
        String id = new String((byte[]) resp.get(i));

        documents.add(Document.load(id, score, payload, sortKey, explainScore, fields, decode));
      }

      return new SearchResult(totalResults, documents);
    }
  }
}
