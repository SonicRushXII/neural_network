package srx12.neuralnetwork;

import srx12.matrix.Matrix;

public class TrainingMatrices {
    private final Matrix input;
    private final Matrix output;

    public Matrix getInput() {
        return input;
    }

    public Matrix getOutput() {
        return output;
    }

    public TrainingMatrices(Matrix input, Matrix output)
    {
        this.input = input;
        this.output = output;
    }

}
