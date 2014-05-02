package io.overloaded.benchmark;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import org.apache.mahout.math.jet.random.AbstractDistribution;
import org.apache.mahout.math.jet.random.Normal;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author mspier
 */
public class DataQuantileBenchmark {

    private static enum ImplFactory {
        T_DIGEST {
            @Override
            IQuantile create() {
                return new TDigestQuantileImpl();
            }
        },
        APACHE_MATH {
            @Override
            IQuantile create() {
                return new ApacheMathQuantileImpl();
            }
        };

        abstract IQuantile create();
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

    @Param
    ImplFactory implFactory;

    @Param
    DistributionFactory distributionFactory;

    @Param({"10000","100000","1000000"})
    int size;

    Random random;
    AbstractDistribution distribution;
    IQuantile implementation;

    @BeforeExperiment
    public void setUp() throws Exception {
        random = ThreadLocalRandom.current();
        distribution = distributionFactory.create(random);
        implementation = implFactory.create();
    }

    @Benchmark
    double dataQuantile(int reps) {
        double res = 0;
        implementation.init(size, distribution);
        for (int i = 0; i < reps; ++i) {
            res = implementation.compute(quantile);
        }
        return res;
    }
}
