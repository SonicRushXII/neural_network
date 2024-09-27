package srx12.neuralnetwork;

 public class TrainingArrays {
    private final double[] input;
    private final double[] output;

    public double[] getInput() {
        return input;
    }

    public double[] getOutput() {
        return output;
    }

    public TrainingArrays(double[] input, double[] output)
    {
        this.input = input;
        this.output = output;
    }

}
