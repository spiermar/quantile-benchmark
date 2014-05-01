package io.overloaded.benchmark;

import com.tdunning.math.stats.AVLTreeDigest;
import com.tdunning.math.stats.TDigest;
import org.apache.mahout.math.jet.random.AbstractDistribution;

/**
 * @author mspier
 */
public class TDigestQuantileImpl implements IQuantile {
    private static final int compression = 10; // defaulting compression to 10
    private static TDigest tdigest;

    public TDigestQuantileImpl(int size, AbstractDistribution distribution) {
        this.tdigest = new AVLTreeDigest(compression); // using AVL Tree as data structure

        // initialize tdigest tree
        for (int i = 0; i < size; ++i) {
            this.tdigest.add(distribution.nextDouble());
        }
    }

    @Override
    public double compute(double quantile) {
        return tdigest.quantile(quantile);
    }
}
