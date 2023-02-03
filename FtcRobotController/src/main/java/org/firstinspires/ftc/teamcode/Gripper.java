/*
 * Controls the end effector of the robot.
 */

import com.qualcomm.robotcore.hardware.Servo;

public class Gripper {
    private Servo finger0;
    private Servo finger1;

    private final double FINGER0_CLOSED = 0.53;
    private final double FINGER0_OPEN = FINGER0_CLOSED + 0.15;

    private final double FINGER1_CLOSED = 0.47;
    private final double FINGER1_OPEN = FINGER1_CLOSED - 0.15;

    public Gripper() {
        this.finger0 = hardwareMap.get(Servo.class, "finger0");
        this.finger1 = hardwareMap.get(Servo.class, "finger1");
        
        this.open();

        telemetry.addData("Status", "Gripper Initialized");
        telemetry.update();
    }

    public void open() {
        /*  Open the gripper. */
        this.finger0.setPosition(FINGER0_OPEN);
        this.finger1.setPosition(FINGER1_OPEN);
    }

    public void close() {
        /* Close the gripper. */
        this.finger0.setPosition(FINGER0_CLOSED);
        this.finger1.setPosition(FINGER1_CLOSED);
    }
    
    public double getPositionFinger0() {
        /*  Get the position of the first finger. */
        return this.finger0.getPosition();
    }

    public double getPositionFinger1() {
        /*  Get the position of the second finger. */
        return this.finger1.getPosition();
    }
}