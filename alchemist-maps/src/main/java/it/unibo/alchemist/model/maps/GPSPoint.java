/*
 * Copyright (C) 2010-2023, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.model.maps;

import it.unibo.alchemist.model.GeoPosition;
import it.unibo.alchemist.model.Time;

/**
 */
public interface GPSPoint extends GeoPosition, Comparable<GPSPoint> {

    /**
     * @return the time
     */
    Time getTime();

    /**
     * @param t time to subtract to the point's time
     * @return new GPSPoint with the new time
     */
    GPSPoint subtractTime(Time t);

    /**
     * @param t time to add to the point's time
     * @return new GPSPoint with the new time
     */
    GPSPoint addTime(Time t);
}
