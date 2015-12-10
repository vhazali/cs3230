import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class implements the polynomial time reduction from a 3-CNF-SAT problem
 * to a DIR-HAM-CYCLE problem.
 * 
 * <pre>
 * Definitions:
 * + Literal in a Boolean formula is a variable or its negation
 * + Positive literal is a variable
 * + Negative literal is a negation of a variable
 * + CNF (Conjunctive Normal Form) is a Boolean formula expressed as AND of
 * clauses, where each clause is the OR of one or more literals
 * + 3CNF is a CNF in which each clause has exactly 3 distinct literals (a
 * literal and its negation are distinct).
 * 
 * Transformation Algorithm from 3CNF-SAT to Dir_Ham_Cyc
 * 
 * Vertex creation:
 * 1. Create 2 vertices S and T (represent Source S and sink T)
 * 2. For each literal Xn, n = 1..N: Create 3K+3 (literal) vertices:
 * + 2 boundary vertices in the left and the right Ln, Rn
 * + A (first, 0-th) buffer vertex Bn.0
 * + 3K internal vertices of form: Xn.k.L, Xn.k.R and Bn.k for each k = 1..K
 * 3. For each clause Ck: create a clausal vertex Ck to represent the clause.
 * 
 * Edge creation:
 * 1. Create edges between N(3K+3) literal vertices
 * +For each literal index n = 1..N:
 * Create horizontal edges: 2-way directed edges
 * +Create 4 vertical edges for n = 1..N-1:
 * 2. Create edges with S and T
 * + Connect S to L1, R1: S->L1, S->R1
 * + Connect LN, RN to T: LN->T, RN->T
 * + Connect T to S: T->S
 * 3. Create edges with the K clausal vertices
 * + For each clausal vertex Ck with index k: (Ck is of form Ck = Xn v Xu v Xv)
 * (total 6 directed edges)
 * + For literal Xn: (2 directed edges)
 * if Xn is positive literal: Connect Xn.k.L -> Ck and connect Ck -> Xn.k.R
 * if Xn is negative literal: Connect Ck -> Xn.k.L and connect Xn.k.R -> Ck
 * + For literal Xu and Xv: the same as above (4 directed edges)
 * 
 * Setting of vertex name:
 * - A name must be a string
 * - Source and sink vertices are always named "S" and "T"
 * - Boundary vertices: names always start with "L" or "R", and then the index
 * name, which is the same as index of the literal.
 * - Buffer vertices: names always start with "B", then first index, which is
 * the index of the literal, second index, which is either 0 or index of clause.
 * Indices saparated by a dot "."
 * - Internal literal vertices: Names always start with "X", then first index,
 * which is the index of the literal, second index, which is the index of
 * clause, and last index, which is either "L" or "R". Indices are separated by
 * a dot "."
 * - Clausal vertices: names always start with "C", index is the same as index
 * of the clause.
 * 
 * Convention:
 * L: left, R: right, B: buffer, X: literal/variable, C: clause, n: literal
 * index, k: clause index
 * True/positive: direction from Left to Right; False/negative: direction from
 * Right to Left
 * </pre>
 * 
 * @Input
 *        First line of input is the number T of testcases. Next are T blocks of
 *        lines, each block describes a testcase.
 *        In each block: First line is N, number of variables, and K, number of
 *        clauses. Variables are indexed from 1 to N, clauses -- from 1 to K.
 *        Next K lines describe K clauses in order from 1 to K, one clause per
 *        line. A clause contains 3 literals. A positive number represent index
 *        of variable (positive literal). A negative number represent an index
 *        of negated variable (negative literal).
 * @output For each test-case, output a directed graph with following format:
 *         First line: V (number of vertices) and E (number of edges)
 *         Next E lines: each line output a directed edge of the graph.
 *         You have to output the edge list in sorted lexicographic order.
 *         (Hint: use standard ordered set/map to store the edges, and then just
 *         print them in order in the set/map).
 * @constraints 1<=T<=20
 *              3<=N<=80
 *              2<=K<=99
 *              All the clauses are distinct. Each clause contains no repeated
 *              variable. Variables in each clauses are listed in increasing
 *              order of indices. Each variable appears at least once.
 * @author Victor Hazali
 */
