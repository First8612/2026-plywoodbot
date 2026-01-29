package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivebase;

public class AimAtHub extends Command {
    private Drivebase drivebase;
    private PIDController rotatePid = new PIDController(0.02, 0, 0);

    public AimAtHub(Drivebase drivebase) {
        this.drivebase = drivebase;
    }

    @Override
    public void initialize() {
        rotatePid.reset();
    }

    @Override
    public void execute() {
        super.execute();

        var angle = drivebase.aimAt(4.625, 4.035);
        var drvRot = drivebase.getPoseEstimator().getEstimatedPosition().getRotation().getDegrees();
        if(drvRot - angle < -180) {
            angle -= 360;
        }else if(drvRot - angle > 180) {
            angle += 360;
        }
        var rot = rotatePid.calculate(drvRot - angle);

        drivebase.drive(0, 0, rot);
    }

    @Override
    public boolean isFinished() {
        var drvRot = drivebase.getPoseEstimator().getEstimatedPosition().getRotation().getDegrees();
        return Math.abs(drvRot - drivebase.aimAt(4.625, 4.035)) < 5 || Math.abs(drvRot - drivebase.aimAt(4.625, 4.035)) > 355;
    }
}
