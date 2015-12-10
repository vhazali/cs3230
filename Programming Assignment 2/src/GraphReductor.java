import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This class will transform an instance of Dir_Ham_Cyc into an instance of
 * Undir_Ham_Cyc, such that an answer yes for the former implies an yes for the
 * latter, and vice versa.
 * The transformation will be using the following algorithm:
 * 
 * <pre>
 * Input: G = (Vg,Eg);
 * Build H = (V,E) -- an undirected graph, where:
 * 1. Each vertex v in Vg is transformed into 3 vertices in V: hv (head-vertex of v), v, and  tv  (tail-vertex of v);
 * 2. For each v in Vg: undirected edges (tv,v) and (v,hv) are in E;
 * 3. Each directed edge (u,v) in Eg is transformed into an undirected edge (hu, tv) in E.
 * Output: H = (V,E)
 * </pre>
 * 
 * @input
 *        First line of input is the number T of testcases. Next are T blocks of
 *        lines, each block describes a testcase (An instance of Dir_Ham_Cyc,
 *        that is, a directed graph Gd).
 *        In each block: First line is two integer numbers: N (number of
 *        vertices), and M (number of edges)
 *        Next M lines describe M edges: each line writes 2 strings u, v
 *        separated by a space, describing a directed edges (u,v), where u,v are
 *        names of the vertices.
 * @output
 *         For each testcase in the input, output a corresponding transformed
 *         graph (An instance of Undir_Ham_Cyc, that is, undirected graph Gu),
 *         with the following blocks of lines:
 *         First line: Nv (number of vertices), and then Me (number of edges)
 *         Next Me lines: Name of two vertices u and v for each edge (u,v) of
 *         the transformed graph, separated by a space.
 * @constraints
 *              If the name of the vertex in original graph is Vxxxx, then the
 *              head-vertex
 *              and tail-vertex of that vertex in transformation must have name
 *              Hxxxx and
 *              Txxxx.
 *              The edges will be output in lexicographic order of the vertice's
 *              name.
 *              The name of vertices in the input is always started with "V",
 *              with max length 6.
 *              1<=T<= 50; 2<=Nv<=300; 2<=Me<=30000
 *              Memory: 256M
 *              Running time: 2 seconds per T testcases.
 * @author Victor Hazali
 */
public class GraphReductor {

	public static final boolean	DEBUG_MODE	= false;

	public static void main(String[] args) {

		// Variables initialization / declaration
		Scanner sc = new Scanner(System.in);
		int testcases = 0, numVert = 0, numEdge = 0, vertexCnt = 0;
		String[] edges, names;
		Vertex[] vertices = new Vertex[7];
		Graph graph;

		// Reading the number of testcases
		testcases = sc.nextInt();
		sc.nextLine();

		// Loop for T testcases
		for (int i = 0; i < testcases; i++) {

			// Reading Nv and Me
			numVert = sc.nextInt();
			numEdge = sc.nextInt();
			sc.nextLine();

			// initializing array of edges
			edges = new String[numEdge];

			// Reading all edges
			for (int j = 0; j < numEdge; j++) {
				edges[j] = sc.nextLine();
			}

			// Creating the graph
			graph = new Graph(3 * numVert);

			// Loop through all edges in the input
			for (String e : edges) {

				// index to keep track of the 6 vertices in this edge
				vertexCnt = 0;
				names = e.split("\\s+");

				for (String n : names) {

					// Creating vertices
					vertices[vertexCnt] = new Vertex(n);
					vertices[vertexCnt + 1] = vertices[vertexCnt]
							.getHeadVertex();
					vertices[vertexCnt + 2] = vertices[vertexCnt]
							.getTailVertex();

					// Adding vertices into the graph
					graph.insertVertex(vertices[vertexCnt]);
					graph.insertVertex(vertices[vertexCnt + 1]);
					graph.insertVertex(vertices[vertexCnt + 2]);

					// Adding edge between header-vertex and vertex
					graph.addEdge(n, vertices[vertexCnt + 1].getName());

					// Adding edge between tail-vertex and vertex
					graph.addEdge(n, vertices[vertexCnt + 2].getName());

					vertexCnt += 3;
				}

				// Adding edge e into the graph
				graph.addEdge(vertices[vertexCnt - 5].getName(),
						vertices[vertexCnt - 1].getName());
			}

			// Printing out Nv and Me
			System.out.println(numVert * 3 + " " + (numVert * 2 + numEdge));

			// Printing out the edges

			// First we get all the vertices in the graph
			List<Vertex> graphVertices = graph.getVertices();
			// Sorting the vertices in the graph lexicographically
			Collections.sort(graphVertices);

			String v1, v2;
			// Printing out all the edges
			for (int j = 0; j < graphVertices.size() - 1; j++) {
				v1 = graphVertices.get(j).getName();
				for (int k = j + 1; k < graphVertices.size(); k++) {
					v2 = graphVertices.get(k).getName();
					if (graph.edgeExists(v1, v2)) {
						System.out.println(v1 + " " + v2);
					}
				}
			}
		}
	}

}
