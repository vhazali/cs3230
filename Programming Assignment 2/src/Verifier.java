import java.util.Scanner;

/**
 * Given a graph G, and a list P of vertices. Program checks whether P forms an
 * undirected Hamiltonian cycle in G.
 * A Hamilton Cycle in G is defined as a cycle in G that contains every vertex
 * of
 * G exactly once.
 * 
 * @input
 *        First line of input is the number T of test cases. Next are T blocks
 *        of
 *        lines, each block describes a test case.
 *        In each block:
 *        First line is N (number of vertices), M (number of edges)
 *        Next M lines write 2 strings u,v describing an undirected edge (u,v),
 *        where u,v are names of the vertices.
 *        Next line writes an integer number p-number of vertices in the path P.
 *        Last line writes names of a p vertices, separated by a space.
 * @output
 *         For each test case in the input, output the corresponding answer on
 *         one line, i.e.:
 *         Output "YES" if P is a undirected Hamiltonian cycle, output "NO"
 *         otherwise.
 * @constraints
 *              Length of the vertex name is at most 6 characters.
 *              1<=T<=50; 2<=N <=300; 2<=M <=30000.
 *              Memory: 256M
 *              Running time: 2 seconds per T test cases.
 * @author Victor Hazali
 */
public class Verifier {

	public static final boolean	DEBUG_MODE	= false;

	public static void main(String[] args) {

		// Variables initialisaiton
		Scanner sc = new Scanner(System.in);
		int testcases = 0, numVertices = 0, numEdges = 0, pathVertices = 0;
		String[] edges;
		String[] path;
		Graph graph;
		boolean successful = false;

		// Reading number of testcases
		testcases = sc.nextInt();
		sc.nextLine();

		// loop for T testcases
		for (int i = 0; i < testcases; i++) {

			// Read number of vertices and edges
			numVertices = sc.nextInt();
			numEdges = sc.nextInt();
			sc.nextLine();

			// For it to be a Hamilton cycle, M >= N
			if (numEdges < numVertices) {
				if (DEBUG_MODE) {
					System.out.println("numEdges < numVertices.");
				}
				System.out.println("NO");
				skipInput(sc, numEdges + 2);
				continue;
			}

			edges = new String[numEdges];

			// Read M number of edges
			for (int j = 0; j < numEdges; j++) {
				edges[j] = sc.nextLine();
			}

			// Read p number of vertices in path P
			pathVertices = sc.nextInt();
			sc.nextLine();

			// For it to be a Hamilton cycle, p must be exactly N+1
			if (pathVertices != numVertices + 1) {
				if (DEBUG_MODE) {
					System.out.println("pathVertices != numVertices+1.");
				}
				System.out.println("NO");
				skipInput(sc, 1);
				continue;
			}
			// Read path P
			path = sc.nextLine().split("\\s+");

			// For it to be a Hamilton cycle, P must start and end at the same
			// vertex
			if (!path[0].equals(path[pathVertices - 1])) {
				if (DEBUG_MODE) {
					System.out
							.println("First and last vertex in path does not match.");
				}
				System.out.println("NO");
				continue;
			}

			// Initialize graph
			graph = new Graph(numVertices);

			// Add all edges into graph
			for (String e : edges) {
				String[] edge = e.split("\\s+");
				graph.addEdge(edge[0], edge[1]);
			}

			// Traverse through path to see if it's a Hamilton cycle
			// Start from the first vertex
			if (graph.traverse(path[0]) == false) {
				if (DEBUG_MODE) {
					System.out.println("Failed to traverse first vertex in P");
				}
				System.out.println("NO");
				continue;
			}

			// Loop all the internal vertices (except first and last one)
			for (int j = 1; j < pathVertices - 1; j++) {
				successful = graph.traverse(path[j - 1], path[j]);
				if (successful == false) {
					if (DEBUG_MODE) {
						System.out.println("Failed to traverse P due to "
								+ path[j]);
					}
					System.out.println("NO");
					break;
				}
			}

			// Check to see if successfully traversed all internal nodes
			if (successful == false) {
				if (DEBUG_MODE) {
					System.out
							.println("Did not complete traversing internal vertices");
				}
				continue;
			}

			// If successful, traverse final node
			if (graph
					.edgeExists(path[pathVertices - 2], path[pathVertices - 1])) {
				System.out.println("YES");
			} else {
				if (DEBUG_MODE) {
					System.out
							.println("No edge from last internal vertex to first vertex");
				}
				System.out.println("NO");
			}
		}
	}

	public static void skipInput(Scanner sc, int lines) {
		if (DEBUG_MODE) {
			System.out.println("Skipping " + lines + " line(s) of input");
		}
		for (int i = 0; i < lines; i++) {
			sc.nextLine();
		}
	}
}
