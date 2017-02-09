package com.google.android.material.motion.streams.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.material.motion.streams.MotionRuntime;
import com.google.android.material.motion.streams.ReactiveProperty;
import com.google.android.material.motion.streams.interactions.DirectlyManipulable;
import com.google.android.material.motion.streams.interactions.Tossable;
import com.google.android.material.motion.streams.operators.FloatArrayOperators;
import com.google.android.material.motion.streams.properties.ViewProperties;
import com.google.android.material.motion.streams.springs.FloatArrayTypeVectorizer;
import com.google.android.material.motion.streams.springs.MaterialSpring;

public class GesturesActivity extends AppCompatActivity {

  private final MotionRuntime runtime = new MotionRuntime();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gestures_activity);

    final View target = findViewById(R.id.target);
    target.setBackgroundDrawable(new CheckerboardDrawable());

//    DirectlyManipulable directlyManipulable = new DirectlyManipulable();
//    directlyManipulable.draggable.constrain(FloatArrayOperators.lockToYAxis(0f));
//    runtime.addInteraction(directlyManipulable, target);

    runtime.addInteraction(new Tossable(ReactiveProperty.of(new Float[]{500f, 500f})), target);

//    MaterialSpring<View, Float[]> spring = new MaterialSpring<>(
//      ViewProperties.TRANSLATION,
//      new FloatArrayTypeVectorizer(2),
//      ReactiveProperty.of(new Float[]{500f, 500f}),
//      ReactiveProperty.of(target, ViewProperties.TRANSLATION),
//      ReactiveProperty.of(new Float[]{0f, 0f}),
//      ReactiveProperty.of(0.01f),
//      ReactiveProperty.of(1f),
//      ReactiveProperty.of(100f));
//    runtime.addInteraction(spring, target);

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ReactiveProperty<Float[]> initialTranslation =
          ReactiveProperty.of(target, ViewProperties.TRANSLATION);
        Float[] translation = initialTranslation.read();

        translation[0] /= 2f;
        translation[1] /= 2f;

        initialTranslation.write(translation);
      }
    });
  }
}
