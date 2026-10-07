package frc.robot.subsystems.superstructure.indexer;

import org.littletonrobotics.junction.AutoLog;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import static edu.wpi.first.units.Units.Amps;
import edu.wpi.first.units.measure.MutCurrent;
import edu.wpi.first.units.measure.MutTemperature;

import static edu.wpi.first.units.Units.RotationsPerSecond;
import edu.wpi.first.units.measure.MutAngularVelocity;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;
import static edu.wpi.first.units.Units.Fahrenheit;

public interface IndexerIO {

    @AutoLog
    public static class IndexerIOInputs {
        public MutCurrent currentDraw = Amps.mutable(0);
        public MutVoltage appliedVoltage = Volts.mutable(0);
        public MutAngularVelocity motorVelocity = RotationsPerSecond.mutable(0);
        public MutTemperature motorTemp = Fahrenheit.mutable(0);
    }

    public default void updateInputs(IndexerIOInputs inputs) {
    }

    public default void setMotorVoltage(Voltage voltage) {
    }

    public default void setMotorVelocity(double velocity) {
    }

    public default void stopMotor() {
    }

    public default void setPID(double kP, double kI, double kD) {
    }
}