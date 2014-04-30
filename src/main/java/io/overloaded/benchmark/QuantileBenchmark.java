package io.overloaded.benchmark;

import com.google.caliper.Benchmark;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.tdunning.math.stats.AVLTreeDigest;
import com.tdunning.math.stats.TDigest;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.mahout.math.jet.random.AbstractDistribution;
import org.apache.mahout.math.jet.random.Normal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author mspier
 */
public final class QuantileBenchmark {
    private static enum TDigestFactory {
        AVL_TREE {
            @Override
            TDigest create(double compression) {
                return new AVLTreeDigest(compression);
            }
        };

        abstract TDigest create(double compression);
    }

    private static enum DistributionFactory {
        NORMAL {
            @Override
            AbstractDistribution create(Random random) {
                return new Normal(0.1, 0.1, random);
            }
        };

        abstract AbstractDistribution create(Random random);
    }

    @Param({"0.5","0.9","0.95","0.99"})
    double quantile;

    // @Param({"10", "100", "1000"})
    double compression = 10.0;

    // @Param
    TDigestFactory tdigestFactory = TDigestFactory.AVL_TREE;

    // @Param
    DistributionFactory distributionFactory = DistributionFactory.NORMAL;

    @Param({"10000"})
    int initialArraySize;

    Random random;
    TDigest tdigest;
    AbstractDistribution distribution;
    List<Double> valueList;
    double[] valueArray;

    @BeforeExperiment
    public void setUp() throws Exception {
        random = ThreadLocalRandom.current();
        tdigest = tdigestFactory.create(compression);
        valueList = new ArrayList();
        distribution = distributionFactory.create(random);
        // first values are cheap to add, so pre-fill the t-digest to have more realistic results
        for (int i = 0; i < initialArraySize; ++i) {
            Double value = distribution.nextDouble();
            tdigest.add(value);
            valueList.add(value);
        }
    }

    @Benchmark
    double tdigest(int reps) {
        for (int i = 0; i < reps; ++i) {
            this.tdigest.add(this.distribution.nextDouble());
        }
        return this.tdigest.quantile(this.quantile);
    }

    @Benchmark
    double math(int reps) {
        for (int i = 0; i < reps; ++i) {
            this.valueList.add(this.distribution.nextDouble());
        }
        this.valueArray = new double[this.valueList.size()];
        for(int i = 0; i < this.valueList.size(); i++) valueArray[i] = this.valueList.get(i);

        return StatUtils.percentile(valueArray, this.quantile);
    }
}
