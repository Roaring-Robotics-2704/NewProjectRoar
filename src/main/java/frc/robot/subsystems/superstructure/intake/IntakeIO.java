package frc.robot.subsystems.superstructure.intake;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volts;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutCurrent;
import edu.wpi.first.units.measure.MutDistance;
import edu.wpi.first.units.measure.MutLinearVelocity;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;

public interface IntakeIO {
    
    @AutoLog public static class IntakeIOInputs {

        public MutVoltage intakeAppliedVoltage = Volts.mutable(0);
        public MutCurrent intakeCurrentDraw = Amps.mutable(0);
        public MutAngularVelocity intakeVelocity = RotationsPerSecond.mutable(0);

    }

    default void updateInputs(IntakeIOInputs inputs) {

    }

    default void stopMotor() {

    }

    default void setRollerVoltage(Voltage voltage) {

    }

    default void setPID(double kP, double kI, double kD) {
        
    }
}