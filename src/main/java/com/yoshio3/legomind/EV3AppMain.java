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

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import lejos.robotics.RegulatedMotor;

/**
 *
 * @author Yoshio Terada
 */
public class EV3AppMain {

    //タッチセンサー
    private static final EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
    //超音波センサー
    private static final EV3UltrasonicSensor ursensor = new EV3UltrasonicSensor(SensorPort.S4);
    //車輪モータ
    private static final RegulatedMotor leftMotor = Motor.B;
    private static final RegulatedMotor rightMotor = Motor.C;

    public EV3AppMain() {
        leftMotor.resetTachoCount();
        rightMotor.resetTachoCount();
        leftMotor.setSpeed(400);
        rightMotor.setSpeed(400);
        ursensor.enable();
    }

    public static void main(String... argv) {
        EV3AppMain main = new EV3AppMain();
        main.forwardLEGO();
        main.onKeyTouchExit();
    }

    // 車輪テスト
    private void forwardLEGO() {
        //タッチセンサー／超音波 センサー共に有効
        leftMotor.addListener(new MyMortorListener(ursensor, touchSensor));
        rightMotor.addListener(new MyMortorListener(ursensor, touchSensor));

        // 回転(360°×回転数)
        leftMotor.rotateTo(360 * 30, true);
        rightMotor.rotateTo(360 * 30, true);

        leftMotor.waitComplete();
        rightMotor.waitComplete();

    }

    private void onKeyTouchExit() {
        EV3 ev3 = (EV3) BrickFinder.getLocal();
        Keys keys = ev3.getKeys();
        keys.waitForAnyPress();
        ursensor.disable();
        System.exit(0);
    }
}
