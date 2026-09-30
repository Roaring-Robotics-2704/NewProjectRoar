package frc.robot.subsystems.superstructure.turret;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.ClosedLoopConfig;
//import com.revrobotics.spark.config.FeedForwardConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

//import edu.wpi.first.units.measure.MutAngle;
//import edu.wpi.first.units.measure.MutAngularAcceleration;
//import edu.wpi.first.units.measure.MutAngularVelocity;
//import edu.wpi.first.units.measure.MutCurrent;
//import edu.wpi.first.units.measure.MutTemperature;
//import edu.wpi.first.units.measure.MutVoltage;
//import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.measure.Angle;

//import frc.robot.subsystems.superstructure.turret.TurretConstants.*;

import static edu.wpi.first.units.Units.*;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import frc.robot.util.SparkUtil;

public class TurretIOReal implements TurretIO {

    public AngularVelocity targetVelocity = RotationsPerSecond.of(0);
    public Angle targetHoodAngle = Degrees.of(0); // DEGREES
    public Angle targetTurretAngle = Radians.of(0); // RADIANS



    private SparkMax turretAimMotor;
    private SparkMaxConfig turretAimMotorConfig;
    private AbsoluteEncoder turretAimEncoder;
    private SparkClosedLoopController turretClosedLoopController;

    private SparkMax flywheelMotorLeft;
    private SparkMaxConfig flywheelMotorLeftConfig;
    private AbsoluteEncoder flywheelLeftEncoder;
    private SparkClosedLoopController flywheelLeftClosedLoopController;

    private SparkMax flywheelMotorRight;
    private SparkMaxConfig flywheelMotorRightConfig;
    private AbsoluteEncoder flywheelRightEncoder;
    private SparkClosedLoopController flywheelRightClosedLoopController;

    private SparkMax hoodAngleMotor;
    private SparkMaxConfig hoodAngleMotorConfig;
    private AbsoluteEncoder hoodAngleEncoder;
    private SparkClosedLoopController hoodClosedLoopController;

    public TurretIOReal() {
        turretAimMotor = new SparkMax(TurretConstants.TURRET_AIM_MOTOR_CAN_ID, MotorType.kBrushless);

        turretAimMotorConfig = new SparkMaxConfig();
        turretAimMotorConfig.smartCurrentLimit(TurretConstants.AIM_MOTOR_CURRENT_LIMIT);
        //intakeMotorConfig.inverted(false); change if necessary
        turretAimMotorConfig.idleMode(IdleMode.kCoast);
        SparkUtil.tryUntilOk(turretAimMotor, 5,
                () -> turretAimMotor.configure(turretAimMotorConfig, ResetMode.kResetSafeParameters,
                        PersistMode.kPersistParameters));
        
        turretAimEncoder = turretAimMotor.getAbsoluteEncoder();
        turretClosedLoopController = turretAimMotor.getClosedLoopController();



        flywheelMotorLeft = new SparkMax(TurretConstants.FLYWHEEL_MOTOR_LEFT_CAN_ID, MotorType.kBrushless);

        flywheelMotorLeftConfig = new SparkMaxConfig();
        flywheelMotorLeftConfig.smartCurrentLimit(TurretConstants.FLYWHEEL_LEFT_CURRENT_LIMIT);
        //intakeMotorConfig.inverted(false); change if necessary
        flywheelMotorLeftConfig.idleMode(IdleMode.kCoast);
        SparkUtil.tryUntilOk(flywheelMotorLeft, 5,
                () -> flywheelMotorLeft.configure(flywheelMotorLeftConfig, ResetMode.kResetSafeParameters,
                        PersistMode.kPersistParameters));

        flywheelLeftEncoder = flywheelMotorLeft.getAbsoluteEncoder();
        flywheelLeftClosedLoopController = flywheelMotorLeft.getClosedLoopController();



        flywheelMotorRight = new SparkMax(TurretConstants.FLYWHEEL_MOTOR_RIGHT_CAN_ID, MotorType.kBrushless);

        flywheelMotorRightConfig = new SparkMaxConfig();
        flywheelMotorRightConfig.smartCurrentLimit(TurretConstants.FLYWHEEL_RIGHT_CURRENT_LIMIT);
        //intakeMotorConfig.inverted(false); change if necessary
        flywheelMotorRightConfig.idleMode(IdleMode.kCoast);
        SparkUtil.tryUntilOk(flywheelMotorRight, 5,
                () -> flywheelMotorRight.configure(flywheelMotorRightConfig, ResetMode.kResetSafeParameters,
                        PersistMode.kPersistParameters));

        flywheelRightEncoder = flywheelMotorRight.getAbsoluteEncoder();
        flywheelRightClosedLoopController = flywheelMotorRight.getClosedLoopController();
        
        

        hoodAngleMotor = new SparkMax(TurretConstants.TURRET_AIM_MOTOR_CAN_ID, MotorType.kBrushless);

        hoodAngleMotorConfig = new SparkMaxConfig();
        hoodAngleMotorConfig.smartCurrentLimit(TurretConstants.HOOD_ANGLE_MOTOR_CAN_ID);
        //intakeMotorConfig.inverted(false); change if necessary
        hoodAngleMotorConfig.idleMode(IdleMode.kCoast);
        SparkUtil.tryUntilOk(hoodAngleMotor, 5,
                () -> hoodAngleMotor.configure(hoodAngleMotorConfig, ResetMode.kResetSafeParameters,
                        PersistMode.kPersistParameters));

        hoodAngleEncoder = hoodAngleMotor.getAbsoluteEncoder();
        hoodClosedLoopController = hoodAngleMotor.getClosedLoopController();
    }


