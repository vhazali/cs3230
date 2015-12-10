/**
 * This class implements a Directed Edge
 * 
 * @author Victor hazali
 */
public class DirectedEdge implements Comparable<DirectedEdge> {
	private Vertex	_from;
	private Vertex	_to;

	public DirectedEdge() {
		setFrom(new Vertex());
		setTo(new Vertex());
	}

	public DirectedEdge(Vertex from, Vertex to) {
		setFrom(from);
		setTo(to);
	}

	public Vertex getFrom() {
		return _from;
	}

	public void setFrom(Vertex from) {
		_from = from;
	}

	public Vertex getTo() {
		return _to;
	}

	public void setTo(Vertex to) {
		_to = to;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		if (Transformation.DEBUG_MODE) {
			result.append("From: " + getFrom().toString());
			result.append("To: " + getTo().toString());
			return result.toString();
		}

		result.append(getFrom().toString());
		result.append(" ");
		result.append(getTo().toString());

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

		if (!(o instanceof DirectedEdge)) {
			return false;
		}

		DirectedEdge other = (DirectedEdge) o;
		return (this.getFrom().equals(other.getFrom())
		&& this.getTo().equals(other.getTo()));
	}

	@Override
	public int hashCode() {
		String temp = getFrom().getName() + getTo().getName();
		return temp.hashCode();
	}

	@Override
	public int compareTo(DirectedEdge o) {
		if (this.getFrom().compareTo(o.getFrom()) == 0) {
			if (this.getTo().compareTo(o.getTo()) == 0) {
				return 0;
			} else
				return this.getTo().compareTo(o.getTo());
		} else
			return this.getFrom().compareTo(o.getFrom());
	}
}
