/*
 * Controls the 4-stage viper lift of the robot.
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Lift {
    private DcMotorEx liftMotor;
    
    // Ticks per inch
    private final double ticksPerRotation = 537.7; // From https://www.gobilda.com/5202-series-yellow-jacket-planetary-gear-motor-19-2-1-ratio-312-rpm-3-3-5v-encoder/
    private final double gearReduction = 1.0; 

    private final double liftDiameter = 1.404; // From https://www.gobilda.com/3407-series-hub-mount-winch-pulley-dual-spool-112mm-circumference/. Comvert 112 mm diameter to diameter in inches.
	private final double ticksPerInchLift =  (ticksPerRotation * gearReduction) / (liftDiameter * Math.PI);
    
    // Position constants
    private final double RESET = -0.7 * ticksPerInchLift;  
    private final double GROUND = -5.0 * ticksPerInchLift;
    private final double LOW = -13.5 * ticksPerInchLift;
    private final double MID = -23.5 * ticksPerInchLift;
    private final double HIGH = -38.5 * ticksPerInchLift;
    
    public Lift() {
        this.liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
    }

    public void move(double position) {
        /* Move the lift to the specified position. */

        this.liftMotor.setTargetPosition((int)Math.round(position));
		this.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		this.liftMotor.setVelocity(800.0);    
    }

    public void liftStop() {
        /* Stop the lift. */

        this.liftMotor.setVelocity(0.0);
    }

    public double getLiftPosition() {
        /* Get the current position of the lift. */

        return this.liftMotor.getCurrentPosition();
    }
}