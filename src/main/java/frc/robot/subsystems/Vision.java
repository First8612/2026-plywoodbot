package frc.robot.subsystems;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import frc.robot.utilities.LimelightHelpers;
import frc.robot.subsystems.Drivebase;

public class Vision {
    
    private StructPublisher<Pose2d> posePublisher = NetworkTableInstance.getDefault().getStructTopic("Poses/Pose" , Pose2d.struct).publish();
    private StructPublisher<Pose2d> poseMT1Publisher = NetworkTableInstance.getDefault().getStructTopic("Poses/Pose_WpiBlue" , Pose2d.struct).publish();
    private StructPublisher<Pose2d> poseMT2Publisher = NetworkTableInstance.getDefault().getStructTopic("Poses/Pose_MT2_WpiBlue" , Pose2d.struct).publish();
    private SwerveDrivePoseEstimator poseEstimator;
    
    public Vision(Drivebase drivebase) {

        super();

        poseEstimator = drivebase.getPoseEstimator();
    }

    public void periodic() {

        var poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight");
        LimelightHelpers.SetRobotOrientation("limelight", poseEstimator.getEstimatedPosition().getRotation().getDegrees(), 0, 0, 0, 0, 0);
        var poseEstimateMT2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight");
        if(poseEstimateMT2.tagCount != 0){
            poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.5,.5,9999999));
            poseEstimator.addVisionMeasurement(poseEstimateMT2.pose, poseEstimateMT2.timestampSeconds);
        }
        


        posePublisher.set(poseEstimator.getEstimatedPosition());
        poseMT1Publisher.set(poseEstimate.pose);
        poseMT2Publisher.set(poseEstimateMT2.pose);
    }

}
