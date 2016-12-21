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
package com.google.android.material.motion.streams;

import android.app.Activity;
import android.view.View;

import com.google.android.material.motion.streams.testing.SimulatedMotionSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.android.material.motion.streams.MotionObservable.ACTIVE;
import static com.google.android.material.motion.streams.MotionObservable.AT_REST;
import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MotionAggregatorTests {

  private static final float E = 0.0001f;

  private MotionAggregator aggregator;

  @Before
  public void setUp() {
    aggregator = new MotionAggregator();
  }

  @Test
  public void defaultStateAtRest() {
    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
  }

  @Test
  public void writesCorrectValueToScopedProperty() {
    SimulatedMotionSource<Float> source = new SimulatedMotionSource<>();
    MotionObservable<Float> stream = source.getObservable();

    aggregator.write(stream, new MotionObservable.ScopedWritable<Float>() {
      @Override
      public void write(Float value) {
        assertThat(value).isWithin(E).of(5f);
      }
    });
    source.next(5f);
  }

  @Test
  public void writesCorrectValueToUnscopedProperty() {
    SimulatedMotionSource<Float> source = new SimulatedMotionSource<>();
    MotionObservable<Float> stream = source.getObservable();

    View target = new View(Robolectric.setupActivity(Activity.class));

    aggregator.write(stream, target, View.TRANSLATION_X);
    source.next(5f);
    assertThat(target.getTranslationX()).isWithin(E).of(5f);
  }

  @Test
  public void activeIfObserverActive() {
    SimulatedMotionSource<Float> source = new SimulatedMotionSource<>();
    MotionObservable<Float> stream = source.getObservable();

    aggregator.write(stream, new MotionObservable.ScopedWritable<Float>() {
      @Override
      public void write(Float value) {
      }
    });

    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
    source.state(MotionObservable.ACTIVE);
    assertThat(aggregator.getAggregateState()).isEqualTo(ACTIVE);
  }

  @Test
  public void atRestIfObserverAtRest() {
    SimulatedMotionSource<Float> source = new SimulatedMotionSource<>();
    MotionObservable<Float> stream = source.getObservable();

    aggregator.write(stream, null, null);

    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
    source.state(MotionObservable.ACTIVE);
    assertThat(aggregator.getAggregateState()).isEqualTo(ACTIVE);
    source.state(MotionObservable.AT_REST);
    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
  }

  @Test
  public void observerCanPassActiveTwice() {
    SimulatedMotionSource<Float> source = new SimulatedMotionSource<>();
    MotionObservable<Float> stream = source.getObservable();

    aggregator.write(stream, null, null);

    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
    source.state(MotionObservable.ACTIVE);
    source.state(MotionObservable.ACTIVE);
    assertThat(aggregator.getAggregateState()).isEqualTo(ACTIVE);
    source.state(MotionObservable.AT_REST);
    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
  }

  @Test
  public void observerCanPassAtRestTwice() {
    SimulatedMotionSource<Float> source = new SimulatedMotionSource<>();
    MotionObservable<Float> stream = source.getObservable();

    aggregator.write(stream, null, null);

    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
    source.state(MotionObservable.ACTIVE);
    assertThat(aggregator.getAggregateState()).isEqualTo(ACTIVE);
    source.state(MotionObservable.AT_REST);
    source.state(MotionObservable.AT_REST);
    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
  }

  @Test
  public void activeIfAtLeastOneObserverIsActive() {
    SimulatedMotionSource<Float> source1 = new SimulatedMotionSource<>();
    SimulatedMotionSource<Float> source2 = new SimulatedMotionSource<>();

    aggregator.write(source1.getObservable(), null, null);
    aggregator.write(source2.getObservable(), null, null);

    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);

    source1.state(MotionObservable.ACTIVE);
    assertThat(aggregator.getAggregateState()).isEqualTo(ACTIVE);
    source2.state(MotionObservable.ACTIVE);
    assertThat(aggregator.getAggregateState()).isEqualTo(ACTIVE);

    source1.state(MotionObservable.AT_REST);
    assertThat(aggregator.getAggregateState()).isEqualTo(ACTIVE);
    source2.state(MotionObservable.AT_REST);
    assertThat(aggregator.getAggregateState()).isEqualTo(AT_REST);
  }
}