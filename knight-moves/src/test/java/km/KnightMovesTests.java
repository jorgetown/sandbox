package km;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

/**
 * @author Jorge De Castro
 */
public final class KnightMovesTests {
    private static final int DEFAULT_NUMBER_OF_VOWELS = 2;
    private static final int DEFAULT_DEPTH = 2;
    private static final Map<String, String[]> EMPTY_GRAPH = Collections.emptyMap();

    private Map<String, String[]> graph;

    private KnightMoves underTest;

    @Before
    public void setup() {
        graph = KnightMoves.createDefaultGraph();
    }

    @Test(expected = NullPointerException.class)
    public void nullGraphArgument() {
        underTest = new KnightMoves(null, 0, 0);
    }

    @Test
    public void notNullGraphArgument() {
        underTest = new KnightMoves(EMPTY_GRAPH, DEFAULT_NUMBER_OF_VOWELS, DEFAULT_DEPTH);

        Assert.assertNotNull(underTest.getGraph());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidNumberOfVowelsArgument() {
        int numberOfVowels = -1;
        underTest = new KnightMoves(EMPTY_GRAPH, numberOfVowels, DEFAULT_DEPTH);
    }

    @Test
    public void validNumberOfVowelsArgument() {
        underTest = new KnightMoves(EMPTY_GRAPH, DEFAULT_NUMBER_OF_VOWELS, DEFAULT_DEPTH);

        Assert.assertEquals(DEFAULT_NUMBER_OF_VOWELS, underTest.getNumberOfVowels());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidDepthArgument() {
        int depth = -1;
        underTest = new KnightMoves(EMPTY_GRAPH, DEFAULT_NUMBER_OF_VOWELS, depth);
    }

    @Test
    public void validDepthArgument() {
        underTest = new KnightMoves(EMPTY_GRAPH, DEFAULT_NUMBER_OF_VOWELS, DEFAULT_DEPTH);

        Assert.assertEquals(DEFAULT_DEPTH, underTest.getDepth());
    }

    @Test
    public void numberOfSequencesAtDepthOneIsNumberOfVertices() {
        underTest = new KnightMoves(graph, DEFAULT_NUMBER_OF_VOWELS, 1);

        Assert.assertEquals(graph.size(), underTest.calculate());
    }

    @Test
    public void numberOfSequencesAtDepthTwoIsSumOfCountOfAllNeighbours() {
        long sequences = 0;
        for (String[] adjacents : graph.values()) {
            sequences += adjacents.length;
        }

        underTest = new KnightMoves(graph, DEFAULT_NUMBER_OF_VOWELS, 2);

        Assert.assertEquals(underTest.calculate(), sequences);
    }

    @Test
    public void numberOfSequencesAtDepthTenIsAsPerSpec() {
        underTest = new KnightMoves(graph, DEFAULT_NUMBER_OF_VOWELS, 10);

        String seqAsString = Long.toString(underTest.calculate());

        Assert.assertTrue(seqAsString.contains("1"));
        Assert.assertTrue(seqAsString.contains("3"));
        Assert.assertTrue(seqAsString.contains("8"));
        Assert.assertTrue(seqAsString.contains("9"));
    }

    @Test
    public void prettyPrint() {
        for (int i = 1; i <= 32; i++) {
            underTest = new KnightMoves(graph, DEFAULT_NUMBER_OF_VOWELS, i);
            System.out.println(i + " | " + underTest.calculate());
        }
    }
}
