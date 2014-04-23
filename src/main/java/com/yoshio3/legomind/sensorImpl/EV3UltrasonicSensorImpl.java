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
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 *
 * @author Yoshio Terada
 */
public class EV3UltrasonicSensorImpl implements Runnable {

    EV3UltrasonicSensor ir;
    RegulatedMotor motor;

    int control = 0;
    
    private final static int DISTANCE = 20; // cm

    public EV3UltrasonicSensorImpl(RegulatedMotor motor, EV3UltrasonicSensor ir) {
        this.ir = ir;
        this.motor = motor;
    }

    @Override
    public void run() {
        SensorMode sonic = ir.getMode(0);
        float value[] = new float[sonic.sampleSize()];
        while (true) { 
            sonic.fetchSample(value, 0);
            for (float res : value) {       
                if(res < DISTANCE){
                    Button.LEDPattern(1);
                    motor.setSpeed(100);
                }else{
                    Button.LEDPattern(2);
                    motor.setSpeed(400);
                }
            }
            Delay.msDelay(100);
        }
    }
}
