package frc.robot.subsystems.superstructure.turret;

import edu.wpi.first.math.system.plant.DCMotor;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Voltage;

public class TurretConstants {
    // pulled from 2026 shooter code

    public static final Voltage MAX_SHOOTER_VOLTAGE = Volts.of(10); // max # of voltage in shooter, change dummy value later 

    public static final int CURRENT_LIMIT = 40; // shooter speed limit, this is a dummy value, change this later

    public static final double SHOOTER_KP = 0.0; // more error = more power
    public static final double SHOOTER_KD = 0.0; // predicts ROC of error, change these PID values later as needed
    public static final double SHOOTER_KV = 0.1;
    public static final double SHOOTER_KA = 30.0;
    public static final double SHOOTER_KS = 0.0;

    public static final AngularVelocity SHOOTER_TARGET =
        RPM.of(2800); // add these if we need them, desired speed
    public static final AngularVelocity SHOOTER_IDLE =
        RPM.of(1400); // add these if we need them, idle speed
    public static final AngularVelocity SHOOTER_TOLERANCE = RotationsPerSecond.of(50);
    // GET REAL VALUES FOR FOLLOWING:
    public static final Angle MIN_ANGLE = Degrees.of(28.048335); // lowest angle shooter can reach
    public static final Angle MAX_ANGLE = Degrees.of(59.874070); // largest angle shooter can reach



    public static final Voltage MAX_TURRET_AIM_VOLTAGE = Volts.of(10); //TODO get an actual value here

    public static final int TURRET_AIM_MOTOR_CAN_ID = 1000; //TODO update these to real CAN IDs
    public static final int FLYWHEEL_MOTOR_LEFT_CAN_ID = 1001;
    public static final int FLYWHEEL_MOTOR_RIGHT_CAN_ID = 1002;
    public static final int HOOD_ANGLE_MOTOR_CAN_ID = 1003;

    public static final DCMotor TURRET_AIM_MOTOR_TYPE = DCMotor.getNEO(1);
    public static final DCMotor FLYWHEEL_MOTOR_GROUP_TYPE = DCMotor.getNEO(2);
    public static final DCMotor HOOD_ANGLE_MOTOR_TYPE = DCMotor.getNeo550(1); // why are the 550 and vortex names Neo and not NEO?
}
