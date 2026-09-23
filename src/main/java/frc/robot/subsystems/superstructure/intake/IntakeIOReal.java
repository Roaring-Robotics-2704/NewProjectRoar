package frc.robot.subsystems.superstructure.intake;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutCurrent;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;
import static frc.robot.subsystems.superstructure.intake.IntakeConstants.ROLLER_CURRENT_LIMIT;
import edu.wpi.first.math.system.plant.DCMotor;

import edu.wpi.first.math.filter.LinearFilter;

public class IntakeIOReal implements IntakeIO{

    public MutVoltage rollerAppliedVoltage = Volts.mutable(0);
    public MutCurrent rollerCurrentDraw = Amps.mutable(0);
    public MutAngularVelocity rollerVelocity = RadiansPerSecond.mutable(0);

    public IntakeIOReal() {
     // Assuming a single NEO motor for the intake roller
        private DCMotor intakeMotor = IntakeConstants.ROLLER_MOTOR_TYPE; //this is NOT THE CORRECT INSTANTIATION, but it is a placeholder for the actual motor controller instance
    }

    @Override
    public void setVoltage(Voltage voltage) {
        intakeMotor.setVoltage(voltage);
    }

    @Override
    public void stopMotor() {
        intakeMotor.stopMotor();
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.rollerAppliedVoltage.set(intakeMotor.getAppliedVoltage());
        inputs.rollerCurrentDraw.set(intakeMotor.getCurrent(ROLLER_CURRENT_LIMIT));
        inputs.rollerVelocity.set(intakeMotor.getVelocity());
    }

    @Override
    public void setPID(double kP, double kI, double kD) {
        intakeMotor.setPID(kP, kI, kD);
    }
}