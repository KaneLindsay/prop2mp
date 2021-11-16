package main;

import java.util.ArrayList;
import java.util.List;

public class Network {

    Neuron root;
    Neuron pointer; // Points to the last added neuron

    // No constructor currently

    public Neuron getRoot() {
        return root;
    }

    public void addNeuron(ArrayList<Object> inputs, String neuronType) {

        // If the network is empty, add the neuron as the root.
        if (root == null) {
            root = new Neuron(inputs, neuronType,null);
            pointer = root;
            return;
        }

        // The parent is the receiver of whatever expression this neuron will replace...?
        for (int i = 0; i < pointer.getInputs().size(); i++) {
            if (pointer.getInputs().get(i) instanceof String) {
                // Check if the string is an un-atomised expression
                if (((String) pointer.getInputs().get(i)).contains("!")
                && ((String) pointer.getInputs().get(i)).contains("||")
                && ((String) pointer.getInputs().get(i)).contains("&&")) {
                    // Replace the un-atomised expression with a new neuron, make new neuron the pointer
                    Neuron newNeuron = new Neuron(inputs, neuronType, pointer);
                    pointer.getInputs().set(i, newNeuron);
                    pointer = newNeuron;
                }
            }
        }
    }

    public void printNetwork(Neuron currentNeuron) {
        // TODO: Network printing method
    }

    static class Neuron {
        Neuron parent;
        ArrayList<Object> inputs;
        String neuronType;

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

        public String getNeuronType() {
            return neuronType;
        }

        public ArrayList<Object> getInputs() {
            return inputs;
        }

    }

}
