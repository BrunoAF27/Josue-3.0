package frc.robot.commands;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.*;

public class RollerCommand extends CommandBase {
    private final RollerSubsystem rollerSubsystem;
    private DoubleSupplier speed1;
    private DoubleSupplier speed2;
    //private int targetPositionIndex = 0;
    public RollerCommand(RollerSubsystem rollerSubsystem, DoubleSupplier speed1,  DoubleSupplier speed2) {
        this.rollerSubsystem = rollerSubsystem;
        this.speed1 = speed1;
        this.speed2 = speed2;
        addRequirements(rollerSubsystem);
    }
    
    @Override
    public void initialize() {
        rollerSubsystem.resetEncoders();
    }
    @Override
    public void execute() {
        rollerSubsystem.setSpeed(speed1.getAsDouble(), speed2.getAsDouble());
        rollerSubsystem.move();
    }    
    @Override
    public boolean isFinished() {
        return false;
    }
}
