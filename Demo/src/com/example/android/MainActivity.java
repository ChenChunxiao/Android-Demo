/* Copyright (C) 2012 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.example.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.android.clock.TestAnalogClock;
import com.example.android.customview.TestCanvas;

public class MainActivity extends Activity implements OnClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {
        case R.id.customview:
            it = new Intent(getBaseContext(), TestCanvas.class);
            break;
        case R.id.clock:
            it = new Intent(getBaseContext(), TestAnalogClock.class);
            break;

        default:
            break;
        }
        if (null != it) {
            startActivity(it);
        }
    }

}
