package srx12.matrix;

import java.io.Serial;
import java.io.Serializable;

public class Matrix implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String NUMBER_FORMAT = "%+12.5f";
    private double tolerance = 0.00001;

    private int rows;
    private int cols;

    public interface Producer {
        double produce(int index);
    }
    public interface IndexValueProducer {
        double produce(int index,double value);
    }

    public interface ValueProducer {
        double produce(double value);
    }

    public interface IndexValueConsumer{
        void consume(int index, double value);
    }

    public interface RowColValueConsumer{
        void consume(int row, int col, double value);
    }

    public interface RowColIndexValueConsumer{
        void consume(int row, int col, int index, double value);
    }

    public interface RowColProducer{
        double produce(int row, int col, double value);
    }

    private double[] A; //Single Dimensional Array used to speed up

    //Create Empty Matrix
    public Matrix(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        A = new double[rows*cols];
    }

    //Create Matrix with dummy Values in it
    public Matrix(int rows, int cols, Producer producer)
    {
        this(rows,cols);
        for(int i=0;i<A.length;++i)
            A[i] = producer.produce(i);
    }

    //Create a Matrix from a Double Array
    public Matrix(int rows, int cols, double[] values) {
        this.rows = rows;
        this.cols = cols;

        Matrix tmp = new Matrix(cols,rows);
        tmp.A = values;
        Matrix transposed = tmp.transpose();
        A = transposed.A;
    }

    //Get The Empty Data
    public double[] get() {
        return A;
    }

    public int getRows(){
        return this.rows;
    }

    public int getCols(){
        return this.cols;
    }

    //Applies a given function, (Can have a constant or variable function be used, Depending on value and index)
    public Matrix apply(IndexValueProducer producer)
    {
        Matrix result = new Matrix(rows,cols);
        for(int i=0;i<A.length;++i)
            result.A[i] = producer.produce(i,A[i]);

        return result;
    }

    public Matrix modify(ValueProducer producer){
        for(int i=0; i<A.length; ++i) {
            A[i] = producer.produce(A[i]);
        }

        return this;
    }

    public Matrix modify(IndexValueProducer producer){
        for(int i=0; i<A.length; ++i) {
            A[i] = producer.produce(i,A[i]);
        }

        return this;
    }

    public Matrix modify(RowColProducer producer)
    {
        int i = 0;
        for(int r = 0; r < rows ; ++r)
        {
            for(int c = 0; c < cols ; ++c)
            {
                A[i] = producer.produce(r,c,A[i]);
                ++i;
            }
        }
        return this;
    }


    //Add Two Matrices Together
    public Matrix add(Matrix other)
    {
        if(this.rows != other.rows || this.cols != other.cols)
            return null;

        Matrix result = new Matrix(this.rows,this.cols);
        for(int i=0;i<A.length;++i)
            result.A[i] = this.A[i] + other.A[i];

        return result;
    }

    //Multiply Matrix with a Scalar
    public Matrix mutiplyScalar(double value)
    {
        Matrix result = new Matrix(this.rows,this.cols);
        for(int i=0;i<A.length;++i)
            result.A[i] = this.A[i]*value;

        return result;
    }

    //Multiply with another Matrix
    public Matrix multiplyMatrix(Matrix m)
    {
        assert cols == m.rows : "Cannot Multiply; Wrong No. of Rows vs Cols";

        Matrix result = new Matrix(rows,m.cols);
        for(int r=0;r<result.rows; ++r)
            for(int k=0;k<this.cols;++k)
                for(int c=0;c<result.cols;++c)
                    result.A[r*result.cols+c] += A[r*cols+k] * m.A[c+k*m.cols];

        return result;
    }

    //Sum up all the Columns(used for Softmax)
    public Matrix sumColumns(){
        Matrix result = new Matrix(1,cols);
        int index = 0;

        for(int row=0; row<rows ; row++){
            for(int col=0; col<cols; col++){
                result.A[col] += A[index++];
            }
        }

        return result;
    }

    //Transpose the Matrix
    public Matrix transpose(){
        Matrix result = new Matrix(cols,rows);

        for(int i=0; i< A.length; ++i){
            int row = i/cols;
            int col = i%cols;

            result.A[col*rows + row] = A[i];
        }

        return result;
    }

    public double sum() {
        double sum = 0;

        for(var v: A){
            sum += v;
        }

        return sum;
    }

    //Gets the Row with the Greatest Number(used to test the ANN's prediction)
    public Matrix getGreatestRowNumbers() {
        Matrix result = new Matrix(1,cols);
        double[] greatest = new double[cols];

        for(int i=0;i<cols;++i) {
            greatest[i] = Double.MIN_VALUE;
        }

        forEach((row,col,value)->{
            if(value > greatest[col]){
                greatest[col] = value;
                result.A[col] = row;
            }
        });

        return result;
    }

    //Get Average of all the Columns
    public Matrix averageColumn(){
        Matrix result = new Matrix(rows,1);

        forEach((row,col,index,value)->{
            result.A[row] += value/cols;
        });

        return result;
    }

    //Apply Softmax Activation funciton
    public Matrix softmax(){
        Matrix result = new Matrix(rows,cols,i->Math.exp(A[i]));
        Matrix colSum = result.sumColumns();
        result.modify((row,col,value)->{
            return value/colSum.get(col);
        });

        return result;
    }

    public void forEach(RowColIndexValueConsumer consumer) {

        int index = 0;

        for(int row = 0; row<rows; ++row)
            for(int col = 0; col<cols; ++col)
            {
                consumer.consume(row,col,index,A[index++]);
            }
    }

    public void forEach(RowColValueConsumer consumer) {

        int index = 0;

        for(int row = 0; row<rows; ++row)
            for(int col = 0; col<cols; ++col)
            {
                consumer.consume(row,col,A[index++]);
            }
    }

    public void forEach(IndexValueConsumer consumer) {
        for(int i=0; i<A.length; ++i){
            consumer.consume(i,A[i]);
        }
    }


    public void set(int row, int col, double value){
        A[row*cols+col] = value;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double get(int row, int col){
        return A[row*cols+col];
    }

    //Adds a Increment Amt to the specified cell of the Matrix
    public Matrix addIncrement(int row, int col, double increment){
        Matrix result = apply((index,value)->A[index]);

        double originalValue = get(row,col);
        double newValue = originalValue + increment;

        result.set(row,col,newValue);

        return result;
    }

    public double get(int index)
    {
        return A[index];
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;

        Matrix other = (Matrix) obj;
        if(this.rows != other.rows || this.cols != other.cols)
            return false;

        for(int i=0;i<A.length;++i)
            if(Math.abs(this.A[i]-other.A[i]) > tolerance)
                return false;

        return true;
    }

    //Display Function
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<A.length;++i)
        {
            if (i % cols == 0)  sb.append("\n");
            sb.append(String.format(NUMBER_FORMAT, A[i]));
        }
        return sb.toString();
    }

    public String toString(boolean showValues)
    {
        if(showValues){
            return toString();
        }
        else{
            return rows + "x" + cols;
        }
    }
}