package srx12.neuralnetwork.loader.test;

import org.junit.jupiter.api.Test;
import srx12.matrix.Matrix;
import srx12.neuralnetwork.loader.BatchData;
import srx12.neuralnetwork.loader.Loader;
import srx12.neuralnetwork.loader.MetaData;

import static org.junit.jupiter.api.Assertions.*;

public class TestTestLoader {
    @Test
    void test(){
        int batchSize = 33;

        Loader testLoader = new TestLoader(600,batchSize);
        MetaData metaData = testLoader.open();

        int numberItems = metaData.getNumberItems();
        int lastBatchSize = numberItems%batchSize;

        int numberBatches = metaData.getNumberBatches();;

        for(int i=0;i<numberBatches;i++){
            BatchData batchData = testLoader.readBatch();
            assertNotNull(batchData);

            int itemsRead = metaData.getItemsRead();

            int inputSize = metaData.getInputSize();
            int expectedSize = metaData.getExpectedSize();

            Matrix input = new Matrix(inputSize, itemsRead, batchData.getInputBatch());
            Matrix expected = new Matrix(expectedSize, itemsRead, batchData.getExpectedBatch());

            assertTrue(input.sum() != 0);
            assertEquals(expected.sum(), itemsRead);

            if(i == numberBatches-1)
                assertEquals(itemsRead, lastBatchSize);
            else
                assertEquals(itemsRead, batchSize);
        }
    }
}