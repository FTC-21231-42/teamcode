package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
//jjjkjkghghhg
public class RobotMap {
    static HardwareMap hwMap;

    //tf and vuforia
    static final String VUFORIA_KEY = "AQMzFW3/////AAABmbeNyVU2Y0RtubAsx+MyOM41z7gags/qzhp4KLi3oPaw4cNKJHv0bGKmQUZUjuqDQfMIwJaiagRRZ8HC+FQAFWwtSQ4NbkAykIX+BlfH9uMbIvXAQrQTk18QFAKsixYyrDXTRKeEFiLr5ppKs1HF0w8awj6HAzwSoIf/h1JJPLsc1ch4GQiHg1KnBMiDMRvmpFKAQxdNEF11QaE/E4P+6KE0wslmhD2iIK0VtBYevOvBRC3kaS0LfM0dy2JxikfOEgUkK/yE8IaYJp2xCZt4b13aglweW9LesT5oZMe2zb8S7GfP+ZLEddFKbKnd7eWqkzcVNvMIliWhGocOZKr3aIvuJCJbS9Sc7OEk7fUTdthK";

    static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
    };

    static VuforiaLocalizer vuforia;
    static TFObjectDetector tFod;

    static DcMotor leftFrontMotor, rightFrontMotor, leftBotMotor, rightBotMotor;
    static DcMotor eleMotor;

    static int ELE_MIN_POS = 0;
    static int ELE_MAX_POS = 2150;

    static double COLLECT_MIN_POS = 0.045;
    static double COLLECT_MAX_POS = 0.28;
    static boolean COLLECT_CURRENT = false;

    static int ELE_COL = 225;
    static int ELE_BOT = 1000;
    static int ELE_MID = 1600;
    static int ELE_TOP = 2125;

    static Servo collectorServo;

    enum IconType{
        BOLT,
        BULB,
        PANEL,
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

//        collectorServo.setPosition(0);

    }

}


