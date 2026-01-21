package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivebase;

public class DriveTankCommand extends Command {
    private Drivebase drivebase;
    private CommandXboxController driveController;

    public DriveTankCommand(Drivebase drivebase, CommandXboxController driveController) {
        super();
        this.drivebase = drivebase;
        this.driveController = driveController;

        addRequirements(drivebase);
    }

    @Override
    public void execute() {
        var leftRate = Math.pow(-driveController.getLeftY(), 3); // -1 to 1
        var rightRate = Math.pow(-driveController.getRightY(), 3); // -1 to 1
        
        var xRate = (leftRate + rightRate) / 2;
        var rotRate = (leftRate - rightRate) / -2;

        drivebase.driveRobotRelative(new ChassisSpeeds(
            xRate * Drivebase.kMaxSpeed,
            0,
            rotRate * Drivebase.kMaxAngularSpeed
        ));
    }
}
