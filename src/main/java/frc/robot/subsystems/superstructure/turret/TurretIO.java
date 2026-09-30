package frc.robot.subsystems.superstructure.turret;

import static edu.wpi.first.units.Units.*;

import org.littletonrobotics.junction.AutoLog;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.MutAngle;
import edu.wpi.first.units.measure.MutAngularVelocity;
//import edu.wpi.first.units.measure.MutAngularAcceleration;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.units.measure.MutCurrent;
import edu.wpi.first.units.measure.MutTemperature;
import edu.wpi.first.units.measure.Angle;

public interface TurretIO {
    
    @AutoLog public static class TurretIOInputs {
        public MutAngle hoodAngle = Degrees.mutable(0);
        public MutAngle turretAngle = Radians.mutable(0);

        public MutAngularVelocity flywheelVelocityAvg = DegreesPerSecond.mutable(0);
        public MutAngularVelocity leftVelocity = DegreesPerSecond.mutable(0);
        public MutAngularVelocity rightVelocity = DegreesPerSecond.mutable(0);
    
        //public MutAngularAcceleration flywheelAcceleration = DegreesPerSecondPerSecond.mutable(0);
        //public MutAngularAcceleration leftAcceleration = DegreesPerSecondPerSecond.mutable(0);
        //public MutAngularAcceleration rightAcceleration = DegreesPerSecondPerSecond.mutable(0);

        public MutVoltage averageFlywheelAppliedVolts = Volt.mutable(0);
        public MutVoltage leftFlywheelAppliedVolts = Volt.mutable(0);
        public MutVoltage rightFlywheelAppliedVolts = Volt.mutable(0);

        public MutCurrent averageFlywheelCurrentAmps = Amps.mutable(0);
        public MutCurrent leftFlywheelCurrentAmps = Amps.mutable(0);
        public MutCurrent rightFlywheelCurrentAmps = Amps.mutable(0);

        public MutTemperature averageTemp = Fahrenheit.mutable(0);
        public MutTemperature lefTemp = Fahrenheit.mutable(0);
        public MutTemperature rightTemp = Fahrenheit.mutable(0);

        public boolean atTargetVelocity = false;
        public boolean atTargetHoodAngle = false;
        public boolean atTargetTurretAngle = false;

        public MutAngularVelocity targetFlywheelVelocity = DegreesPerSecond.mutable(0);
    }

    public default void updateInputs(TurretIOInputs inputs) {
    }


    public default void setFlywheelVoltage(Voltage voltage) {
    }

    public default void stopFlywheel() {
    }
    
    public default void setHoodAngle(double angle) {
    }

    /**
     * Sets the turret to the desired angle IN RADIANS.
     * @param angle The angle IN RADIANS.
    */
    public default void setTurretAngle(double angle) {
    }

    public default void setFlywheelVelocity(double velocity) {
    }

    public default void setPID(SparkMax motor, SparkMaxConfig motorConfig, double kP, double kI, double kD, double kS, double kV, double kA) {
    }
}