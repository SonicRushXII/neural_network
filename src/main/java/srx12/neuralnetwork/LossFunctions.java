package srx12.neuralnetwork;

import srx12.matrix.Matrix;

public class LossFunctions {
    public static Matrix crossEntropy(Matrix expected, Matrix actual) {
        return actual.apply((index, value) -> -expected.get(index) * Math.log(value)).sumColumns();
    }
}
