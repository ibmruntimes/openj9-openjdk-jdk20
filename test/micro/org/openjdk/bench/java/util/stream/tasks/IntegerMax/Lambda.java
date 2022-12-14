/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.bench.java.util.stream.tasks.IntegerMax;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Bulk scenario: search for max value in array.
 *
 * This test covers lambda-specific solutions for the problem.
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 4, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 4, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class Lambda {

    private IntegerMaxProblem problem;

    @Setup(Level.Trial)
    public void populateData(){
        problem = new IntegerMaxProblem();
    }

    public static Integer maxInt(Integer l, Integer r) {
        return l > r ? l : r;
    }

    @Benchmark
    public int bulk_seq_lambda() {
        return Arrays.stream(problem.get()).reduce(Integer.MIN_VALUE, (l, r) -> l > r ? l : r);
    }

    @Benchmark
    public int bulk_seq_methodRef() {
        return Arrays.stream(problem.get()).reduce(Integer.MIN_VALUE, Lambda::maxInt);
    }

    @Benchmark
    public int bulk_par_lambda() {
        return Arrays.stream(problem.get()).parallel().reduce(Integer.MIN_VALUE, (l, r) -> l > r ? l : r);
    }

    @Benchmark
    public int bulk_par_methodRef() {
        return Arrays.stream(problem.get()).parallel().reduce(Integer.MIN_VALUE, Lambda::maxInt);
    }

}
