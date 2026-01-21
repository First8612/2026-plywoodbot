package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Drivebase;

public class AimTowardAprilTag extends Command {
    private Drivebase drivebase;
    private boolean continuous;
    private PIDController rotPid = new PIDController(0.02, 0, 0);
    //private PIDController rotPid = new PIDController(0.01, 0.003, 0.00003);

    public AimTowardAprilTag(Drivebase drivebase, boolean continuous) {
        super();
        this.drivebase = drivebase;
        this.continuous = continuous;

        SmartDashboard.putData("AimTowardAprilTag/RotPID", rotPid);
    }

    @Override
    public void initialize() {
        rotPid.reset();
    }

    @Override
    public void execute() {
        super.execute();

        var limelightName = "limelight-right";
        if (LimelightHelpers.getFiducialID(limelightName) > -1) {
            var tx = LimelightHelpers.getTX("limelight-right");
            var rot = rotPid.calculate(tx);
            SmartDashboard.putNumber("AimTowardAprilTag/rot", rot);

            drivebase.drive(0, 0, rot);
        }
    }
}
