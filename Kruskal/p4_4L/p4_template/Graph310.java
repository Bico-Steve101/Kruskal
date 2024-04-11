//TODO:
//  (1) Implement the graph!
//  (2) Update this code to meet the style and JavaDoc requirements.
//			Why? So that you get experience with the code for a graph!
//			Also, this happens a lot in industry (updating old code
//			to meet your new standards).

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;

import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.EdgeType;

import org.apache.commons.collections15.Factory;

import java.util.Collection;

/**
 * Represents an undirected graph.
 * Nodes are instances of the GraphNode class, and edges are instances of the GraphEdge class.
 * The graph is implemented using custom Map310 and Set310 classes.
 */
class Graph310 implements Graph<GraphNode,GraphEdge>, UndirectedGraph<GraphNode,GraphEdge> {

	//you may not have any other instance variables, only these two
	//if you make more instance variables your graph class will receive a 0,
	//no matter how well it works


	/**
	 * Maximum number of nodes allowed in the graph.
	 */
	private static final int MAX_NUMBER_NODES = 200;

	/**
	 * Storage for the graph: adjacency list for each node.
	 */
	private final Map310<GraphNode,Map310<GraphNode,GraphEdge>> storage; //I made the storage final here but you can revert to(private Map310<GraphNode,Map310<GraphNode,GraphEdge>> storage;)


	//********************************************************************************
	//   HINTS:
	//   - Set310 implements the collection interface.
	//     So if you're supposed to return a collection,
	//     then return one of those.
	//********************************************************************************

	public Graph310() {
		this.storage = new Map310<>();

	}

	/**
	 * Returns a view of all edges in this graph. In general, this
	 * obeys the Collection contract, and therefore makes no guarantees
	 * about the ordering of the edges within the set.
	 * @return a Collection view of all edges in this graph
	 */
	@Override
	public Collection<GraphEdge> getEdges() {
		Set310<GraphEdge> edges = new Set310<>();

		for (Map310<GraphNode, GraphEdge> adjacencyList : storage.values()) {
			edges.addAll(adjacencyList.values());
		}

		return edges;
	}


	public Collection<GraphNode> getVertices() {
		return storage.keySet(); // calls keySet function to return all the vertices in graph
	}

	/**
	 * Returns the number of edges in this graph.
	 * @return the number of edges in this graph
	 */
	public int getEdgeCount() {
		int count = 0;

		// Iterate through the vertices and add up their edge counts
		for (Map310<GraphNode, GraphEdge> adjacencyList : storage.values()) {
			count += adjacencyList.size();
		}

		// in an undirected graph, each edge is counted twice, so divide by two
		return count/2;
	}

	public int getVertexCount() {
		return storage.size(); // returns the size of the graph (number of vertices)
	}
	@Override
	public boolean containsVertex(GraphNode vertex) {
		return storage.containsKey(vertex); // calls helped function to see if this graph's vertex collection contains vertex.
	}

	@Override
	public Collection<GraphNode> getNeighbors(GraphNode vertex) {
		if (!containsVertex(vertex)) {
			throw new IllegalArgumentException("Vertex not in graph");
		}

		return storage.get(vertex).keySet();
	}

	public int getNeighborCount(GraphNode vertex) {
		Map310<GraphNode, GraphEdge> adjacencyList = storage.get(vertex);
		// Checks if the vertex is in the graph
		if (adjacencyList != null) {
			// Return the number of neighboring vertices
			return adjacencyList.size();
		} else {
			// if the vertex is not in the graph
			return -1;
		}
	}

	public GraphEdge findEdge(GraphNode v1, GraphNode v2) {
		if (containsVertex(v1) && containsVertex(v2)) { // Checks if both vertices are in the graph
			Map310<GraphNode, GraphEdge> adjacencyList = storage.get(v1); // getthe adjacency list for the first vertex
			return adjacencyList.get(v2);
		}
		return null; // if vertex is not there or edge does not exist
	}

	public boolean isIncident(GraphNode vertex, GraphEdge edge) {
		if (containsVertex(vertex)) { // Check if the vertex is in the graph
			Map310<GraphNode, GraphEdge> adjacencyList = storage.get(vertex); //get the adjacency list for the vertex
			return adjacencyList != null && adjacencyList.containsValue(edge); // Check if the adjacency list is not null and contains the specified edge
		}
		return false;
	}

