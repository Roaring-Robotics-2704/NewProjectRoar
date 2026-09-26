package frc.robot.subsystems.superstructure.intake;

import static edu.wpi.first.units.Units.Amps;
//import static edu.wpi.first.units.Units.Inches;
//import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;
//import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutCurrent;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.util.SparkUtil;

//import static frc.robot.subsystems.superstructure.intake.IntakeConstants.*;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

//import edu.wpi.first.math.system.plant.DCMotor;

//import edu.wpi.first.math.filter.LinearFilter;

public class IntakeIOReal implements IntakeIO{

    public MutVoltage rollerAppliedVoltage = Volts.mutable(0);
    public MutCurrent rollerCurrentDraw = Amps.mutable(0);
    public MutAngularVelocity rollerVelocity = RadiansPerSecond.mutable(0);

    private SparkMax intakeMotor;
    private SparkMaxConfig intakeMotorConfig;

    public IntakeIOReal() {
        // Assuming a single NEO motor for the intake roller
        intakeMotor = new SparkMax(IntakeConstants.ROLLER_MOTOR_ID, MotorType.kBrushless);

        intakeMotorConfig = new SparkMaxConfig();
        intakeMotorConfig.smartCurrentLimit(IntakeConstants.ROLLER_CURRENT_LIMIT);
        //intakeMotorConfig.inverted(false); change if necessary
        intakeMotorConfig.idleMode(IdleMode.kCoast);
        SparkUtil.tryUntilOk(intakeMotor, 5,
                () -> intakeMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters,
                        PersistMode.kPersistParameters));
    }

    @Override
    public void setRollerVoltage(Voltage voltage) {
        intakeMotor.setVoltage(voltage);
    }

    @Override
    public void stopMotor() {
        intakeMotor.stopMotor();
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.intakeAppliedVoltage.mut_replace(intakeMotor.getAppliedOutput(), Volts);
        inputs.intakeCurrentDraw.mut_replace(intakeMotor.getOutputCurrent(), Amps);
        inputs.intakeVelocity.mut_replace(intakeMotor.getEncoder().getVelocity(), RadiansPerSecond);
    }

    @Override
    public void setPID(double kP, double kI, double kD) {
        ClosedLoopConfig config = new ClosedLoopConfig();
        config.p(kP);
        config.i(kI);
        config.d(kD);
        intakeMotorConfig.apply(config);
        SparkUtil.tryUntilOk(intakeMotor, 5, () -> intakeMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters));
    }
}