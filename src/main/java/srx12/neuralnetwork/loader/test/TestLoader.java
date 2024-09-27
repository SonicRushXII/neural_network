package srx12.neuralnetwork.loader.test;

import srx12.neuralnetwork.Util;
import srx12.neuralnetwork.loader.BatchData;
import srx12.neuralnetwork.loader.Loader;
import srx12.neuralnetwork.loader.MetaData;

public class TestLoader implements Loader {
    private MetaData metaData;

    private int numberItems;
    private int inputSize = 500;
    private int expectedSize = 3;

    private int numberBatches;
    private int batchSize;

    private int totalItemsRead;
    private int itemsRead;

    public TestLoader(int numberItems, int batchSize){

        this.numberItems = numberItems;
        this.batchSize = batchSize;

        metaData = new TestMetaData();
        metaData.setNumberItems(numberItems);

        numberBatches = numberItems/batchSize;
        if(numberItems%batchSize != 0)
            numberBatches += 1;

        metaData.setNumberBatches(numberBatches);
        metaData.setInputSize(inputSize);
        metaData.setExpectedSize(expectedSize);
    }

    @Override
    public MetaData open() {
        return metaData;
    }

    @Override
    public void close() {
        totalItemsRead = 0;
    }

    @Override
    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public synchronized BatchData readBatch() {
        //Check if full
        if(totalItemsRead == numberItems){
            return null;
        }

        itemsRead = batchSize;
        totalItemsRead += itemsRead;

        int excessItems = totalItemsRead - numberItems;
        if(excessItems > 0){
            totalItemsRead -= excessItems;
            itemsRead -= excessItems;
        }

        //Generate Data
        var io = Util.generateTrainingArrays(inputSize,expectedSize,itemsRead);
        var batchData = new TestBatchData();
        batchData.setInputBatch(io.getInput());
        batchData.setExpectedBatch(io.getOutput());

        //Record it in Meta Data.
        metaData.setTotalItemsRead(totalItemsRead);
        metaData.setItemsRead(itemsRead);

        return batchData;
    }
}
