package srx12.matrix;

import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixTest {
    private final Random random = new Random();

    @Test
    public void testGetGreatestRowNumber() {
        double[] values = {2,6,2,7,2,6,11,-1,1};
        Matrix m = new Matrix(3,3,i->values[i]);

        Matrix result = m.getGreatestRowNumbers();

        double[] expectedValues = {2,0,1};
        Matrix expected = new Matrix(1,3,i->expectedValues[i]);

        assertEquals(expected,result);
    }

    @Test
    public void testAverageColumn(){
        int rows = 3;
        int cols = 4;

        Matrix m = new Matrix(rows,cols,i->i);

        double averageIndex = (cols-1)/2.0;

        Matrix expected = new Matrix(rows,1);
        expected.modify((row,col,value)->row * cols + averageIndex);

        Matrix result = m.averageColumn();

        assertEquals(expected, result);
    }


    @Test
    public void testTranspose()
    {
        Matrix m = new Matrix(2,3,i->i);
        Matrix result = m.transpose();

        double[] expectedValues = {0,3,1,4,2,5};
        Matrix expected = new Matrix(3,2,i->expectedValues[i]);

        assertEquals(expected,result);
    }

    @Test
    public void testAddIncrement() {
        Matrix m = new Matrix(5,8,i -> random.nextGaussian());

        final int row = 3;
        final int col = 2;
        final double inc = 10;

        Matrix result = m.addIncrement(row,col,inc);

        double incrementedValue = result.get(row,col);
        double originalValue = m.get(row,col);

        assertTrue(Math.abs(incrementedValue-(originalValue+inc))<0.00001);
    }

    @Test
    public void testEquals(){
        Matrix m1 = new Matrix(3,4,i->0.5*(i-6));
        Matrix m2 = new Matrix(3,4,i->0.5*(i-6));
        Matrix m3 = new Matrix(4,3,i->0.5*(i-6));

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
    }

    @Test
    public void testMultiply() {
        Matrix m1 = new Matrix(2,3,(i)->i);
        Matrix m2 = new Matrix(3,2,(i)->i);

        double[] expectedValues = {10,13,28,40};
        Matrix expected = new Matrix(2,2,i->expectedValues[i]);

        Matrix result = m1.multiplyMatrix(m2);

        assertEquals(expected,result);
    }

    @Test
    public void testSumColumns() {
        Matrix m = new Matrix(4,5,i->i);

        Matrix result = m.sumColumns();

        double[] expectedValues = {+30.00000,   +34.00000,   +38.00000,   +42.00000,   +46.00000};
        Matrix expected = new Matrix(1, 5, i->expectedValues[i]);

        assertEquals(expected, result);
    }




    @Test
    public void testMultiplySpeed() {

        int rows = 500;
        int cols = 500;
        int mid = 50;

        Matrix m1 = new Matrix(rows,mid,(i)->i);
        Matrix m2 = new Matrix(mid,cols,(i)->i);

        var start = System.currentTimeMillis();
        m1.multiplyMatrix(m2);
        var end = System.currentTimeMillis();

        System.out.printf("Matrix Multiplication Time Taken: %dms\n",end-start);
    }

    @Test
    public void testMultiplyDouble() {
        double x = 0.5;
        Matrix m = new Matrix(3,4,(i)->0.5*(i-6));
        Matrix expected = new Matrix(3,4,(i)->x*0.5*(i-6));

        Matrix result = m.mutiplyScalar(x);

        assertEquals(result,expected);
        assertTrue(Math.abs(result.get(1) + 1.25000) < 0.0001);
    }

    @Test
    public void toStringTest(){
        Matrix m = new Matrix(3,4,(i)->(i*2));
        String text = m.toString();


        double[] expected = new double[12];
        for(int i=0;i<expected.length;++i)
            expected[i] = i * 2;

        var rows = text.split("\n");
        assertEquals(3, rows.length-1);

        int index = 0;

        for(var row: rows) {
            var values = row.split("\\s+");
            for(var textValue : values) {
                if(textValue.isEmpty())
                    continue;

                var doubleValue = Double.valueOf(textValue);
                assertTrue(Math.abs(doubleValue-expected[index])<0.0001);
                index++;
            }
        }
    }

    @Test
    public void testAddMatrices(){
        Matrix m1 = new Matrix(2,2,i->i);
        Matrix m2 = new Matrix(2,2,i->i*1.5);

        Matrix expected = new Matrix(2,2, i->i*2.5);
        Matrix result = m1.add(m2);

        assertEquals(result,expected);
    }


}