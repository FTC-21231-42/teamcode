package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotMap.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "DriveControl_new", group = "1")
public class DriveControl_3 extends OpMode {
    //define variables
    double ovaPowerModify = 1.0;
    double basePower = 0;
    double leftFrontPower = 0, rightFrontPower = 0, leftBotPower = 0, rightBotPower = 0;

    float stickRightX1, stickRightY1, stickLeftX1;

    int elePos = 0;
    int eleTargetPos = 0;
    double collectorPos = 0;

    ElapsedTime runtime = new ElapsedTime();

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
        stickLeftX1 = this.gamepad1.left_stick_x;

        //use left trigger 1 to control the speed, push to set slower
        basePower = ((1 - (gamepad1.left_trigger + gamepad1.right_trigger) * 0.45) + 0.1) * ovaPowerModify;

        //set the power of motor
        //FINAL POWER = power * modify

        leftFrontPower = (-stickLeftX1 + (stickRightY1 - stickRightX1)) * basePower;
        rightFrontPower = (stickLeftX1 + (stickRightY1 + stickRightX1)) * basePower;
        leftBotPower = (-stickLeftX1 + (stickRightY1 + stickRightX1)) * basePower;
        rightBotPower = (stickLeftX1 + (stickRightY1 - stickRightX1)) * basePower;

        leftFrontMotor.setPower(leftFrontPower);
        rightFrontMotor.setPower(-rightFrontPower);
        leftBotMotor.setPower(leftBotPower);
        rightBotMotor.setPower(-rightBotPower);
        //end of driving

        elePos = eleMotor.getCurrentPosition();
        if (gamepad2.left_stick_y < -0.1) {
            //up
            eleTargetPos += (int) Math.abs(gamepad2.left_stick_y * 5 + 1);

        } else if (gamepad2.left_stick_y > 0.1) {
            //down
            eleTargetPos -= (int) Math.abs(gamepad2.left_stick_y * 5 + 1);

        }

        if (eleTargetPos > ELE_MAX_POS) eleTargetPos = ELE_MAX_POS;
        if (eleTargetPos < ELE_MIN_POS) eleTargetPos = ELE_MIN_POS;

        eleMotor.setTargetPosition(eleTargetPos);
        eleMotor.setPower(0.95);
        eleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //collector
        if (gamepad2.right_trigger > 0.1 && runtime.seconds() >= 0.5) {
            if (COLLECT_CURRENT) {
                collectorServo.setPosition(COLLECT_MAX_POS);
                COLLECT_CURRENT = false;

            } else {
                collectorServo.setPosition(COLLECT_MIN_POS);
                COLLECT_CURRENT = true;

            }
            //if last press was too close, won't do anything
            runtime.reset();

        }

        //logging
        telemetry.addData("stickRightX1", stickRightX1);
        telemetry.addData("stickRightY1", stickRightY1);
        telemetry.addData("left_stick_x;", gamepad1.left_stick_x);
        telemetry.addData("left_stick_y;", gamepad1.left_stick_y);
        telemetry.addData("lTrigger", gamepad1.left_trigger);
        telemetry.addData("rTrigger", gamepad1.right_stick_x);
        telemetry.addData("bPower", basePower);
        telemetry.addData("elePos", elePos);
        telemetry.update();

    }

    @Override
    public void stop() {
        super.stop();
    }

}
