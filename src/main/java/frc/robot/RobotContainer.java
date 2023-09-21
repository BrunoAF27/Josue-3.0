// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//Here is where most of the magic happens, the robots commands and subsystems work together to move the robot.
package frc.robot;
//Libraries used in the project:
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autos.*;
public class RobotContainer {
  /*First we need to declare the Subsystems. Subsystems are the parts of the robot divided in different
  sections. They are just the pieces, they hold the actions like move, return speed or anything you want.
  However the Commands are the ones that will define which actions the subsystems will do.
  */
  //If you want to use a subsystem uncomment it and the things done in the code.
  //private final BoomStickSubsystem boomStickSubsystem = new BoomStickSubsystem(7);
  private final SparkSwingSubsystem swingSubsystem = new SparkSwingSubsystem(23,21,24,22);
  //private final BoomStickSubsystem boomStickSubsystem = new BoomStickSubsystem(6);
  //private final WristSubsystem wristSubsystem = new WristSubsystem(8);
  //private final RollerSubsystem rollerSubsystem = new RollerSubsystem(20);
  //Now that we have every robot component, we are going to declare the controllers.
  //Player1 is for swerve drive
  public  Joystick controllerPlayer1 = new Joystick(0);
  //Player2 is for controlling other mechanisms
  public  Joystick controllerPlayer2 = new Joystick(1);
  /*
  //The JoystickButton objects are capable of calling commands depending on their state
  private final JoystickButton player2AButton = new JoystickButton(controllerPlayer2, XboxController.Button.kA.value);
  private final JoystickButton player2BButton = new JoystickButton(controllerPlayer2, XboxController.Button.kB.value);
  private final JoystickButton player2YButton = new JoystickButton(controllerPlayer2, XboxController.Button.kY.value);
  */
  //Swerve:
  /* Drive Controls */
  private final int translationAxis = XboxController.Axis.kLeftY.value;
  private final int strafeAxis = XboxController.Axis.kLeftX.value;
  private final int rotationAxis = XboxController.Axis.kRightX.value;

  /* Driver Buttons */
  private final JoystickButton zeroGyro = new JoystickButton(controllerPlayer1, XboxController.Button.kY.value);
  private final JoystickButton robotCentric = new JoystickButton(controllerPlayer1, XboxController.Button.kLeftBumper.value);

   /* Subsystems */
   private final Swerve s_Swerve = new Swerve();
   
  public RobotContainer() {
    /*Default commands are the ones that will be called over and over again, since a PID is
    constantly updating the values of the motors, the commands must act consistently.
    In the case of this program they just tell the robot to go to the PID position. The subsystem must
    update its values if it wants to change the position.*/
    /*swingSubsystem.setDefaultCommand(
      new SwingCommand(swingSubsystem)
    );
    */
    /*boomStickSubsystem.setDefaultCommand(
      new BoomStickCommand(boomStickSubsystem)
    );
    wristSubsystem.setDefaultCommand(
      new WristCommand(wristSubsystem)
    );
    */
    /*The roller subsystem as the swerve will recieve constant values from the rt and lt
    rollerSubsystem.setDefaultCommand(
      new RollerCommand(
        rollerSubsystem,
        ()-> controllerPlayer2.getRawAxis(2), 
        ()-> controllerPlayer2.getRawAxis(3)
      )
    );
  */
  //The swerve constantly recieves the updated data of the controller's axis.
    s_Swerve.setDefaultCommand(
        new TeleopSwerve(
            s_Swerve, 
            () -> -controllerPlayer1.getRawAxis(translationAxis), 
            () -> -controllerPlayer1.getRawAxis(strafeAxis), 
            () -> -controllerPlayer1.getRawAxis(rotationAxis), 
            () -> robotCentric.getAsBoolean()
        )
    );
    configureButtonBindings();
  }
  //configureButtonBindings() is in charge of updating values when a button is pressed.
  private void configureButtonBindings() {
    /*Instant commands can only execute a single function or a single command, but Sequential Command Groups
    execute commands in order. Choose which one you want to use wisely.
    */
    /*player2AButton.onTrue(new SequentialCommandGroup(new InstantCommand(()-> swingSubsystem.setPosition(0))));
    player2BButton.onTrue(new InstantCommand(() -> swingSubsystem.setPosition(1)));
    player2YButton.onTrue(new InstantCommand(() -> swingSubsystem.setPosition(2)));*/
    /*
    player2YButton.onTrue(
      //new InstantCommand(()-> swingSubsystem.setPosition(2))
      /*new SequentialCommandGroup(
        new InstantCommand(()-> swingSubsystem.setPosition(2)),
        new InstantCommand(()-> boomStickSubsystem.setTargetPosition(2)) 
      )
    */
    //);
    /*player2AButton.onTrue(new InstantCommand(() -> boomStickSubsystem.setTargetPosition(0)));
    player2BButton.onTrue(new InstantCommand(() -> boomStickSubsystem.setNegativeTargetPosition(1)));
    player2YButton.onTrue(new InstantCommand(() -> boomStickSubsystem.setTargetPosition(2)));
    */
    /*Use this to test if the swing has enough strength to lift itsel:
    player2BButton.onTrue(
      new InstantCommand(()-> swingSubsystem.setSpeed(-0.9))
    );
    player2YButton.onTrue(
      new InstantCommand(()-> swingSubsystem.setSpeed(0.9))
    );*/
    //The button that changes the orintation of the robot
    zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
  }

  public void start(){
    //Start will set initial values and reset everything if necessary
  }

  /*We know teleopPeriodic is in the Robot class, but it isn't enough in order to control multiple
  tasks. That is why all the commands and subsytems interaction will be ran here.*/
  public void teleopPeriodic(){
    //Only the command scheduler will be ran.
     CommandScheduler.getInstance().run();
  }
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new exampleAuto(s_Swerve);
  }
  public void autonomousInitialize(){
    //CommandScheduler.getInstance().schedule(autonomousCommand3);
    //CommandScheduler.getInstance().run();
 }
 public void autonomousPeriodic(){  
    CommandScheduler.getInstance().run();
  }
}
