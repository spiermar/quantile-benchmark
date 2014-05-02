package io.overloaded.benchmark;

import org.apache.mahout.math.jet.random.AbstractDistribution;

/**
 * @author mspier
 */
public interface IQuantile {
    public void init(int size, AbstractDistribution distribution);
    public double compute(double quantile);
}
