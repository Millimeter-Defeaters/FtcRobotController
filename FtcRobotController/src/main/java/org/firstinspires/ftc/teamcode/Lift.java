package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Lift {
    private DcMotorEx liftMotor;
    
    // Ticks per inch
    static final double ticksPerRotation = 537.7; // From https://www.gobilda.com/5202-series-yellow-jacket-planetary-gear-motor-19-2-1-ratio-312-rpm-3-3-5v-encoder/
    static final double gearReduction = 1.0; 
    
    static final double wheelDiameter = 3.77953; // From https://www.gobilda.com/96mm-mecanum-wheel-set-70a-durometer-bearing-supported-rollers/. Convert 96 mm diameter to diameter in inches.
	static final double ticksPerInch = (ticksPerRotation * gearReduction) / (wheelDiameter * Math.PI);

    static final double liftDiameter = 1.404; // From https://www.gobilda.com/3407-series-hub-mount-winch-pulley-dual-spool-112mm-circumference/. Comvert 112 mm diameter to diameter in inches.
	static final double ticksPerInchLift =  (ticksPerRotation * gearReduction) / (lsDiameter * Math.PI);
    
    // Position constants
    private final double RESET = -0.7 * ticksPerInchLift;  
    private final double GROUND = -5.0 * ticksPerInchLift;
    private final double LOW = -13.5 * Items.ticksPerInchLift;
    private final double MID = -23.5 * Items.ticksPerInchLift;
    private final double HIGH = -23.5 * Items.ticksPerInchLift;
    
    public Lift() {
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift");

        telemetry.addData("Status", "Lift Motor Initialized");
        telemetry.update();
    }

    public void liftMove(double position) {
        /* Move the lift to the specified position. */

        liftMotor.setTargetPosition((int)Math.ceil(position));
		liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		liftMotor.setVelocity(800.0);    
    }

    public void liftStop() {
        /* Stop the lift. */

        liftMotor.setVelocity(0.0);
    }
}