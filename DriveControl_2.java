package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotMap.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "DriveControl_old_8", group = "1")
public class DriveControl_2 extends OpMode {
    //define variables
    double ovaPowerModify = 0.35;
    double basePower = 0;
    double leftFrontPower = 0, rightFrontPower = 0, leftBotPower = 0, rightBotPower = 0;

    float stickRightX1, stickRightY1;
    float fixedStickRightX1, fixedStickRightY1;
    boolean x, y;
    boolean x0, y0;


    @Override
    public void init() {
        //init hardware
        initRobot(hardwareMap);

        //update log
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }

    @Override
    public void loop() {
        //main

        //get the input from the game pad
        stickRightX1 = this.gamepad1.right_stick_x;
        stickRightY1 = this.gamepad1.right_stick_y;

        //using 0.05 but not 0
        //for stability and error-tolerant
        //sin 22.5 = 0.3827
        if (Math.abs(stickRightX1) > 0.3827) {
            fixedStickRightX1 = stickRightX1;
            x0 = false;

        } else {
            fixedStickRightX1 = 0;
            x0 = true;

        }

        if (Math.abs(stickRightY1) > 0.3827) {
            fixedStickRightY1 = stickRightY1;
            y0 = false;

        } else {
            fixedStickRightY1 = 0;
            y0 = true;

        }

        //moving logic part
        //boolean operation
        //might need to change
        y = fixedStickRightY1 < 0;
        x = fixedStickRightX1 < 0;
        basePower = (Math.abs(fixedStickRightX1) + Math.abs(fixedStickRightY1)) * (2 - gamepad1.left_trigger);

        if ((y && x0) || (!x && y0) || (y && !x)) {
            leftFrontPower = basePower;
            rightBotPower = basePower;

        } else if ((!y && x0) || (x && y0) || (!y && x)) {
            leftFrontPower = -basePower;
            rightBotPower = -basePower;

        } else {
            leftFrontPower = 0;
            rightBotPower = 0;

        }

        if ((y && x0) || (x && y0) || (y && x)) {
            rightFrontPower = basePower;
            leftBotPower = basePower;

        } else if ((!y && x0) || (!x && y0) || (!y && !x)) {
            rightFrontPower = -basePower;
            leftBotPower = -basePower;

        } else {
            rightFrontPower = 0;
            leftBotPower = 0;

        }

        //rotate
        //simple, might need to change
        if (Math.abs(gamepad1.left_stick_x) > 0.1) {
            leftFrontPower = basePower * gamepad1.left_stick_x;
            leftBotPower = basePower * gamepad1.left_stick_x;
            rightFrontPower = -basePower * gamepad1.left_stick_x;
            rightBotPower = -basePower * gamepad1.left_stick_x;

        }
        //end of driving



        //set the power of motor
        //FINAL POWER = power * modify
        leftFrontMotor.setPower(-leftFrontPower * (1 - gamepad1.left_trigger) * 0.8);
        rightFrontMotor.setPower(rightFrontPower * (1 - gamepad1.left_trigger) * 0.8);
        leftBotMotor.setPower(-leftBotPower * (1 - gamepad1.left_trigger) * 0.8);
        rightBotMotor.setPower(rightBotPower * (1 - gamepad1.left_trigger) * 0.8);


        //logging
        telemetry.addData("stickRightX1", stickRightX1);
        telemetry.addData("stickRightY1", stickRightY1);
        telemetry.addData("left_stick_x;", gamepad1.left_stick_x);
        telemetry.addData("lTrigger", gamepad1.left_trigger);
        telemetry.addData("rTrigger", gamepad1.right_stick_x);
        telemetry.addData("bPower", basePower);
        telemetry.addData("x0", x0);
        telemetry.addData("y0", y0);
        telemetry.update();

    }
}
