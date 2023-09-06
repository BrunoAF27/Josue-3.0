package frc.robot.commands;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.*;

public class SwingCommand extends CommandBase {
    private final SwingSubsystem swingSubsystem;
    private int targetPositionIndex = 0;
    public SwingCommand(SwingSubsystem swingSubsystem) {
        this.swingSubsystem = swingSubsystem;
        addRequirements(swingSubsystem);
    }
    
    public void setPosition(int targetPositionIndex){
        this.targetPositionIndex = targetPositionIndex;
    }
    
    @Override
    public void initialize() {
        swingSubsystem.resetAngle();
    }
    @Override
    public void execute() {
        swingSubsystem.PIDStraightPosition();
    }    
    @Override
    public boolean isFinished() {
        return false;
    }
}
