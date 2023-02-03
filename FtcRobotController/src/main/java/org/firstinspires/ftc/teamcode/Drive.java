package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class Drive {
    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

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

    public void drive(double[] powers) {
        /* Drive the robot with the given powers */

        frontLeftDrive.setPower(powers[0]);
        frontRightDrive.setPower(powers[1]);
        backLeftDrive.setPower(powers[2]);
        backRightDrive.setPower(powers[3]);
    }

    public void drive(double twist, double forward, double strafe) {
        /* Drive the robot with the given twist, forward, and strafe values */

        double[] powers = normalizeInputs(twist, forward, strafe);
        drive(powers);
    }

    public void stop() {
        /* Stop the robot */

        drive(0, 0, 0);
    }
}