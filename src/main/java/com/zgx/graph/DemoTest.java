package com.zgx.graph;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.jgrapht.util.VertexToIntegerMapping;

import java.io.StringWriter;
import java.util.*;

public class DemoTest {
    public static void main(String[] args) {

        DirectedAcyclicGraph<Node, DefaultEdge> graph = new DirectedAcyclicGraph(DefaultEdge.class);

        List<Node> nodes = new ArrayList<>();

        for (int i = 1; i < 20; i++) {
            Node v = new Node(i);
            nodes.add(v);
            graph.addVertex(v);
        }

        graph.addEdge(nodes.get(14), nodes.get(16));
        graph.addEdge(nodes.get(16), nodes.get(0));
        graph.addEdge(nodes.get(0), nodes.get(1));
        graph.addEdge(nodes.get(0), nodes.get(2));
        graph.addEdge(nodes.get(1), nodes.get(4));
        graph.addEdge(nodes.get(1), nodes.get(5));
        graph.addEdge(nodes.get(2), nodes.get(3));
        graph.addEdge(nodes.get(4), nodes.get(6));
        graph.addEdge(nodes.get(5), nodes.get(7));
        graph.addEdge(nodes.get(5), nodes.get(8));
        graph.addEdge(nodes.get(6), nodes.get(12));
        graph.addEdge(nodes.get(7), nodes.get(9));
        graph.addEdge(nodes.get(8), nodes.get(10));
        graph.addEdge(nodes.get(9), nodes.get(11));
        graph.addEdge(nodes.get(10), nodes.get(11));
        graph.addEdge(nodes.get(11), nodes.get(12));
        graph.addEdge(nodes.get(12), nodes.get(13));
        graph.addEdge(nodes.get(3), nodes.get(13));
        graph.addEdge(nodes.get(13), nodes.get(15));
        graph.addEdge(nodes.get(13), nodes.get(17));
        graph.addEdge(nodes.get(15), nodes.get(18));
        graph.addEdge(nodes.get(17), nodes.get(18));


        List<Node> result = new ArrayList<>();

        buildResult(graph, result, nodes);

//        DOTExporter exporter = new DOTExporter(Object::toString);
//        StringWriter writer = new StringWriter();
//        exporter.exportGraph(graph, writer);
//        System.out.println(writer.toString());
    }

    private static void buildResult(DirectedAcyclicGraph<Node, DefaultEdge> graph, List<Node> result, List<Node> nodes) {

        TopologicalOrderIterator<Node, DefaultEdge> iterator = new TopologicalOrderIterator(graph);

        Stack<Node> branchNodeStack = new Stack<>();

//        System.out.println(nodes.get(1) + " successorList is : "+Graphs.successorListOf(graph, nodes.get(1)));
//        System.out.println("node:" + nodes.get(6) + " prev：" + Graphs.predecessorListOf(graph, nodes.get(6)));;
//        System.out.println("node:" + nodes.get(7) + "all prev：" + Graphs.predecessorListOf(graph, nodes.get(7)));;
//        System.out.println("node:" + nodes.get(9) + " prev：" + Graphs.predecessorListOf(graph, nodes.get(9)));;
//        System.out.println("node:" + nodes.get(11) + "all prev：" + Graphs.predecessorListOf(graph, nodes.get(11)));;

        iterator.forEachRemaining(n -> {
            processGraph(graph, result, branchNodeStack, n);
        });

        System.out.println("xx");

    }

    private static void processGraph(DirectedAcyclicGraph<Node, DefaultEdge> graph, List<Node> result, Stack<Node> branchNodeStack, Node n) {
        //inDegree & outDegree
        int inDegree = graph.inDegreeOf(n);
        int outDegree = graph.outDegreeOf(n);
        //first or last
        if (inDegree <= 0 || outDegree <= 0) {
            result.add(n);
            return;
        }
        //get which node data list to add
        System.out.println("node:" + n + ", branch stack:" + branchNodeStack);
        //simple
        if (inDegree == 1 && outDegree ==1) {
            if (branchNodeStack.isEmpty()) {
                result.add(n);
                return;
            }
            //prev出度
            Node prev = Graphs.predecessorListOf(graph, n).get(0);
            if (graph.outDegreeOf(prev) > 1) {
                ArrayList<Node> e = new ArrayList<>();
                e.add(n);
                prev.getData().add(e);
            } else {
                processInsertBranchNode(graph, branchNodeStack, n);
            }
        }

        // branch
        if (inDegree == 1 && outDegree >1) {
            if (branchNodeStack.isEmpty()) {
                result.add(n);
            }
            System.out.println("push:" + n);
            branchNodeStack.push(n);
            Node prev = Graphs.predecessorListOf(graph, n).get(0);
            if (graph.outDegreeOf(prev) > 1) {
                ArrayList<Node> e = new ArrayList<>();
                e.add(n);
                prev.getData().add(e);
            }
        }

        //merge
//        if (inDegree > 1 && outDegree ==1) {
        if (inDegree > 1) {
            //todo
            System.out.println("pop:" + n);
            branchNodeStack.pop();
            if (branchNodeStack.isEmpty()) {
                result.add(n);
            } else {
                processInsertBranchNode(graph, branchNodeStack, n);
            }
            if (outDegree > 1) {
                branchNodeStack.push(n);
            }
        }
        //

    }

    private static void processInsertBranchNode(DirectedAcyclicGraph<Node, DefaultEdge> graph, Stack<Node> branchNodeStack, Node n) {
        //get prev branch node
        Node b = null;
        Set<Node> ancestors = graph.getAncestors(n);
        for (Node tmp : branchNodeStack) {
            if (ancestors.contains(tmp)) {
                b = tmp;
            }
        }
        //
        List<Node> list = Graphs.successorListOf(graph, b);
        for (Node node : list) {
            if (ancestors.contains(node)) {
//                        System.out.println("get prev branch of " + n+" is :" + b + ",successor List of " + b + " is:" + list + ", got :" + node);
                for (List<Node> datum : b.getData()) {
                    if (datum.contains(node)) {
                        datum.add(n);
                    }
                }
            }
        }
    }
}
