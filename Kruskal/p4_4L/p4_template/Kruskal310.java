//TODO: Implement the required methods and add JavaDoc as needed.
//Remember: Do NOT add any additional instance or class variables (local variables are ok)
//and do NOT alter any provided methods or change any method signatures!

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.awt.Color;

import javax.swing.JPanel;


/**
 *  Simulation of Kruskal algorithm.
 *  
 */
class Kruskal310 implements ThreeTenAlg {
	/**
	 *  The graph the algorithm will run on.
	 */
	Graph<GraphNode, GraphEdge> graph;
	
	/**
	 *  The priority queue of edges for the algorithm.
	 */
	WeissBST<GraphEdge> pqueue;
	
	/**
	 *  The subgraph of the MST in construction.
	 */
	private Graph310 markedGraph;
	
	/**
	 *  Whether or not the algorithm has been started.
	 */
	private boolean started = false;
	
	/**
	 *  The color when a node has "no color".
	 */
	public static final Color COLOR_NONE_NODE = Color.WHITE;
	
	/**
	 *  The color when an edge has "no color".
	 */
	public static final Color COLOR_NONE_EDGE = Color.BLACK;
		
	/**
	 *  The color when a node is inactive.
	 */
	public static final Color COLOR_INACTIVE_NODE = Color.LIGHT_GRAY;

	/**
	 *  The color when an edge is inactive.
	 */
	public static final Color COLOR_INACTIVE_EDGE = Color.LIGHT_GRAY;
	
	/**
	 *  The color when a node is highlighted.
	 */
	public static final Color COLOR_HIGHLIGHT = new Color(255,204,51);
	
	/**
	 *  The color when a node is in warning.
	 */
	public static final Color COLOR_WARNING = new Color(255,51,51);

	/**
	 *  The color when a node/edge is selected and added to MST.
	 */
	public static final Color COLOR_SELECTED = Color.BLUE;
			
	/**
	 *  {@inheritDoc}
	 */
	public EdgeType graphEdgeType() {
		return EdgeType.UNDIRECTED;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void reset(Graph<GraphNode, GraphEdge> graph) {
		this.graph = graph;
		started = false;
		pqueue = null;	
		markedGraph = new Graph310();	
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public boolean isStarted() {
		return started;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void cleanUpLastStep() {
		// Unused. Required by the interface.		
	}
	

	//----------------------------------------------------
	// TODO: Complete the methods below to implement kruskal's algorithm.
	// - DO NOT change the signature of any required public methods;
	// - Feel free to define additional method but they must be private.
	//
	//----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public void start() {
		started = true;
		// Complete the missing part:
		// - add all edges into the priority queue

		pqueue = new WeissBST<>();
		// Iterate over all edges in the graph and add them to the priority queue
		for (GraphEdge edge : graph.getEdges()) {
			pqueue.insert(edge);
		}
		// End of missing part

		// highlight the edge with min weight
		highlightNext();

	}

	/**
	 * Highlights the next minimum-weight edge in the priority queue.
	 * Finds the current minimum-weight edge in the priority queue and change its color to COLOR_HIGHLIGHT .
	 */
	public void highlightNext() {

		// Find the current min edge in the priority queue
		// and change the color of the edge to be COLOR_HIGHLIGHT.
		// Note: do not dequeue the node.

		if (pqueue.isEmpty()) { // checks if there are no edges left
			return;
		}
		GraphEdge minEdge = pqueue.findMin();
		minEdge.setColor(COLOR_HIGHLIGHT); // updates color
	}

	/**
	 * {@inheritDoc}
	 */
	public void finish() {

		// wrapping up the algorithm
		// - mark all edges not selected to be inactive
		// - mark all nodes not in the constructed MST with COLOR_WARINING

		for (GraphEdge edge : graph.getEdges()) {
			if (!markedGraph.containsEdge(edge)) {
				edge.setColor(COLOR_INACTIVE_EDGE);
			}
		}

		for (GraphNode node : graph.getVertices()) {
			if (!markedGraph.containsVertex(node)) {
				node.setColor(COLOR_WARNING);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean setupNextStep() {

		// decide whether we are done with the MST algorithm
		// return true if more steps to continue; return false if done
		// Hint: you may not always need to check all edges.

		return !pqueue.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public void doNextStep() {

		// remove the next min edge from priority queue and check:
		// - if edge should be included in MST, add it into MST and change the color of
		// the edge and nodes (COLOR_SELECTED)
		// - if edge should not be included, change its color to COLOR_INACTIVE_EDGE
		// - if MST is not completed, highlight next min edge

		GraphEdge minEdge = pqueue.findMin();
		pqueue.removeMin();
		boolean valid = true;

		Pair<GraphNode> ends = graph.getEndpoints(minEdge);
		GraphNode endOne = ends.getFirst();
		GraphNode endTwo = ends.getSecond();

		if (markedGraph.containsVertex(endTwo)) {
			Set310<GraphNode> set = markedGraph.reachableSet(endTwo);
			valid = !set.contains(endOne);
		}

		if (valid) {
			// Edge not in the marked graph, add it to the MST
			markedGraph.addVertex(endOne);
			markedGraph.addVertex(endTwo);
			markedGraph.addEdge(minEdge, endOne, endTwo);
			endOne.setColor(COLOR_SELECTED);
			endTwo.setColor(COLOR_SELECTED);
			minEdge.setColor(COLOR_SELECTED);
		} else {
			// Edge in the marked graph, mark it as inactive
			minEdge.setColor(COLOR_INACTIVE_EDGE);
		}
		// If MST is not done, highlight the next min edge
		if (setupNextStep()) {
			highlightNext();
		}
	}


}