public class Transformation {

	/* Constants */
	public static final boolean			DEBUG_MODE		= false;
	private static final InputStream	INPUT_STREAM	= System.in;
	private static final OutputStream	OUTPUT_STREAM	= System.out;

	/* Member Variables */
	private static Scanner				sc;
	private static PrintWriter			writer;
	private int							_litCount;
	private int							_clausCount;
	private Map<String, Vertex>			_vertexMap;
	private SortedSet<DirectedEdge>		_edges;

	/* Constructor */
	public Transformation() {
		sc = new Scanner(INPUT_STREAM);
		writer = new PrintWriter(OUTPUT_STREAM, true);
	}

	/* Accessors and Modifiers */

	public int getLitCount() {
		return _litCount;
	}

	public void setLitCount(int litCount) {
		_litCount = litCount;
	}

	public int getClausCount() {
		return _clausCount;
	}

	public void setClausCount(int clausCount) {
		_clausCount = clausCount;
	}

	public Map<String, Vertex> getVertexMap() {
		return _vertexMap;
	}

	public void setVertexMap(Map<String, Vertex> vertexList) {
		_vertexMap = vertexList;
	}

	public void addVertex(Vertex toAdd) {
		if (!_vertexMap.containsKey(toAdd.getName())) {
			_vertexMap.put(toAdd.getName(), toAdd);
		}
	}

	public Vertex getVertex(String name) {
		return _vertexMap.get(name);
	}

	public SortedSet<DirectedEdge> getEdges() {
		return _edges;
	}

	public void setEdges(SortedSet<DirectedEdge> edgesList) {
		_edges = edgesList;
	}

	public void addEdge(DirectedEdge edge) {
		if (!_edges.contains(edge)) {
			_edges.add(edge);
		}
	}

	public void addEdge(Vertex from, Vertex to) {
		DirectedEdge toAdd = new DirectedEdge(from, to);
		addEdge(toAdd);
	}

	/* Private Methods */

	/**
	 * Closes all open resources prior to termination of program
	 */

	/**
	 * Initializes variables for the upcoming test case.
	 */
	private void initialise() {
		setVertexMap(new HashMap<String, Vertex>());
		setEdges(new TreeSet<DirectedEdge>());
	}

	/**
	 * Create the graph.
	 */
	private void createGraph() {
		createVertices();
		createEdges();
	}

	private void createVertices() {
		createSource();
		createSink();
		createLiteral();
		createClausal();
	}

	/**
	 * Creates the source vertex and adds it into the map of vertices.
	 */
	private void createSource() {
		addVertex(new Vertex(Vertex.VertexType.SOURCE, Vertex.INVALID_INDEX,
				Vertex.INVALID_INDEX));
	}

	/**
	 * Creates the sink vertex and adds it into the map of vertices.
	 */
	private void createSink() {
		Vertex sink = new Vertex(Vertex.VertexType.SINK, Vertex.INVALID_INDEX,
				Vertex.INVALID_INDEX);
		addVertex(sink);
	}

	/**
	 * For each literal, create 3k+3 vertices. In total creates n*(3k+3)
	 * vertices and adds them into the map of vertices.
	 */
	private void createLiteral() {
		for (int litIndex = 1; litIndex <= getLitCount(); litIndex++) {
			createBoundary(litIndex);
			createBuffer(litIndex, Vertex.FIRST_BUFFER_INDEX);
			for (int clausIndex = 1; clausIndex <= getClausCount(); clausIndex++) {
				createLeftInt(litIndex, clausIndex);
				createRightInt(litIndex, clausIndex);
				createBuffer(litIndex, clausIndex);
			}
		}
	}

	/**
	 * Creates the left and right boundary vertices with the given literal index
	 * and adds them into the map of vertices.
	 * 
	 * @param litIndex
	 *            literal index. Must be in the range [1,n]
	 */
	private void createBoundary(int litIndex) {
		addVertex(new Vertex(Vertex.VertexType.BOUND_LEFT, litIndex,
				Vertex.INVALID_INDEX));
		addVertex(new Vertex(Vertex.VertexType.BOUND_RIGHT, litIndex,
				Vertex.INVALID_INDEX));
	}

