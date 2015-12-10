import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class specifies an undirected graph G
 * 
 * @author Victor Hazali
 */
public class Graph {

	/* Constants */
	private static final boolean	SUCCEEDED	= true;
	private static final boolean	FAILED		= false;

	/* Member Variables */
	private int[][]					_adjMat;
	private Map<String, Integer>	_nameMapping;
	private int						_currVertexCnt;
	private List<Vertex>			_vertices;
	private int						_edgeCount;

	/* Constructor */

	public Graph(int vertexCount) {
		setAdjMat(new int[vertexCount][vertexCount]);
		setNameMapping(new HashMap<String, Integer>());
		setCurrVertexCnt(0);
		setVertices(new ArrayList<Vertex>());
		setEdgeCount(0);
	}

	/* Accessors and Modifiers */

	public int[][] getAdjMat() {
		return _adjMat;
	}

	public void setAdjMat(int[][] adjMat) {
		_adjMat = adjMat;
	}

	public void addEdge(int vertex1, int vertex2) {
		_adjMat[vertex1][vertex2] += 1;
		_adjMat[vertex2][vertex1] += 1;
		incEdgeCnt();
	}

	public void removeEdge(int vertex1, int vertex2) {
		_adjMat[vertex1][vertex2] -= 1;
		_adjMat[vertex2][vertex1] -= 1;
		decEdgeCnt();
	}

	public Map<String, Integer> getNameMapping() {
		return _nameMapping;
	}

	public void setNameMapping(Map<String, Integer> nameMapping) {
		_nameMapping = nameMapping;
	}

	public void addNameMapping(String name, int value) {
		_nameMapping.put(name, value);
	}

	public int getIndexFromName(String name) {
		return _nameMapping.get(name);
	}

	public int getCurrVertexCnt() {
		return _currVertexCnt;
	}

	public void setCurrVertexCnt(int currIndex) {
		_currVertexCnt = currIndex;
	}

	public void incCurrVertexCnt() {
		_currVertexCnt = _currVertexCnt + 1;
	}

	public List<Vertex> getVertices() {
		return _vertices;
	}

	public void setVertices(List<Vertex> vertices) {
		_vertices = vertices;
	}

	private void addVertex(Vertex vertex) {
		_vertices.add(vertex);
		incCurrVertexCnt();
	}

	public int getEdgeCount() {
		return _edgeCount;
	}

	public void setEdgeCount(int edgeCount) {
		_edgeCount = edgeCount;
	}

	public void incEdgeCnt() {
		_edgeCount = _edgeCount + 1;
	}

	public void decEdgeCnt() {
		assert (_edgeCount > 0);
		_edgeCount = _edgeCount - 1;
	}

	/* Public Methods */

	/**
	 * Adds a vertex into the graph. If the number of vertices currently in the
	 * graph is already at its limit, the new vertex will not be added. If the
	 * vertex to be added is already in the graph, nothing will be done.
	 * 
	 * @param name
	 *            name of vertex to be added
	 * @return true if the vertex is successfully added or is already inside the
	 *         graph. False otherwise.
	 */
	public boolean addVertex(String name) {

		// Checks if vertex is already in graph
		if (getNameMapping().containsKey(name)) {
			return SUCCEEDED;
		}

		// Check if limit of vertices reached
		if (getCurrVertexCnt() == getAdjMat().length) {
			return FAILED;
		}

		// Adding new vertex
		addNameMapping(name, getCurrVertexCnt());
		addVertex(new Vertex(name));

		return SUCCEEDED;
	}

	/**
	 * Inserts a vertex into the graph. If the number of vertices currently in
	 * the graph is already at its limit, the new vertex will not be inserted.
	 * If the vertex to be added is already in the graph, nothing will be done.
	 * 
	 * @param vertex
	 *            vertex to be added into graph
	 * @return true if the vertex is successfully added or is already inside the
	 *         graph. False otherwise.
	 */
	public boolean insertVertex(Vertex vertex) {

		// Checks if vertex is already in graph
		if (getVertices().contains(vertex)) {
			return SUCCEEDED;
		}

		// Check if limit of vertices reached
		if (getCurrVertexCnt() == getAdjMat().length) {
			return FAILED;
		}

		// Adding the new vertex
		addNameMapping(vertex.getName(), getCurrVertexCnt());
		addVertex(vertex);

		return SUCCEEDED;
	}

