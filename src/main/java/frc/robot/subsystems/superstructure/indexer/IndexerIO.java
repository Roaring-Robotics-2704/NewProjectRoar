package frc.robot.subsystems.superstructure.indexer;

import org.littletonrobotics.junction.AutoLog;

import static edu.wpi.first.units.Units.Amps;
import edu.wpi.first.units.measure.MutCurrent;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import edu.wpi.first.units.measure.MutAngularVelocity;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;

public interface IndexerIO {
    
    @AutoLog public static class IndexerIOInputs {
        public MutCurrent currentDraw = Amps.mutable(0);
        public MutVoltage appliedVoltage = Volts.mutable(0);
        public MutAngularVelocity motorVelocity = RotationsPerSecond.mutable(0);
    }

    public default void updateInputs(IndexerIOInputs inputs) {
    }

    public default void setMotorVoltage(Voltage voltage) {
    }

    public default void stopMotor() {
    }
}