	/**
	 * Creates a buffer vertex with the given literal index and clausal index
	 * and adds it into the map of vertices
	 * 
	 * @param litIndex
	 *            literal index. Must be in the range [1,n]
	 * @param clausIndex
	 *            clausal index. Must be in the range [0,k]
	 */
	private void createBuffer(int litIndex, int clausIndex) {
		addVertex(new Vertex(Vertex.VertexType.BUFFER, litIndex, clausIndex));
	}

	/**
	 * Creates a left internal vertex with the given literal index and clausal
	 * index and adds it into the map of vertices.
	 * 
	 * @param litIndex
	 *            literal index. Must be in the range [1,n]
	 * @param clausIndex
	 *            clausal index. Must be in the range [1,k]
	 */
	private void createLeftInt(int litIndex, int clausIndex) {
		addVertex(new Vertex(Vertex.VertexType.INT_LEFT, litIndex, clausIndex));
	}

	/**
	 * Creates a right internal vertex with the given literal index and clausal
	 * index and adds it into the map of vertices.
	 * 
	 * @param litIndex
	 *            literal index. Must be in the range [1,n]
	 * @param clausIndex
	 *            clausal index. Must be in the range [1,k]
	 */
	private void createRightInt(int litIndex, int clausIndex) {
		addVertex(new Vertex(Vertex.VertexType.INT_RIGHT, litIndex, clausIndex));
	}

	/**
	 * Creates a Clausal Vertex for all k clauses and adds them into the map of
	 * vertices
	 */
	private void createClausal() {
		for (int clausIndex = 1; clausIndex <= getClausCount(); clausIndex++) {
			addVertex(new Vertex(Vertex.VertexType.CLAUSAL,
					Vertex.INVALID_INDEX, clausIndex));
		}
	}

	private void createEdges() {
		createLiteralEdges();
		createSourceSinkEdges();
		createClausalEdges();
	}

	private void createLiteralEdges() {
		createHorizontalEdges();
		createVerticalEdges();
	}

	/**
	 * Creates all the horizontal edges within a row.
	 */
	private void createHorizontalEdges() {
		for (int literalIndex = 1; literalIndex <= getLitCount(); literalIndex++) {
			for (int clausIndex = 1; clausIndex <= getClausCount(); clausIndex++) {
				createLeftIntAndBuffer(literalIndex, clausIndex);
				createLeftIntAndRightInt(literalIndex, clausIndex);
				createRightIntAndBuffer(literalIndex, clausIndex);
			}
			createLeftAndBuffer(literalIndex, Vertex.FIRST_BUFFER_INDEX);
			createRightAndBuffer(literalIndex, getClausCount());
		}
	}

	/**
	 * Creates two edges between the left internal vertex and the buffer to its
	 * left. Rule: literal index is the same, internal vertex will have clause
	 * index of clausIndex, buffer will have clause index of clausIndex-1.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index to indicate which internal vertex we are
	 *            working on
	 */
	private void createLeftIntAndBuffer(int literalIndex, int clausIndex) {
		String lIntName = getName(Vertex.VertexType.INT_LEFT, literalIndex,
				clausIndex);
		String buffName = getName(Vertex.VertexType.BUFFER, literalIndex,
				clausIndex - 1);

		Vertex leftInt = getVertex(lIntName);
		Vertex buff = getVertex(buffName);

		if (leftInt == null || buff == null) {
			throw new Error("Vertices do not exist");
		}

		addEdge(leftInt, buff);
		addEdge(buff, leftInt);

	}

	/**
	 * Creates two edges between the left and right internal vertices. Rule:
	 * literal and clause indices are the same.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index to indicate which clause we are working on
	 */
	private void createLeftIntAndRightInt(int literalIndex, int clausIndex) {
		String rIntName = getName(Vertex.VertexType.INT_RIGHT, literalIndex,
				clausIndex);
		String lIntName = getName(Vertex.VertexType.INT_LEFT, literalIndex,
				clausIndex);

		Vertex rightInt = getVertex(rIntName);
		Vertex leftInt = getVertex(lIntName);

		if (rightInt == null || leftInt == null) {
			throw new Error("Vertices do not exist");
		}

		addEdge(rightInt, leftInt);
		addEdge(leftInt, rightInt);

	}

