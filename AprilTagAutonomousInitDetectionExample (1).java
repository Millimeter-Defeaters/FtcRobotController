/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;

@Autonomous
public class AprilTagAutonomousInitDetectionExample extends LinearOpMode
{
	OpenCvCamera camera;
	AprilTagDetectionPipeline aprilTagDetectionPipeline;

	static final double FEET_PER_METER = 3.28084;

	static final double TILE_SQUARE_LENGTH = 23.5;
    static final double TITLE_DIAGONAL_LENGTH = Math.sqrt(2 * TILE_SQUARE_LENGTH * TILE_SQUARE_LENGTH);
    static final double OVERESTIMATE = 0.5; // TODO: Tune this value

	// motors

	private DcMotor frontLeftDrive;
	private DcMotor frontRightDrive;
	private DcMotor backLeftDrive;
	private DcMotor backRightDrive;


	// Lens intrinsics
	// UNITS ARE PIXELS
	// NOTE: this calibration is for the C920 webcam at 800x448.
	// You will need to do your own calibration for other configurations!
	double fx = 578.272;
	double fy = 578.272;
	double cx = 402.145;
	double cy = 221.506;

	// UNITS ARE METERS
	double tagsize = 0.166;

	int[] ID_TAG_OF_INTEREST = {11, 9, 2}; // Tag ID 18 from the 36h11 family

	AprilTagDetection tagOfInterest = null;

	@Override
	public void runOpMode()
	{
		frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
		frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
		backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
		backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

		frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
		backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
		frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
		backRightDrive.setDirection(DcMotor.Direction.REVERSE);

		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
		aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

		camera.setPipeline(aprilTagDetectionPipeline);
		camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
		{
			@Override
			public void onOpened()
			{
				camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
			}

			@Override
			public void onError(int errorCode)
			{

			}
		});

		telemetry.setMsTransmissionInterval(50);

		/*
		 * The INIT-loop:
		 * This REPLACES waitForStart!
		 */
		boolean tagFound[] = {false, false, false};
		while (!isStarted() && !isStopRequested())
		{
			ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

			if(currentDetections.size() != 0)
			{
				for(AprilTagDetection tag : currentDetections)
				{
					for(int i = 0; i <= ID_TAG_OF_INTEREST.length; i++){
						if(tag.id == ID_TAG_OF_INTEREST[i])
						{
							tagOfInterest = tag;
							tagFound[i] = true;
							break;
						}
					}
				}

				if(tagFound[0] || tagFound[1] || tagFound[2])
				{
					telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
					tagToTelemetry(tagOfInterest);
				}
				else
				{
					telemetry.addLine("Don't see tag of interest :(");

					if(tagOfInterest == null)
					{
						telemetry.addLine("(The tag has never been seen)");
					}
					else
					{
						telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
						tagToTelemetry(tagOfInterest);
					}
				}

			}
			else
			{
				telemetry.addLine("Don't see tag of interest :(");

				if(tagOfInterest == null)
				{
					telemetry.addLine("(The tag has never been seen)");
				}
				else
				{
					telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
					tagToTelemetry(tagOfInterest);
				}

			}

			telemetry.update();
			sleep(20);
		}

		/*
		 * The START command just came in: now work off the latest snapshot acquired
		 * during the init loop.
		 */

		/* Update the telemetry */
		if(tagOfInterest != null)
		{
			telemetry.addLine("Tag snapshot:\n");
			tagToTelemetry(tagOfInterest);
			telemetry.update();
		}
		else
		{
			telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
			telemetry.update();
		}

		/* Actually do something useful */
		if(tagOfInterest == null)
		{
			/* Move claw to open position*/
			Items.clawOpen();
			
		}
		else
		{
			/*
			 * Insert your autonomous code here, probably using the tag pose to decide your configuration.
			 */
			double ticksTravel = driveTrain.calculateTicks(TILE_SQUARE_LENGTH + OVERESTIMATE);

			if(tagFound[0] /* 11 */)
			{
				// Move right
				// Set motors to run with encoders
				frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				
				frontLeftDrive.setTargetPosition(ticksTravel);
				frontRightDrive.setTargetPosition(-ticksTravel);
				backLeftDrive.setTargetPosition(-ticksTravel);
				backRightDrive.setTargetPosition(ticksTravel);
		
				// Set motor powers
				frontLeftDrive.setPower(0.25);
				frontRightDrive.setPower(0.25);
				backLeftDrive.setPower(0.25);
				backRightDrive.setPower(0.25);

				// Wait for motors to finish
				while (frontLeftDrive.isBusy() && frontRightDrive.isBusy() && backLeftDrive.isBusy() && backRightDrive.isBusy()) {
					// Halt the program until the motors finish
				}
		
				// Stop motors
				stop();
			}
			else if(tagFound[1] /* 9 */)
			{
				// Move left

				// Set motors to run with encoders
				frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				
				frontLeftDrive.setTargetPosition(ticks);
				frontRightDrive.setTargetPosition(ticks);
		
				// Set motor powers
				frontLeftDrive.setPower(0.25);
				frontRightDrive.setPower(0.25);
				backLeftDrive.setPower(0.25);
				backRightDrive.setPower(0.25);
		
				// Wait for motors to finish
				while (this.frontLeftDrive.isBusy() && this.frontRightDrive.isBusy()) {
					// Halt the program until the motors finish
				}
		
				// Stop motors
				stop();
			}
			else if(tagFound[2] /* 2 */)
			{
				/// Set motors to run with encoders
				frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				
				frontLeftDrive.setTargetPosition(ticks);
				frontRightDrive.setTargetPosition(ticks);
				backLeftDrive.setTargetPosition(ticks);
				backRightDrive.setTargetPosition(ticks);
		
				// Set motor powers
				frontLeftDrive.setPower(0.25);
				frontRightDrive.setPower(0.25);
				backLeftDrive.setPower(0.25);
				backRightDrive.setPower(0.25);
		
				// Wait for motors to finish
				while (frontLeftDrive.isBusy() && frontRightDrive.isBusy() && backLeftDrive.isBusy() && backRightDrive.isBusy()) {
					// Halt the program until the motors finish
				}
		
				// Stop motors
				stop();
			}
		}


		/* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
		while (opModeIsActive()) {sleep(20);}
	}

	void tagToTelemetry(AprilTagDetection detection)
	{
		telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
		telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
		telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
		telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
		telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
		telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
		telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
	}
}
