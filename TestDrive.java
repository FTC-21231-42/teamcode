package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.TestMap.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TestDrive extends OpMode {
    @Override
    public void init() {
        initRobot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        rightMotor.setPower(0.7);
        leftMotor.setPower(-0.7);

    }

}