	/**
	 * Creates two edges between the right internal vertex with the buffer
	 * vertex to its right. Rule: literal and clause indices are the same.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index to indicate which internal vertex we are
	 *            working on
	 */
	private void createRightIntAndBuffer(int literalIndex, int clausIndex) {
		String rIntName = getName(Vertex.VertexType.INT_RIGHT, literalIndex,
				clausIndex);
		String buffName = getName(Vertex.VertexType.BUFFER, literalIndex,
				clausIndex);

		Vertex rightInt = getVertex(rIntName);
		Vertex buff = getVertex(buffName);

		if (rightInt == null || buff == null) {
			throw new Error("Vertices do not exist");
		}

		addEdge(rightInt, buff);
		addEdge(buff, rightInt);

	}

	/**
	 * Creates two edges between the left boundary vertex and the first vertex
	 * in the row. Rule: literal index are the same, buffer will have clause
	 * index of the FIRST_BUFFER_INDEX as specified in Vertex class
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index to indicate which buffer to link to.
	 */
	private void createLeftAndBuffer(int literalIndex, int clausIndex) {
		String leftName = getName(Vertex.VertexType.BOUND_LEFT, literalIndex,
				Vertex.INVALID_INDEX);
		String buffName = getName(Vertex.VertexType.BUFFER, literalIndex,
				clausIndex);

		Vertex left = getVertex(leftName);
		Vertex buff = getVertex(buffName);

		if (left == null || buff == null) {
			throw new Error("Vertices do not exist");
		}

		addEdge(left, buff);
		addEdge(buff, left);

	}

	/**
	 * Creates two edges between the right boundary vertex and the last vertex
	 * in the row. Rule: literal index are the same, buffer will have clause
	 * index at K
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index to indicate which buffer to link to
	 */
	private void createRightAndBuffer(int literalIndex, int clausIndex) {
		String rightName = getName(Vertex.VertexType.BOUND_RIGHT, literalIndex,
				Vertex.INVALID_INDEX);
		String buffName = getName(Vertex.VertexType.BUFFER, literalIndex,
				clausIndex);

		Vertex right = getVertex(rightName);
		Vertex buff = getVertex(buffName);

		if (right == null || buff == null) {
			throw new Error("Vertices do not exist");
		}

		addEdge(right, buff);
		addEdge(buff, right);
	}

	/**
	 * Creates all the vertical edges across rows. Does not include edges to and
	 * from Source and Sink.
	 */
	private void createVerticalEdges() {
		for (int literalIndex = 1; literalIndex < getLitCount(); literalIndex++) {
			createLeftToLeft(literalIndex);
			createLeftToRight(literalIndex);
			createRightToRight(literalIndex);
			createRightToLeft(literalIndex);
		}
	}

	/**
	 * Creates an edge from the left boundary vertex to the left boundary
	 * vertex of the next row.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 */
	private void createLeftToLeft(int literalIndex) {
		String fromName = getName(Vertex.VertexType.BOUND_LEFT, literalIndex,
				Vertex.INVALID_INDEX);
		String toName = getName(Vertex.VertexType.BOUND_LEFT,
				literalIndex + 1, Vertex.INVALID_INDEX);

		Vertex from = getVertex(fromName);
		Vertex to = getVertex(toName);
		if (from == null || to == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, to);

	}

	/**
	 * Creates an edge from the left boundary vertex to the right boundary
	 * vertex of the next row.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 */
	private void createLeftToRight(int literalIndex) {
		String fromName = getName(Vertex.VertexType.BOUND_LEFT, literalIndex,
				Vertex.INVALID_INDEX);
		String toName = getName(Vertex.VertexType.BOUND_RIGHT,
				literalIndex + 1, Vertex.INVALID_INDEX);

		Vertex from = getVertex(fromName);
		Vertex to = getVertex(toName);
		if (from == null || to == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, to);

	}

