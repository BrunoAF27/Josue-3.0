package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.*;
public class SwingSubsystem extends SubsystemBase {
    private final TalonSRX leftFMotor;
    private final TalonSRX rightFMotor;
    private final Encoder externalEncoder = new Encoder(1,2);
    private final VictorSPX leftBMotor;
    private final VictorSPX rightBMotor;
    // Define your position setpoints
    private static final double angle1 = 51;
    private static final double angle2 = 55;
    private static final double maxAngle = 190;
    private static final double[] positionSetpoints = {0.0,setAngle(angle1), setAngle(angle2)};
    private double targetAngle = 0.0;
    private double targetPosition = 0;
    //Maximum output value
    private static final double maxOutput = 1.0; 
    public SwingSubsystem(int talonLeftFId, int talonLeftBId, int talonRightFId, int talonRightBId) {
        leftFMotor = new TalonSRX(talonLeftFId);
        leftBMotor = new VictorSPX(talonLeftBId);
        rightFMotor = new TalonSRX(talonRightFId);
        rightBMotor = new VictorSPX(talonRightBId);
        //Bruno: First we set everything to Factory Default settings.
    leftFMotor.configFactoryDefault();
    leftBMotor.configFactoryDefault();
    rightFMotor.configFactoryDefault();
    rightBMotor.configFactoryDefault();  

    leftFMotor.configPeakCurrentLimit(30);
    rightFMotor.configPeakCurrentLimit(30);

    leftFMotor.setNeutralMode(NeutralMode.Brake);
    leftBMotor.setNeutralMode(NeutralMode.Brake);
    rightFMotor.setNeutralMode(NeutralMode.Brake);
    rightBMotor.setNeutralMode(NeutralMode.Brake);

    leftFMotor.configVoltageCompSaturation(12,RobotMap.sensorsTimeoutMS);
    leftBMotor.configVoltageCompSaturation(12,RobotMap.sensorsTimeoutMS);
    rightFMotor.configVoltageCompSaturation(12, RobotMap.sensorsTimeoutMS);
    rightBMotor.configVoltageCompSaturation(12, RobotMap.sensorsTimeoutMS);
    //Bruno:Then we set the followers.
    leftBMotor.follow(leftFMotor);
    rightBMotor.follow(rightFMotor);
    //Bruno: Then we configure the quadrature encoders
    leftFMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    rightFMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    resetEncoders();
    //Bruno: Then we configure the maximum voltage
    leftFMotor.configVoltageCompSaturation(12);
    leftBMotor.configVoltageCompSaturation(12);
    rightFMotor.configVoltageCompSaturation(12);
    rightBMotor.configVoltageCompSaturation(12);
    //Bruno: Once configured we enable it.
    leftFMotor.enableVoltageCompensation(false);
    leftBMotor.enableVoltageCompensation(false);
    rightFMotor.enableVoltageCompensation(false);
    rightBMotor.enableVoltageCompensation(false);
    //Bruno: Next we configure the time of the ramp. The time it takes to reach its full speed.
    leftFMotor.configOpenloopRamp(RobotMap.rampingTime);
    //leftBMotor.configOpenloopRamp(RobotMap.rampingTime);
    rightFMotor.configOpenloopRamp(RobotMap.rampingTime);
    rightBMotor.configOpenloopRamp(RobotMap.rampingTime);
    //Bruno: Next we set the sensor phase to true. Remember to do this before inverting the motors.
    leftFMotor.setSensorPhase(true);
    leftBMotor.setSensorPhase(true);
    rightFMotor.setSensorPhase(true);
    rightBMotor.setSensorPhase(true);
    //Bruno:Then we invert the motors of the right side.
    rightBMotor.setInverted(true);
    leftBMotor.setInverted(true);
    //Bruno: Then we set the PID values:
    /* Configure the left Talon's selected sensor as local QuadEncoder */
		leftFMotor.configSelectedFeedbackSensor(	FeedbackDevice.QuadEncoder,				// Local Feedback Source
													RobotMap.PIDDistnaceID,					// PID Slot for Source [0, 1]
													RobotMap.sensorsTimeoutMS);					// Configuration Timeout

		/* Configure the Remote Talon's selected sensor as a remote sensor for the right Talon */
		rightFMotor.configRemoteFeedbackFilter(leftFMotor.getDeviceID(),					// Device ID of Source
												RemoteSensorSource.TalonSRX_SelectedSensor,	// Remote Feedback Source
												RobotMap.REMOTE_1,							// Source number [0, 1]
												RobotMap.sensorsTimeoutMS);						// Configuration Timeout

		
		/* Setup Sum signal to be used for Distance */
		rightFMotor.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor1, RobotMap.sensorsTimeoutMS);				// Feedback Device of Remote Talon
		rightFMotor.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.sensorsTimeoutMS);	// Quadrature Encoder of current Talon
		
		/* Setup Difference signal to be used for Turn */
		rightFMotor.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.RemoteSensor1, RobotMap.sensorsTimeoutMS);
		rightFMotor.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.sensorsTimeoutMS);
		
		/* Configure Sum [Sum of both QuadEncoders] to be used for Primary PID Index */
		rightFMotor.configSelectedFeedbackSensor(	FeedbackDevice.SensorSum, 
													RobotMap.PIDDistnaceID,
													RobotMap.sensorsTimeoutMS);
		
		/* Scale Feedback by 0.5 to half the sum of Distance */
		rightFMotor.configSelectedFeedbackCoefficient(	0.5, 						// Coefficient
														RobotMap.PIDDistnaceID,		// PID Slot of Source 
														RobotMap.sensorsTimeoutMS);		// Configuration Timeout
		
		/* Configure Difference [Difference between both QuadEncoders] to be used for Auxiliary PID Index */
		rightFMotor.configSelectedFeedbackSensor(	FeedbackDevice.SensorDifference, 
													RobotMap.PIDTurnID, 
													RobotMap.sensorsTimeoutMS);
		/* Scale the Feedback Sensor using a coefficient */
		rightFMotor.configSelectedFeedbackCoefficient(	1,
														RobotMap.PIDTurnID, 
														RobotMap.sensorsTimeoutMS);
    /* Scale the Feedback Sensor using a coefficient */
	
		/* Configure output and sensor direction */
		/* Set status frame periods to ensure we don't have stale data */
		rightFMotor.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, RobotMap.sensorsTimeoutMS);
		rightFMotor.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, RobotMap.sensorsTimeoutMS);
		rightFMotor.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, RobotMap.sensorsTimeoutMS);
		leftFMotor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, RobotMap.sensorsTimeoutMS);
		/* Configure neutral deadband */
		rightFMotor.configNeutralDeadband(RobotMap.minSpeed, RobotMap.sensorsTimeoutMS);
		leftFMotor.configNeutralDeadband(RobotMap.minSpeed, RobotMap.sensorsTimeoutMS);

		/* Max out the peak output (for all modes).  
		 * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
		 */
		leftFMotor.configPeakOutputForward(+0.2, RobotMap.sensorsTimeoutMS);
		leftFMotor.configPeakOutputReverse(-0.2, RobotMap.sensorsTimeoutMS);
		rightFMotor.configPeakOutputForward(+0.2, RobotMap.sensorsTimeoutMS);
		rightFMotor.configPeakOutputReverse(-0.2, RobotMap.sensorsTimeoutMS);

		/* FPID Gains for distance servo */
		rightFMotor.config_kP(RobotMap.PIDDistnaceID, RobotMap.PIDDistance.kP, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kI(RobotMap.PIDDistnaceID, RobotMap.PIDDistance.kI, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kD(RobotMap.PIDDistnaceID, RobotMap.PIDDistance.kD, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kF(RobotMap.PIDDistnaceID, RobotMap.PIDDistance.kF, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_IntegralZone(RobotMap.PIDDistnaceID, RobotMap.PIDDistance.kIzone, RobotMap.sensorsTimeoutMS);
		rightFMotor.configClosedLoopPeakOutput(RobotMap.PIDDistnaceID, RobotMap.PIDDistance.kPeakOutput, RobotMap.sensorsTimeoutMS);

		/* FPID Gains for turn servo */
		/*rightFMotor.config_kP(RobotMap.PIDTurnID, RobotMap.PIDTurning.kP, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kI(RobotMap.PIDTurnID, RobotMap.PIDTurning.kI, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kD(RobotMap.PIDTurnID, RobotMap.PIDTurning.kD, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kF(RobotMap.PIDTurnID, RobotMap.PIDTurning.kF, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_IntegralZone(RobotMap.PIDTurnID, RobotMap.PIDTurning.kIzone, RobotMap.sensorsTimeoutMS);
		rightFMotor.configClosedLoopPeakOutput(RobotMap.PIDTurnID, RobotMap.PIDTurning.kPeakOutput, RobotMap.sensorsTimeoutMS);
		*/	
        rightFMotor.config_kP(RobotMap.PIDTurnID, RobotMap.PIDDistance.kP, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kI(RobotMap.PIDTurnID, RobotMap.PIDDistance.kI, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kD(RobotMap.PIDTurnID, RobotMap.PIDDistance.kD, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_kF(RobotMap.PIDTurnID, RobotMap.PIDDistance.kF, RobotMap.sensorsTimeoutMS);
		rightFMotor.config_IntegralZone(RobotMap.PIDTurnID, RobotMap.PIDDistance.kIzone, RobotMap.sensorsTimeoutMS);
		rightFMotor.configClosedLoopPeakOutput(RobotMap.PIDTurnID, RobotMap.PIDDistance.kPeakOutput, RobotMap.sensorsTimeoutMS);
		/* 1ms per loop.  PID loop can be slowed down if need be.
		 * For example,
		 * - if sensor updates are too slow
		 * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		 * - sensor movement is very slow causing the derivative error to be near zero.
		 */
        int closedLoopTimeMs = 1;
        rightFMotor.configClosedLoopPeriod(0, closedLoopTimeMs, RobotMap.sensorsTimeoutMS);
        rightFMotor.configClosedLoopPeriod(1, closedLoopTimeMs, RobotMap.sensorsTimeoutMS);

		/* configAuxPIDPolarity(boolean invert, int timeoutMs)
		 * false means talon's local output is PID0 + PID1, and other side Talon is PID0 - PID1
		 * true means talon's local output is PID0 - PID1, and other side Talon is PID0 + PID1
		 */
		rightFMotor.configAuxPIDPolarity(false, RobotMap.sensorsTimeoutMS);
    }

    public void setPosition(int positionIndex) {
        if (positionIndex >= 0 && positionIndex < positionSetpoints.length) {
            targetPosition = positionSetpoints[positionIndex];
            //leftFMotor.set(ControlMode.Position, targetPosition);
            //rightFMotor.set(ControlMode.Position, targetPosition);
        }
    }
    public void setNegativePosition(int positionIndex) {
        if (positionIndex >= 0 && positionIndex < positionSetpoints.length) {
            targetPosition = -positionSetpoints[positionIndex];
            //leftFMotor.set(ControlMode.Position, targetPosition);
            //rightFMotor.set(ControlMode.Position, targetPosition);
        }
    }
    public void resetEncoders(){
        leftFMotor.getSensorCollection().setQuadraturePosition(0, 30);
        rightFMotor.getSensorCollection().setQuadraturePosition(0, 30);
    }
    // Move the Swing in a straight line
    public void PIDStraightPosition() {
        //Bruno: First we  get the position from the encoders
        rightFMotor.selectProfileSlot(0, 0);
        //rightFMotor.selectProfileSlot(1, 1);
        /*double currentPos = rightFMotor.getSelectedSensorPosition();
        if(currentPos > targetDistance){
            speed = -1.0;
        }
        else if(currentPos < targetDistance){
            speed = 1.0;
        }
        */
        SmartDashboard.putNumber("Target Distance", targetPosition);
        SmartDashboard.putNumber("Left Position", leftFMotor.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Position", rightFMotor.getSelectedSensorPosition());
        //Bruno: First we  get the position from the encoders
        rightFMotor.selectProfileSlot(RobotMap.PIDDistnaceID, RobotMap.PIDDistnaceID);
        //rightFMotor.selectProfileSlot(RobotMap.PIDTurnID, RobotMap.PIDTurnID);
        // Set the adjusted motor speeds
        //rightFMotor.set(ControlMode.Position,targetPosition,DemandType.AuxPID, targetAngle);
        //leftFMotor.follow(rightFMotor,FollowerType.AuxOutput1);
        rightFMotor.set(ControlMode.Position,targetPosition);
        //leftFMotor.follow(rightFMotor);
        rightBMotor.follow(rightFMotor);
        //leftBMotor.follow(leftFMotor);
        SmartDashboard.putNumber("Right Position", rightFMotor.getSelectedSensorPosition());
        SmartDashboard.putNumber("Left Position", leftFMotor.getSelectedSensorPosition());
    }
    //Think this makes things go straight.
    public void setTargetAngle(){
        targetAngle = rightFMotor.getSelectedSensorPosition(1);
        SmartDashboard.putNumber("Target Angle", targetAngle);
    }
    public boolean isAtPosition() {
        double currentPosition = leftFMotor.getSelectedSensorPosition();
        double setpoint = leftFMotor.getClosedLoopTarget();
        return Math.abs(currentPosition - setpoint) < leftFMotor.getSelectedSensorPosition(0) + 1;
    }
    public void resetAngle(){
         targetAngle = leftFMotor.getSelectedSensorPosition(1);
         SmartDashboard.putNumber("Target Angle", targetAngle);
     }
    public static double setAngle(double degrees){
        degrees = 180-degrees;

        if(degrees > maxAngle){   
        return (degrees*20.0*((14/48)*(18/48))*(12/65));
        }
        else if(degrees < 0){
            return 0; 
        }
        
        return degrees*20*((14/48)*(18/48))*(12/65);
    }
}

