package und.map;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {

	private final List<Vertex> vertice;
	private final List<Edge> arcos;
	private Set<Vertex> visitedVertices;
	private Set<Vertex> unvisitedVertices;
	private Map<Vertex, Vertex> predecessors;
	private Map<Vertex, Integer> distancia;

	public DijkstraAlgorithm(Graph graph){
		// Copia o array
		this.vertice = new ArrayList<Vertex>(graph.getVertexes());
		this.arcos = new ArrayList<Edge>(graph.getEdges());
	}
	
	public int printDistancia(Vertex target){
	//	System.out.println(target +"-----" +distancia.get(target));
		return distancia.get(target);
	}

	public void execute(Vertex inicio){
		visitedVertices = new HashSet<Vertex>();
		unvisitedVertices = new HashSet<Vertex>();
		distancia = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		distancia.put(inicio, 0);
		unvisitedVertices.add(inicio);
		
		while (unvisitedVertices.size() > 0){			//enquanto houver vertices nao visitados
			Vertex node = getMinimum(unvisitedVertices);
			visitedVertices.add(node);
			unvisitedVertices.remove(node);
			getMenorDistancia(node);
		}
	}

	private void getMenorDistancia(Vertex node){
		List<Vertex> adjacentes = getAdjacentes(node);
		for (Vertex target : adjacentes){
			if (getShortestDistance(target) > getShortestDistance(node)	+ getDistance(node, target)) {
				distancia.put(target, getShortestDistance(node)	+ getDistance(node, target));
				predecessors.put(target, node);
				unvisitedVertices.add(target);
			}
		}

	}

	private int getDistance(Vertex node, Vertex target){
		for (Edge edge : arcos) {
			if (edge.getSource().equals(node) && edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Inalcancavel");
	}

	private List<Vertex> getAdjacentes(Vertex vert){
		List<Vertex> adj = new ArrayList<Vertex>();
		for (Edge arco : arcos) {
			if (arco.getSource().equals(vert) && !isVisited(arco.getDestination())) {
				adj.add(arco.getDestination());
			}
		}
		return adj;
	}

	private Vertex getMinimum(Set<Vertex> vertexes){
		Vertex minimum = null;
		for (Vertex vert : vertexes){
			if (minimum == null){
				minimum = vert;
			}else{
				if (getShortestDistance(vert) < getShortestDistance(minimum)) {
					minimum = vert;
				}
			}
		}
		return minimum;
	}

	private boolean isVisited(Vertex vert){
		return visitedVertices.contains(vert);
	}

	private int getShortestDistance(Vertex destination){
		Integer dist = distancia.get(destination);
		if (dist == null) {
			return Integer.MAX_VALUE;
		}else{
			return dist;
		}
	}


	public LinkedList<Vertex> getPath(Vertex target){
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = target;
		// verificar se nao esta null
		if (predecessors.get(step) == null){
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null){
			step = predecessors.get(step);
			path.add(step);
		}
		//ordenar
		Collections.reverse(path);
		return path;
	}

}