	/**
	 * Creates an edge from the right boundary vertex to the right boundary
	 * vertex of the next row.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 */
	private void createRightToRight(int literalIndex) {
		String fromName = getName(Vertex.VertexType.BOUND_RIGHT, literalIndex,
				Vertex.INVALID_INDEX);
		String toName = getName(Vertex.VertexType.BOUND_RIGHT,
				literalIndex + 1, Vertex.INVALID_INDEX);

		Vertex from = getVertex(fromName);
		Vertex to = getVertex(toName);
		if (from == null || to == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, to);

	}

	/**
	 * Creates an edge from the right boundary vertex to the left boundary
	 * vertex of the next row.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 */
	private void createRightToLeft(int literalIndex) {
		String fromName = getName(Vertex.VertexType.BOUND_RIGHT, literalIndex,
				Vertex.INVALID_INDEX);
		String toName = getName(Vertex.VertexType.BOUND_LEFT, literalIndex + 1,
				Vertex.INVALID_INDEX);

		Vertex from = getVertex(fromName);
		Vertex to = getVertex(toName);
		if (from == null || to == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, to);
	}

	/**
	 * Creates edges from and to the source and sink vertices
	 */
	private void createSourceSinkEdges() {
		createSourceEdges();
		createSinkEdges();
	}

	/**
	 * Creates 2 outgoing edges from the source to the left and right boundary
	 * vertices of the first row
	 */
	private void createSourceEdges() {
		String fromName = getName(Vertex.VertexType.SOURCE,
				Vertex.INVALID_INDEX, Vertex.INVALID_INDEX);
		String leftName = getName(Vertex.VertexType.BOUND_LEFT, 1,
				Vertex.INVALID_INDEX);
		String rightName = getName(Vertex.VertexType.BOUND_RIGHT, 1,
				Vertex.INVALID_INDEX);
		Vertex from = getVertex(fromName);
		Vertex left = getVertex(leftName);
		Vertex right = getVertex(rightName);
		if (from == null || left == null || right == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, left);
		addEdge(from, right);

	}

