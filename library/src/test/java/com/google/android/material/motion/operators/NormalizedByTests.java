/*
 * Copyright 2016-present The Material Motion Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.material.motion.operators;

import com.google.android.material.motion.ReactiveProperty;
import com.google.android.material.motion.gestures.BuildConfig;
import com.google.android.material.motion.testing.TrackingMotionObserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class NormalizedByTests {
  private static final float E = 0.0001f;

  @Test
  public void testCGFloatByCGFloat() {
    TrackingMotionObserver<Float> tracker = new TrackingMotionObserver<>();
    ReactiveProperty<Float> property = ReactiveProperty.of(0f);
    property.getStream().compose(NormalizedBy.normalizedBy(100))
      .subscribe(tracker);

    float[] input = {10f, 150f, -10f};
    for (float i : input) {
      property.write(i);
    }

    float[] expected = {0f, 0.1f, 1.5f, -0.1f};
    for(int i = 0; i < tracker.values.size(); i++)
      assertThat(tracker.values.get(i)).isWithin(E).of(expected[i]);
  }
}
