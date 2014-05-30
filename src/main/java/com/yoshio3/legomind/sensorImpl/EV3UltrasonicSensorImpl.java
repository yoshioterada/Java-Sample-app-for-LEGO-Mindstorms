/*
 * Copyright 2014 Yoshio Terada
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
import lejos.hardware.lcd.LCD;
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
    boolean isRight;

    int control = 0;

    private final static int SPEED_DOWN = 30; // cm
    private final static int TURN_LEFT = 15; // cm

    private static boolean isTurnFinished = false;

    public EV3UltrasonicSensorImpl(RegulatedMotor motor, EV3UltrasonicSensor ir, boolean isRight) {
        this.ir = ir;
        this.motor = motor;
        this.isRight = isRight;
    }

    @Override
    public void run() {
        SensorMode sonic = ir.getMode(0);
        float value[] = new float[sonic.sampleSize()];
        while (true) {
            sonic.fetchSample(value, 0);
            // for 文でループしているが実際には、
            // sonic.sampleSize()が1を返すので value[0] しかない。
            for (float res : value) {
                double centimeterDouble = (double) res * 100;
                int distance = (int) centimeterDouble;

                if (distance < TURN_LEFT) {
                    Button.LEDPattern(6);
                    //このフラグの実装はちょっと設計がまずい、
                    //当初シングルリスナーで複数のモータを制御しようとした設計をしたため
                    //現在クラス変数で状態を判定し、処理中は片側を停止する実装といる。
                    if (isRight == true) {
                        isTurnFinished = false;
                        motor.rotate(360 + 80); //90度左回転
                        motor.waitComplete();
                        isTurnFinished = true;
                    } else {
                        while (!isTurnFinished) {
                            motor.stop();
                            Delay.msDelay(200);
                        }
                    }
                    motor.forward();

                } else if (distance >= TURN_LEFT && distance < SPEED_DOWN) {
                    Button.LEDPattern(5);
                    motor.setSpeed(100);
                    LCD.clear();
                    LCD.drawString(Integer.toString(distance) + "cm", 0, 1);
                } else if (distance != Integer.MAX_VALUE) {
                    Button.LEDPattern(1);
                    motor.setSpeed(400);
                    LCD.clear();
                    LCD.drawString(Integer.toString(distance) + "cm", 0, 1);
                } else {
                    LCD.drawString("Infinity", 0, 1);
                }
            }
            Delay.msDelay(100);
        }
    }
}