	/**
	 * Creates 2 incoming edges from the left and right boundary vertices of the
	 * last row and 1 outgoing edge from the sink to source vertex.
	 */
	private void createSinkEdges() {
		String sinkName = getName(Vertex.VertexType.SINK, Vertex.INVALID_INDEX,
				Vertex.INVALID_INDEX);
		String sourceName = getName(Vertex.VertexType.SOURCE,
				Vertex.INVALID_INDEX, Vertex.INVALID_INDEX);
		String leftName = getName(Vertex.VertexType.BOUND_LEFT,
				getLitCount(), Vertex.INVALID_INDEX);
		String rightName = getName(Vertex.VertexType.BOUND_RIGHT,
				getLitCount(), Vertex.INVALID_INDEX);
		Vertex sink = getVertex(sinkName);
		Vertex source = getVertex(sourceName);
		Vertex left = getVertex(leftName);
		Vertex right = getVertex(rightName);
		if (sink == null || source == null || left == null || right == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(left, sink);
		addEdge(right, sink);
		addEdge(sink, source);

	}

	/**
	 * Creates 6 edges for each clause
	 */
	private void createClausalEdges() {
		for (int clausIndex = 1; clausIndex <= getClausCount(); clausIndex++) {
			for (int i = 0; i < 3; i++) {
				int literal = sc.nextInt();
				if (literal > 0) {
					createLeftIntToClause(literal, clausIndex);
					createClauseToRightInt(literal, clausIndex);
				} else if (literal < 0) {
					literal = literal * -1;
					createRightIntToClause(literal, clausIndex);
					createClauseToLeftInt(literal, clausIndex);
				}
			}
		}
	}

	/**
	 * Creates an edge from left internal vertex to clause vertex.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index of the clause vertex to point to
	 */
	private void createLeftIntToClause(int literalIndex, int clausIndex) {
		String fromName = getName(Vertex.VertexType.INT_LEFT, literalIndex,
				clausIndex);
		String toName = getName(Vertex.VertexType.CLAUSAL,
				Vertex.INVALID_INDEX, clausIndex);
		Vertex from = getVertex(fromName);
		Vertex to = getVertex(toName);
		if (from == null || to == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, to);

	}

	/**
	 * Creates an edge from clause vertex to right internal vertex.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index of the clause vertex to point from
	 */
	private void createClauseToRightInt(int literalIndex, int clausIndex) {
		String fromName = getName(Vertex.VertexType.CLAUSAL,
				Vertex.INVALID_INDEX, clausIndex);
		String toName = getName(Vertex.VertexType.INT_RIGHT, literalIndex,
				clausIndex);
		Vertex from = getVertex(fromName);
		Vertex to = getVertex(toName);
		if (from == null || to == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, to);

	}

	/**
	 * Creates an edge from right internal vertex to clause vertex.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index of the clause vertex to point to
	 */
	private void createRightIntToClause(int literalIndex, int clausIndex) {
		String fromName = getName(Vertex.VertexType.INT_RIGHT, literalIndex,
				clausIndex);
		String toName = getName(Vertex.VertexType.CLAUSAL,
				Vertex.INVALID_INDEX, clausIndex);
		Vertex from = getVertex(fromName);
		Vertex to = getVertex(toName);
		if (from == null || to == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, to);
	}

	/**
	 * Creates an edge from clause vertex to left internal vertex.
	 * 
	 * @param literalIndex
	 *            the literal index to indicate which row we are working on
	 * @param clausIndex
	 *            the clause index of the clause vertex to point from
	 */
	private void createClauseToLeftInt(int literalIndex, int clausIndex) {
		String fromName = getName(Vertex.VertexType.CLAUSAL,
				Vertex.INVALID_INDEX, clausIndex);
		String toName = getName(Vertex.VertexType.INT_LEFT, literalIndex,
				clausIndex);
		Vertex from = getVertex(fromName);
		Vertex to = getVertex(toName);
		if (from == null || to == null) {
			throw new Error("Vertices do not exist");
		}
		addEdge(from, to);
	}

	private String getName(Vertex.VertexType type, int litIndex, int clausIndex) {
		StringBuilder name = new StringBuilder();

		switch (type) {
			case BOUND_LEFT :
				name.append(Vertex.LEFT_BOUND_PREFIX);
				name.append(litIndex);
				break;
			case BOUND_RIGHT :
				name.append(Vertex.RIGHT_BOUND_PREFIX);
				name.append(litIndex);
				break;
			case BUFFER :
				name.append(Vertex.BUFFER_PREFIX);
				name.append(litIndex + ".");
				name.append(clausIndex);
				break;
			case CLAUSAL :
				name.append(Vertex.CLAUSAL_PREFIX);
				name.append(clausIndex);
				break;
			case INT_LEFT :
				name.append(Vertex.INTERNAL_PREFIX);
				name.append(litIndex + ".");
				name.append(clausIndex + ".");
				name.append(Vertex.INT_LEFT_POSTFIX);
				break;
			case INT_RIGHT :
				name.append(Vertex.INTERNAL_PREFIX);
				name.append(litIndex + ".");
				name.append(clausIndex + ".");
				name.append(Vertex.INT_RIGHT_POSTFIX);
				break;
			case SINK :
				name.append(Vertex.SINK_NAME);
				break;
			case SOURCE :
				name.append(Vertex.SOURCE_NAME);
				break;
			case INVALID :
				name.append("Invalid");
				break;
			default:
				throw new IllegalArgumentException("Unrecognized type");
		}

		return name.toString();
	}

	private void displayGraph() {
		displayVertexCnt();
		displayEdgeCnt();
		displayEdges();
	}

	private void displayVertexCnt() {
		writer.print(getVertexMap().size() + " ");
	}

	private void displayEdgeCnt() {
		writer.println(getEdges().size());
	}

	private void displayEdges() {
		for (DirectedEdge e : getEdges()) {
			writer.println(e.toString());
		}
	}

	/**
	 * Method to close all resources used
	 */
	private void cleanup() {
		sc.close();
		writer.close();
	}

	/* Public Methods */
	public void run() {
		int testCases = sc.nextInt();
		for (int i = 0; i < testCases; i++) {
			setLitCount(sc.nextInt());
			setClausCount(sc.nextInt());
			initialise();
			createGraph();
			displayGraph();
		}

		cleanup();
	}

	public static void main(String[] args) {
		Transformation transform = new Transformation();

		transform.run();
	}

}
