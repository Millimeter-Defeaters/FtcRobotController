package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp

public class motorTests extends LinearOpMode {
    public Blinker control_Hub;
    public Servo finger0;
    private Servo finger1;
    private Servo wrist;
    private Gyroscope imu;
    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    public DcMotor backRightDrive;
    private DcMotor liftMotor;

    @Override
    public void runOpMode() {

        Items item = new Items(hardwareMap);

        control_Hub = hardwareMap.get(Blinker.class, "Control Hub");

        imu = hardwareMap.get(Gyroscope.class, "imu");

        finger0 = hardwareMap.get(Servo.class, "finger0");
        finger1 = hardwareMap.get(Servo.class, "finger1");

        wrist = hardwareMap.get(Servo.class, "wrist");

        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        liftMotor = hardwareMap.get(DcMotor.class, "lift");

        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        double defaultpos0;
        double defaultpos1;
        double wristrot;
        wrist.setPosition(0.04);

        while (opModeIsActive()) {
            //Drive Train Mecaunm Wheel Controls

            double max;

            double twist = gamepad1.left_stick_x * 0.75;
            double strafe = 0; //gamepad1.right_stick_x;
            double drive = gamepad1.right_trigger + -gamepad1.left_trigger;

            double[] speeds = {
                ((twist + strafe + drive) * 1.5),
                ((twist - strafe - drive) * 1.5),
                (twist - strafe + drive),
                (twist + strafe - drive)
            };

            max = Math.max(Math.abs(speeds[0]), Math.abs(speeds[1]));
            max = Math.max(max, Math.abs(speeds[2]));
            max = Math.max(max, Math.abs(speeds[3]));

            if (max > 1.0) {
                speeds[0] /= max;
                speeds[1] /= max;
                speeds[2] /= max;
                speeds[3] /= max;
            }

            frontLeftDrive.setPower(speeds[0]);
            frontRightDrive.setPower(speeds[1]);
            backLeftDrive.setPower(speeds[2]);
            backRightDrive.setPower(speeds[3]);

            /*Claw*/

            defaultpos0 = 0.53;
            defaultpos1 = 0.47;

            if (gamepad1.right_bumper) {
                finger0.setPosition(defaultpos0 += 0.15);
                finger1.setPosition(defaultpos1 -= 0.15);
            } else {
                defaultpos0 = 0.53;
                defaultpos1 = 0.47;
                finger0.setPosition(defaultpos0);
                finger1.setPosition(defaultpos1);
            }

            /*Wrist Rotation*/
            if (gamepad1.dpad_right) {
                wrist.setPosition(0.04);
            } else if (gamepad1.dpad_left) {
                if (wrist.getPosition() <= 0.4) {
                    wrist.setPosition(0.72);
                }
            }

            /*Lift Code*/

            double reset = -0.7 * Items.ticksPerInchLs;
            double ground = -5.0 * Items.ticksPerInchLs;
            double low = -13.5 * Items.ticksPerInchLs;
            double mid = -23.5 * Items.ticksPerInchLs;
            double high = -38.5 * Items.ticksPerInchLs;


            if (gamepad1.a) {
                Items.liftMove(ground);
            } else if (gamepad1.x) {
                Items.liftMove(low);
            } else if (gamepad1.y) {
                Items.liftMove(mid);
            } else if (gamepad1.b) {
                Items.liftMove(high);
            }

            if (gamepad1.left_bumper) {
                Items.liftMove(reset);
            }

            /*Display Information*/

            telemetry.addData("Forward/Backward", "Right Trigger/ Left Trigger");
            telemetry.addData("Turn", "Left Joy Stick");
            telemetry.addData("Open Claw", "Right Bumper");
            telemetry.addData("Reset Lift", "Left Bumper");
            telemetry.addData("Ground/Low/Mid/High", "A/X/Y/B");
            telemetry.addData("Turn Claw", "Right/Left Direction Key");
            telemetry.update();
        }
    }
}