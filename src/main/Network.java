package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Network {

    Neuron root;
    Neuron pointer; // Points to the last added neuron
    Stack<Neuron> incompleteNeurons = new Stack<>();

    // No constructor currently

    public Neuron getRoot() {
        return root;
    }

    public void addNeuron(ArrayList<Object> inputs, String neuronType) {

        // If the network is empty, add the neuron as the root.
        if (root == null) {
            root = new Neuron(inputs, neuronType,null);
            pointer = root;
            incompleteNeurons.push(root);
            return;
        }

        // Find if there are more than 1 un-atomised expression in the neuron's inputs.
        int expressionCounter = 0;
        for (int i = 0; i < pointer.getInputs().size(); i++) {
            if (pointer.getInputs().get(i) instanceof String) {
                // Check if the string is an un-atomised expression
                if (((String) pointer.getInputs().get(i)).contains("!")
                        || ((String) pointer.getInputs().get(i)).contains("||")
                        || ((String) pointer.getInputs().get(i)).contains("&&")) {
                    expressionCounter++;
                }
            }
        }

        if (expressionCounter > 1) {
            incompleteNeurons.push(pointer);
        } else if (expressionCounter == 0) {
            pointer = incompleteNeurons.pop();
        }

        for (int i = 0; i < pointer.getInputs().size(); i++) {
            if (pointer.getInputs().get(i) instanceof String) {
                // Check if the string is an un-atomised expression
                if (((String) pointer.getInputs().get(i)).contains("!")
                        || ((String) pointer.getInputs().get(i)).contains("||")
                        || ((String) pointer.getInputs().get(i)).contains("&&")) {
                    // Replace the un-atomised expression with a new neuron, make new neuron the pointer
                    Neuron newNeuron = new Neuron(inputs, neuronType, pointer);

                    ArrayList<Object> pointerInputs = pointer.getInputs();
                    pointerInputs.set(i, newNeuron);
                    pointer.setInputs(pointerInputs);
                    pointer = newNeuron;
                    break;
                }
            }
        }
    }

    public void testNetwork(Neuron currentNeuron) {

        System.out.println("Current Neuron: " + currentNeuron);
        ArrayList<Object> inputs = currentNeuron.getInputs();
        System.out.println("Inputs: " + inputs);
        System.out.println("Neuron Type: " + currentNeuron.getNeuronType());
        System.out.println("----------");

        for (Object input : inputs) {
            if (input instanceof Neuron) {

                testNetwork((Neuron) input);
            }
        }
    }

    public void printNetwork(Neuron neuron) {
        Stack<Neuron> stack = new Stack<>();
        System.out.println("|\n|");
        stack.push(neuron);

        String indent = "";

        while (!stack.isEmpty()) {

            neuron = stack.pop();
            System.out.println(indent + "*" + neuron.getThreshold());

            ArrayList<String> atomInputs = new ArrayList<>();
            ArrayList<Neuron> neuronInputs = new ArrayList<>();

            for (int i = 0; i < neuron.getInputs().size(); i++) {
                if (neuron.getInputs().get(i) instanceof Neuron) {
                    neuronInputs.add((Neuron) neuron.getInputs().get(i));
                } else {
                    atomInputs.add((String) neuron.getInputs().get(i));
                }
            }

            for (int i = 0; i < atomInputs.size(); i++) {
                System.out.println(indent + "|__" + neuron.getWeight() + "__" + atomInputs.get(i));
            }

            for (int i = 0; i < neuronInputs.size(); i++) {
                System.out.println(indent + "|__" + neuron.getWeight() + "__");
            }

            indent = indent + "      ";

            for (int i = 0; i < neuron.getInputs().size(); i++) {
                if (neuron.getInputs().get(i) instanceof Neuron) {
                    stack.push((Neuron) neuron.getInputs().get(i));
                }
            }
        }
    }

    public void optimiseNetwork(Neuron rootNeuron) {

        Stack<Neuron> stack = new Stack<>();
        stack.push(rootNeuron);
        boolean optimisedFlag = true;

        while (!(stack.isEmpty())) {
            Neuron currentNeuron = stack.pop();

            String currNeuronType = currentNeuron.getNeuronType();
            ArrayList<Object> inputs = currentNeuron.getInputs();

            // For all inputs of neuron
            for (int i = 0; i < inputs.size(); i++) {
                // This will get the first neuron input with the same type as the current neuron.
                if (inputs.get(i) instanceof Neuron && ((Neuron) inputs.get(i)).getNeuronType().equals(currNeuronType)){

                    // Merge the inputs of the child neuron & the current neuron, excluding the child neuron itself.
                    ArrayList<Object> childInputs = ((Neuron) inputs.get(i)).getInputs();
                    inputs.remove(i);
                    ArrayList<Object> mergedInputs = new ArrayList<>();
                    mergedInputs.addAll(inputs);
                    mergedInputs.addAll(childInputs);
                    // Set the inputs of the current neuron as the merged inputs.
                    currentNeuron.setInputs(mergedInputs);

                    // If the child node had neuron inputs, set their parent as the current node instead.
                    for (Object childInput : childInputs) {
                        if (childInput instanceof Neuron) {
                            ((Neuron) childInput).setParent(currentNeuron);
                        }
                    }
                    optimisedFlag = false;
                    break;
                }
            }
            for (int k = inputs.size()-1; k >= 0; k--) {
                if (inputs.get(k) instanceof Neuron) {
                    stack.push((Neuron) inputs.get(k));
                }
            }
            if (!optimisedFlag) { optimiseNetwork(rootNeuron); }
        }
    }

    static class Neuron {
        private Neuron parent;
        private ArrayList<Object> inputs;
        private String neuronType;

        public Neuron(ArrayList<Object> inputs, String neuronType, Neuron parent) {
            this.parent = parent;
            this.inputs = inputs;
            this.neuronType = neuronType;
        }

        public void setInputs(ArrayList<Object> inputs) {
            this.inputs = inputs;
        }

        public Neuron getParent() {
            return parent;
        }

        public void setParent(Neuron parent) {
            this.parent = parent;
        }

        public String getNeuronType() {
            return neuronType;
        }

        public ArrayList<Object> getInputs() {
            return inputs;
        }

        public int getWeight() {
            switch (getNeuronType()) {
                case "OR":
                case "AND":
                    return (1);
                case "LOGICAL_COMPLEMENT":
                    return (-1);
                default:
                    return 0;
            }
        }

        public String getThreshold(){
            switch (getNeuronType()) {
                case "OR":
                    return ("1");
                case "LOGICAL_COMPLEMENT":
                    return ("0");
                case "AND":
                    return String.valueOf(getInputs().size());
                default:
                    return "";
            }
        }
    }
}
