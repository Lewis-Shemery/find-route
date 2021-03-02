/**
 *
 * @author Lewis Shemery
 * 1001402131
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;


public class find_route {

    public static Map<String, Double> input;
    public static ArrayList<Node> fringe;
    public static ArrayList<String> closedSet;
    public static Node startNode;
    public static String goalNode;
    public static int nodesExpanded = 0;
    public static int nodesGenerated = 0;

    public static void main(String[] args) {
        String fileName = args[0];
        startNode = new Node(args[1],null,0,0.0);
        nodesGenerated++;
        goalNode = args[2];
        input = new LinkedHashMap<String,Double>();
        parseFile(fileName);
        fringe = new ArrayList<Node>();
        fringe.add(startNode);
        closedSet = new ArrayList<String>();
        Node goalNode = dequeueFringe();

        if (goalNode==null) {
            System.out.println("nodes expanded: " + nodesExpanded);
            System.out.println("nodes generated: " + nodesGenerated);
            System.out.println("distance: infinty\nroute:\n none");
        }
        else {
            buildPath(goalNode);
        }
    }

    public static Node dequeueFringe() {
        Node fringeNode,goalNode;
        goalNode = null;

        while(fringe.isEmpty()==false) {
            nodesExpanded++;
            fringeNode = fringe.remove(0);
            if((fringeNode.getCity()).equals(find_route.goalNode)){
                goalNode = fringeNode;
                break;
            }
            else {
                // if either the closedSet is empty or it doesn't contain the current city 
                // get all the successors of the current node and add them to the fringe.
                // add the current city to the closedSet
                if(closedSet.isEmpty() || !(closedSet.contains((fringeNode.getCity())))) {
                    fringe.addAll(getSuccessor(fringeNode));
                    closedSet.add((fringeNode.getCity()));
                }
                Collections.sort(fringe); // uses the overidden compare method below
            }
        }
        return goalNode;
    }

    public static ArrayList<Node> getSuccessor(Node parentNode){
        ArrayList<Node> successors = new ArrayList<Node>();
        for(String path:input.keySet()) {
            Node tempNode;
            StringTokenizer pathString = new StringTokenizer(path, " ");
            String city1 = pathString.nextToken();
            String city2 = pathString.nextToken();
            if((city1.equals(parentNode.getCity())) ){
                nodesGenerated++;
                tempNode = new Node(city2,parentNode,parentNode.getDepth()+1,parentNode.getPathCost()+input.get(path));
                successors.add(tempNode);
            }
            else if(city2.equals(parentNode.getCity())) {
                nodesGenerated++;
                tempNode = new Node(city1,parentNode,parentNode.getDepth()+1,parentNode.getPathCost()+input.get(path));
                successors.add(tempNode);
            }
        }
        return successors;
    }

    // uses the goalNode's parent nodes to build the final path 
    public static void buildPath(Node goalNode) {
        Node currentNode;
        double totalDistance = goalNode.getPathCost();
        ArrayList<String>outputDisplay = new ArrayList<>();
        currentNode=goalNode;
        
        while(currentNode.getParent()!=null) {
            String outputString = currentNode.getParent().getCity()+ " to "+currentNode.getCity();
            double distance = currentNode.getPathCost()-(currentNode.getParent().getPathCost());
            outputString+=" "+distance;
            outputDisplay.add(0, outputString);
            currentNode = currentNode.getParent();
        }
        System.out.println("nodes expanded: " + nodesExpanded);
        System.out.println("nodes generated: " + nodesGenerated);
        System.out.println("distance: "+totalDistance+" km"+"\n"+"route:");
        outputDisplay.forEach(outputLine -> {
            System.out.println(outputLine+" km");
        });
    }

    // parsing the map data into a Linked Hash Map
    public static void parseFile(String fileName) {
        try {
            Scanner ioFile = new Scanner(new File(fileName));
            String ioFileData;
            
            while(!(ioFileData=ioFile.nextLine()).equals("END OF INPUT")) {
                StringTokenizer fileStringToken = new StringTokenizer(ioFileData, " ");
                String key = fileStringToken.nextToken()+" "+fileStringToken.nextToken();
                double value = (double)Integer.parseInt(fileStringToken.nextToken());
                input.put(key, value);
            }
            ioFile.close();
        }
        catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("File read error.");
        }
    }
}

class Node implements Comparable<Node>{

    private final String city;
    private final Node parent;
    private final Integer depth;
    private final double pathCost;

    public Node(String city,Node parent,Integer depth,double pathCost){
        this.city = city;
        this.parent = parent;
        this.depth = depth;
        this.pathCost = pathCost;
    }

    public String getCity() {
        return city;
    }

    public Node getParent() {
        return parent;
    }

    public Integer getDepth() {
        return depth;
    }

    public double getPathCost() {
        return pathCost;
    }
    
    // defining the abstract compareTo method in the Comparable interface to allow for sorting
    @Override
    public int compareTo(Node node) {
        double tempCost=node.pathCost;
        if (this.pathCost > tempCost) {
            return 1;
        } else if (this.pathCost == tempCost) {
            return 0;
        } else {
            return -1;
        }
    }
}
