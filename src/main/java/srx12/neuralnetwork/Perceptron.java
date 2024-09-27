package srx12.neuralnetwork;

@FunctionalInterface
interface Lambda {
    double apply(double z);
}

public class Perceptron {
    private final double[] x;
    private final double[] w;
    private final double b;

    Perceptron(double[] inputs, double[] weights, double bias)
    {
        this.x = inputs;
        this.w = weights;
        this.b = bias;
    }

    public double runPerceptron(Lambda activationFunction) {
        double sum = 0.0;
        for(int i=0;i<x.length;++i)
            sum += x[i]*w[i];
        sum += b;

        return activationFunction.apply(sum);
    }
}
