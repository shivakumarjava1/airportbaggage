package com.ab;

import java.util.List;
import java.util.Map;

import com.ab.business.ConveyorGraph;
import com.ab.constants.Gate;
import com.ab.input.BagHandler;
import com.ab.input.ConveyorGraphHandler;
import com.ab.input.FlightDepartureHandler;
import com.ab.pojos.Bag;
import com.ab.pojos.ConveyorNode;
import com.ab.pojos.FlightDeparture;


/**
 * @author shiva
 *
 */
public class App 
{
    public static void main( String[] args )
    {
/*          
    	Setp 1: declare 3 sections
    	Step 2: Read the input by sections
    	Step 3: logic + print the output
    	*/
    	
       ConveyorGraph conveyorGraph = null;
        Map<String, FlightDeparture> flightIdToDepartureMap = null;
        Map<String, Bag> bagIdToBagMap = null;

        ConveyorGraphHandler conveyorGraphHandler = new ConveyorGraphHandler();
        try {
            conveyorGraphHandler.process();
            conveyorGraph = conveyorGraphHandler.getConveyorGraph();          
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        FlightDepartureHandler flightDepartureHandler = new FlightDepartureHandler();
        try {
            flightDepartureHandler.process();
            flightIdToDepartureMap = flightDepartureHandler.getFlightIdToDepartureMap();           
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        BagHandler bagHandler = new BagHandler();
        try {
            bagHandler.process();
            bagIdToBagMap = bagHandler.getBagIdToBagMap();           
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StringBuffer output = new StringBuffer();

        for (Map.Entry<String, Bag> entry : bagIdToBagMap.entrySet()) {
            Bag bag = entry.getValue();
            String bagId = bag.getId();
            String flightId = bag.getFlightId();
            Gate sourceGate = bag.getEntryPoint();

            output.append(bagId + " ");


            Gate departureGate = null;
            if (flightId.equals("ARRIVAL")) {
                departureGate = sourceGate.BAGGAGE_CLAIM;
            } else {
                departureGate = flightIdToDepartureMap.get(flightId).getDepartureGate();
            }

            ConveyorNode sourceNode = new ConveyorNode(sourceGate, sourceGate.getValue());
            ConveyorNode targetNode = new ConveyorNode(departureGate, departureGate.getValue());
            List<ConveyorNode> shortestPath = conveyorGraph.findShortestPath(sourceNode, targetNode);

            if (!shortestPath.isEmpty()) {
                output.append(sourceGate.getValue() + " ");
                ConveyorNode prevNode = shortestPath.get(0);
                output.append(prevNode.getNodeId().getValue() + " ");

                for (int i = 1; i < shortestPath.size(); i++) {
                    ConveyorNode current = shortestPath.get(i);
                    prevNode = current;
                    output.append(current.getNodeId().getValue() + " ");
                }
                output.append(": " + prevNode.getMinDistance());
                output.append(System.lineSeparator());
            } else { //PATH NOT FOUND
                output.append("PATH_NOT_EXISTS");
                output.append(System.lineSeparator());
            }
        }

        System.out.println(output.toString());
    
    }
}
