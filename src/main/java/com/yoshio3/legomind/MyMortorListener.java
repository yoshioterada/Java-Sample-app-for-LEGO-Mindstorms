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

package com.yoshio3.legomind;

import com.yoshio3.legomind.sensorImpl.EV3TouchSensorImpl;
import com.yoshio3.legomind.sensorImpl.EV3UltrasonicSensorImpl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

/**
 *
 * @author Yoshio Terada
 */
public class MyMortorListener implements RegulatedMotorListener {

    EV3UltrasonicSensor urSensor;
    EV3TouchSensor touchSensor;
    ExecutorService execSvc;

    // タッチセンサー／ 超音波センサー共に有効
    public MyMortorListener(EV3UltrasonicSensor urSensor, EV3TouchSensor touchSensor) {
        this.urSensor = urSensor;
        this.touchSensor = touchSensor;
        execSvc = Executors.newFixedThreadPool(2);
    }

    @Override
    public void rotationStarted(RegulatedMotor rm, int i, boolean bln, long l) {
        if (touchSensor != null) {
            execSvc.execute(new EV3TouchSensorImpl(rm, touchSensor));
        }
        if (urSensor != null) {
            execSvc.execute(new EV3UltrasonicSensorImpl(rm, urSensor));
        }
    }

    @Override
    public void rotationStopped(RegulatedMotor rm, int i, boolean bln, long l) {

    }
}
