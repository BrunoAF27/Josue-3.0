package frc.robot;
import frc.robot.subsystems.*;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

import edu.wpi.first.math.geometry.Rotation3d;
public class RobotMap{
    //CAN Ids
    //⬇Swerve Chasis Motor IDS:
    //8 Falcons
    //⬇Swerve Chasis Encoder IDS:


    //⬇Boomstick Motor IDS:
    //1 neo brushless
    //⬇Boomstick Encoder IDS:

    //⬇Boomstick Values:
    private final double minBoomStickDistance = 0.0;
    private final double maxBoomStickDistance = 1000;
    private final double[] boomStickLevelPosition = {10, 100, 900};
    //⬇Swing Motor IDS:
     //4 transmision cims
    //⬇Swing Encoder IDS:

     //⬇Swing Values:
     private final double minSwingDistance = 0.0;
     private final double maxSwingDistance = 1000;
     private final double[] swingLevelPosition = {10, 100, 900};
     //CAN Ids
    //⬇Drivetrain Motor IDS:
    public static final int swingLeftMotorFID = 2;
    public static final int swingLeftMotorBID = 4;
    public static final int swingRightMotorFID = 3;
    public static final int swingRightMotorBID = 5;
    //⬇Drivetrain Values:
    public static final double rampingTime = 0.3;
    public static final double maxSpeed = 1.0;
    //Bruno: The sensorsTimeoutMS will tell in how much time the information will be refreshed.
    public final static int sensorsTimeoutMS = 30;
   /*Bruno: kp also known as proportional gain, allows a better control of speed in order to reach a desired distance
    kp is a value of PID*/
    public static final double encoderkP = 0.1;
	/**
	 * How many sensor units per rotation.
	 * Using CTRE Magnetic Encoder.
	 * @link https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
	 */
	public final static int encoderTicksPerRotation = 4096;
	
	/**
	 * Number of rotations to drive when performing Distance Closed Loop
	 */
	public final static double wheelRotations = 6;
	/**
	 * Motor neutral dead-band, set to the minimum 0.1%.
	 */
	public final static double minSpeed = 0.001;
	
	/**
	 * PID Gains may have to be adjusted based on the responsiveness of control loop
	 * 	                                    			  kP   kI   kD   kF               Iz    PeakOut */
	public final static CustomPID PIDDistance = new CustomPID( 0.1, 0.0, 0.0, 0.0, 100,  0.5 );
  //public final static CustomPID PIDTurning = new CustomPID( 2.0, 0.01,  4.0, 0.0,200,1.00 );
	public final static CustomPID kGains_Velocit = new CustomPID( 0.1, 0.0, 20.0, 1023.0/6800.0,300,0.50 ); /* measured 6800 velocity units at full motor output */
	public final static CustomPID kGains_MotProf = new CustomPID( 1.0, 0.0,  0.0, 1023.0/6800.0,400,1.00 ); /* measured 6800 velocity units at full motor output */
	
	/** ---- Flat constants, you should not need to change these ---- */
	/* We allow either a 0 or 1 when selecting an ordinal for remote devices [You can have up to 2 devices assigned remotely to a talon/victor] */
	public final static int REMOTE_0 = 0;
	public final static int REMOTE_1 = 1;
	/* We allow either a 0 or 1 when selecting a PID Index, where 0 is primary and 1 is auxiliary */
	public final static int PIDDistnaceID = 0;
	public final static int PIDTurnID = 1;
    //⬇Wrist Motor IDS:
     //1 neo brushless
    //⬇Wrist Encoder IDS:

    //⬇Wrist Values:
    private final double minWristDistance = 0.0;
    private final double maxWristDistance = 1000;
    private final double[] wristLevelPosition = {10, 100, 900};

    //⬇Roller Motor IDS:
      //1 neo brushless
    //⬇Roller Encoder IDS:

}