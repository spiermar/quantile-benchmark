package io.overloaded.benchmark;

import com.google.caliper.Benchmark;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.tdunning.math.stats.AVLTreeDigest;
import com.tdunning.math.stats.TDigest;
import org.apache.mahout.math.jet.random.AbstractDistribution;
import org.apache.mahout.math.jet.random.Normal;

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

    @Param({"10", "100", "1000"})
    double compression;

    @Param
    TDigestFactory tdigestFactory;

    @Param
    DistributionFactory distributionFactory;

    Random random;
    TDigest tdigest;
    AbstractDistribution distribution;

    @BeforeExperiment
    public void setUp() throws Exception {
        random = ThreadLocalRandom.current();
        tdigest = tdigestFactory.create(compression);
        distribution = distributionFactory.create(random);
        // first values are cheap to add, so pre-fill the t-digest to have more realistic results
        for (int i = 0; i < 10000; ++i) {
            tdigest.add(distribution.nextDouble());
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
    double apachemath(int reps) {

        //TODO: implement apache math benchmark

        return 0.0;
    }
}
