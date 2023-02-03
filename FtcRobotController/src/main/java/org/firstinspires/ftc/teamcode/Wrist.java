/* 
 * Controls the flange of the robot.
 */

import com.qualcomm.robotcore.hardware.Servo;

public class Wrist {
    private Servo wrist;

    private final double WRIST_FRONT = 0.04;
    private final double WRIST_BACK = 0.72;

    public Wrist() {
        this.wrist = hardwareMap.get(Servo.class, "wrist");
        
        this.setPosition(WRIST_DEFAULT);

        telemetry.addData("Status", "Wrist Initialized");
        telemetry.update();
    }

    public void rotateFront() {
        /*  Rotate the wrist to the front. */
        this.wrist.setPosition(WRIST_FRONT);
    }

    public void rotateBack() {
        /*  Rotate the wrist to the back. */
        this.wrist.setPosition(WRIST_BACK);
    }

    public void setPosition(double pos) {
        /*  Set the position of the wrist. */
        this.wrist.setPosition(pos);
    }

    public double getPosition() {
        /*  Get the position of the wrist. */
        return this.wrist.getPosition();
    }
}