	@Override
	public Pair<GraphNode> getEndpoints(GraphEdge edge) {
		// Iterate through the nodes in the graph
		for (GraphNode node : storage.keySet()) {
			// Get the adjacency list of the current node
			Map310<GraphNode, GraphEdge> adjacencyList = storage.get(node);
			// Iterate through the neighbors of the current node
			for (GraphNode neighbor : adjacencyList.keySet()) {
				// Get the edge associated with the current neighbor
				GraphEdge e = adjacencyList.get(neighbor);

				// Check if the current edge matches the specified edge
				if (e.equals(edge)) {
					// Return a Pair containing the source and target nodes of the edge
					return new Pair<>(node, neighbor);
				}
			}
		}
		// Return null if the edge is not found in the graph
		return null;
	}

	public Collection<GraphNode> edgeEndpoints(GraphEdge edge) {
		Pair<GraphNode> endpoints = getEndpoints(edge);
		if (endpoints != null) {
			Set310<GraphNode> result = new Set310<>();
			result.add(endpoints.getFirst());
			result.add(endpoints.getSecond());
			return result;
		}
		return null;
	}
	@Override
	public Collection<GraphEdge> getIncidentEdges(GraphNode vertex) {
		if (!containsVertex(vertex)) {
			throw new IllegalArgumentException("Vertex not in graph");
		}

		return storage.get(vertex).values();
	}

	@Override
	public boolean addEdge(GraphEdge e, GraphNode v1, GraphNode v2) {
		if (e == null) {
			throw new IllegalArgumentException("Edge cannot be null");
		}

		if (v1 == null || v2 == null) {
			throw new IllegalArgumentException("Vertices cannot be null");
		}

		// Check if v1 and v2 are the same vertex
		if (v1.equals(v2)) {
			return false; // Adding an edge with the same start and end vertices is not allowed
		}

		if (!containsVertex(v1)) {
			addVertex(v1);
		}
		if (!containsVertex(v2)) {
			addVertex(v2);
		}

		// Add the edge from v1 to v2
		Map310<GraphNode, GraphEdge> adjacencyList1 = storage.get(v1);
		adjacencyList1.put(v2, e);

		// Add the edge from v2 to v1
		Map310<GraphNode, GraphEdge> adjacencyList2 = storage.get(v2);
		adjacencyList2.put(v1, e);

		return true;
	}

	@Override
	public boolean addVertex(GraphNode vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException("Vertex cannot be null");
		}

		if (storage.size() >= MAX_NUMBER_NODES) {
			throw new IllegalStateException("Maximum number of nodes reached");
		}

		if (!containsVertex(vertex)) {
			storage.put(vertex, new Map310<>());
			return true;
		}

