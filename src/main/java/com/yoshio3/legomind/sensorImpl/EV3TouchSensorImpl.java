/*
 * Copyright 2013 Yoshio Terada
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yoshio3.legomind.sensorImpl;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 *
 * @author Yoshio Terada
 */
public class EV3TouchSensorImpl implements Runnable {

    EV3TouchSensor touchSensor;
    RegulatedMotor rm;

    public EV3TouchSensorImpl(RegulatedMotor rm, EV3TouchSensor touchSensor) {
        this.rm = rm;
        this.touchSensor = touchSensor;
    }

    @Override
    public void run() {
        boolean onForward = true;
        SensorMode touchMode = touchSensor.getTouchMode();
        float[] data = new float[touchMode.sampleSize()];

        while (true) {
            touchMode.fetchSample(data, 0);
            for (float res : data) {
                if (res == 1.0f) {
                    rm.stop();
                    //
                    if (onForward) {
                        Button.LEDPattern(3);
                        rm.backward();
                        onForward = false;
                    } else {
                        Button.LEDPattern(4);
                        rm.forward();
                        onForward = true;
                    }
//                    rm.rotateTo(360, true);
                }
            }
            Delay.msDelay(100);
        }
    }
}
