package io.overloaded.benchmark;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.mahout.math.jet.random.AbstractDistribution;

/**
 * @author mspier
 */
public class ApacheMathQuantileImpl implements IQuantile {
    private static double[] array;

    @Override
    public void init(int size, AbstractDistribution distribution) {
        array = new double[size];

        // initialize value array
        for (int i = 0; i < size; ++i) {
            array[i] = distribution.nextDouble();
        }
    }

    @Override
    public double compute(double quantile) {
        return StatUtils.percentile(array, quantile);
    }
}
