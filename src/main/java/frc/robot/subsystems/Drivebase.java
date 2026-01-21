package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;

public class Drivebase extends SubsystemBase {
    public static final double kMaxSpeed = 4.0; // 4 meters per second
    public static final double kMaxAngularSpeed = 1.5 * Math.PI; // 1.5 rotations per second

    private final SwerveDrive swerveDrive;

    public Drivebase() {
        super();

        File directory = new File(Filesystem.getDeployDirectory(), "swerve");

        try {
            swerveDrive = new SwerveParser(directory).createSwerveDrive(kMaxSpeed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RobotConfig config = null;
        try {
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            // Handle exception as needed
            e.printStackTrace();
        }

        AutoBuilder.configure(
                this::getPose, // Robot pose supplier
                this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
                this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                (speeds, feedforwards) -> driveRobotRelative(speeds), // Method that will drive the robot given ROBOT
                                                                      // RELATIVE ChassisSpeeds. Also optionally outputs
                                                                      // individual module feedforwards
                new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for
                                                // holonomic drive trains
                    new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                    new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
                ),
                config, // The robot configuration
                () -> {
                    // Boolean supplier that controls when the path will be mirrored for the red
                    // alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                this // Reference to this subsystem to set requirements
        );
    }

    public void drive(double xSpeed, double ySpeed, double rot) {
        swerveDrive.drive(
                new Translation2d(
                        xSpeed * swerveDrive.getMaximumChassisVelocity(),
                        ySpeed * swerveDrive.getMaximumChassisVelocity()),
                rot * swerveDrive.getMaximumChassisAngularVelocity(),
                true,
                false);
    }

    public void driveFieldRelative(ChassisSpeeds chassisSpeed) {
        swerveDrive.driveFieldOriented(chassisSpeed);
    }

    public void driveRobotRelative(ChassisSpeeds chassisSpeed) {
        swerveDrive.drive(chassisSpeed);
    }

    public ChassisSpeeds getRobotRelativeSpeeds() {
        return swerveDrive.getRobotVelocity();
    }

    public Pose2d getPose() {
        return swerveDrive.getPose();
    }

    public void resetPose(Pose2d pose) {
        swerveDrive.resetOdometry(pose);
    }
}
