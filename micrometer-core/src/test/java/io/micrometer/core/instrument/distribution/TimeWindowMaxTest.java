/**
 * Copyright 2021 VMware, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.core.instrument.distribution;

import io.micrometer.core.instrument.MockClock;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TimeWindowMax}
 */
class TimeWindowMaxTest {

    MockClock clock = new MockClock();
    TimeWindowMax timeWindowMax;

    @Test
    void decaysToZero() {
        int bufferLength = 3;
        long rotateFrequencyMillis = Duration.ofMinutes(1).toMillis();
        timeWindowMax = new TimeWindowMax(clock, rotateFrequencyMillis, bufferLength);
        timeWindowMax.record(100);

        for (int i = 0; i < bufferLength; i++) {
            assertThat(timeWindowMax.poll()).isEqualTo(100);
            clock.add(rotateFrequencyMillis, TimeUnit.MILLISECONDS);
        }

        assertThat(timeWindowMax.poll()).isZero();
    }
}