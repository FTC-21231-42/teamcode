package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.TestMap.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestHub")
public class TestDrive extends OpMode {
    @Override
    public void init() {
        initRobot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        testMotor.setPower(-gamepad1.right_stick_y);
        telemetry.addData("-gamepad1.right_stick_y", -gamepad1.right_stick_y);
        telemetry.update();

    }

}
