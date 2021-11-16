package main;

import java.util.List;

public class Network {

    Neuron root;
    Neuron pointer;

    public Network(List<Object> rootInputs, String rootNeuronType) {
        root = new Neuron(rootInputs, rootNeuronType,null);
        pointer = root;
    }

    public void addNeuron(List<Object> inputs, String neuronType) {
        // The parent is the receiver of whatever expression this neuron will replace...?

    }

    public void printNetwork() {
        // TODO: network printing function
    }

    static class Neuron {
        Neuron parent;
        List<Object> inputs;
        String neuronType;

        public Neuron(List<Object> inputs, String neuronType, Neuron parent) {
            this.parent = parent;
            this.inputs = inputs;
            this.neuronType = neuronType;
        }

        public void setInputs(List<Object> inputs) {
            this.inputs = inputs;
        }

        public Neuron getParent() {
            return parent;
        }

        public String getNeuronType() {
            return neuronType;
        }

        public List<Object> getInputs() {
            return inputs;
        }

    }

}
