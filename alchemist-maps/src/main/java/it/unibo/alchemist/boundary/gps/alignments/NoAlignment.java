/*
 * Copyright (C) 2010-2023, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.boundary.gps.alignments;

import it.unibo.alchemist.model.Time;
import it.unibo.alchemist.model.maps.GPSTrace;
import it.unibo.alchemist.model.times.DoubleTime;

import java.util.List;

/**
 * No alignment is performed.
 * If you have two traces, the first trace start with time = 2 and second point with time = 5,
 * the second trace starts with time = 4 and second point with time = 6,
 * the result will be:
 * - first trace start with time = 2 and second point with time = 5
 * - second trace starts with time = 4 and second point with time = 6
 */
public final class NoAlignment extends AbstractGPSTimeAlignment {

    private static final SinglePointBehavior POLICY = SinglePointBehavior.RETAIN_SINGLE_POINTS;

    /**
     * Default empty constructor builds a NoAlignment with RETAIN_SINGLE_POINTS
     * behavior for trace with a single point.
     */
    public NoAlignment() {
        super(POLICY);
    }

    @Override
    protected Time computeStartTime(final List<GPSTrace> allTraces, final GPSTrace currentTrace) {
        return new DoubleTime(0.0);
    }
}
