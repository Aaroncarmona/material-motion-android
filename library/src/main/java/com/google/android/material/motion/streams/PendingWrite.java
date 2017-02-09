/*
 * Copyright 2017-present The Material Motion Authors. All Rights Reserved.
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

public class PendingWrite<T> {

  public final MotionRuntime runtime;
  public final MotionObservable<T> observable;
  public final ReactiveWritable<T> property;

  public PendingWrite(MotionRuntime runtime, MotionObservable<T> observable, ReactiveWritable<T> property) {
    this.runtime = runtime;
    this.observable = observable;
    this.property = property;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PendingWrite<?> that = (PendingWrite<?>) o;

    if (runtime != null ? !runtime.equals(that.runtime) : that.runtime != null) return false;
    if (observable != null ? !observable.equals(that.observable) : that.observable != null)
      return false;
    return property != null ? property.equals(that.property) : that.property == null;

  }

  @Override
  public int hashCode() {
    int result = runtime != null ? runtime.hashCode() : 0;
    result = 31 * result + (observable != null ? observable.hashCode() : 0);
    result = 31 * result + (property != null ? property.hashCode() : 0);
    return result;
  }
}
