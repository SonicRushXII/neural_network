package srx12.neuralnetwork;

/*
INPUT   AND     OR      XOR     NOR     NAND    XNOR
0,0     0       0       0       1       1       1
0,1     0       1       1       0       1       0
1,0     0       1       1       0       1       0
1,1     1       1       0       0       0       1
 */



public class App {

    public static void main(String[] args)
    {
        //Mimicking AND Gate using a Perceptron
        for(int i=0;i<4;++i)
        {
            double x1 = i/2;
            double x2 = i%2;

            Perceptron neuron = new Perceptron(
                    new double[]{x1,x2},
                    new double[]{1,1},
                    -1
            );
            System.out.printf("%d,%d -> %d\n",(int)x1,(int)x2, (int)neuron.runPerceptron(
                    (z) -> {return z>0 ? 1.0 : 0.0;}
            ));
        }
    }
}