/**
 * Class implements a vertex
 * 
 * @author Victor Hazali
 */
public class Vertex implements Comparable<Vertex> {

	public static final String	LEFT_BOUND_PREFIX	= "L";
	public static final String	RIGHT_BOUND_PREFIX	= "R";
	public static final String	BUFFER_PREFIX		= "B";
	public static final String	CLAUSAL_PREFIX		= "C";
	public static final String	INTERNAL_PREFIX		= "X";
	public static final String	INT_LEFT_POSTFIX	= "L";
	public static final String	INT_RIGHT_POSTFIX	= "R";
	public static final String	INVALID_NAME		= "Invalid Name";
	public static final String	SOURCE_NAME			= "S";
	public static final String	SINK_NAME			= "T";
	public static final int		INVALID_INDEX		= -1;
	public static final int		FIRST_BUFFER_INDEX	= 0;

	public enum VertexType {
		BOUND_LEFT, BOUND_RIGHT, CLAUSAL, INT_LEFT, INT_RIGHT, BUFFER,
		SOURCE, SINK, INVALID;

		@Override
		public String toString() {
			switch (this) {
				case BOUND_LEFT :
					return "Left Boundary";
				case BOUND_RIGHT :
					return "Right Boundary";
				case CLAUSAL :
					return "Clausal";
				case INT_LEFT :
					return "Internal Left";
				case INT_RIGHT :
					return "Internal Right";
				case BUFFER :
					return "Buffer";
				case SINK :
					return "Sink";
				case SOURCE :
					return "Source";
				case INVALID :
					return "Invalid";
				default:
					throw new IllegalArgumentException();
			}
		}
	}

	private String		_name;
	private VertexType	_type;
	private int			_litIndex;
	private int			_clausIndex;

	public Vertex() {
		setName(INVALID_NAME);
		setType(VertexType.INVALID);
		setLitIndex(INVALID_INDEX);
		setClausIndex(INVALID_INDEX);
	}

	public Vertex(VertexType type, int litIndex, int clausIndex) {
		setName(INVALID_NAME);
		setType(type);
		setLitIndex(litIndex);
		setClausIndex(clausIndex);
		try {
			giveName();
		} catch (IllegalArgumentException e) {
			if (Transformation.DEBUG_MODE) {
				e.getMessage();
				this.toString();
			}
			System.exit(1);
		}
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private void giveName() {
		StringBuilder name = new StringBuilder();
		switch (getType()) {
			case BOUND_LEFT :
				name.append(LEFT_BOUND_PREFIX);
				name.append(getLitIndex());
				break;
			case BOUND_RIGHT :
				name.append(RIGHT_BOUND_PREFIX);
				name.append(getLitIndex());
				break;
			case BUFFER :
				name.append(BUFFER_PREFIX);
				name.append(getLitIndex() + ".");
				name.append(getClausIndex());
				break;
			case CLAUSAL :
				name.append(CLAUSAL_PREFIX);
				name.append(getClausIndex());
				break;
			case INT_LEFT :
				name.append(INTERNAL_PREFIX);
				name.append(getLitIndex() + ".");
				name.append(getClausIndex() + ".");
				name.append(INT_LEFT_POSTFIX);
				break;
			case INT_RIGHT :
				name.append(INTERNAL_PREFIX);
				name.append(getLitIndex() + ".");
				name.append(getClausIndex() + ".");
				name.append(INT_RIGHT_POSTFIX);
				break;
			case SINK :
				name.append(SINK_NAME);
				break;
			case SOURCE :
				name.append(SOURCE_NAME);
				break;
			case INVALID :
				name.append("Invalid");
				break;
			default:
				throw new IllegalArgumentException("Unrecognized type");
		}
		setName(name.toString());
	}

	public VertexType getType() {
		return _type;
	}

	public void setType(VertexType type) {
		_type = type;
	}

	public int getLitIndex() {
		return _litIndex;
	}

	public void setLitIndex(int litIndex) {
		_litIndex = litIndex;
	}

	public int getClausIndex() {
		return _clausIndex;
	}

	public void setClausIndex(int clausIndex) {
		_clausIndex = clausIndex;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (Transformation.DEBUG_MODE) {
			result.append("Name: " + getName() + "\n");
			result.append("Type: " + getType().toString() + "\n");
			result.append("Literal Index: " + getLitIndex() + "\n");
			result.append("Clausal Index: " + getClausIndex() + "\n");
			return result.toString();
		}
		result.append(getName());
		return result.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (o == this) {
			return true;
		}

		if (!(o instanceof Vertex)) {
			return false;
		}

		Vertex other = (Vertex) o;
		return this.getName().equals(other.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public int compareTo(Vertex o) {
		return this.getName().compareTo(o.getName());
	}
}
