package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class RobotMap {
    static HardwareMap hwMap;

    //tf and vuforia
    static final String VUFORIA_KEY = "AQMzFW3/////AAABmbeNyVU2Y0RtubAsx+MyOM41z7gags/qzhp4KLi3oPaw4cNKJHv0bGKmQUZUjuqDQfMIwJaiagRRZ8HC+FQAFWwtSQ4NbkAykIX+BlfH9uMbIvXAQrQTk18QFAKsixYyrDXTRKeEFiLr5ppKs1HF0w8awj6HAzwSoIf/h1JJPLsc1ch4GQiHg1KnBMiDMRvmpFKAQxdNEF11QaE/E4P+6KE0wslmhD2iIK0VtBYevOvBRC3kaS0LfM0dy2JxikfOEgUkK/yE8IaYJp2xCZt4b13aglweW9LesT5oZMe2zb8S7GfP+ZLEddFKbKnd7eWqkzcVNvMIliWhGocOZKr3aIvuJCJbS9Sc7OEk7fUTdthK";

    static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/tflitemodels/model_c1.tflite";
//    static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
//    static final String[] LABELS = {
//            "1 Bolt",
//            "2 Bulb",
//            "3 Panel"
//    };

    static final String[] LABELS = {
            "one",
            "three",
            "two"
    };

    static IconTypeN targetPos = IconTypeN.NA;

    static VuforiaLocalizer vuforia;
    static TFObjectDetector tFod;

    static DcMotor leftFrontMotor, rightFrontMotor, leftBotMotor, rightBotMotor;
    static DcMotor eleMotor;

    static int lfPos, rfPos, lbPos, rbPos;

    static int ELE_MIN_POS = 0;
//    static int ELE_MAX_POS = 2050;
//    static int ELE_MAX_POS = 2850;
    static int ELE_MAX_POS = 4830;

    static double COLLECT_MIN_POS = 0.045;
    static double COLLECT_MAX_POS = 0.205;
//    static double COLLECT_MAX_POS = 0.195;
    static boolean COLLECT_CURRENT = false;

    static double ARM_CLOSE = 0.53;
    static double ARM_OPEN = 1;

    static int ELE_COL = 1000;
    static int ELE_BOT = 2200;
    static int ELE_MID = 3600;
    static int ELE_TOP = 4575;

//    static int ELE_COL = 450;
//    static int ELE_BOT = 1400;
//    static int ELE_MID = 2150;
//    static int ELE_TOP = 2850;

//    static int ELE_COL = 225;
//    static int ELE_BOT = 1000;
//    static int ELE_MID = 1600;
//    static int ELE_TOP = 2050;

    static Servo collectorServo;
    static Servo armServo;
    static CRServo colExtendServo;

    enum IconType{
        BOLT,
        BULB,
        PANEL,
        NA

    }

    enum IconTypeN{
        ONE,
        TWO,
        THREE,
        NA

    }

    enum Direction{
        FRONT_BACK,
        LEFT_RIGHT,
        SIDE_FRONT_LEFT,
        SIDE_FRONT_RIGHT

    }

        public static void initRobot(HardwareMap map){
        hwMap = map;

        //Motors
        leftFrontMotor = hwMap.get(DcMotor.class, "leftFrontMotor");
        rightFrontMotor = hwMap.get(DcMotor.class, "rightFrontMotor");
        leftBotMotor = hwMap.get(DcMotor.class, "leftBotMotor");
        rightBotMotor = hwMap.get(DcMotor.class, "rightBotMotor");

        eleMotor = hwMap.get(DcMotor.class, "eleMotor");

        //Servos
        collectorServo = hwMap.get(Servo.class, "collectorServo");
        armServo = hwMap.get(Servo.class, "armServo");
        colExtendServo = hwMap.get(CRServo.class, "colExtendServo");

        //Init hardware
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBotMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBotMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        eleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBotMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBotMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        eleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        eleMotor.setTargetPosition(0);

        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBotMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBotMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        eleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        eleMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        Claw(false);

        lfPos = 0;
        rfPos = 0;
        lbPos = 0;
        rbPos = 0;

    }

    public static IconTypeN detectObject() {
        List<Recognition> updatedRecognitions = tFod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            for (Recognition recognition : updatedRecognitions) {
                switch (recognition.getLabel()) {
                    case "one":
                        return IconTypeN.ONE;

                    case "two":
                        return IconTypeN.TWO;

                    case "three":
                        return IconTypeN.THREE;

                    default:
                        break;

                }

            }
        }
        return IconTypeN.NA;

    }

    static double lastLevel = 0;
    static void Elevator(double level) {
        eleMotor.setTargetPosition((int) level);
        eleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (lastLevel > level) eleMotor.setPower(0.95);
        else eleMotor.setPower(1);
        lastLevel = level;

    }

    static void Claw(boolean state) {
        if (!state) collectorServo.setPosition(COLLECT_MIN_POS);
        else collectorServo.setPosition(COLLECT_MAX_POS);

    }

}


