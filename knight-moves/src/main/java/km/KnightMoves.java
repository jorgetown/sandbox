package km;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Jorge De Castro
 */
public final class KnightMoves {
    private static final int DEFAULT_MAX_NUMBER_OF_VOWELS = 2;
    private final int depth;
    private final int numberOfVowels;
    private final Set<String> vowels;
    private final Map<String, long[][]> cache;
    private final Map<String, String[]> graph;

    public KnightMoves(final Map<String, String[]> graph, final int numberOfVowels, final int depth) {
        checkNotNull(graph, "Graph argument must not be null");
        checkArgument(numberOfVowels >= 0, "Number of vowels argument must not be negative");
        checkArgument(depth > 0 && depth <= 32, "Depth argument must be an integer between 1 and 32, inclusive");

        this.graph = graph;
        this.numberOfVowels = numberOfVowels;
        this.depth = depth;
        this.vowels = Sets.newHashSet("A", "E", "I", "O", "U");
        this.cache = new HashMap<>();

        for (String s : graph.keySet()) {
            cache.put(s, new long[numberOfVowels + 1][depth + 1]); // @TODO: Should guard against int overflow, if public API changes
        }
    }

    public long calculate() {
        return calculate(graph.keySet().toArray(new String[0]), numberOfVowels, depth);
    }

    private long calculate(final String[] vertices, int vowelsAllowed, int depth) {
        long sequences = 0;

        if (depth == 0) {
            return 1;
        } else {
            depth--;
            for (String vertex : vertices) {
                long count = cache.get(vertex)[vowelsAllowed][depth];
                if (count == 0) { // Cache miss = 0
                    int vowelsFound = vowels.contains(vertex) ? 1 : 0; // @TODO: Could factor out vowel constraints logic for improved testability
                    if (vowelsAllowed > 0 || vowelsFound == 0) {
                        count = calculate(graph.get(vertex), vowelsAllowed - vowelsFound, depth);
                    }
                    cache.get(vertex)[vowelsAllowed][depth] = count;
                }
                sequences += count;
            }
        }
        return sequences;
    }

    public Map<String, String[]> getGraph() {
        return Collections.unmodifiableMap(graph);
    }

    public int getNumberOfVowels() {
        return numberOfVowels;
    }

    public int getDepth() {
        return depth;
    }

    public static Map<String, String[]> createDefaultGraph() {
        Map<String, String[]> graph = new HashMap<>();
        graph.put("A", new String[]{ "H", "L" });
        graph.put("B", new String[]{ "K", "M", "I" });
        graph.put("C", new String[]{ "F", "L", "N", "J" });
        graph.put("D", new String[]{ "G", "M", "O" });
        graph.put("E", new String[]{ "H", "N" });
        graph.put("F", new String[]{ "1", "M", "C" });
        graph.put("G", new String[]{ "2", "N", "D" });
        graph.put("H", new String[]{ "A", "K", "1", "3", "E", "O" });
        graph.put("I", new String[]{ "2", "B", "L" });
        graph.put("J", new String[]{ "3", "C", "M" });
        graph.put("K", new String[]{ "B", "H", "2" });
        graph.put("L", new String[]{ "A", "C", "I", "3" });
        graph.put("M", new String[]{ "B", "D", "F", "J" });
        graph.put("N", new String[]{ "1", "G", "C", "E" });
        graph.put("O", new String[]{ "2", "H", "D" });
        graph.put("1", new String[]{ "F", "H", "N" });
        graph.put("2", new String[]{ "G", "I", "K", "O" });
        graph.put("3", new String[]{ "L", "H", "J" });
        return graph;
    }

    public static void main(String[] args) {
        try {
            int depth = Integer.parseInt(args[0]);
            Map<String, String[]> graph = createDefaultGraph();

            KnightMoves km = new KnightMoves(graph, DEFAULT_MAX_NUMBER_OF_VOWELS, depth);

            System.out.println(km.calculate());
        } catch (Exception ignored) {
            System.out.println("Please provide a command-line argument as an integer between 1 and 32, inclusive");
            System.exit(1);
        }
    }
}
