package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TestMap {
    static HardwareMap hwMap;
    static DcMotor testMotor;

    public static void initRobot(HardwareMap map){
        testMotor = map.get(DcMotor.class, "testMotor");

    }
}
