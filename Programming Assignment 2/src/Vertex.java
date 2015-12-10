/**
 * This class specifies a graph vertex
 * 
 * @author Victor Hazali
 */
public class Vertex implements Comparable<Vertex> {

	private static final String	VERTEX_FIRST_CHAR	= "V";
	private static final String	HEAD_VTX_FIRST_CHAR	= "H";
	private static final String	TAIL_VTX_FIRST_CHAR	= "T";

	/* Member Variables */
	private String				_name;
	private boolean				_traversed;

	/* Constructors */
	public Vertex() {
		setName(null);
		setTraversed(false);
	}

	public Vertex(String name) {
		setName(name);
		setTraversed(false);
	}

	/* Accessors and Modifiers */

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public boolean isTraversed() {
		return _traversed;
	}

	public void setTraversed(boolean traversed) {
		_traversed = traversed;
	}

	/* Public Methods */

	/**
	 * Creates the head vertex for this vertex by changing the first character
	 * of its name.
	 * 
	 * @return a Vertex object representing the head-vertex of this vertex
	 */
	public Vertex getHeadVertex() {
		String headName = getName().replaceFirst(VERTEX_FIRST_CHAR,
				HEAD_VTX_FIRST_CHAR);
		Vertex head = new Vertex(headName);
		head.setTraversed(this.isTraversed());
		return head;
	}

	/**
	 * Creates the tail vertex for this vertex by changing the first character
	 * of its name.
	 * 
	 * @return a Vertex object representing the tail-vertex of this vertex
	 */
	public Vertex getTailVertex() {
		String tailName = getName().replaceFirst(VERTEX_FIRST_CHAR,
				TAIL_VTX_FIRST_CHAR);
		Vertex tail = new Vertex(tailName);
		tail.setTraversed(this.isTraversed());
		return tail;
	}

	/**
	 * Returns a string representation of the vertex in the format:
	 * 
	 * <pre>
	 * Vertex: [vertex name]
	 * Is traversed (if vertex has been traversed by a path)
	 * Not traversed (if vertex has yet to be traversed by any path)
	 * </pre>
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Vertex: " + getName() + "\n");
		if (isTraversed()) {
			result.append("Is traversed\n");
		} else {
			result.append("Not traversed\n");
		}

		return result.toString();
	}

	/**
	 * Checks for equality between two vertices
	 * Two vertices are said to be equal if and only if they have the same name.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Vertex))
			return false;
		if (o == this)
			return true;
		Vertex other = (Vertex) o;
		return getName().equals(other.getName());
	}

	/**
	 * Defines the natural ordering of Vertex. Vertex are ordered by their names
	 * Returns
	 * 
	 * @return a negative value if this object's name is lexicographically
	 *         smaller than the other object's name. A positive value if this
	 *         object's name is lexicographically larger than the object's name.
	 *         0 if the two vertices are equal as defined by the equals method.
	 *         The lexicographically ordering is defined by the java String
	 *         Class's compareTo method
	 */
	@Override
	public int compareTo(Vertex other) {
		if (other == null)
			throw new NullPointerException();

		return getName().compareTo(other.getName());
	}
}
