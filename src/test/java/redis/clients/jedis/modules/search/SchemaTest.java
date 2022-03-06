package redis.clients.jedis.modules.search;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import redis.clients.jedis.search.Schema;

import java.util.HashMap;

public class SchemaTest {

  private final static String TITLE = "title";
  private final static String GENRE = "genre";
  private final static String VOTES = "votes";
  private final static String RATING = "rating";
  private final static String RELEASE_YEAR = "release_year";
  private final static String PLOT = "plot";
  private final static String VECTOR = "vector";

  @Test
  public void printSchemaTest() throws Exception {
    Schema sc = new Schema()
        .addTextField(TITLE, 5.0)
        .addSortableTextField(PLOT, 1.0)
        .addSortableTagField(GENRE, ",")
        .addSortableNumericField(RELEASE_YEAR)
        .addSortableNumericField(RATING)
        .addSortableNumericField(VOTES)
        .addVectorField(VECTOR, Schema.VectorField.VectorAlgo.HNSW, new HashMap<>());

    String schemaPrint = sc.toString();
    MatcherAssert.assertThat(schemaPrint, CoreMatchers.startsWith("Schema{fields=[TextField{name='title'"));
    MatcherAssert.assertThat(schemaPrint, CoreMatchers.containsString("{name='release_year', type=NUMERIC, sortable=true, noindex=false}"));
    MatcherAssert.assertThat(schemaPrint, CoreMatchers.containsString("VectorField{name='vector', type=VECTOR, algorithm=HNSW"));
  }
}
