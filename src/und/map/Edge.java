package und.map;
public class Edge  {
	
	private final String id; 
	private final Vertex inicio;
	private final Vertex fim;
	private final int peso; 
	
	public Edge(String id, Vertex source, Vertex destination, int weight) {
		this.id = id;
		this.inicio = source;
		this.fim = destination;
		this.peso = weight;
	}
	
	public String getId() {
		return this.id;
	}
	public Vertex getDestination() {
		return this.fim;
	}

	public Vertex getSource() {
		return this.inicio;
	}
	public int getWeight() {
		return this.peso;
	}
	
	@Override
	public String toString() {
		return this.inicio + " " + this.fim;
	}
	
	
}