	/**
	 * Adds an edge into the graph. An edge will only be added if the edge is
	 * between two nodes that both exist in the graph. If an edge already exist
	 * between the two nodes, an additional edge will be added. There are no
	 * limits to the number of edges between a pair of nodes.
	 * 
	 * @param node1
	 *            name of first node
	 * @param node2
	 *            name of second node
	 * @return true if the edge was successfully added into the graph. False
	 *         otherwise.
	 */
	public boolean addEdge(String node1, String node2) {

		// Adds vertex into the graph
		if (!addVertex(node1) || !addVertex(node2)) {
			return FAILED;
		}

		int n1 = getIndexFromName(node1);
		int n2 = getIndexFromName(node2);
		addEdge(n1, n2);

		return SUCCEEDED;
	}

	/**
	 * Marks a vertex as traversed. Method will only successfully traverse a
	 * vertex if the vertex exist in the graph and has yet to be traversed. Note
	 * that this method should only be directly called when it is the first node
	 * of a path, otherwise, use traverse(from, to).
	 * 
	 * @param name
	 *            Name of vertex to traverse
	 * @return true if the node has been successfully traversed, false
	 *         otherwise.
	 */
	public boolean traverse(String name) {

		// Checks if graph has any vertices
		if (getVertices() == null || getVertices().size() == 0) {
			return FAILED;
		}

		// Checks if graph contain the vertex to traverse
		if (!getNameMapping().containsKey(name)) {
			return FAILED;
		}

		// Gets a pointer to the vertex to be traversed
		Vertex toTraverse = getVertices().get(getIndexFromName(name));

		// If vertex is already traversed, return
		if (toTraverse.isTraversed()) {
			return FAILED;
		}

		toTraverse.setTraversed(true);
		return SUCCEEDED;
	}

	/**
	 * Traverses from a vertex to the next vertex. Method will only successfully
	 * traverse if both vertices exist in the graph, at least an edge exist
	 * between them and the target node has yet to be traversed. After
	 * traversing, the edge used is removed from the graph.
	 * 
	 * @param from
	 *            vertex to traverse from
	 * @param to
	 *            vertex to traverse to
	 * @return true if successfully traverse to the next node, false otherwise
	 */
	public boolean traverse(String from, String to) {

		// Checks if graph has vertices
		if (getVertices() == null || getVertices().size() == 0) {
			return FAILED;
		}

		// Checks if graph has edges
		if (getEdgeCount() == 0) {
			return FAILED;
		}

		// Checks if an edge exist between these two nodes
		int n1 = getIndexFromName(from);
		int n2 = getIndexFromName(to);
		if (!edgeExists(n1, n2)) {
			return FAILED;
		}
		if (traverse(to)) {
			removeEdge(n1, n2);
			return SUCCEEDED;
		}

		return FAILED;
	}

	/**
	 * Checks if there is at least an edge between a pair of vertices.
	 * 
	 * @param from
	 *            the first vertex
	 * @param to
	 *            the second vertex
	 * @return true if the adjacency matrix entry of [from][to] is greater than
	 *         0 (there is at least one edge). False otherwise.
	 */
	public boolean edgeExists(int from, int to) {
		int[][] matrix = getAdjMat();
		if (matrix[from][to] > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if there is at least an edge between a pair of vertices
	 * 
	 * @param from
	 *            Name of first vertex
	 * @param to
	 *            Name of second vertex
	 * @return true if there exist at least an edge between the two vertices as
	 *         specified by edgeExists(int from, int to). False otherwise
	 */
	public boolean edgeExists(String from, String to) {
		int n1 = getIndexFromName(from);
		int n2 = getIndexFromName(to);

		return edgeExists(n1, n2);
	}
}
