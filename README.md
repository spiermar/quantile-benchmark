quantile-benchmark
==================

Java microbenchmark for quantile implementations. Currently using Apache Math3 and t-Digest with AVL Tree.

Usage
-----

Clone the repository

    git clone https://github.com/spiermar/quantile-benchmark.git

Compile

    mvn compile

Execute the benchmark

    mvn exec:java -Dexec.mainClass=com.google.caliper.runner.CaliperMain -Dexec.args=io.overloaded.benchmark.QuantileBenchmark

Results
-------

[microbenchmarks.appspot.com](https://microbenchmarks.appspot.com/runs/f8b36b5c-e634-4e12-927a-34d38059322d#r:scenario.benchmarkSpec.parameters.size,scenario.benchmarkSpec.parameters.implFactory) - MacBook Pro, Late 2013, 2 GHz Intel Core i7, 16 GB 1600 MHz DDR3, OS X 10.9.2 (13C64)