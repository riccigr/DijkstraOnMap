package und.map;
import java.util.List;

public class Graph {
	private final List<Vertex> vertices;
	private final List<Edge> arcos;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertices = vertexes;
		this.arcos = edges;
	}

	public List<Vertex> getVertexes() {
		return vertices;
	}

	public List<Edge> getEdges() {
		return arcos;
	}
	
	
	
}