		return false;
	}

	@Override
	public boolean removeEdge(GraphEdge edge) {
		if (edge == null) {
			throw new IllegalArgumentException("Edge cannot be null");
		}

		boolean removed = false;

		for (GraphNode vertex : storage.keySet()) {
			Map310<GraphNode, GraphEdge> adjacencyList = storage.get(vertex);

			// Use foreach loop to iterate through keys
			for (GraphNode neighbor : adjacencyList.keySet()) {
				GraphEdge currentEdge = adjacencyList.get(neighbor);
				if (currentEdge.equals(edge)) {
					adjacencyList.remove(neighbor);
					removed = true;
					break;  // exit the loop after removing the edge
				}
			}
		}

		// Also remove the edge from the reverse direction
		Pair<GraphNode> endpoints = getEndpoints(edge);
		if (endpoints != null) {
			GraphNode first = endpoints.getFirst();
			GraphNode second = endpoints.getSecond();
			storage.get(first).remove(second);
			storage.get(second).remove(first);
		}

		return removed;
	}

	@Override
	public boolean removeVertex(GraphNode vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException("Vertex cannot be null");
		}

		if (!containsVertex(vertex)) {
			return false;
		}

		// Remove the vertex from all adjacency lists
		for (Map310<GraphNode, GraphEdge> adjacencyList : storage.values()) {
			adjacencyList.remove(vertex);
		}

		// Remove the vertex from the main storage
		storage.remove(vertex);

		return true;
	}

	public Set310<GraphNode> reachableSet(GraphNode vertex) {
		if (containsVertex(vertex)) { // Check if the vertex is in the graph
			Set310<GraphNode> visited = new Set310<>(); // stores visited nodes during the depth-first search
			dfs(vertex, visited); // Perform depth-first search (DFS) starting from the specified vertex
			visited.remove(vertex);  // Remove the starting vertex from the result
			return visited;
		}
		return null;
	}

	private void dfs(GraphNode current, Set310<GraphNode> visited) {
		visited.add(current);

		for (GraphNode neighbor : getNeighbors(current)) { //itertaes through neighbors of current vertex
			if (!visited.contains(neighbor)) {
				dfs(neighbor, visited); // Recursively perform DFS for unvisited neighbors
			}
		}
	}
	//********************************************************************************
	//   YOU MAY, BUT DON'T NEED TO EDIT THINGS IN THIS SECTION
	//   NOTE: you do need to fix JavaDoc issues if there is any in this section.
	//********************************************************************************

	/**
	 * Returns a Collection view of the incoming edges incident to vertex
	 * in this graph.
	 * @param vertex    the vertex whose incoming edges are to be returned
	 * @return  a Collection view of the incoming edges incident
	 * to vertex in this graph
	 */
	public Collection<GraphEdge> getInEdges(GraphNode vertex) {

		//actually, for undirected graph, this is the same as getOutEdges
		//get all edge records
		return getIncidentEdges(vertex);

	}

	/**
	 * Returns a Collection view of the outgoing edges incident to vertex
	 * in this graph.
	 * @param vertex    the vertex whose outgoing edges are to be returned
	 * @return  a Collection view of the outgoing edges incident
	 * to vertex in this graph
	 */
	public Collection<GraphEdge> getOutEdges(GraphNode vertex) {
		return getIncidentEdges(vertex);
	}

	/**
	 * Returns the number of incoming edges incident to vertex.
	 * Equivalent to getInEdges(vertex).size().
	 * @param vertex    the vertex whose indegree is to be calculated
	 * @return  the number of incoming edges incident to vertex
	 */
	public int inDegree(GraphNode vertex) {
		return degree(vertex);
	}

	/**
	 * Returns the number of outgoing edges incident to vertex.
	 * Equivalent to getOutEdges(vertex).size().
	 * @param vertex    the vertex whose outdegree is to be calculated
	 * @return  the number of outgoing edges incident to vertex
	 */
	public int outDegree(GraphNode vertex) {
		return degree(vertex);

	}


	/**
	 * Returns a Collection view of the predecessors of vertex
	 * in this graph.  A predecessor of vertex is defined as a vertex v
	 * which is connected to
	 * vertex by an edge e, where e is an outgoing edge of
	 * v and an incoming edge of vertex.
	 * @param vertex    the vertex whose predecessors are to be returned
	 * @return  a Collection view of the predecessors of
	 * vertex in this graph
	 */
	public Collection<GraphNode> getPredecessors(GraphNode vertex) {
		//undirected graph
		return getNeighbors(vertex);

	}

	/**
	 * Returns a Collection view of the successors of vertex
	 * in this graph.  A successor of vertex is defined as a vertex v
	 * which is connected to
	 * vertex by an edge e, where e is an incoming edge of
	 * v and an outgoing edge of vertex.
	 * @param vertex    the vertex whose predecessors are to be returned
	 * @return  a Collection view of the successors of
	 * vertex in this graph
	 */
	public Collection<GraphNode> getSuccessors(GraphNode vertex) {
		return getNeighbors(vertex);

	}

	/**
	 * Returns true if v1 is a predecessor of v2 in this graph.
	 * Equivalent to v1.getPredecessors().contains(v2).
	 * @param v1 the first vertex to be queried
	 * @param v2 the second vertex to be queried
	 * @return true if v1 is a predecessor of v2, and false otherwise.
	 */
	public boolean isPredecessor(GraphNode v1, GraphNode v2) {
		return isNeighbor(v1, v2);

	}

	/**
	 * Returns true if v1 is a successor of v2 in this graph.
	 * Equivalent to v1.getSuccessors().contains(v2).
	 * @param v1 the first vertex to be queried
	 * @param v2 the second vertex to be queried
	 * @return true if v1 is a successor of v2, and false otherwise.
	 */
	public boolean isSuccessor(GraphNode v1, GraphNode v2) {
		return isNeighbor(v1, v2);

	}


	/**
	 * If directed_edge is a directed edge in this graph, returns the source;
	 * otherwise returns null.
	 * The source of a directed edge d is defined to be the vertex for which
	 * d is an outgoing edge.
	 * directed_edge is guaranteed to be a directed edge if
	 * its EdgeType is DIRECTED.
//	 * @param directedEdge
	 * @return  the source of directed_edge if it is a directed edge in this graph, or null otherwise
	 */
	public GraphNode getSource(GraphEdge directedEdge) {
		Pair<GraphNode> endpoints = getEndpoints(directedEdge);
		return endpoints != null ? endpoints.getFirst() : null;
	}

	/**
	 * If directed_edge is a directed edge in this graph, returns the destination;
	 * otherwise returns null.
	 * The destination of a directed edge d is defined to be the vertex
	 * incident to d for which
	 * d is an incoming edge.
	 * directed_edge is guaranteed to be a directed edge if
	 * its EdgeType is DIRECTED.
//	 * @param directedEdge
	 * @return  the destination of directed_edge if it is a directed edge in this graph, or null otherwise
	 */
	public GraphNode getDest(GraphEdge directedEdge) {
		Pair<GraphNode> endpoints = getEndpoints(directedEdge);
		return endpoints != null ? endpoints.getSecond() : null;
	}
	/**
	 * Returns the number of edges incident to vertex.
	 * Special cases of interest:
	 * <ul>c
	 * <li/> Incident self-loops are counted once.
	 * <li> If there is only one edge that connects this vertex to
	 * each of its neighbors (and vice versa), then the value returned
	 * will also be equal to the number of neighbors that this vertex has
	 * (that is, the output of getNeighborCount).
	 * <li> If the graph is directed, then the value returned will be
	 * the sum of this vertex's indegree (the number of edges whose
	 * destination is this vertex) and its outdegree (the number
	 * of edges whose source is this vertex), minus the number of
	 * incident self-loops (to avoid double-counting).
	 * </ul>
	 * <p>Equivalent to getIncidentEdges(vertex).size().
	 *
	 * @param vertex the vertex whose degree is to be returned
	 * @return the degree of this node
//	 * @see Hypergraph#getNeighborCount(Object)
	 */
	public int degree(GraphNode vertex) {
		if (containsVertex(vertex)) {
			return getNeighborCount(vertex);
		} else {
			return 0; // or throw an exception, depending on your requirements
		}
	}

	/**
	 * Returns true if v1 and v2 share an incident edge.
	 * Equivalent to getNeighbors(v1).contains(v2).
	 *
	 * @param v1 the first vertex to test
	 * @param v2 the second vertex to test
	 * @return true if v1 and v2 share an incident edge
	 */
	public boolean isNeighbor(GraphNode v1, GraphNode v2) {
		return (findEdge(v1, v2) != null || findEdge(v2, v1)!=null);
	}


	/**
	 * Returns the collection of vertices in this graph which are connected to edge.
	 * Note that for some graph types there are guarantees about the size of this collection
	 * (i.e., some graphs contain edges that have exactly two endpoints, which may or may
	 * not be distinct).  Implementations for those graph types may provide alternate methods
	 * that provide more convenient access to the vertices.
	 *
	 * @param edge the edge whose incident vertices are to be returned
	 * @return  the collection of vertices which are connected to edge,
	 * or null if edge is not present
	 */
	public Collection<GraphNode> getIncidentVertices(GraphEdge edge) {
		Pair<GraphNode> p = getEndpoints(edge);
		Set310<GraphNode> ret = new Set310<>();
		ret.add(p.getFirst());
		ret.add(p.getSecond());
		return ret;
	}


	/**
	 * Returns true if this graph's edge collection contains edge.
	 * Equivalent to getEdges().contains(edge).
	 * @param edge the edge whose presence is being queried
	 * @return true iff this graph contains an edge edge
	 */
	@Override
	public boolean containsEdge(GraphEdge edge) {
		return storage.values().stream()
				.anyMatch(adjacencyList -> adjacencyList.values().contains(edge));
	}

	/**
	 * Returns the collection of edges in this graph which are of type edge_type.
	 * @param edgeType the type of edges to be returned
	 * @return the collection of edges which are of type edge_type, or
	 * null if the graph does not accept edges of this type
	 * @see EdgeType
	 */
	public Collection<GraphEdge> getEdges(EdgeType edgeType) {
		if(edgeType == EdgeType.UNDIRECTED) {
			return getEdges();
		}
		return null;
	}

	/**
	 * Returns the number of edges of type edge_type in this graph.
	 * @param edgeType the type of edge for which the count is to be returned
	 * @return the number of edges of type edge_type in this graph
	 */
	public int getEdgeCount(EdgeType edgeType) {
		if(edgeType == EdgeType.UNDIRECTED) {
			return getEdgeCount();
		}
		return 0;
	}

	/**
	 * Returns the number of predecessors that vertex has in this graph.
	 * Equivalent to vertex.getPredecessors().size().
	 * @param vertex the vertex whose predecessor count is to be returned
	 * @return  the number of predecessors that vertex has in this graph
	 */
	public int getPredecessorCount(GraphNode vertex) {
		return inDegree(vertex);
	}

	/**
	 * Returns the number of successors that vertex has in this graph.
	 * Equivalent to vertex.getSuccessors().size().
	 * @param vertex the vertex whose successor count is to be returned
	 * @return  the number of successors that vertex has in this graph
	 */
	public int getSuccessorCount(GraphNode vertex) {
		return outDegree(vertex);
	}

	/**
	 * Returns the vertex at the other end of edge from vertex.
	 * (That is, returns the vertex incident to edge which is not vertex.)
	 * @param vertex the vertex to be queried
	 * @param edge the edge to be queried
	 * @return the vertex at the other end of edge from vertex
	 */
	public GraphNode getOpposite(GraphNode vertex, GraphEdge edge) {
		Pair<GraphNode> p = getEndpoints(edge);
		if(p.getFirst().equals(vertex)) {
			return p.getSecond();
		}
		else {
			return p.getFirst();
		}
	}

	/**
	 * Returns all edges that connects v1 to v2.
	 * If this edge is not uniquely
	 * defined (that is, if the graph contains more than one edge connecting
	 * v1 to v2), any of these edges
	 * may be returned.  findEdgeSet(v1, v2) may be
	 * used to return all such edges.
	 * Returns null if v1 is not connected to v2.
	 * <br/>Returns an empty collection if either v1 or v2 are not present in this graph.
	 *
	 * <p><b>Note</b>: for purposes of this method, v1 is only considered to be connected to
	 * v2 via a given <i>directed</i> edge d if
	 * v1 == d.getSource() && v2 == d.getDest() evaluates to true.
	 * (v1 and v2 are connected by an undirected edge u if
	 * u is incident to both v1 and v2.)
	 *
	 * @return  a collection containing all edges that connect v1 to v2,
	 * or null if either vertex is not present
//	 * @see Hypergraph#findEdge(Object, Object)
	 */
	public Collection<GraphEdge> findEdgeSet(GraphNode v1, GraphNode v2) {
		GraphEdge edge = findEdge(v1, v2);
		if(edge == null) {
			return null;
		}

		Set310<GraphEdge> ret = new Set310<>();
		ret.add(edge);
		return ret;

	}

	/**
	 * Returns true if vertex is the source of edge.
	 * Equivalent to getSource(edge).equals(vertex).
	 * @param vertex the vertex to be queried
	 * @param edge the edge to be queried
	 * @return true iff vertex is the source of edge
	 */
	public boolean isSource(GraphNode vertex, GraphEdge edge) {
		if (getSource(edge)==null) return false;
		return getSource(edge).equals(vertex);
	}

	/**
	 * Returns true if vertex is the destination of edge.
	 * Equivalent to getDest(edge).equals(vertex).
	 * @param vertex the vertex to be queried
	 * @param edge the edge to be queried
	 * @return true iff vertex is the destination of edge
	 */
	public boolean isDest(GraphNode vertex, GraphEdge edge) {
		if (getSource(edge)==null) return false;
		return getDest(edge).equals(vertex);
	}

	/**
	 * Adds edge e to this graph such that it connects
	 * vertex v1 to v2.
	 * Equivalent to addEdge(e, new Pair<GraphNode>(v1, v2)).
	 * If this graph does not contain v1, v2,
	 * or both, implementations may choose to either silently add
	 * the vertices to the graph or throw an IllegalArgumentException.
	 * If edgeType is not legal for this graph, this method will
	 * throw IllegalArgumentException.
	 * See Hypergraph.addEdge() for a listing of possible reasons
	 * for failure.
	 * @param e the edge to be added
	 * @param v1 the first vertex to be connected
	 * @param v2 the second vertex to be connected
	 * @param edgeType the type to be assigned to the edge
	 * @return true if the add is successful, false otherwise
//	 * @see Hypergraph#addEdge(Object, Collection)
//	 * @see #addEdge(Object, Object, Object)
	 */
	public boolean addEdge(GraphEdge e, GraphNode v1, GraphNode v2, EdgeType edgeType) {
		//NOTE: Only directed edges allowed

		if(edgeType == EdgeType.DIRECTED) {
			throw new IllegalArgumentException();
		}

		return addEdge(e, v1, v2);
	}

	/**
	 * Adds edge to this graph.
	 * Fails under the following circumstances:
	 * <ul>
	 * <li/>edge is already an element of the graph
	 * <li/>either edge or vertices is null
	 * <li/>vertices has the wrong number of vertices for the graph type
	 * <li/>vertices are already connected by another edge in this graph,
	 * and this graph does not accept parallel edges
	 * </ul>
	 *
//	 * @param edge
//	 * @param vertices
	 * @return true if the add is successful, and false otherwise
	 * @throws IllegalArgumentException if edge or vertices is null,
	 * or if a different vertex set in this graph is already connected by edge,
	 * or if vertices are not a legal vertex set for edge
	 */
	@SuppressWarnings("unchecked")
	public boolean addEdge(GraphEdge edge, Collection<? extends GraphNode> vertices) {
		if(edge == null || vertices == null || vertices.size() != 2) {
			return false;
		}

		GraphNode[] vs = (GraphNode[])vertices.toArray();
		return addEdge(edge, vs[0], vs[1]);
	}

	/**
	 * Adds edge to this graph with type edge_type.
	 * Fails under the following circumstances:
	 * <ul>
	 * <li/>edge is already an element of the graph
	 * <li/>either edge or vertices is null
	 * <li/>vertices has the wrong number of vertices for the graph type
	 * <li/>vertices are already connected by another edge in this graph,
	 * and this graph does not accept parallel edges
	 * <li/>edge_type is not legal for this graph
	 * </ul>
	 *
	 * @param edge
	 * @param vertices
	 * @return true if the add is successful, and false otherwise
	 * @throws IllegalArgumentException if edge or vertices is null,
	 * or if a different vertex set in this graph is already connected by edge,
	 * or if vertices are not a legal vertex set for edge
	 */

	@SuppressWarnings("unchecked")
	public boolean addEdge(GraphEdge edge, Collection<? extends GraphNode> vertices, EdgeType edge_type) {
		if(edge == null || vertices == null || vertices.size() != 2) {
			return false;
		}

		GraphNode[] vs = (GraphNode[])vertices.toArray();
		return addEdge(edge, vs[0], vs[1], edge_type);
	}

	//********************************************************************************
	//   DO NOT EDIT ANYTHING BELOW THIS LINE EXCEPT FOR FIXING JAVADOC
	//********************************************************************************

	/**
	 * Returns a {@code Factory} that creates an instance of this graph type.
	 * @param <V> the vertex type for the graph factory
	 * @param <E> the edge type for the graph factory
	 * @return a factory for creating instances of the Graph310 class
	 */

	public static <V,E> Factory<UndirectedGraph<GraphNode,GraphEdge>> getFactory() {
		return new Factory<UndirectedGraph<GraphNode,GraphEdge>> () {
			@SuppressWarnings("unchecked")
			public UndirectedGraph<GraphNode,GraphEdge> create() {
				return (UndirectedGraph<GraphNode,GraphEdge>) new Graph310();
			}
		};
	}
	/**
	 * Returns the edge type of edge in this graph.
	 * @param edge the edge whose type is to be determined
	 * @return the EdgeType of edge, or null if edge has no defined type
	 */
	public EdgeType getEdgeType(GraphEdge edge) {
		return EdgeType.UNDIRECTED;
	}

	/**
	 * Returns the default edge type for this graph.
	 *
	 * @return the default edge type for this graph
	 */
	public EdgeType getDefaultEdgeType() {
		return EdgeType.UNDIRECTED;
	}

	/**
	 * Returns the number of vertices that are incident to edge.
	 * For hyperedges, this can be any nonnegative integer; for edges this
	 * must be 2 (or 1 if self-loops are permitted).
	 *
	 * <p>Equivalent to getIncidentVertices(edge).size().
	 * @param edge the edge whose incident vertex count is to be returned
	 * @return the number of vertices that are incident to edge.
	 */
	public int getIncidentCount(GraphEdge edge) {
		return 2;
	}

}  

