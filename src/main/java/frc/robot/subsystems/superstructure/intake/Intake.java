package frc.robot.subsystems.superstructure.intake;

//import static edu.wpi.first.units.Units.Amps;
//import static edu.wpi.first.units.Units.Inches;
//import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.*;

//import edu.wpi.first.math.filter.LinearFilter;
//import edu.wpi.first.units.measure.Distance;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final IntakeIO intakeIO;

    public Intake(IntakeIO intakeIO) {
        this.intakeIO = intakeIO;
    }

    public void setRollersEnabled(boolean enabled) {
        intakeIO.setRollerVoltage(Volts.of(enabled ? 10 : 0));
    }

    public Command stopIntake() {
        return Commands.run(() -> setRollersEnabled(false));
    }

    public Command intake() {
        return Commands.parallel(
            Commands.startEnd(() -> setRollersEnabled(true), () -> setRollersEnabled(false))
            ).finallyDo(this::stopIntake);
    }

    @Override
    public void periodic() {
    }
}