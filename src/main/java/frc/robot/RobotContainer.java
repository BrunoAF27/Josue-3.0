// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//Bruno: Here is where most of the magic happens, the robots commands and subsystems work together to move the robot.
package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.sql.Time;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {
  /*⬇Bruno: First we need to declare the Subsystems. Subsystems are the parts of the robot divided in different
  sections. They are just the pieces, they hold the actions like move, return speed or anything you want.
  However the Commands are the ones that will define which actions the subsystems will do.
  */
  //private final BoomStickSubsystem boomStickSubsystem = new BoomStickSubsystem(7);
  private final SwingSubsystem swingSubsystem = new SwingSubsystem(2,4,3,5);
  private final BoomStickSubsystem boomStickSubsystem = new BoomStickSubsystem(6);
  private final WristSubsystem wristSubsystem = new WristSubsystem(8);
  private final RollerSubsystem rollerSubsystem = new RollerSubsystem(7);
  //⬇Bruno: Now that we have every robot component, we are going to declare the controllers.
  public  Joystick controllerPlayer1 = new Joystick(0);
  public  Joystick controllerPlayer2 = new Joystick(1);
  private final JoystickButton player2AButton = new JoystickButton(controllerPlayer2, XboxController.Button.kA.value);
  private final JoystickButton player2BButton = new JoystickButton(controllerPlayer2, XboxController.Button.kB.value);
  private final JoystickButton player2YButton = new JoystickButton(controllerPlayer2, XboxController.Button.kY.value);

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
    //swingSubsystem.setDefaultCommand(swingCommand);
    swingSubsystem.setDefaultCommand(
      new SwingCommand(swingSubsystem)
    );
    boomStickSubsystem.setDefaultCommand(
      new BoomStickCommand(boomStickSubsystem)
    );
    wristSubsystem.setDefaultCommand(
      new WristCommand(wristSubsystem)
    );
    rollerSubsystem.setDefaultCommand(
      new RollerCommand(
        rollerSubsystem,
        ()-> controllerPlayer2.getRawAxis(2), 
        ()-> controllerPlayer2.getRawAxis(3)
      )
    );
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

  private void configureButtonBindings() {
    //↑Bruno: This method is for running instant commands
    /*player2AButton.onTrue(new SequentialCommandGroup(new InstantCommand(()-> swingSubsystem.setPosition(0))));
    player2BButton.onTrue(new InstantCommand(() -> swingSubsystem.setPosition(1)));
    player2YButton.onTrue(new InstantCommand(() -> swingSubsystem.setPosition(2)));*/
    /*player2AButton.onTrue(new InstantCommand(() -> swingSubsystem.setPosition(0)));
    player2BButton.onTrue(new InstantCommand(() -> swingSubsystem.setPosition(1)));
    player2YButton.onTrue(new InstantCommand(() -> swingSubsystem.setNegativePosition(1)));
    /*player2AButton.onTrue(new InstantCommand(() -> boomStickSubsystem.setTargetPosition(0)));
    player2BButton.onTrue(new InstantCommand(() -> boomStickSubsystem.setNegativeTargetPosition(1)));
    player2YButton.onTrue(new InstantCommand(() -> boomStickSubsystem.setTargetPosition(2)));
    */
    player2AButton.onTrue(
      new SequentialCommandGroup(
        new InstantCommand(()-> swingSubsystem.setPosition(0)),
        new InstantCommand(()-> boomStickSubsystem.setTargetPosition(0)) 
      )
    );
    player2BButton.onTrue(
      new SequentialCommandGroup(
        new InstantCommand(()-> swingSubsystem.setPosition(1)),
        new InstantCommand(()-> boomStickSubsystem.setTargetPosition(1)) 
      )
    );
    player2YButton.onTrue(
      new SequentialCommandGroup(
        new InstantCommand(()-> swingSubsystem.setPosition(2)),
        new InstantCommand(()-> boomStickSubsystem.setTargetPosition(2)) 
      )
    );
    zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
    //player2BButton.onTrue(new InstantCommand(() -> swingSubsystem.setPosition(1)));
    //player2YButton.onTrue(new InstantCommand(() -> swingSubsystem.setPosition(2)));
  }

  //⬇Bruno: This funcition will run commands by checking if a button was pressed from the player 1 controller
  private void checkPlayer1Buttons() {
    //↑Bruno: The player 1 controller will be in charge of moving the chasis.
  }
  //⬇Bruno: This funcition will run commands by checking if a button was pressed from the player 2 controller
  private void checkPlayer2Buttons() {
    //↑Bruno: The player 2 controller will be in charge of moving the arm.
    /*if(controllerPlayer2.getAButtonPressed()){
      swingCommand.setPosition(0);
    }
    if(controllerPlayer2.getBButtonPressed()){
      swingCommand.setPosition(1);
    }
    if(controllerPlayer2.getYButtonPressed()){
      swingCommand.setPosition(2);
    }
    */
  }
  
  //⬇Bruno: Right now this isn't used and we will probably change it
  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
  public void start(){
    //⬇Bruno: Start will set initial values and reset everything
  }

  /*⬇Bruno: We know teleopPeriodic is in the Robot class, but it isn't enough in order to control multiple
  tasks. That is why all the commands and subsytems interaction will be ran here.*/
  public void teleopPeriodic(){
     //⬇Bruno: In here we are updating the player1 controller input 
     checkPlayer1Buttons();
      //⬇Bruno: In here we are updating the player2 controller input 
      checkPlayer2Buttons();
     CommandScheduler.getInstance().run();
  }
  public void autonomousInitialize(){
    //CommandScheduler.getInstance().schedule(autonomousCommand3);
    //CommandScheduler.getInstance().run();
 }
 public void autonomousPeriodic(){  
    CommandScheduler.getInstance().run();
  }
}