    @Override
    public void updateInputs(TurretIOInputs inputs) {
        double hoodAngle = hoodAngleEncoder.getPosition();
        inputs.hoodAngle.mut_replace(hoodAngle, Degrees);
        double turretAngle = turretAimEncoder.getPosition();
        inputs.turretAngle.mut_replace(turretAngle, Radians);

        double avgVelocity = (flywheelLeftEncoder.getVelocity() + flywheelRightEncoder.getVelocity()) / (2 /*averages the two*/ * 60 /*converts from RPM to RPS*/);
        inputs.flywheelVelocityAvg.mut_replace(avgVelocity, RotationsPerSecond);
        inputs.leftVelocity.mut_replace(flywheelLeftEncoder.getVelocity() / 60, RotationsPerSecond);
        inputs.rightVelocity.mut_replace(flywheelRightEncoder.getVelocity() / 60, RotationsPerSecond);
    
        //inputs.flywheelAcceleration.mut_replace();
        //inputs.leftAcceleration.mut_replace();
        //inputs.rightAcceleration.mut_replace();

        inputs.averageFlywheelAppliedVolts.mut_replace(
            (flywheelMotorLeft.getAppliedOutput() + flywheelMotorRight.getAppliedOutput()) / 2,
            Volts);
        inputs.leftFlywheelAppliedVolts.mut_replace(flywheelMotorLeft.getAppliedOutput(), Volts);
        inputs.rightFlywheelAppliedVolts.mut_replace(flywheelMotorRight.getAppliedOutput(), Volts);

        inputs.averageFlywheelCurrentAmps.mut_replace(
            (flywheelMotorLeft.getOutputCurrent() + flywheelMotorRight.getOutputCurrent()) / 2,
            Amps);
        inputs.leftFlywheelCurrentAmps.mut_replace(flywheelMotorLeft.getOutputCurrent(), Amps);
        inputs.rightFlywheelCurrentAmps.mut_replace(flywheelMotorRight.getOutputCurrent(), Amps);

        inputs.averageTemp.mut_replace(
            (flywheelMotorLeft.getMotorTemperature() + flywheelMotorRight.getMotorTemperature()) / 2,
            Fahrenheit);
        inputs.lefTemp.mut_replace(flywheelMotorLeft.getMotorTemperature(), Fahrenheit);
        inputs.rightTemp.mut_replace(flywheelMotorRight.getMotorTemperature(), Fahrenheit);

        inputs.atTargetVelocity = targetVelocity.isEquivalent(RotationsPerSecond.of(avgVelocity));
        inputs.atTargetHoodAngle = (targetHoodAngle.magnitude() - Rotations.of(hoodAngle).in(Degrees) <= Measure.EQUIVALENCE_THRESHOLD);
        inputs.atTargetTurretAngle = (targetTurretAngle.magnitude() - Rotations.of(turretAngle).in(Degrees) <= Measure.EQUIVALENCE_THRESHOLD);

        inputs.targetFlywheelVelocity.mut_replace(targetVelocity);
    }


    @Override
    public void setFlywheelVoltage(Voltage voltage) {
        flywheelMotorLeft.setVoltage(voltage);
        flywheelMotorRight.setVoltage(voltage);
    }

    @Override
    public void stopFlywheel() {
        flywheelMotorLeft.stopMotor();
        flywheelMotorRight.stopMotor();
    }

    /**
     * Sets the hood to the desired angle IN DEGREES.
     * @param angle The angle IN DEGREES.
    */
    @Override
    public void setHoodAngle(double angle) {
        hoodClosedLoopController.setSetpoint(Degrees.of(angle).in(Rotations), ControlType.kPosition);
        targetHoodAngle = Degrees.of(angle);
    }

    /**
     * Sets the turret to the desired angle IN RADIANS.
     * @param angle The angle IN RADIANS.
    */
    @Override
    public void setTurretAngle(double angle) {
        hoodClosedLoopController.setSetpoint(Radians.of(angle).in(Rotations), ControlType.kPosition);
        targetTurretAngle = Degrees.of(angle);
    }

    @Override
    public void setFlywheelVelocity(double angularVelocity) {
        flywheelLeftClosedLoopController.setSetpoint(angularVelocity, ControlType.kVelocity);
        flywheelLeftClosedLoopController.setSetpoint(angularVelocity, ControlType.kVelocity);
        targetVelocity = RotationsPerSecond.of(angularVelocity);
    }

    @Override
    public void setPID(SparkMax motor, SparkMaxConfig motorConfig, double kP, double kI, double kD, double kS, double kV, double kA) {
        ClosedLoopConfig config = new ClosedLoopConfig();
        config.p(kP);
        config.i(kI);
        config.d(kD);
        motorConfig.apply(config);
        SparkUtil.tryUntilOk(motor, 5, () -> motor.configure(motorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters));
    }
}
