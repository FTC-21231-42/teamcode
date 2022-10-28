package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TestMap {
    static HardwareMap hwMap;
    static DcMotor rightMotor, leftMotor;

    public static void initRobot(HardwareMap map){
        rightMotor = map.get(DcMotor.class, "rightMotor");
        leftMotor = map.get(DcMotor.class, "leftMotor");



    }
}
