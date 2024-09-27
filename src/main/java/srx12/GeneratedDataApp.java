package srx12;

import srx12.neuralnetwork.NeuralNetwork;
import srx12.neuralnetwork.Transform;
import srx12.neuralnetwork.loader.Loader;
import srx12.neuralnetwork.loader.test.TestLoader;

public class GeneratedDataApp
{
    public static void main(String[] args){

        String filename = "neural1.net";
        NeuralNetwork neuralNetwork = NeuralNetwork.load(filename);
        if(neuralNetwork == null){
            System.out.println("Unable to Load Neural Network from Saved. Creating from Scratch");

            int inputRows = 10;
            int outputRows = 3;

            neuralNetwork = new NeuralNetwork();
            neuralNetwork.add(Transform.DENSE,100,inputRows);
            neuralNetwork.add(Transform.RELU);
            neuralNetwork.add(Transform.DENSE,50,inputRows);
            neuralNetwork.add(Transform.RELU);
            neuralNetwork.add(Transform.DENSE,outputRows);
            neuralNetwork.add(Transform.SOFTMAX);

            neuralNetwork.setThreads(5);
            neuralNetwork.setEpochs(30);
            neuralNetwork.setLearningRates(0.02,0.001);
        }
        else{
            System.out.println("Loaded from "+filename);
        }

        System.out.println(neuralNetwork);

        Loader trainLoader = new TestLoader(60_000, 32);
        Loader testLoader = new TestLoader(10_000,32);

        neuralNetwork.fit(trainLoader,testLoader);

        if(neuralNetwork.save(filename)){
            System.out.println("Saved to "+filename);
        }
        else{
            System.out.println("Unable to save to "+filename);
        }
    }
}
