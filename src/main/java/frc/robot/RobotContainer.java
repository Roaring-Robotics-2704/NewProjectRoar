package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIONavX;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSpark;
import frc.robot.subsystems.superstructure.intake.Intake;
import frc.robot.subsystems.superstructure.intake.IntakeIO;
import frc.robot.subsystems.superstructure.intake.IntakeIOReal;

public class RobotContainer {
    private static final double controllerDeadband = 0.08;

    private final Drive drive;
    private final Intake intake;
    private final CommandXboxController controller = new CommandXboxController(0);

    public RobotContainer() {
        drive = switch (Constants.currentMode) {
            case REAL -> new Drive(
                    new GyroIONavX(),
                    new ModuleIOSpark(0),
                    new ModuleIOSpark(1),
                    new ModuleIOSpark(2),
                    new ModuleIOSpark(3),
                    pose -> {});
            case SIM, REPLAY -> new Drive(
                    new GyroIO() {},
                    new ModuleIO() {},
                    new ModuleIO() {},
                    new ModuleIO() {},
                    new ModuleIO() {},
                    pose -> {});
        };

        configureButtonBindings();
    }

    private void configureButtonBindings() {
        drive.setDefaultCommand(Commands.run(
                () -> {
                    double xSpeed = -MathUtil.applyDeadband(controller.getLeftY(), controllerDeadband)
                            * drive.getMaxLinearSpeedMetersPerSec();
                    double ySpeed = -MathUtil.applyDeadband(controller.getLeftX(), controllerDeadband)
                            * drive.getMaxLinearSpeedMetersPerSec();
                    double rotSpeed = -MathUtil.applyDeadband(controller.getRightX(), controllerDeadband)
                            * drive.getMaxAngularSpeedRadPerSec();

                    drive.runVelocity(ChassisSpeeds.fromFieldRelativeSpeeds(
                            xSpeed,
                            ySpeed,
                            rotSpeed,
                            drive.getRotation()));
                },
                drive));

        controller.x().onTrue(Commands.runOnce(drive::stopWithX, drive));

        controller.start().onTrue(Commands.runOnce(
                        () -> drive.resetOdometry(new Pose2d(drive.getPose().getTranslation(), new Rotation2d())),
                        drive)
                .ignoringDisable(true));
    }

    public Command getAutonomousCommand() {
        return Commands.none();
    }

    public void resetSimulationField() {}

    public void updateSimulation() {}
}
