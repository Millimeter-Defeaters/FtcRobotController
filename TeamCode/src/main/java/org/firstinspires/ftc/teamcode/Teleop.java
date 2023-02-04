/*
 * FTC Team 22281 - SLAM MMDs 2022-2023 TeleOp
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class TeleOp extends LinearOpMode {
    private DriveTrain driveTrain = new DriveTrain();
    private Gripper gripper = new Gripper();
    private Lift lift = new Lift();
    private Wrist wrist = new Wrist();
    
    private final double ticksPerRotation = 537.7; // From https://www.gobilda.com/5202-series-yellow-jacket-planetary-gear-motor-19-2-1-ratio-312-rpm-3-3-5v-encoder/
    private final double gearReduction = 1.0; 
    
    private final double wheelDiameter = 3.77953; // From https://www.gobilda.com/96mm-mecanum-wheel-set-70a-durometer-bearing-supported-rollers/. Convert 96 mm diameter to diameter in inches.
	private final double ticksPerInch = (ticksPerRotation * gearReduction) / (wheelDiameter * Math.PI);

    @Override
    public void runOpMode() {
        waitForStart();

        while (opModeIsActive()) {
            /* 
             * Drive controls. Mapping:
             * - Right stick x: Twist
             * - Right trigger: Forward
             * - Left trigger: Backward
             */

            double twist = gamepad1.right_stick_x * 0.75;
            double forward = gamepad1.right_trigger - gamepad1.left_trigger;
            double strafe = 0;

            driveTrain.drive(twist, forward, strafe);

            /*
             * Gripper controls. Mapping:
             * - Right bumper: Toggle open and close
             */

            if (gamepad1.right_bumper) {
                gripper.toggle();
            }

            /* 
             * Wrist rotation. Mapping:
             * - Dpad right: Rotate front
             * - Dpad left: Rotate back
             */ 

            if (game1.dpad_right) {
                wrist.rotateFront();
            }
            else if (game1.dpad_left) {
                wrist.rotateBack();
            }

            /*
             * Lift controls. Mapping:
             * - A: Ground 
             * - X: Low
             * - Y: Mid
             * - B: High
             * - Left bumper: Reset
             */
            
            if (gamepad1.a) {
                lift.move(Lift.GROUND);
            }
            else if (gamepad1.x) {
                lift.move(Lift.LOW);
            }
            else if (gamepad1.y) {
                lift.move(Lift.MID);
            }
            else if (gamepad1.b) {
                lift.move(Lift.HIGH);
            }

            if (gamepad1.left_bumper) {
                lift.move(Lift.RESET);
            }
        }

        // Display Info
        telemetry.addData("Forward/Backward", "Right Trigger/ Left Trigger");
        telemetry.addData("Turn", "Left Joy Stick");
        telemetry.addData("Toggle Open/Close Claw", "Right Bumper");
        telemetry.addData("Reset Lift", "Left Bumper");
        telemetry.addData("Ground/Low/Mid/High", "A/X/Y/B");
        telemetry.addData("Turn Claw", "Right/Left Direction Key");
        telemetry.update();
    }
}
