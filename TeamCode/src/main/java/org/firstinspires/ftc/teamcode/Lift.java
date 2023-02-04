/*
 * Controls the 4-stage viper lift of the robot.
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Lift {
    private DcMotorEx liftMotor;
    
    // Ticks per inch
    private final double TICKS_PER_ROTATION = 537.7; // From https://www.gobilda.com/5202-series-yellow-jacket-planetary-gear-motor-19-2-1-ratio-312-rpm-3-3-5v-encoder/
    private final double GEAR_REDUCTION = 1.0; 

    private final double LIFT_DIAMETER = 1.404; // From https://www.gobilda.com/3407-series-hub-mount-winch-pulley-dual-spool-112mm-circumference/. Comvert 112 mm diameter to diameter in inches.
	private final double TICKS_PER_INCH_LIFT =  (TICKS_PER_ROTATION * GEAR_REDUCTION) / (LIFT_DIAMETER * Math.PI);
    
    // Position constants
    private final double RESET = -0.7 * TICKS_PER_INCH_LIFT;  
    private final double GROUND = -5.0 * TICKS_PER_INCH_LIFT;
    private final double LOW = -13.5 * TICKS_PER_INCH_LIFT;
    private final double MID = -23.5 * TICKS_PER_INCH_LIFT;
    private final double HIGH = -38.5 * TICKS_PER_INCH_LIFT;
    
    public Lift(HardwareMap hardwareMap) {
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