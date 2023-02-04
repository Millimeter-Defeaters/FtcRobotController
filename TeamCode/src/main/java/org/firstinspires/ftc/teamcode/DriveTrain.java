/*
 * Controls the drive train of the robot.
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class DriveTrain {
    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

	private final double ticksPerRotation = 537.7;
	private final double gearReduction = 1.0;
	
	private final double wheelDiameter = 3.77953;
	private final double ticksPerInch = (ticksPerRotation * gearReduction) / (wheelDiameter * Math.PI);

    public Drive() {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);

        telemetry.addData("Status", "Drive Motors Initialized");
        telemetry.update();
    }

    public double[] normalizeInputs(double twist, double forward, double strafe) {
        /* Normalize powers to be between -1 and 1 */

        double[] powers = {
            (twist + strafe + forward) * 1.5,
            (twist - strafe - forward) * 1.5,
            (twist - strafe + forward),
            (twist + strafe - forward)
        };

        // summaryStatistics() is a reduction operation and allows for parallelization -> faster runtime
        DoubleSummaryStatistics stats = Arrays.stream(powers).summaryStatistics();
        double maxPower = stats.getMax();
        double minPower = stats.getMin();

        if (maxPower > 1 || minPower < -1) {
            double scale = 1 / Math.max(Math.abs(maxPower), Math.abs(minPower));
            for (int i = 0; i < powers.length; i++) {
                powers[i] *= scale;
            }
        }

        return powers;
    }

    public int calculateTicks(double distance) {
        /* 
         * Calculate the number of ticks to drive the robot for the given distance in inches. 
         */

        return (int) Math.ceil(distance * ticksPerInch);
    }

    public void driveForwardTicks(int ticks) {
        /* 
         * Drive forward for the given number of ticks.
         * Use this for autonomous.
         */
    
        // Set motors to run with encoders
        this.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        this.frontLeftDrive.setTargetPosition(ticks);
        this.frontRightDrive.setTargetPosition(ticks);

        // Set motor powers
        this.frontLeftDrive.setPower(0.25);
        this.rightMotor.setPower(0.25);

        // Wait for motors to finish
        while (this.frontLeftDrive.isBusy() && this.frontRightDrive.isBusy()) {
            // Halt the program until the motors finish
        }

        // Stop motors
        this.stop();
    }

    public void driveLeftTicks(int ticks) {
        /* 
         * Drive left for the given number of ticks.
         * Use this for autonomous.
         */
    
        // Set motors to run with encoders
        this.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        this.frontLeftDrive.setTargetPosition(-ticks);
        this.frontRightDrive.setTargetPosition(ticks);
        this.backLeftDrive.setTargetPosition(ticks);
        this.backRightDrive.setTargetPosition(-ticks);

        // Set motor powers
        this.frontLeftDrive.setPower(0.25);
        this.frontRightDrive.setPower(0.25);
        this.backLeftDrive.setPower(0.25);
        this.backRightDrive.setPower(0.25);

        // Wait for motors to finish
        while (this.frontLeftDrive.isBusy() && this.frontRightDrive.isBusy() && this.backLeftDrive.isBusy() && this.backRightDrive.isBusy()) {
            // Halt the program until the motors finish
        }

        // Stop motors
        this.stop();
    }

    public void driveRightTicks(int ticks) {
        /* 
         * Drive right for the given number of ticks.
         * Use this for autonomous.
         */
    
        // Set motors to run with encoders
        this.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        this.frontLeftDrive.setTargetPosition(ticks);
        this.frontRightDrive.setTargetPosition(-ticks);
        this.backLeftDrive.setTargetPosition(-ticks);
        this.backRightDrive.setTargetPosition(ticks);

        // Set motor powers
        this.frontLeftDrive.setPower(0.25);
        this.frontRightDrive.setPower(0.25);
        this.backLeftDrive.setPower(0.25);
        this.backRightDrive.setPower(0.25);

        // Wait for motors to finish
        while (this.frontLeftDrive.isBusy() && this.frontRightDrive.isBusy() && this.backLeftDrive.isBusy() && this.backRightDrive.isBusy()) {
            // Halt the program until the motors finish
        }

        // Stop motors
        this.stop();
    }


    public void drivePower(double twist, double forward, double strafe) {
        /* 
         * Drive the robot with the given twist, forward, and strafe values with no encoders.
         * Use this for teleop.
         */
        
        // Set motors to run without encoders
        this.frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double[] powers = normalizeInputs(twist, forward, strafe);
        
        this.frontLeftDrive.setPower(powers[0]);
        this.frontRightDrive.setPower(powers[1]);
        this.backLeftDrive.setPower(powers[2]);
        this.backRightDrive.setPower(powers[3]);
    }

    public void stop() {
        /* 
         * Stop the robot. Even if the robot stops, the motors will still be running. This method will stop the motors.
         */

        this.drivePower(0, 0, 0);
    }
}