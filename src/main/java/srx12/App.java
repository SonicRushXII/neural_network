package srx12;

import srx12.neuralnetwork.NeuralNetwork;
import srx12.neuralnetwork.Transform;
import srx12.neuralnetwork.loader.Loader;
import srx12.neuralnetwork.loader.MetaData;
import srx12.neuralnetwork.loader.image.ImageLoader;

import java.io.File;

public class App {
    public static void main(String[] args){

        final String filename = "mnistNeural0.net";

        if(args.length == 0){
            System.out.println("Usage: [app] < MNIST DATA DIRECTORY >");
            return;
        }
        String directory = args[0];
        if(!new File(directory).isDirectory()){
            System.out.println("'"+directory+"' is not a directory");
            return;
        }

        final String trainImages = String.format("%s%s%s",directory, File.separator,"train-images.idx3-ubyte");
        final String trainLabels = String.format("%s%s%s",directory, File.separator,"train-labels.idx1-ubyte");
        final String testImages = String.format("%s%s%s",directory, File.separator,"t10k-images.idx3-ubyte");
        final String testLabels = String.format("%s%s%s",directory, File.separator,"t10k-labels.idx1-ubyte");

        Loader trainLoader = new ImageLoader(trainImages, trainLabels, 32);
        Loader testLoader = new ImageLoader(testImages, testLabels, 32);

        MetaData metaData = trainLoader.open();

        int inputSize = metaData.getInputSize();
        int outputSize = metaData.getExpectedSize();

        trainLoader.close();

        NeuralNetwork neuralNetwork = NeuralNetwork.load(filename);
        if(neuralNetwork == null){
            System.out.println("Unable to Load Neural Network from Saved. Creating from Scratch");

            neuralNetwork = new NeuralNetwork();
            neuralNetwork.setScaleInitialWeights(0.2);
            neuralNetwork.setThreads(5);
            neuralNetwork.setEpochs(10);
            neuralNetwork.setLearningRates(0.02,0.001);

            neuralNetwork.add(Transform.DENSE,200,inputSize);
            neuralNetwork.add(Transform.RELU);
            neuralNetwork.add(Transform.DENSE,outputSize);
            neuralNetwork.add(Transform.SOFTMAX);

        }
        else{
            System.out.println("Loaded from "+filename);
        }

        System.out.println(neuralNetwork);

        neuralNetwork.fit(trainLoader,testLoader);

        if(neuralNetwork.save(filename)){
            System.out.println("Saved to "+filename);
        }
        else{
            System.out.println("Unable to save to "+filename);
        }
    }
}
